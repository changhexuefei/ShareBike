package com.dcch.sharebike.moudle.user.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dcch.sharebike.MainActivity;
import com.dcch.sharebike.R;
import com.dcch.sharebike.alipay.WeixinPay;
import com.dcch.sharebike.app.App;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.http.Api;
import com.dcch.sharebike.moudle.login.activity.PersonalCenterActivity;
import com.dcch.sharebike.moudle.user.bean.UserInfo;
import com.dcch.sharebike.utils.ClickUtils;
import com.dcch.sharebike.utils.JsonUtils;
import com.dcch.sharebike.utils.LogUtils;
import com.dcch.sharebike.utils.SPUtils;
import com.dcch.sharebike.utils.ToastUtils;
import com.dcch.sharebike.view.RefundPopuwindow;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

public class WalletInfoActivity extends BaseActivity {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.transactionDetail)
    TextView transactionDetail;
    @BindView(R.id.remainingSum)
    TextView remainingSum;
    @BindView(R.id.recharge)
    Button recharge;
    @BindView(R.id.chargeDeposit)
    TextView chargeDeposit;
    @BindView(R.id.showArea)
    TextView showArea;
    private RefundPopuwindow refundPopuwindow;
    private final String msg = "骑行单车必须支付押金，押金可退还。";
    private final String tipThere = "押金退款";
    private final String tipFour = "充押金";
    private final String Title = "提示";
    private final String ToCharge = "充押金";
    private final String goToDeposit = "去充值";
    private String uID;
    private UserInfo mInfo;
    private int mCashStatus;
    private String mOutRefundNo;
    private String mTotal_fee;
    private String mRefund_fee;
    private String mToken;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_wallet_info;
    }

    @Override
    protected void initData() {
        if (SPUtils.isLogin()) {
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();//"bundle"
            mInfo = (UserInfo) bundle.getSerializable("bundle");
            if (mInfo != null && !mInfo.equals("")) {
                mCashStatus = mInfo.getCashStatus();
                remainingSum.setText(String.valueOf(mInfo.getAggregateAmount()));
                if (mInfo.getCashStatus() == 1) {
                    showArea.setText("押金" + mInfo.getPledgeCash() + "元");
                    chargeDeposit.setText(tipThere);
                } else if (mInfo.getCashStatus() == 0) {
                    showArea.setText("押金" + mInfo.getPledgeCash() + "元");
                    chargeDeposit.setText(tipFour);
                }
            }

            String userDetail = (String) SPUtils.get(App.getContext(), "userDetail", "");
            if (userDetail != null) {
                try {
                    JSONObject object = new JSONObject(userDetail);
                    int userId = object.optInt("id");
                    uID = String.valueOf(userId);
                    mToken = object.optString("token");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @OnClick({R.id.back, R.id.transactionDetail, R.id.recharge, R.id.chargeDeposit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                if(ClickUtils.isFastClick()){
                    return;
                }
                startActivity(new Intent(this, PersonalCenterActivity.class));
                finish();
                break;
            case R.id.transactionDetail:
                if(ClickUtils.isFastClick()){
                    return;
                }
                Intent dealDetail = new Intent(this, TransactionDetailActivity.class);
                dealDetail.putExtra("userId", uID);
                dealDetail.putExtra("token",mToken);
                startActivity(dealDetail);
                break;
            case R.id.recharge:
                if(ClickUtils.isFastClick()){
                    return;
                }
                if (mCashStatus == 1) {
                    Intent intent = new Intent(WalletInfoActivity.this, RechargeBikeFareActivity.class);
                    startActivity(intent);
                } else if (mCashStatus == 0) {
                    popupDialog();
                }
                break;
            case R.id.chargeDeposit:
                if(ClickUtils.isFastClick()){
                    return;
                }
                if (chargeDeposit.getText().equals(tipFour)) {
                    Intent rechargeDeposit = new Intent(WalletInfoActivity.this, RechargeDepositActivity.class);
                    startActivity(rechargeDeposit);
                } else if (chargeDeposit.getText().equals(tipThere)) {
                    showRefundPopuwindow();
                }
                break;
        }
    }


    private void showRefundPopuwindow() {
        if (refundPopuwindow != null && !refundPopuwindow.equals("")) {
            refundPopuwindow.dismiss();
        }
        refundPopuwindow = new RefundPopuwindow(WalletInfoActivity.this, refundViewOnClick);
        refundPopuwindow.showAtLocation(findViewById(R.id.activity_wallet_info), Gravity.CENTER, 0, 0);
    }

    private void popupDialog() {
        new AlertDialog.Builder(this)
                .setTitle(Title)
                .setMessage(msg)
                .setNegativeButton(ToCharge, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(WalletInfoActivity.this, RechargeDepositActivity.class));
                    }
                })
                .setPositiveButton(goToDeposit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(WalletInfoActivity.this, RechargeBikeFareActivity.class));

                    }
                }).create()
                .show();
    }

    View.OnClickListener refundViewOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_cancel:
                    refundPopuwindow.dismiss();
                    break;
                case R.id.btn_confirm:
                    WeixinPay weixinPay = new WeixinPay(WalletInfoActivity.this);
                    mOutRefundNo = weixinPay.getOutRefundNo();
                    LogUtils.d("退款", mOutRefundNo + "\n" + uID);
                    refundPledgeCash(uID, mOutRefundNo,mToken);
                    ToastUtils.showShort(WalletInfoActivity.this, "您点击的是退押金按钮");
                    break;
            }
        }
    };

    private void refundPledgeCash(String uID, String outRefundNo,String mToken) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", uID);
        map.put("out_refund_no", outRefundNo);
        map.put("total_fee", "0.01");
        map.put("refund_fee", "0.01");
        map.put("token",mToken);
        OkHttpUtils.post().url(Api.BASE_URL + Api.REFUNDWXPAY).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.e("错误",e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d("退款", response);
                //{"resultStatus":"0"}
                if (JsonUtils.isSuccess(response)) {
                    startActivity(new Intent(WalletInfoActivity.this, ShowRefundResultsActivity.class));
                } else {
//                    startActivity(new Intent(WalletInfoActivity.this, ShowRefundResultsActivity.class));
                    ToastUtils.showShort(WalletInfoActivity.this, "退款失败！");
                }
                refundPopuwindow.dismiss();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (uID != null) {
            getUserInfo(uID,mToken);
        }
    }

    //从服务端拿到客户信息
    private void getUserInfo(String uID,String mToken) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", uID);
        map.put("token",mToken);
        OkHttpUtils.post().url(Api.BASE_URL + Api.INFOUSER).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
//                Log.e("获取用户信息", e.getMessage());
                ToastUtils.showShort(App.getContext(), "服务器正忙");
            }

            @Override
            public void onResponse(String response, int id) {
                Log.d("用户的信息", response);
                if (JsonUtils.isSuccess(response)) {
                    Gson gson = new Gson();
                    mInfo = gson.fromJson(response, UserInfo.class);
                    mCashStatus = mInfo.getCashStatus();
                    if (mCashStatus == 0) {
                        remainingSum.setText(String.valueOf(mInfo.getAggregateAmount()));
                        showArea.setText("押金" + String.valueOf(mInfo.getPledgeCash()) + "元");
                        chargeDeposit.setText(tipFour);

                    } else if (mCashStatus == 1) {
                        remainingSum.setText(String.valueOf(mInfo.getAggregateAmount()));
                        showArea.setText("押金" + String.valueOf(mInfo.getPledgeCash()) + "元");
                        chargeDeposit.setText(tipThere);
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(WalletInfoActivity.this, MainActivity.class));
    }
}
