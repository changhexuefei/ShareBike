package com.dcch.sharebike.moudle.user.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.dcch.sharebike.R;
import com.dcch.sharebike.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class ShowRefundResultsActivity extends BaseActivity {


    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.verify)
    Button mVerify;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_show_refund_results;
    }

    @Override
    protected void initData() {

    }


    @OnClick({R.id.back, R.id.verify})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.verify:
                startActivity(new Intent(ShowRefundResultsActivity.this,WalletInfoActivity.class));
                finish();
                break;
        }
    }
}
