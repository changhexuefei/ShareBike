package com.dcch.sharebike.moudle.user.activity;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.dcch.sharebike.R;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.http.Api;

import butterknife.BindView;

public class RechargeAgreementActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recharge_agreement_webView)
    WebView mRechargeAgreementWebView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_recharge_agreement;
    }

    @Override
    protected void initData() {
        mToolbar.setTitle("");
        mTitle.setText(getResources().getString(R.string.recharge_agreement));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mRechargeAgreementWebView.getSettings().setJavaScriptEnabled(true);
        mRechargeAgreementWebView.getSettings().setSupportZoom(false);
        mRechargeAgreementWebView.getSettings().setBuiltInZoomControls(false);
        mRechargeAgreementWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        mUserAgreement.getSettings().setDefaultFontSize(18);
//        mUserAgreement.getSettings().setUseWideViewPort(true);//关键点
        mRechargeAgreementWebView.loadUrl(Api.RECHARGEAGREEMENT);

    }

}
