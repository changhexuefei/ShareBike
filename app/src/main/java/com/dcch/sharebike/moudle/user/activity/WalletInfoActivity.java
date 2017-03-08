package com.dcch.sharebike.moudle.user.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dcch.sharebike.R;
import com.dcch.sharebike.app.App;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.utils.SPUtils;
import com.dcch.sharebike.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

public class WalletInfoActivity extends BaseActivity {
    String msg = "骑行单车必须支付押金，押金可退还。";
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
            if (cashStatus == 1) {
                showArea.setText("押金299元");
                chargeDeposit.setText("押金退款");
            } else if (cashStatus == 0) {
                showArea.setText("押金0元");
                chargeDeposit.setText("充押金");
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
                finish();
                break;
            case R.id.transactionDetail:
                Intent detail = new Intent(this, TransactionDetailActivity.class);
                startActivity(detail);
                break;
            case R.id.recharge:
                if (cashStatus == 1) {
                    Intent rechargeBikeFare = new Intent(WalletInfoActivity.this, RechargeBikeFareActivity.class);
                    startActivity(rechargeBikeFare);
                } else if (cashStatus == 0) {
                    popupDialog();
                }
                break;
            case R.id.chargeDeposit:
                Intent rechargeDeposit = new Intent(WalletInfoActivity.this, RechargeDepositActivity.class);
                startActivity(rechargeDeposit);
                break;
        }
    }

    private void popupDialog() {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage(msg)
                .setNegativeButton("充押金", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ToastUtils.showShort(WalletInfoActivity.this, which + "");
                        Intent rechargeDeposit = new Intent(WalletInfoActivity.this, RechargeDepositActivity.class);
                        startActivity(rechargeDeposit);
                    }
                })
                .setPositiveButton("去充值", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ToastUtils.showShort(WalletInfoActivity.this, which + "");
                        Intent rechargeBikeFare = new Intent(WalletInfoActivity.this, RechargeBikeFareActivity.class);
                        startActivity(rechargeBikeFare);

                    }
                }).create()
                .show();
    }

}
