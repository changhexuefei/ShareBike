package com.dcch.sharebike.moudle.user.activity;

import android.widget.ImageView;

import com.dcch.sharebike.R;
import com.dcch.sharebike.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class CreditIntegralActivity extends BaseActivity {

    @BindView(R.id.back)
    ImageView back;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_credit_integral;
    }

    @Override
    protected void initData() {

    }


    @OnClick(R.id.back)
    public void onClick() {
        finish();

    }
}
