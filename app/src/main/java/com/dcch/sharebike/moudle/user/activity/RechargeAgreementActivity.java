package com.dcch.sharebike.moudle.user.activity;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.dcch.sharebike.R;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.http.Api;
import com.tencent.smtt.sdk.WebView;

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

        mRechargeAgreementWebView.loadUrl(Api.RECHARGEAGREEMENT);

    }

}
