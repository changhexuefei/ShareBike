package com.dcch.sharebike.moudle.user.activity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dcch.sharebike.R;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.utils.ClickUtils;
import com.dcch.sharebike.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class ChangeUserNickNameActivity extends BaseActivity {


    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.save_nickname)
    TextView saveNickname;
    @BindView(R.id.change_nickname)
    EditText changeNickname;
    private String mNickname;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_change_user_nick_name;
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        mNickname = intent.getStringExtra("nickname");
        if (!mNickname.equals("") && mNickname != null) {
            changeNickname.setText(mNickname);
        }


    }


    @OnClick({R.id.back, R.id.save_nickname})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                if(ClickUtils.isFastClick()){
                    return;
                }
                finish();
                break;
            case R.id.save_nickname:
                if(ClickUtils.isFastClick()){
                    return;
                }
                String newName = changeNickname.getText().toString().trim();
                if (newName != null) {
                    if (newName.equals("")) {
                        ToastUtils.showLong(ChangeUserNickNameActivity.this, "昵称不能为空！");
                    }else if (newName.equals(mNickname)) {
                        ToastUtils.showLong(ChangeUserNickNameActivity.this, "昵称不能相同，请重新提交！");
                    }else {
                        saveNewName(newName);
                        finish();
                    }
                }

                break;
        }
    }

    private void saveNewName(String newName) {
        Intent result = new Intent();
        result.putExtra("newName", newName);
        this.setResult(0, result);
    }


}
