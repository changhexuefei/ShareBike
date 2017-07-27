package com.dcch.sharebike.moudle.login.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dcch.sharebike.MainActivity;
import com.dcch.sharebike.R;
import com.dcch.sharebike.app.App;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.base.MessageEvent;
import com.dcch.sharebike.http.Api;
import com.dcch.sharebike.moudle.home.content.MyContent;
import com.dcch.sharebike.moudle.user.bean.UserInfo;
import com.dcch.sharebike.utils.AES;
import com.dcch.sharebike.utils.ClickUtils;
import com.dcch.sharebike.utils.InPutUtils;
import com.dcch.sharebike.utils.JsonUtils;
import com.dcch.sharebike.utils.LogUtils;
import com.dcch.sharebike.utils.NetUtils;
import com.dcch.sharebike.utils.SPUtils;
import com.dcch.sharebike.utils.ToastUtils;
import com.google.gson.Gson;
import com.hss01248.dialog.StyledDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.userPhone)
    EditText userPhone;
    @BindView(R.id.securityCode)
    EditText securityCode;
    @BindView(R.id.getSecurityCode)
    TextView getSecurityCode;
    @BindView(R.id.login_confirm)
    Button confirm;
    @BindView(R.id.rules)
    TextView rules;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.title)
    TextView mTitle;

    private static final int CODE_ING = 1;   //已发送，倒计时
    private static final int CODE_REPEAT = 2;  //重新发送
    private static final int SMSDDK_HANDLER = 3;  //短信回调
    private int TIME = 60;//倒计时60s
    private String seCode;
    private String phone;
    private String verificationCode;
    private final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CODE_ING://已发送,倒计时
                    if (TIME > 0) {
                        getSecurityCode.setText("(" + --TIME + "s)");
                    }
                    if (TIME <= 0) {
                        TIME = 60;
                    }
                    getSecurityCode.setBackgroundColor(getResources().getColor(R.color.btn_bg));
                    getSecurityCode.setClickable(false);
                    break;
                case CODE_REPEAT://重新发送
                    getSecurityCode.setText(getString(R.string.regain_verifyCode));
                    securityCode.setText("");
                    getSecurityCode.setBackgroundColor(getResources().getColor(R.color.btn_bg_other));
                    getSecurityCode.setClickable(true);
                    break;

            }
        }
    };


    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initData() {
        mToolbar.setTitle("");
        mTitle.setText(getResources().getString(R.string.phone_verification));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new MessageEvent(), "show");
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    //
    @Override
    protected void initListener() {
        userPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.equals("") && s != null) {
                    phone = s.toString().trim();
                    if (TextUtils.isEmpty(phone)) {
                        getSecurityCode.setEnabled(false);
                        getSecurityCode.setBackgroundColor(getResources().getColor(R.color.btn_bg));
                    } else if (!InPutUtils.isMobilePhone(phone)) {
                        getSecurityCode.setEnabled(false);
                        getSecurityCode.setBackgroundColor(getResources().getColor(R.color.btn_bg));
                    } else if (!TextUtils.isEmpty(phone) && InPutUtils.isMobilePhone(phone)) {
                        getSecurityCode.setEnabled(true);
                        getSecurityCode.setBackgroundColor(getResources().getColor(R.color.btn_bg_other));
                    }
                }
            }
        });
        securityCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.equals("") && s != null) {
                    seCode = s.toString().trim();
                    if (TextUtils.isEmpty(seCode)) {
                        confirm.setEnabled(false);
                        confirm.setBackgroundColor(getResources().getColor(R.color.btn_bg));
                    } else if (!TextUtils.isEmpty(phone)) {
                        confirm.setEnabled(true);
                        confirm.setBackgroundColor(getResources().getColor(R.color.btn_bg_other));
                    }
                }
            }
        });
    }

    //, R.id.back
    @OnClick({R.id.getSecurityCode, R.id.login_confirm, R.id.rules})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.getSecurityCode:
                if (ClickUtils.isFastClick()) {
                    return;
                }
                if (NetUtils.isConnected(App.getContext())) {
                    getseCode(phone);
                } else {
                    ToastUtils.showLong(LoginActivity.this, getString(R.string.no_network_tip));
                }

                break;
            case R.id.login_confirm:
                if (ClickUtils.isFastClick()) {
                    return;
                }
                //将收到的验证码和手机号提交再次核对
