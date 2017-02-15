package com.dcch.sharebike.moudle.login.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dcch.sharebike.R;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.utils.InPutUtils;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

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

    private static final int CODE_ING = 1;   //已发送，倒计时
    private static final int CODE_REPEAT = 2;  //重新发送
    private static final int SMSDDK_HANDLER = 3;  //短信回调
    private int TIME = 60;//倒计时60s


    private String seCode;
    private String phone;

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CODE_ING://已发送,倒计时
                    getSecurityCode.setText("(" + --TIME + "s)");
                    getSecurityCode.setBackgroundColor(Color.parseColor("#c6bfbf"));
                    getSecurityCode.setEnabled(false);
                    break;
                case CODE_REPEAT://重新发送
                    getSecurityCode.setText("获取验证码");
                    getSecurityCode.setClickable(true);
                    break;
                case SMSDDK_HANDLER:
                    int event = msg.arg1;
                    int result = msg.arg2;
                    Object data = msg.obj;
                    //回调完成
                    if (result == SMSSDK.RESULT_COMPLETE) {
                        //验证码验证成功
                        if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                            Toast.makeText(LoginActivity.this, "验证成功", Toast.LENGTH_LONG).show();
                            Intent i1 = new Intent(LoginActivity.this, RechargeActivity.class);
                            startActivity(i1);
                            finish();
                        }
                        //已发送验证码
                        else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                            Toast.makeText(getApplicationContext(), "验证码已经发送",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            ((Throwable) data).printStackTrace();
                        }
                    }
                    if (result == SMSSDK.RESULT_ERROR) {
                        try {
                            Throwable throwable = (Throwable) data;
                            throwable.printStackTrace();
                            JSONObject object = new JSONObject(throwable.getMessage());
                            String des = object.optString("detail");//错误描述
                            int status = object.optInt("status");//错误代码
                            if (status > 0 && !TextUtils.isEmpty(des)) {
                                Toast.makeText(getApplicationContext(), des, Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (Exception e) {
                            //do something
                        }
                    }
            }
        }
    };


    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initData() {
        initSDK();
        init();
    }

    public void init() {
//        phone = userPhone.getText().toString().trim();
//        seCode = securityCode.getText().toString().trim();
    }

    private void initSDK() {
        SMSSDK.initSDK(LoginActivity.this, "1b4c24f4f4475", "3ba2116b2a11e2836bc5eb1a00fa84ac");
        EventHandler eventHandler = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                msg.what = SMSDDK_HANDLER;
                handler.sendMessage(msg);
            }
        };
        //注册回调监听接口
        SMSSDK.registerEventHandler(eventHandler);
    }

    @Override
    protected void initListener() {
        userPhone.addTextChangedListener(this);
        securityCode.addTextChangedListener(this);
    }

    @OnClick({R.id.getSecurityCode, R.id.confirm, R.id.rules, R.id.back})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.getSecurityCode:
                new AlertDialog.Builder(LoginActivity.this)
                        .setTitle("发送短信")
                        .setMessage("我们将把验证码发送到以下号码:\n" + "+86:" + phone)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SMSSDK.getVerificationCode("86", phone);
                                getSecurityCode.setClickable(false);
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        for (int i = 60; i > 0; i--) {
                                            handler.sendEmptyMessage(CODE_ING);
                                            if (i <= 0) {
                                                break;
                                            }
                                            try {
                                                Thread.sleep(1000);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        handler.sendEmptyMessage(CODE_REPEAT);
                                    }
                                }).start();
                            }
                        })
                        .create()
                        .show();
                break;
            case R.id.confirm:
                //将收到的验证码和手机号提交再次核对
                SMSSDK.submitVerificationCode("86", phone, securityCode.getText().toString());
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
    protected void onDestroy() {
        SMSSDK.unregisterAllEventHandler();
        super.onDestroy();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

        phone = userPhone.getText().toString().trim();
        seCode = securityCode.getText().toString().trim();

        if (!TextUtils.isEmpty(phone) && InPutUtils.isMobilePhone(phone)) {
            getSecurityCode.setClickable(true);
            getSecurityCode.setBackgroundColor(Color.parseColor("#F05B47"));
        } else {
            getSecurityCode.setText("获取验证码");
            getSecurityCode.setClickable(false);
            getSecurityCode.setBackgroundColor(Color.parseColor("#c6bfbf"));
        }
        if (!TextUtils.isEmpty(seCode) && !TextUtils.isEmpty(phone)) {
            confirm.setEnabled(true);
            confirm.setBackgroundColor(Color.parseColor("#41c0dc"));
        } else {
            confirm.setEnabled(false);
            confirm.setBackgroundColor(Color.parseColor("#c6bfbf"));
        }

    }
}
