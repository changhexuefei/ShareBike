package com.dcch.sharebike.moudle.user.activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.dcch.sharebike.R;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.moudle.user.fragment.CycleFailureFragment;
import com.dcch.sharebike.moudle.user.fragment.ReportIllegalParkingFragment;
import com.dcch.sharebike.moudle.user.fragment.UnableUnlockFragment;

import butterknife.BindView;

public class CustomerServiceActivity extends BaseActivity {

    @BindView(R.id.customerService)
    FrameLayout customerService;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_customer_service;
    }

    @Override
    protected void initData() {
        mToolbar.setTitle("");
        mTitle.setText(getResources().getString(R.string.about_us));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        if (!name.equals("") && name != null) {
            if (name.equals("0")) {
                getSupportFragmentManager().beginTransaction().replace(R.id.customerService, new UnableUnlockFragment()).commit();
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

}
