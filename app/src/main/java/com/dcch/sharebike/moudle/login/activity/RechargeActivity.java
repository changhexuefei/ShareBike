package com.dcch.sharebike.moudle.login.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.dcch.sharebike.R;
import com.dcch.sharebike.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class RechargeActivity extends BaseActivity {


    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.ali)
    RadioButton ali;
    @BindView(R.id.weixin)
    RadioButton weixin;
    @BindView(R.id.pay)
    RadioGroup pay;
    @BindView(R.id.btn_recharge)
    Button btnRecharge;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_recharge;
    }

    @Override
    protected void initData() {

    }


    @OnClick({R.id.back, R.id.btn_recharge})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.btn_recharge:
                Intent intent = new Intent(this,IdentityAuthentication.class);
                startActivity(intent);
                break;
        }
    }
}
