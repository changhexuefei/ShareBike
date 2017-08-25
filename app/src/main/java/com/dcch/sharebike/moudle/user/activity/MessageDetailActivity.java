package com.dcch.sharebike.moudle.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.dcch.sharebike.R;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.view.ProgressWebview;

import butterknife.BindView;


public class MessageDetailActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.msg_webView)
    ProgressWebview mMsgWebview;
    private String mActivityUrl;
    private String mActivityTheme;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_message_detail;
    }

    @Override
    protected void initData() {

        Intent intent = getIntent();
        if (intent != null) {
            mActivityUrl = intent.getStringExtra("activityUrl");
            mActivityTheme = intent.getStringExtra("theme");
        }
        mToolbar.setTitle("");
        mTitle.setText(mActivityTheme);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMsgWebview.loadUrl(mActivityUrl);
    }
}
