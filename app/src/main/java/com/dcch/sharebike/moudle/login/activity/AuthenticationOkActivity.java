package com.dcch.sharebike.moudle.login.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dcch.sharebike.MainActivity;
import com.dcch.sharebike.R;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.base.MessageEvent;
import com.dcch.sharebike.utils.ClickUtils;

import org.simple.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

public class AuthenticationOkActivity extends BaseActivity {

    @BindView(R.id.back_to_main)
    Button backToMain;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_authentication_ok;
    }

    @Override
    protected void initData() {
        mToolbar.setTitle("");
        mTitle.setText(getResources().getString(R.string.authen_ok));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backToLoginMain = new Intent(AuthenticationOkActivity.this, MainActivity.class);
                startActivity(backToLoginMain);
                finish();
            }
        });
    }

    @OnClick( R.id.back_to_main)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_to_main:
                if(ClickUtils.isFastClick()){
                    return;
                }
                Intent backToMain = new Intent(this, MainActivity.class);
                EventBus.getDefault().post(new MessageEvent(), "gone");
                startActivity(backToMain);
                //这里需要将验证成功的消息发送到服务器，从服务器中获得标记，并且将信息
                //保存到本地的sp文件中
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent backToLoginMain = new Intent(AuthenticationOkActivity.this, MainActivity.class);
        startActivity(backToLoginMain);
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
