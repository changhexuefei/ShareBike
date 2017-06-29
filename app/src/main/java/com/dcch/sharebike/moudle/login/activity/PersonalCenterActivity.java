package com.dcch.sharebike.moudle.login.activity;

import android.content.Intent;
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
import com.dcch.sharebike.utils.LogUtils;
import com.dcch.sharebike.utils.SPUtils;

import butterknife.BindView;

public class PersonalCenterActivity extends BaseActivity {

    @BindView(R.id.showFragment)
    FrameLayout showFragment;
    LoginFragment lf;
    UnLoginFragment uf;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    //记录Fragment的位置
    private int position = 0;
//    private static PersonalCenterActivity pca;

    public PersonalCenterActivity() {
    }

//    public static PersonalCenterActivity getInstance() {
//        // 提供一个全局的静态方法
//        if (pca == null) {
//            synchronized (PersonalCenterActivity.class) {
//                if (pca == null) {
//                    pca = new PersonalCenterActivity();
//                }
//            }
//        }
//        return pca;
//    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_personal_center;
    }

    @Override
    protected void initData() {
        mToolbar.setTitle("");
        mTitle.setText(getResources().getString(R.string.people_center));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToMain();
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
        goToMain();
    }

    private void goToMain() {
        Intent backToLoginMain = new Intent(PersonalCenterActivity.this, MainActivity.class);
        startActivity(backToLoginMain);
//        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.d("状态", SPUtils.isLogin() + "2");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (pca != null) {
//            pca = null;
//        }
//        App.getRefWatcher().watch(this);
    }
}
