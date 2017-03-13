package com.dcch.sharebike.moudle.login.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.dcch.sharebike.R;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.base.MessageEvent;

import org.simple.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;


public class ClickCameraPopupActivity extends BaseActivity {

    @BindView(R.id.close)
    ImageView close;
    @BindView(R.id.toLoginActivity)
    Button toLoginActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_click_camera_popup;
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.close, R.id.toLoginActivity})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.close:
                EventBus.getDefault().post(new MessageEvent(),"allShow");
                finish();
                break;
            case R.id.toLoginActivity:
                startActivity(new Intent(this,LoginActivity.class));
                finish();
                break;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
