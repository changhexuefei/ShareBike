package com.dcch.sharebike.moudle.login.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dcch.sharebike.R;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class RechargeActivity extends BaseActivity {


    @BindView(R.id.back)
    ImageView back;

    @BindView(R.id.btn_recharge)
    Button btnRecharge;
    @BindView(R.id.ali_checkbox)
    CheckBox aliCheckbox;
    @BindView(R.id.aliArea)
    RelativeLayout aliArea;
    @BindView(R.id.weixin_checkbox)
    CheckBox weixinCheckbox;
    @BindView(R.id.weixinArea)
    RelativeLayout weixinArea;
    @BindView(R.id.money)
    TextView money;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_recharge;
    }

    @Override
    protected void initData() {
        //默认支付宝选中
        aliCheckbox.setChecked(true);
        String money_sum = money.getText().toString().trim();
    }


    @OnClick({R.id.back, R.id.btn_recharge, R.id.aliArea, R.id.weixinArea})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.btn_recharge:
                Intent intent = new Intent(this, IdentityAuthentication.class);
                //将选中的支付方式和money_sum的值传递到微信或者支付宝的支付页面
                startActivity(intent);
                break;

            case R.id.aliArea:
                aliCheckbox.setChecked(true);
                weixinCheckbox.setChecked(false);

                aliCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                        if(isChecked){
                            ToastUtils.showShort(RechargeActivity.this, "选中支付宝支付");
                        }else {

                        }
                    }
                });

                break;

            case R.id.weixinArea:
                weixinCheckbox.setChecked(true);
                aliCheckbox.setChecked(false);
                weixinCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                        if(isChecked){
                            ToastUtils.showShort(RechargeActivity.this, "选中微信支付");
                        }else{

                        }
                    }
                });
                break;
        }
    }

}
