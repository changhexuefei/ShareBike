package com.dcch.sharebike.moudle.user.activity;

import android.content.Intent;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.dcch.sharebike.R;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.moudle.user.fragment.CycleFailureFragment;
import com.dcch.sharebike.moudle.user.fragment.ReportIllegalParkingFragment;
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
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        if (!name.equals("") && name != null) {
            if (name.equals("0")) {
                getSupportFragmentManager().beginTransaction().add(R.id.customerService, new UnableUnlockFragment()).commit();
            }
            if (name.equals("1")) {
                getSupportFragmentManager().beginTransaction().replace(R.id.customerService, new CycleFailureFragment()).commit();
            }
            if (name.equals("2")) {
                getSupportFragmentManager().beginTransaction().replace(R.id.customerService, new ReportIllegalParkingFragment()).commit();

            }
            if (name.equals("3")) {

            }
        }
    }


    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }
}
