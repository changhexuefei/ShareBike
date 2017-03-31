package com.dcch.sharebike.moudle.login.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.dcch.sharebike.MainActivity;
import com.dcch.sharebike.R;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.moudle.login.fragment.LoginFragment;
import com.dcch.sharebike.moudle.login.fragment.UnLoginFragment;
import com.dcch.sharebike.utils.SPUtils;

import butterknife.BindView;

public class PersonalCenterActivity extends BaseActivity {

    @BindView(R.id.showFragment)
    FrameLayout showFragment;
    FragmentManager supportFragmentManager;
    LoginFragment lf;
    UnLoginFragment uf;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    //记录Fragment的位置
    private int position = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_personal_center;
    }

    @Override
    protected void initData() {
        mToolbar.setTitle("");
        mTitle.setText(getResources().getString(R.string.share_title));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backToLoginMain = new Intent(PersonalCenterActivity.this, MainActivity.class);
                startActivity(backToLoginMain);
                finish();
            }
        });
        showFragment();
    }


    private void showFragment() {
        hideFragments();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        if (SPUtils.isLogin()) {
            if (lf == null) {
                lf = new LoginFragment();
                ft.add(R.id.showFragment, lf);
                ft.show(lf);
            }
        } else {
            if (uf == null) {
                uf = new UnLoginFragment();
                ft.add(R.id.showFragment, uf);
                ft.show(uf);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent backToLoginMain = new Intent(PersonalCenterActivity.this, MainActivity.class);
        startActivity(backToLoginMain);
        finish();

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        position = savedInstanceState.getInt("position");
        showFragment();
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //记录当前的position
        outState.putInt("position", position);
    }

}
