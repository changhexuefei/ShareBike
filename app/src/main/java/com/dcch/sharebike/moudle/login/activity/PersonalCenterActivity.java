package com.dcch.sharebike.moudle.login.activity;

import android.support.v4.app.FragmentManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.dcch.sharebike.R;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.moudle.login.fragment.LoginFragment;

import butterknife.BindView;
import butterknife.OnClick;


public class PersonalCenterActivity extends BaseActivity {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.showFragment)
    FrameLayout showFragment;
    FragmentManager supportFragmentManager;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_personal_center;
    }

    @Override
    protected void initData() {
        supportFragmentManager = getSupportFragmentManager();
//        supportFragmentManager.beginTransaction().add(R.id.showFragment, new UnLoginFragment()).commit();
        supportFragmentManager.beginTransaction().add(R.id.showFragment, new LoginFragment()).commit();

    }


    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }
}
