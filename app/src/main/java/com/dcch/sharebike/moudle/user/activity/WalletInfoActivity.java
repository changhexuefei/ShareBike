package com.dcch.sharebike.moudle.user.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dcch.sharebike.MainActivity;
import com.dcch.sharebike.R;
import com.dcch.sharebike.alipay.WeixinPay;
import com.dcch.sharebike.app.App;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.http.Api;
import com.dcch.sharebike.moudle.login.activity.IdentityAuthenticationActivity;
import com.dcch.sharebike.moudle.login.activity.LoginActivity;
import com.dcch.sharebike.moudle.user.bean.UserInfo;
import com.dcch.sharebike.utils.ClickUtils;
import com.dcch.sharebike.utils.JsonUtils;
import com.dcch.sharebike.utils.LogUtils;
import com.dcch.sharebike.utils.NetUtils;
import com.dcch.sharebike.utils.SPUtils;
import com.dcch.sharebike.utils.ToastUtils;
import com.dcch.sharebike.view.RefundPopuwindow;
import com.google.gson.Gson;
import com.hss01248.dialog.StyledDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

import static com.dcch.sharebike.http.Api.CHECKAGGREGATE;

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
    @BindView(R.id.my_red_packet_sum)
    TextView mMyRedPacketSum;
    @BindView(R.id.red_packet_page)
    RelativeLayout mRedPacketPage;
    @BindView(R.id.card_voucher_page)
    RelativeLayout mCardVoucherPage;
    @BindView(R.id.my_red_packet)
    TextView mMyRedPacket;
    @BindView(R.id.month_card_image)
    ImageView mMonthCardImage;
    @BindView(R.id.usable_time)
    TextView mUsableTime;
    @BindView(R.id.remained_days)
    TextView mRemainedDays;
    @BindView(R.id.month_card_show)
    RelativeLayout mMonthCardShow;
    @BindView(R.id.my_card_voucher)
    TextView mMyCardVoucher;
    @BindView(R.id.card_voucher_recharge)
    Button mCardVoucherRecharge;
    private RefundPopuwindow refundPopuwindow;
    private String uID;
    private UserInfo mInfo;
    private int mCashStatus;
    private String mOutRefundNo;
    private String mTotal_fee;
    private String mRefund_fee;
    private String mToken;
    private Map<String, String> mMap;
    private int mStatus;
    private Dialog mDialog;
    private double mAggregateAmount;


    @Override
    protected int getLayoutId() {
        return R.layout.spare_layout;
    }

    @Override
    protected void initData() {
        if (SPUtils.isLogin()) {
            uID = String.valueOf(SPUtils.get(App.getContext(), "userId", 0));
            mToken = (String) SPUtils.get(App.getContext(), "token", "");
            LogUtils.d("用户的信息", uID + "\n" + mToken);
            SpannableStringBuilder stringBuilder = new SpannableStringBuilder(getString(R.string.red_packet));
            AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(30);
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.RED);
            stringBuilder.setSpan(absoluteSizeSpan, stringBuilder.length() - 9,
                    stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            stringBuilder.setSpan(foregroundColorSpan, stringBuilder.length() - 9,
                    stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            mMyRedPacket.setText(stringBuilder);
        }
    }

    @OnClick({R.id.back, R.id.transactionDetail, R.id.recharge, R.id.chargeDeposit,
            R.id.red_packet_page, R.id.card_voucher_page, R.id.card_voucher_recharge})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                if (ClickUtils.isFastClick()) {
                    return;
                }
//                startActivity(new Intent(WalletInfoActivity.this, PersonalCenterActivity.class));
                finish();
                break;
            case R.id.transactionDetail:
                if (ClickUtils.isFastClick()) {
                    return;
                }
                Intent dealDetail = new Intent(this, TransactionDetailActivity.class);
                dealDetail.putExtra("userId", uID);
                dealDetail.putExtra("token", mToken);
                startActivity(dealDetail);
                break;
            case R.id.recharge:
                if (ClickUtils.isFastClick()) {
                    return;
                }
//                rechargeBikeFare();
                choosePrepaid();
                break;
            case R.id.chargeDeposit:
                if (ClickUtils.isFastClick()) {
                    return;
                }
                if (chargeDeposit.getText().equals(getString(R.string.tipFour))) {
                    Intent rechargeDeposit = new Intent(WalletInfoActivity.this, RechargeDepositActivity.class);
                    startActivity(rechargeDeposit);
                } else if (chargeDeposit.getText().equals(getString(R.string.tipThere))) {
                    showRefundPopuwindow();
                }
                break;
            case R.id.red_packet_page:
                if (ClickUtils.isFastClick()) {
                    return;
                }
                if (uID != null && !uID.equals("") && !mToken.equals("") && mToken != null) {
                    Intent intent = new Intent(this, RedPacketListActivity.class);
                    intent.putExtra("userId", uID);
                    intent.putExtra("token", mToken);
                    startActivity(intent);
                }

                break;
            case R.id.card_voucher_recharge:
                if (ClickUtils.isFastClick()) {
                    return;
                }
                Intent monthCardIntent = new Intent(WalletInfoActivity.this, BuyMonthlyActivity.class);
                monthCardIntent.putExtra("userId", uID);
                monthCardIntent.putExtra("token", mToken);
                monthCardIntent.putExtra("mAggregateAmount",mAggregateAmount);
                startActivity(monthCardIntent);
                break;
        }
    }

    private void choosePrepaid() {
        if (mCashStatus == 1 && mStatus == 1) {
            startActivity(new Intent(WalletInfoActivity.this, RechargeBikeFareActivity.class));
        } else if (mCashStatus == 0 && mStatus == 0) {
            startActivity(new Intent(WalletInfoActivity.this, RechargeDepositActivity.class));
        } else if (mCashStatus == 0 && mStatus == 1) {
            popupDialog();
        } else if (mCashStatus == 1 && mStatus == 0) {
            startActivity(new Intent(WalletInfoActivity.this, IdentityAuthenticationActivity.class));
        }
    }

    private void showRefundPopuwindow() {
        if (refundPopuwindow != null && !refundPopuwindow.equals("")) {
            refundPopuwindow.dismiss();
        }
        refundPopuwindow = new RefundPopuwindow(WalletInfoActivity.this, refundViewOnClick);
        refundPopuwindow.showAtLocation(findViewById(R.id.activity_wallet_info), Gravity.CENTER, 0, 0);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.4f;
        getWindow().setAttributes(lp);
        //设置popupWindow消失时的监听
        refundPopuwindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            //在dismiss中恢复透明度
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });
    }

    private void popupDialog() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.title))
                .setMessage(getString(R.string.recharge_deposit_tip))
                .setNegativeButton(getString(R.string.toCharge), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(WalletInfoActivity.this, RechargeDepositActivity.class));
                    }
                })
                .setPositiveButton(getString(R.string.goToDeposit), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(WalletInfoActivity.this, RechargeBikeFareActivity.class));
                    }
                }).create()
                .show();
    }

    private final View.OnClickListener refundViewOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_cancel:
                    if (ClickUtils.isFastClick()) {
                        return;
                    }
                    refundPopuwindow.dismiss();
                    break;
                case R.id.btn_confirm:
                    if (ClickUtils.isFastClick()) {
                        return;
                    }
                    //先进行检查余额操作
                    if (NetUtils.isConnected(App.getContext())) {
                        checkAccountBalances(uID, mToken);
                        mDialog = StyledDialog.buildLoading(WalletInfoActivity.this, "退款中...", true, false).show();
                    } else {
                        refundPopuwindow.dismiss();
                        ToastUtils.showShort(WalletInfoActivity.this, R.string.no_network_tip);
                    }
                    break;
            }
        }
    };

    private void checkAccountBalances(final String uID, String token) {
        mMap = new HashMap<>();
        mMap.clear();
        mMap.put("userId", uID);
        mMap.put("token", token);
        OkHttpUtils.post().url(Api.BASE_URL + CHECKAGGREGATE).params(mMap).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.showShort(App.getContext(), getString(R.string.server_tip));
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d("余额", response);
                try {
                    JSONObject object = new JSONObject(response);
                    String resultStatus = object.optString("resultStatus");
                    switch (resultStatus) {
                        case "1":
                            WeixinPay weixinPay = new WeixinPay(WalletInfoActivity.this);
                            mOutRefundNo = weixinPay.getOutRefundNo();
                            if (NetUtils.isConnected(App.getContext())) {
                                refundPledgeCash(uID, mOutRefundNo, mToken, mTotal_fee, mRefund_fee);
                            } else {
                                StyledDialog.dismiss(mDialog);
                                refundPopuwindow.dismiss();
                                ToastUtils.showShort(WalletInfoActivity.this, R.string.no_network_tip);
                            }
                            break;
                        case "0":
                            StyledDialog.dismiss(mDialog);
                            refundPopuwindow.dismiss();
                            ToastUtils.showShort(WalletInfoActivity.this, getString(R.string.balance_outstanding));
                            startActivity(new Intent(WalletInfoActivity.this, RechargeBikeFareActivity.class));
                            break;

                        case "2":
                            StyledDialog.dismiss(mDialog);
                            refundPopuwindow.dismiss();
                            goToLogin();
                            break;

                        case "3":
                            StyledDialog.dismiss(mDialog);
                            refundPopuwindow.dismiss();
                            ToastUtils.showShort(WalletInfoActivity.this, getString(R.string.refund_tip));
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void goToLogin() {
        ToastUtils.showShort(App.getContext(), getString(R.string.logged_in_other_devices));
        startActivity(new Intent(WalletInfoActivity.this, LoginActivity.class));
        SPUtils.put(App.getContext(), "islogin", false);
        SPUtils.put(App.getContext(), "cashStatus", 0);
        SPUtils.put(App.getContext(), "status", 0);
        this.finish();
    }


    private void refundPledgeCash(String uID, String outRefundNo, String mToken, String mTotal_fee, String mRefund_fee) {
        mMap = new HashMap<>();
        mMap.put("userId", uID);
        mMap.put("out_refund_no", outRefundNo);
        mMap.put("total_fee", mTotal_fee);
        mMap.put("refund_fee", mRefund_fee);
        mMap.put("token", mToken);
        LogUtils.e("退款", uID + "\n" + outRefundNo + "\n" + mTotal_fee + "\n" + mRefund_fee + "\n" + mToken);
        OkHttpUtils.post().url(Api.BASE_URL + Api.REFUNDWXPAY).params(mMap).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                StyledDialog.dismiss(mDialog);
                ToastUtils.showShort(App.getContext(), getString(R.string.server_tip));
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d("退款", response);
                //{"resultStatus":"0"}
                StyledDialog.dismiss(mDialog);
                try {
                    JSONObject object = new JSONObject(response);
                    String resultStatus = object.optString("resultStatus");
                    switch (resultStatus) {
                        case "0":
                            ToastUtils.showShort(WalletInfoActivity.this, getString(R.string.refund_error));
                            break;
                        case "1":
                            SPUtils.put(App.getContext(), "cashStatus", 0);
                            startActivity(new Intent(WalletInfoActivity.this, ShowRefundResultsActivity.class));
                            break;

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                refundPopuwindow.dismiss();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCashStatus = (Integer) SPUtils.get(App.getContext(), "cashStatus", 0);
        mStatus = (Integer) SPUtils.get(App.getContext(), "status", 0);
        if (uID != null && mToken != null) {
            if (NetUtils.isConnected(App.getContext())) {
                getUserInfo(uID, mToken);
            } else {
                //如果没有网络连接，用SP中存储的数据，以给用户更好的体验
                showArea.setText("您已交押金");
                chargeDeposit.setText(R.string.tipThere);
                ToastUtils.showShort(WalletInfoActivity.this, getResources().getString(R.string.no_network_tip));
            }
        }
    }

    //从服务端拿到客户信息
    private void getUserInfo(String uID, String mToken) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", uID);
        map.put("token", mToken);
        OkHttpUtils.post().url(Api.BASE_URL + Api.INFOUSER).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.showShort(App.getContext(), getString(R.string.server_tip));
            }

            @Override
            public void onResponse(String response, int id) {
                Log.d("用户的信息", response);
                if (JsonUtils.isSuccess(response)) {
                    Gson gson = new Gson();
                    mInfo = gson.fromJson(response, UserInfo.class);
                    if (mInfo != null && !mInfo.equals("")) {
                        mTotal_fee = String.valueOf(mInfo.getPledgeCash());
                        mRefund_fee = mTotal_fee;
                        mCashStatus = mInfo.getCashStatus();
                        double merchanBillAmount = mInfo.getMerchanBillAmount();
                        mAggregateAmount = mInfo.getAggregateAmount();
                        int rideDay = mInfo.getRideDay();
                        if (rideDay == 0) {
                            mMyCardVoucher.setText("购买月卡");
                            mCardVoucherRecharge.setText("购买");
                        } else if (rideDay > 0) {
                            mMonthCardShow.setVisibility(View.VISIBLE);
                            mMyCardVoucher.setVisibility(View.GONE);
                            mRemainedDays.setText(String.valueOf(rideDay));
                            mCardVoucherRecharge.setText("续费");
                        }
                        if (String.valueOf(merchanBillAmount) != null && !String.valueOf(merchanBillAmount).equals("")) {
                            mMyRedPacketSum.setText(String.valueOf(merchanBillAmount + "元"));
                        }
                        if (!String.valueOf(mAggregateAmount).equals("") && String.valueOf(mAggregateAmount) != null) {
                            remainingSum.setText(String.valueOf(mAggregateAmount));
                        }
                        if (mCashStatus == 0) {
                            showArea.setText("押金" + String.valueOf(mInfo.getPledgeCash()) + "元");
                            chargeDeposit.setText(R.string.tipFour);

                        } else if (mCashStatus == 1) {
                            showArea.setText("押金" + String.valueOf(mInfo.getPledgeCash()) + "元");
                            chargeDeposit.setText(R.string.tipThere);
                        }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (refundPopuwindow != null) {
            refundPopuwindow.dismiss();
        }
        if (mDialog != null) {
            StyledDialog.dismiss(mDialog);
        }
    }

}
