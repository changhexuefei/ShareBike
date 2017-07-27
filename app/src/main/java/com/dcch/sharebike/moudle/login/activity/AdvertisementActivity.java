package com.dcch.sharebike.moudle.login.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.dcch.sharebike.R;
import com.dcch.sharebike.base.BaseActivity;
import com.tencent.smtt.sdk.WebView;

import butterknife.BindView;

@SuppressWarnings("ALL")
public class AdvertisementActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.advertisement_webview)
    WebView mAdvertisementWebview;
    private String mActivityWebView;
    private String titleName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_advertisement;
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        if (intent != null && !intent.equals("")) {
            mActivityWebView = intent.getStringExtra("activityWebView");
            titleName = intent.getStringExtra("title");
            mAdvertisementWebview.loadUrl(mActivityWebView);
        }
        mToolbar.setTitle("");
        if (titleName != null && !titleName.equals("")) {
            mTitle.setText(titleName);
        } else {
            mTitle.setText("麒麟单车");
        }
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mAdvertisementWebview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return true;
            }
        });


    }
}
