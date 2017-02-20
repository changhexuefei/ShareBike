package com.dcch.sharebike.moudle.user.activity;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.dcch.sharebike.R;
import com.dcch.sharebike.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class RechargeBikeFareActivity extends BaseActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {


    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.moneySum)
    EditText moneySum;
    @BindView(R.id.rb_rg1_10)
    RadioButton rbRg110;
    @BindView(R.id.rb_rg1_20)
    RadioButton rbRg120;
    @BindView(R.id.rg_rec_rg1)
    RadioGroup rgRecRg1;
    @BindView(R.id.rb_rg2_50)
    RadioButton rbRg250;
    @BindView(R.id.rb_rg2_100)
    RadioButton rbRg2100;
    @BindView(R.id.rg_rec_rg2)
    RadioGroup rgRecRg2;
    @BindView(R.id.rbf_ali_checkbox)
    CheckBox rbfAliCheckbox;
    @BindView(R.id.rbf_aliArea)
    RelativeLayout rbfAliArea;
    @BindView(R.id.rbf_weixin_checkbox)
    CheckBox rbfWeixinCheckbox;
    @BindView(R.id.rbf_weixinArea)
    RelativeLayout rbfWeixinArea;
    @BindView(R.id.pay)
    LinearLayout pay;
    @BindView(R.id.btn_rbf_recharge)
    Button btnRbfRecharge;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_recharge_bike_fare;
    }

    @Override
    protected void initData() {
        rbfAliCheckbox.setChecked(true);
    }

    @Override
    protected void initListener() {
        super.initListener();
        rgRecRg1.setOnCheckedChangeListener(this);
        rgRecRg2.setOnCheckedChangeListener(this);

    }

    @OnClick({R.id.back, R.id.rbf_aliArea, R.id.rbf_weixinArea, R.id.btn_rbf_recharge})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;

            case R.id.rbf_aliArea:
                rbfAliCheckbox.setChecked(true);
                rbfWeixinCheckbox.setChecked(false);
                break;
            case R.id.rbf_weixinArea:
                rbfAliCheckbox.setChecked(false);
                rbfWeixinCheckbox.setChecked(true);
                break;
            case R.id.btn_rbf_recharge:
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        switch (checkedId){
            case R.id.rb_rg1_10:
                rgRecRg2.clearCheck();
                radioGroup.check(R.id.rb_rg1_10);

                break;
            case R.id.rb_rg1_20:
                rgRecRg2.clearCheck();
                radioGroup.check(R.id.rb_rg1_20);
                break;

            case R.id.rb_rg2_50:
                rgRecRg1.clearCheck();
                radioGroup.check(R.id.rb_rg2_50);
                break;
            case R.id.rb_rg2_100:
                rgRecRg1.clearCheck();
                radioGroup.check(R.id.rb_rg2_100);
                break;

        }
    }
}
