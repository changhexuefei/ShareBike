package com.dcch.sharebike.moudle.login.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.dcch.sharebike.MainActivity;
import com.dcch.sharebike.R;
import com.dcch.sharebike.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class AuthenticationOkActivity extends BaseActivity {


    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.back_to_main)
    Button backToMain;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_authentication_ok;
    }

    @Override
    protected void initData() {

    }


    @OnClick({R.id.back, R.id.back_to_main})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                Intent backToLoginMain = new Intent(AuthenticationOkActivity.this, MainActivity.class);
                startActivity(backToLoginMain);
                finish();
                break;
            case R.id.back_to_main:
                Intent backToMain= new Intent(this, MainActivity.class);
                backToMain.putExtra("success","ok");
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
}
