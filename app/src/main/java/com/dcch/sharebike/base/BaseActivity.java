package com.dcch.sharebike.base;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.dcch.sharebike.app.App;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/2/7 0007.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        App.getInstance().addActivity(this);
        initData();
        initListener();
    }

    protected void initListener() {
    }

    protected abstract int getLayoutId();

    protected abstract void initData();

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        App.getInstance().exit();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
