package com.dcch.sharebike.moudle.user.activity;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dcch.sharebike.R;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.utils.LogUtils;
import com.louisgeek.multiedittextviewlib.MultiEditInputView;

import butterknife.BindView;
import butterknife.OnClick;

public class UnlockBillPageActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.unlockDesc)
    MultiEditInputView mUnlockDesc;
    @BindView(R.id.select_photo_two)
    ImageView mSelectPhotoTwo;
    @BindView(R.id.select_photo_area)
    RelativeLayout mSelectPhotoArea;
    @BindView(R.id.tv20)
    TextView mTv20;
    @BindView(R.id.input_moneySum)
    EditText mInputMoneySum;
    @BindView(R.id.rg_rec_rg_1)
    RadioGroup mRgRecRg1;
    @BindView(R.id.rg_rec_rg_2)
    RadioGroup mRgRecRg2;
    @BindView(R.id.select_bikefare)
    RelativeLayout mSelectBikefare;
    @BindView(R.id.rb_rg_1)
    RadioButton mRbRg1;
    @BindView(R.id.rb_rg_2)
    RadioButton mRbRg2;
    @BindView(R.id.rb_rg_3)
    RadioButton mRbRg3;
    @BindView(R.id.rb_rg1_4)
    RadioButton mRbRg14;
    @BindView(R.id.rb_rg1_5)
    RadioButton mRbRg15;
    @BindView(R.id.rb_rg1_6)
    RadioButton mRbRg16;
    @BindView(R.id.mbikefareconfirm)
    TextView mMbikefareconfirm;
    private String rechargeNumber="";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_unlock_bill_page;
    }

    @Override
    protected void initData() {
        mToolbar.setTitle("");
        mTitle.setText(getResources().getString(R.string.unlock_fare));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void initListener() {
        super.initListener();
        mRgRecRg1.setOnCheckedChangeListener(this);
        mRgRecRg2.setOnCheckedChangeListener(this);
    }

    @OnClick(R.id.mbikefareconfirm)
    public void onViewClicked() {
        String contentText = mUnlockDesc.getContentText();
        Intent commit = new Intent(UnlockBillPageActivity.this,dealFeedbackActivity.class);
        startActivity(commit);
    }


    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.rb_rg_1:
                mRgRecRg2.clearCheck();
                mRgRecRg1.check(R.id.rb_rg_1);
                String s1 = mRbRg1.getText().toString().trim();
                rechargeNumber = s1.substring(0, s1.length()-1);
                LogUtils.d("你是谁",rechargeNumber);
                break;
            case R.id.rb_rg_2:
                mRgRecRg2.clearCheck();
                mRgRecRg1.check(R.id.rb_rg_2);
                String s2 = mRbRg2.getText().toString().trim();
                rechargeNumber = s2.substring(0, s2.length()-1);
                LogUtils.d("你是谁",rechargeNumber);
                break;

            case R.id.rb_rg_3:
                mRgRecRg2.clearCheck();
                mRgRecRg1.check(R.id.rb_rg_3);
                String s3 = mRbRg3.getText().toString().trim();
                rechargeNumber = s3.substring(0, s3.length()-1);
                LogUtils.d("你是谁",rechargeNumber);
                break;
            case R.id.rb_rg1_4:
                mRgRecRg1.clearCheck();
                mRgRecRg2.check(R.id.rb_rg1_4);
                String s4 = mRbRg14.getText().toString().trim();
                rechargeNumber = s4.substring(0, s4.length()-1);
                LogUtils.d("你是谁",rechargeNumber);
                break;

            case R.id.rb_rg1_5:
                mRgRecRg1.clearCheck();
                mRgRecRg2.check(R.id.rb_rg1_5);
                String s5 = mRbRg15.getText().toString().trim();
                rechargeNumber = s5.substring(0, s5.length()-1);
                LogUtils.d("你是谁",rechargeNumber);
                break;
            case R.id.rb_rg1_6:
                mRgRecRg1.clearCheck();
                mRgRecRg2.check(R.id.rb_rg1_6);
                String s6 = mRbRg16.getText().toString().trim();
                rechargeNumber = s6.substring(0, s6.length()-1);
                LogUtils.d("你是谁",rechargeNumber);
                break;
        }
    }
}
