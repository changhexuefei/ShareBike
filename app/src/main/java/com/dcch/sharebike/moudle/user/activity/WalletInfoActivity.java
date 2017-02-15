package com.dcch.sharebike.moudle.user.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dcch.sharebike.R;
import com.dcch.sharebike.app.App;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.utils.ToastUtils;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyDialogListener;

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

    @Override
    protected int getLayoutId() {
        return R.layout.activity_wallet_info;
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        String remainsum = intent.getStringExtra("remainSum");
        Log.d("余额",remainsum);
        if (remainsum!=null && !remainsum.equals("")){
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
                Intent detail = new Intent(this,TransactionDetailActivity.class);
                startActivity(detail);
                break;
            case R.id.recharge:
                popupDialog();
                break;
            case R.id.chargeDeposit:
                ToastUtils.showLong(this,"充押金");
                break;
        }
    }

    private void popupDialog() {
        StyledDialog.buildMdAlert(this, "提示", msg,  new MyDialogListener() {
            @Override
            public void onFirst() {
                ToastUtils.showLong(App.getContext(),"1");
            }

            @Override
            public void onSecond() {
                ToastUtils.showLong(App.getContext(),"2");
            }

        }).setBtnText("去充值", "充押金")
                .setTitleColor(R.color.colorTitle)
                .show();
    }
}
