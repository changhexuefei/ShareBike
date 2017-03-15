package com.dcch.sharebike.moudle.user.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dcch.sharebike.R;
import com.dcch.sharebike.app.App;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.moudle.login.activity.PersonalCenterActivity;
import com.dcch.sharebike.utils.SPUtils;
import com.dcch.sharebike.utils.ToastUtils;
import com.dcch.sharebike.view.RefundPopuwindow;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

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
    private int cashStatus;
    private RefundPopuwindow refundPopuwindow;
    private final String msg = "骑行单车必须支付押金，押金可退还。";
    private final String tipOne = "押金199元";
    private final String tipTwo = "押金0元";
    private final String tipThere = "押金退款";
    private final String tipFour = "充押金";
    private final String Title = "提示";
    private final String ToCharge = "充押金";
    private final String goToDeposit = "去充值";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_wallet_info;
    }

    @Override
    protected void initData() {
        String userDetail = (String) SPUtils.get(App.getContext(), "userDetail", "");
        Log.d("用户明细", userDetail);
        try {
            JSONObject object = new JSONObject(userDetail);
            cashStatus = object.getInt("cashStatus");
            Log.d("押金的情况", cashStatus + "");
            if (cashStatus == 1) {
                showArea.setText(tipOne);
                chargeDeposit.setText(tipThere);
            } else if (cashStatus == 0) {
                showArea.setText(tipTwo);
                chargeDeposit.setText(tipFour);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Intent intent = getIntent();
        String remainsum = intent.getStringExtra("remainSum");
        if (remainsum != null && !remainsum.equals("")) {
            remainingSum.setText(remainsum);
        }
    }

    @OnClick({R.id.back, R.id.transactionDetail, R.id.recharge, R.id.chargeDeposit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                startActivity(new Intent(this, PersonalCenterActivity.class));
                finish();
                break;
            case R.id.transactionDetail:
                startActivity(new Intent(this, TransactionDetailActivity.class));
                break;
            case R.id.recharge:
                if (cashStatus == 1) {
                    Intent intent = new Intent(WalletInfoActivity.this, RechargeBikeFareActivity.class);
                    startActivityForResult(intent, 0);
//                    startActivity();
                } else if (cashStatus == 0) {
                    popupDialog();
                }
                break;
            case R.id.chargeDeposit:
                if (chargeDeposit.getText().equals(tipFour)) {
                    startActivity(new Intent(WalletInfoActivity.this, RechargeDepositActivity.class));
                } else if (chargeDeposit.getText().equals(tipThere)) {
                    showRefundPopuwindow();
                }
                break;
        }
    }

    //充值车费的回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int i = Integer.valueOf(remainingSum.getText().toString()).intValue();
        if(data!=null){
            String recherge = data.getStringExtra("recherge");
            int i1 = Integer.parseInt(recherge.split("\\.")[0]);
            Log.d("数值",i1+"");
            String rechergeSum = String.valueOf(i+i1);
            Log.d("总和",rechergeSum);
            // 根据上面发送过去的请求码来区别
            switch (requestCode) {
                case 0:
                    remainingSum.setText(rechergeSum);
            }
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
                    ToastUtils.showShort(WalletInfoActivity.this, "您点击的是退押金按钮");
                    break;

            }
        }
    };


}
