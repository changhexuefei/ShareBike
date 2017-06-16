package com.dcch.sharebike.moudle.user.activity;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.dcch.sharebike.R;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.http.Api;
import com.dcch.sharebike.view.ProgressWebview;

import butterknife.BindView;

public class RefundExplainActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.refund_explain_web_view)
    ProgressWebview mRefundExplainWebView;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_refund_explain;
    }

    @Override
    protected void initData() {
        mToolbar.setTitle("");
        mTitle.setText(getResources().getString(R.string.refund_instructions));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mRefundExplainWebView.loadUrl(Api.REFUNDEXPLAIN);
        mRefundExplainWebView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return true;
            }
        });
    }

}
