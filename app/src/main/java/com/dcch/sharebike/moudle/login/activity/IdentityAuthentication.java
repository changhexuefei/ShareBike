package com.dcch.sharebike.moudle.login.activity;

import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.dcch.sharebike.MainActivity;
import com.dcch.sharebike.R;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.utils.InPutUtils;
import com.dcch.sharebike.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 实名认证页面
 */

public class IdentityAuthentication extends BaseActivity implements TextWatcher {


    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.IDCardNo)
    EditText IDCardNo;
    @BindView(R.id.btn_authentication)
    Button btnAuthentication;
    @BindView(R.id.userRealName)
    EditText userRealName;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_identity_authentication;
    }

    @Override
    protected void initData() {
        IDCardNo.addTextChangedListener(this);
        userRealName.addTextChangedListener(this);
    }

    @OnClick({R.id.back, R.id.btn_authentication})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.btn_authentication:

                Intent backToMain = new Intent(this, MainActivity.class);
//                backToMain.putExtra("result","correct");
                startActivity(backToMain);
                ToastUtils.showShort(this,"实名认证成功");
                finish();
                break;
        }
    }

    //获得用户输入的姓名和身份证号的方法
    public void getPersonNameAndIDCard() {
        String realName = userRealName.getText().toString().trim();
        ToastUtils.showShort(this, realName);
        String cardNum = IDCardNo.getText().toString().trim();
        if (!TextUtils.isEmpty(realName)&&!TextUtils.isEmpty(cardNum) && InPutUtils.IDCardValidate(cardNum)) {
//            ToastUtils.showShort(this, cardNum);
            btnAuthentication.setClickable(true);
            btnAuthentication.setBackgroundColor(Color.parseColor("#41c0dc"));

        } else {
            btnAuthentication.setClickable(false);
            btnAuthentication.setBackgroundColor(Color.parseColor("#c6bfbf"));
        }


    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
//        ToastUtils.showShort(this,editable.toString().trim());
        getPersonNameAndIDCard();

    }
}