//                SMSSDK.submitVerificationCode("86", phone, securityCode.getText().toString());
//                compareVerificationCode("86", phone, securityCode.getText().toString());
                if (phone != null && verificationCode != null && verificationCode.equals(seCode)) {
                    StyledDialog.buildLoading(LoginActivity.this, "登录中", true, false).show();
                    registerAndLogin(phone);
                } else {
                    ToastUtils.showShort(this, getString(R.string.verifyCodeerror));
                }
                break;
            case R.id.rules:
                if (ClickUtils.isFastClick()) {
                    return;
                }
                startActivity(new Intent(this, AgreementActivity.class));
                break;
        }
    }

    private void getseCode(final String phone) {
        new AlertDialog.Builder(LoginActivity.this)
                .setTitle("发送短信")
                .setMessage("我们将把验证码发送到以下号码:\n" + "+86:" + phone)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        SMSSDK.getVerificationCode("86", phone);
                        getSecurityCode(phone);
                        getSecurityCode.setClickable(false);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                for (int i = 60; i > 0; i--) {
                                    handler.sendEmptyMessage(CODE_ING);
//                                    EventBus.getDefault().post(new MessageEvent(), "continue");
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
//                                EventBus.getDefault().post(new MessageEvent(), "repeat");
                            }
                        }).start();
                    }
                })
                .create()
                .show();
    }


    private void registerAndLogin(String phone) {
        phone = AES.encrypt(phone.getBytes(), MyContent.key);
//        byte[] encrypt = AESUtil.encrypt(phone, MyContent.key);
//        phone = AESUtil.parseByte2HexStr(encrypt);
        Map<String, String> map = new HashMap<>();
        map.put("phone", phone);
        final String finalPhone = phone;
        OkHttpUtils.post().url(Api.BASE_URL + Api.SAVEUSER).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                StyledDialog.dismissLoading();
                ToastUtils.showShort(LoginActivity.this, getString(R.string.login_error));
            }

            @Override
            public void onResponse(String response, int id) {
                Log.d("测试", response);
                StyledDialog.dismissLoading();
                if (JsonUtils.isSuccess(response)) {
                    Gson gson = new Gson();
                    UserInfo userInfo = gson.fromJson(response, UserInfo.class);
                    ToastUtils.showLong(LoginActivity.this, "验证码验证成功！");
                    if (userInfo.getCashStatus() == 1 && userInfo.getStatus() == 0) {
                        startActivity(new Intent(LoginActivity.this, IdentityAuthenticationActivity.class));
                    } else if (userInfo.getCashStatus() == 0 && userInfo.getStatus() == 0) {
                        startActivity(new Intent(LoginActivity.this, RechargeActivity.class));
                    } else if (userInfo.getCashStatus() == 0 && userInfo.getStatus() == 1) {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    } else if (userInfo.getCashStatus() == 1 && userInfo.getStatus() == 1) {
                        goToPersonCenter();
                    }
                    EventBus.getDefault().post(new MessageEvent(), "gone");
                    LoginActivity.this.finish();
                    //储存用户信息(登录储存一次)
                    SPUtils.clear(App.getContext());
//                    SPUtils.put(App.getContext(), "userDetail", response);
                    SPUtils.put(App.getContext(), "userId", userInfo.getId());
                    SPUtils.put(App.getContext(), "islogin", true);
                    SPUtils.put(App.getContext(), "isfirst", false);
                    SPUtils.put(App.getContext(), "cashStatus", userInfo.getCashStatus());
                    SPUtils.put(App.getContext(), "status", userInfo.getStatus());
                    SPUtils.put(App.getContext(), "phone", finalPhone);
                    SPUtils.put(App.getContext(), "token", userInfo.getToken());
                } else {
                    ToastUtils.showShort(LoginActivity.this, getString(R.string.login_error));
                }
            }
        });
    }

    private void getSecurityCode(String phone) {
        phone = AES.encrypt(phone.getBytes(), MyContent.key);
//        byte[] encrypt = AESUtil.encrypt(phone, MyContent.key);
//        phone = AESUtil.parseByte2HexStr(encrypt);
        Map<String, String> map = new HashMap<>();
        map.put("phone", phone);
//        byte[] decryptFrom = AESUtil.parseHexStr2Byte(phone);
//        byte[] decryptResult = AESUtil.decrypt(decryptFrom, MyContent.key);

//        LogUtils.d("测试", phone+"\n"+decryptFrom+"\n"+encrypt+"\n"+decryptResult);
        OkHttpUtils.post().url(Api.BASE_URL + Api.REGISTER).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.showLong(LoginActivity.this, getString(R.string.server_tip));
            }

            @Override
            public void onResponse(String response, int id) {
                Log.d("测试", response);
                ToastUtils.showLong(LoginActivity.this, getString(R.string.verifyCode_send));
                try {
                    JSONObject object = new JSONObject(response);
                    verificationCode = object.optString("code");
//                    if (verificationCode != null && !verificationCode.equals("")) {
//                        Log.d("测试", verificationCode);
//                        securityCode.setText(verificationCode);
//                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.d("实验", "Login+onCreate");
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.d("实验", "Login+onPause");

    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtils.d("实验", "Login+onStop");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.d("实验", "Login+onDestroy");
        StyledDialog.dismiss();
        handler.removeCallbacksAndMessages(null);
//        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goToPersonCenter();
    }

    private void goToPersonCenter() {
        startActivity(new Intent(LoginActivity.this, PersonalCenterActivity.class));
    }

    //接收到继续倒计时的消息
    @Subscriber(tag = "continue", mode = ThreadMode.MAIN)
    private void receiveContinue(MessageEvent info) {
        LogUtils.d("输入", info.toString());
        if (TIME > 0) {
            getSecurityCode.setText("(" + --TIME + "s)");
        }
        if (TIME <= 0) {
            TIME = 60;
        }
        getSecurityCode.setBackgroundColor(getResources().getColor(R.color.btn_bg));
        getSecurityCode.setClickable(false);
    }

    //接收到继续倒计时的消息
    @Subscriber(tag = "repeat", mode = ThreadMode.MAIN)
    private void receiveRepeat(MessageEvent info) {
        LogUtils.d("输入", info.toString());
        getSecurityCode.setText(getString(R.string.regain_verifyCode));
        getSecurityCode.setBackgroundColor(getResources().getColor(R.color.btn_bg_other));
        getSecurityCode.setClickable(true);
    }

}
