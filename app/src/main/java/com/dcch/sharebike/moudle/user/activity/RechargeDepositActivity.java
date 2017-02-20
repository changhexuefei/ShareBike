package com.dcch.sharebike.moudle.user.activity;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dcch.sharebike.R;
import com.dcch.sharebike.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class RechargeDepositActivity extends BaseActivity {


    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.figure)
    TextView figure;
    @BindView(R.id.rd_ali_checkbox)
    CheckBox rdAliCheckbox;
    @BindView(R.id.rd_aliArea)
    RelativeLayout rdAliArea;
    @BindView(R.id.rd_weixin_checkbox)
    CheckBox rdWeixinCheckbox;
    @BindView(R.id.rd_weixinArea)
    RelativeLayout rdWeixinArea;
    @BindView(R.id.btn_rd_recharge)
    Button btnRdRecharge;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_recharge_deposit;
    }

    @Override
    protected void initData() {
        rdAliCheckbox.setChecked(true);
    }


    @OnClick({R.id.back, R.id.rd_aliArea, R.id.rd_weixinArea, R.id.btn_rd_recharge})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.rd_aliArea:
                rdAliCheckbox.setChecked(true);
                rdWeixinCheckbox.setChecked(false);
                break;
            case R.id.rd_weixinArea:
                rdAliCheckbox.setChecked(false);
                rdWeixinCheckbox.setChecked(true);
                break;
            case R.id.btn_rd_recharge:
                break;
        }
    }
}
