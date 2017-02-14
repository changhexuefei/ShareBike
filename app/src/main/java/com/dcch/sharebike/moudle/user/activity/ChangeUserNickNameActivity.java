package com.dcch.sharebike.moudle.user.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dcch.sharebike.R;
import com.dcch.sharebike.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class ChangeUserNickNameActivity extends BaseActivity {


    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.save_nickname)
    TextView saveNickname;
    @BindView(R.id.change_nickname)
    TextView changeNickname;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_change_user_nick_name;
    }

    @Override
    protected void initData() {

    }


    @OnClick({R.id.back, R.id.save_nickname})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.save_nickname:
                getIntentInfo();
                break;
        }
    }
    public void getIntentInfo(){
        Intent intent = getIntent();
        String nickname = intent.getStringExtra("nickname");
        if(!nickname.equals("") && nickname != null){


        }

    }

}
