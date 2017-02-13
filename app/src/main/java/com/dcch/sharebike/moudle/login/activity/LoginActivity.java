package com.dcch.sharebike.moudle.login.activity;

import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dcch.sharebike.R;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.utils.InPutUtils;
import com.dcch.sharebike.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements View.OnClickListener, TextWatcher {

    @BindView(R.id.userPhone)
    EditText userPhone;
    @BindView(R.id.securityCode)
    EditText securityCode;
    @BindView(R.id.getSecurityCode)
    TextView getSecurityCode;
    @BindView(R.id.confirm)
    TextView confirm;
    @BindView(R.id.rules)
    TextView rules;
    @BindView(R.id.back)
    ImageView back;


    // 定义变量
    private String userName;
    public boolean isChange = false;
    private boolean tag = true;
    private int i = 60;
    Thread thread = null;

    /**
     * 客户端输入的验证码
     */
    private String valicationCode;
    /**
     * 服务器端获取的验证码
     */
    private static String serverValicationCode;
    /**
     * 注册时所带的参数
     */
    private Map<String, Object> registerParams = new HashMap<String, Object>();
    /**
     * 获取验证码时所带的参数
     */
    private Map<String, Object> codeParams = new HashMap<String, Object>();
    /**
     * 注册是否成功
     */
    private String regisgerStatus;
    private String seCode;
    private String phone;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initData() {

    }


    @Override
    protected void initListener() {
        confirm.setOnClickListener(this);
        getSecurityCode.setOnClickListener(this);
        securityCode.addTextChangedListener(this);
        userPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                phone = userPhone.getText().toString().trim();

                if (phone.length() == 11 && !phone.equals("")) {
                    getSecurityCode.setClickable(true);
                    getSecurityCode.setBackgroundColor(Color.parseColor("#F05B47"));

                }else{
                    getSecurityCode.setClickable(false);
                    getSecurityCode.setBackgroundColor(Color.parseColor("#c6bfbf"));
                }
            }
        });

    }

    @OnClick({R.id.getSecurityCode, R.id.confirm, R.id.rules, R.id.back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.getSecurityCode:
                if(!InPutUtils.isMobilePhone(phone)){
                    ToastUtils.showShort(LoginActivity.this, "请输入正确的手机号");
                }else {
                    ToastUtils.showShort(this,"手机号正确");
                }
                break;
            case R.id.confirm:
                ToastUtils.showLong(this, "手机验证成功！");
                Intent i1 = new Intent(this, RechargeActivity.class);
                startActivity(i1);
                finish();
                break;
            case R.id.rules:
                Intent intent = new Intent(this, AgreementActivity.class);
                startActivity(intent);
                break;
            case R.id.back:
                finish();
                break;
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
        seCode = securityCode.getText().toString().trim();
        if (!TextUtils.isEmpty(seCode) && !TextUtils.isEmpty(phone) ){
            confirm.setEnabled(true);
            confirm.setBackgroundColor(Color.parseColor("#41c0dc"));
        }else {
            confirm.setEnabled(false);
            confirm.setBackgroundColor(Color.parseColor("#c6bfbf"));
        }
    }
}
