package com.dcch.sharebike.moudle.login.activity;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.dcch.sharebike.R;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.http.Api;

import butterknife.BindView;

public class AgreementActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.agreement_webView)
    WebView mAgreementWebView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_agreement;
    }

    @Override
    protected void initData() {
        mToolbar.setTitle("");
        mTitle.setText(getResources().getString(R.string.ride_result));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mAgreementWebView.getSettings().setJavaScriptEnabled(true);
        mAgreementWebView.getSettings().setSupportZoom(false);
        mAgreementWebView.getSettings().setBuiltInZoomControls(false);
        mAgreementWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        mUserAgreement.getSettings().setDefaultFontSize(18);
//        mUserAgreement.getSettings().setUseWideViewPort(true);//关键点
        mAgreementWebView.loadUrl(Api.USERAGREEMENT);
    }
}
