package com.dcch.sharebike.moudle.user.activity;

import android.widget.ImageView;

import com.dcch.sharebike.R;
import com.dcch.sharebike.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class MyMessageActivity extends BaseActivity {


    @BindView(R.id.back)
    ImageView mBack;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_message;
    }

    @Override
    protected void initData() {

    }


    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }
}
