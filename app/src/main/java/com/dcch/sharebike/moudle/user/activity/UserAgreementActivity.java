package com.dcch.sharebike.moudle.user.activity;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.dcch.sharebike.R;
import com.dcch.sharebike.base.BaseActivity;

import butterknife.BindView;

public class UserAgreementActivity extends BaseActivity {


    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.userAgreement)
    WebView mUserAgreement;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_agreement;
    }

    @Override
    protected void initData() {

        mToolbar.setTitle("");
        mTitle.setText(getResources().getString(R.string.user_agreement));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mUserAgreement.getSettings().setJavaScriptEnabled(true);
        mUserAgreement.getSettings().setSupportZoom(false);
        mUserAgreement.getSettings().setBuiltInZoomControls(false);
        mUserAgreement.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        mUserAgreement.getSettings().setDefaultFontSize(18);
//        mUserAgreement.getSettings().setUseWideViewPort(true);//关键点
        mUserAgreement.loadUrl("http://192.168.1.131:8080/MavenSSM/Explain/userAgreement.jsp");
    }

}
