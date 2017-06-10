package com.dcch.sharebike.moudle.user.activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dcch.sharebike.R;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.utils.ClickUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class ShowRefundResultsActivity extends BaseActivity {

    @BindView(R.id.verify)
    Button mVerify;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_show_refund_results;
    }

    @Override
    protected void initData() {
        mToolbar.setTitle("");
        mTitle.setText(getResources().getString(R.string.refund_result));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    @OnClick(R.id.verify)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.verify:
                if (ClickUtils.isFastClick()) {
                    return;
                }
                goToWallet();
                break;
        }
    }

    private void goToWallet() {
        startActivity(new Intent(ShowRefundResultsActivity.this, WalletInfoActivity.class));
        this.finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goToWallet();
    }

}
