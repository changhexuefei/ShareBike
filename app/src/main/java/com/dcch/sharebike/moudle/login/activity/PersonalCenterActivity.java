package com.dcch.sharebike.moudle.login.activity;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.dcch.sharebike.R;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.moudle.login.fragment.LoginFragment;
import com.dcch.sharebike.moudle.login.fragment.UnLoginFragment;

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
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        if (name.equals("login")) {
            supportFragmentManager.beginTransaction().add(R.id.showFragment, new LoginFragment()).commit();
        }
        if (name.equals("unLogin")) {
            supportFragmentManager.beginTransaction().add(R.id.showFragment, new UnLoginFragment()).commit();
        }

    }

    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }
}
