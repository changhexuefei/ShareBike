package com.dcch.sharebike.moudle.user.activity;

import android.widget.FrameLayout;
import android.widget.ImageView;

import com.dcch.sharebike.R;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.moudle.user.fragment.UnableUnlockFragment;

import butterknife.BindView;
import butterknife.OnClick;

public class CustomerServiceActivity extends BaseActivity {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.customerService)
    FrameLayout customerService;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_customer_service;
    }

    @Override
    protected void initData() {
         getSupportFragmentManager().beginTransaction().add(R.id.customerService,new UnableUnlockFragment()).commit();

    }


    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }
}
