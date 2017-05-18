package com.dcch.sharebike.moudle.login.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;
import org.simple.eventbus.EventBus;

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
    TextView confirm;
    @BindView(R.id.rules)
    TextView rules;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.title)
    TextView mTitle;
    String ciphertext;

    private static final int CODE_ING = 1;   //已发送，倒计时
    private static final int CODE_REPEAT = 2;  //重新发送
    private static final int SMSDDK_HANDLER = 3;  //短信回调
    private int TIME = 60;//倒计时60s
    private String seCode;
    private String phone;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CODE_ING://已发送,倒计时
                    if (TIME > 0) {
                        getSecurityCode.setText("(" + --TIME + "s)");
                    }
                    if (TIME <= 0) {
                        TIME = 60;
                    }
                    getSecurityCode.setBackgroundColor(Color.parseColor("#c6bfbf"));
                    getSecurityCode.setClickable(false);
                    break;
                case CODE_REPEAT://重新发送
                    getSecurityCode.setText("重新获取验证码");
                    getSecurityCode.setBackgroundColor(Color.parseColor("#F05B47"));
                    getSecurityCode.setClickable(true);
                    break;

            }
        }
    };
    private String verificationCode;


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
                        getSecurityCode.setBackgroundColor(Color.parseColor("#c6bfbf"));
                    } else if (!InPutUtils.isMobilePhone(phone)) {
                        getSecurityCode.setEnabled(false);
                        getSecurityCode.setBackgroundColor(Color.parseColor("#c6bfbf"));
                    } else if (!TextUtils.isEmpty(phone) && InPutUtils.isMobilePhone(phone)) {
                        getSecurityCode.setEnabled(true);
                        getSecurityCode.setBackgroundColor(Color.parseColor("#F8941D"));
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
                        confirm.setBackgroundColor(Color.parseColor("#c6bfbf"));
                    } else if (!TextUtils.isEmpty(phone)) {
                        confirm.setEnabled(true);
                        confirm.setBackgroundColor(Color.parseColor("#F8941D"));
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
                if (NetUtils.isConnected(LoginActivity.this)) {

                    getseCode(phone);
                } else {
                    ToastUtils.showLong(LoginActivity.this, "请检查网络后重试！！");
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
                    registerAndLogin(phone);
                } else {
                    ToastUtils.showShort(this, "验证码错误");
                }
                break;
            case R.id.rules:
                if (ClickUtils.isFastClick()) {
                    return;
                }
                Intent intent = new Intent(this, AgreementActivity.class);
                startActivity(intent);
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
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        EventBus.getDefault().unregister(this);
    }

    private void registerAndLogin(String phone) {
        phone = AES.encrypt(phone.getBytes(), MyContent.key);
        Map<String, String> map = new HashMap<>();
        map.put("phone", phone);
        OkHttpUtils.post().url(Api.BASE_URL + Api.SAVEUSER).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.e("onError:" + e.getMessage());
                ToastUtils.showShort(LoginActivity.this, "登录失败，网络异常！");
            }

            @Override
            public void onResponse(String response, int id) {
                Log.d("测试", response);
                if (JsonUtils.isSuccess(response)) {
                    Gson gson = new Gson();
                    UserInfo userInfo = gson.fromJson(response, UserInfo.class);
                    ToastUtils.showLong(LoginActivity.this, "验证码验证成功！");
                    if (userInfo.getCashStatus() == 1 && userInfo.getStatus() == 0) {
                        startActivity(new Intent(LoginActivity.this, IdentityAuthentication.class));
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
                    SPUtils.clear(LoginActivity.this);
                    SPUtils.put(LoginActivity.this, "userDetail", response);
                    LogUtils.d("userDetail", response);
                    SPUtils.put(LoginActivity.this, "islogin", true);
                    SPUtils.put(LoginActivity.this, "isfirst", false);
                } else {
                    ToastUtils.showShort(LoginActivity.this, "未知错误！请重试。");
                }
            }
        });
    }

    public void getSecurityCode(String phone) {
        phone = AES.encrypt(phone.getBytes(), MyContent.key);
        Map<String, String> map = new HashMap<>();
        map.put("phone", phone);
        OkHttpUtils.post().url(Api.BASE_URL + Api.REGISTER).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.showLong(App.getContext(), "网络错误，请重试");
            }

            @Override
            public void onResponse(String response, int id) {
                Log.d("测试", response);
                ToastUtils.showLong(LoginActivity.this, "验证码已发送");
                try {
                    JSONObject object = new JSONObject(response);
                    verificationCode = object.getString("code");
                    securityCode.setText(verificationCode);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goToPersonCenter();
    }

    private void goToPersonCenter() {
        startActivity(new Intent(LoginActivity.this, PersonalCenterActivity.class));
    }
}
