package com.dcch.sharebike.moudle.login.activity;

import android.widget.ImageView;

import com.dcch.sharebike.R;
import com.dcch.sharebike.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class PropagandaPosterActivity extends BaseActivity {


    @BindView(R.id.close)
    ImageView mClose;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_propaganda_poster;
    }

    @Override
    protected void initData() {

    }

    @OnClick(R.id.close)
    public void onViewClicked() {
        finish();
    }
}
