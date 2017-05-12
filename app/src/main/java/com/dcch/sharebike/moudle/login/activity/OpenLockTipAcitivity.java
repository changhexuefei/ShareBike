package com.dcch.sharebike.moudle.login.activity;

import android.widget.ImageView;

import com.dcch.sharebike.R;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.utils.ClickUtils;
import com.dcch.sharebike.utils.LogUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class OpenLockTipAcitivity extends BaseActivity {

    @BindView(R.id.open_lock_close)
    ImageView mOpenLockClose;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_open_lock_tip_acitivity;
    }

    @Override
    protected void initData() {

    }

    @OnClick(R.id.open_lock_close)
    public void onViewClicked() {
        if(ClickUtils.isFastClick()){
            return;
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.d("onDestroy","释放onDestroy");
    }
}
