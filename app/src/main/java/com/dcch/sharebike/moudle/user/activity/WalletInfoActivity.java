package com.dcch.sharebike.moudle.user.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dcch.sharebike.R;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.moudle.login.activity.PersonalCenterActivity;
import com.dcch.sharebike.moudle.user.bean.UserInfo;
import com.dcch.sharebike.utils.ToastUtils;
import com.dcch.sharebike.view.RefundPopuwindow;

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
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();//"bundle"
        UserInfo user = (UserInfo)bundle.getSerializable("bundle");
        if (user != null && !user.equals("")) {
            cashStatus = user.getCashStatus();
            remainingSum.setText(String.valueOf(user.getAggregateAmount()));
            if(user.getPledgeCash()==199){
                showArea.setText("押金"+user.getPledgeCash()+"元");
                chargeDeposit.setText(tipThere);
            }else if(user.getPledgeCash()==0){
                showArea.setText("押金"+user.getPledgeCash()+"元");
                chargeDeposit.setText(tipFour);
            }
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
                } else if (cashStatus == 0) {
                    popupDialog();
                }
                break;
            case R.id.chargeDeposit:
                if (chargeDeposit.getText().equals(tipFour)) {
                    Intent rechargeDeposit = new Intent(WalletInfoActivity.this, RechargeDepositActivity.class);
                    startActivityForResult(rechargeDeposit, 1);
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
        if (data != null) {
            String recherge = data.getStringExtra("recherge");
            String deposit = data.getStringExtra("deposit");
            // 根据上面发送过去的请求码来区别
            switch (requestCode) {
                case 0:
                    int i1 = Integer.parseInt(recherge.split("\\.")[0]);
                    String rechergeSum = String.valueOf(i + i1);
                    remainingSum.setText(rechergeSum);
                    break;
                case 1:
                    showArea.setText("押金" + deposit + "元");
                    chargeDeposit.setText(tipThere);
                    break;
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
                    startActivity(new Intent(WalletInfoActivity.this,ShowRefundResultsActivity.class));
                    break;

            }
        }
    };


}
