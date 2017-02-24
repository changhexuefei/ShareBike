package com.dcch.sharebike.moudle.login.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.dcch.sharebike.R;
import com.dcch.sharebike.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;


public class ClickCameraPopupActivity extends BaseActivity {


    @BindView(R.id.close)
    ImageView close;
    @BindView(R.id.toLoginActivity)
    Button toLoginActivity;

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
                finish();
                break;
            case R.id.toLoginActivity:
                startActivity(new Intent(this,LoginActivity.class));
                finish();
                break;
        }
    }
}
