package com.dcch.sharebike.moudle.login.activity;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.dcch.sharebike.MainActivity;
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
    LoginFragment lf;
    UnLoginFragment uf;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_personal_center;
    }

    @Override
    protected void initData() {
        showFragment();

    }

    @OnClick(R.id.back)
    public void onClick() {
        Intent backToLoginMain = new Intent(PersonalCenterActivity.this, MainActivity.class);
        startActivity(backToLoginMain);
        finish();

    }

    private void showFragment() {
        hideFragments();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        Log.d("=====", name);
        if (name.equals("login")) {
            Log.d("______", "qqqqqqq");
            if (lf == null) {
                lf = new LoginFragment();
                ft.add(R.id.showFragment, lf);
                ft.show(lf);
                Log.d("+++++++", "wwwwwww");
            }
        }
        if (name.equals("unLogin")) {
            if (uf == null) {
                uf = new UnLoginFragment();
                ft.add(R.id.showFragment, uf);
                ft.show(uf);
                Log.d("######", "eeeeee");
            }
        }
        ft.commit();
    }


    /**
     * 隐藏所有fragment
     */
    private void hideFragments() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (lf != null) {
            transaction.hide(lf);
        }
        if (uf != null) {
            transaction.hide(uf);
        }

        transaction.commit();
    }

}
