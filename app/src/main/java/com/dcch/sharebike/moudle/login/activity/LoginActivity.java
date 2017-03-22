package com.dcch.sharebike.moudle.login.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dcch.sharebike.MainActivity;
import com.dcch.sharebike.R;
import com.dcch.sharebike.app.App;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.base.MessageEvent;
import com.dcch.sharebike.http.Api;
import com.dcch.sharebike.moudle.user.bean.UserInfo;
import com.dcch.sharebike.utils.InPutUtils;
import com.dcch.sharebike.utils.JsonUtils;
import com.dcch.sharebike.utils.LogUtils;
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
//                case SMSDDK_HANDLER:
//                    int event = msg.arg1;
//                    int result = msg.arg2;
//                    Object data = msg.obj;
//                    //回调完成
//                    if (result == SMSSDK.RESULT_COMPLETE) {
//                        //验证码验证成功
//                        if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
//                            Toast.makeText(LoginActivity.this, "验证成功", Toast.LENGTH_LONG).show();
//                            Intent i1 = new Intent(LoginActivity.this, RechargeActivity.class);
//                            startActivity(i1);
//                            finish();
//                        }
//                        //已发送验证码
//                        else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
//                            Toast.makeText(getApplicationContext(), "验证码已经发送",
//                                    Toast.LENGTH_SHORT).show();
//                        } else {
//                            ((Throwable) data).printStackTrace();
//                        }
//                    }
//                    if (result == SMSSDK.RESULT_ERROR) {
//                        try {
//                            Throwable throwable = (Throwable) data;
//                            throwable.printStackTrace();
//                            JSONObject object = new JSONObject(throwable.getMessage());
//                            String des = object.optString("detail");//错误描述
//                            int status = object.optInt("status");//错误代码
//                            if (status > 0 && !TextUtils.isEmpty(des)) {
//                                Toast.makeText(getApplicationContext(), des, Toast.LENGTH_SHORT).show();
//                                return;
//                            }
//                        } catch (Exception e) {
//                            //do something
//                        }
//                    }
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
//        initSDK();
        init();
    }

    public void init() {


    }

//    private void initSDK() {
//        SMSSDK.initSDK(LoginActivity.this, "1b4c24f4f4475", "3ba2116b2a11e2836bc5eb1a00fa84ac");
//        EventHandler eventHandler = new EventHandler() {
//            @Override
//            public void afterEvent(int event, int result, Object data) {
//                Message msg = new Message();
//                msg.arg1 = event;
//                msg.arg2 = result;
//                msg.obj = data;
//                msg.what = SMSDDK_HANDLER;
//                handler.sendMessage(msg);
//            }
//        };
    //注册回调监听接口
//        SMSSDK.registerEventHandler(eventHandler);
//    }

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

    @OnClick({R.id.getSecurityCode, R.id.login_confirm, R.id.rules, R.id.back})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.getSecurityCode:

                getseCode(phone);

                break;
            case R.id.login_confirm:
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
                Intent intent = new Intent(this, AgreementActivity.class);
                startActivity(intent);
                break;
            case R.id.back:
                EventBus.getDefault().post(new MessageEvent(), "show");
                finish();
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
//        SMSSDK.unregisterAllEventHandler();
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void registerAndLogin(String phone) {
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
                if(JsonUtils.isSuccess(response)){
                    Gson gson = new Gson();
                    UserInfo userInfo = gson.fromJson(response, UserInfo.class);
                    ToastUtils.showLong(LoginActivity.this, "验证码验证成功！");
                    if (userInfo.getCashStatus() == 1 && userInfo.getStatus() == 0) {
                        startActivity(new Intent(LoginActivity.this, IdentityAuthentication.class));
                    } else if (userInfo.getCashStatus() == 0 && userInfo.getStatus() == 0) {
                        startActivity(new Intent(LoginActivity.this, RechargeActivity.class));
                    } else if (userInfo.getCashStatus() == 0 && userInfo.getStatus() == 1) {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }else if(userInfo.getCashStatus() == 1 && userInfo.getStatus() == 1){
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }

                    EventBus.getDefault().post(new MessageEvent(), "gone");
                    LoginActivity.this.finish();
                    //储存用户信息(登录储存一次)
                    SPUtils.put(App.getContext(), "userDetail", response);
                    SPUtils.put(App.getContext(), "islogin", true);

                }else {
                    ToastUtils.showShort(LoginActivity.this, "未知错误！请重试。");
                }
            }
        });
    }


    public void getSecurityCode(String phone) {

        OkHttpUtils.post().url(Api.BASE_URL + Api.REGISTER).addParams("phone", phone).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.showLong(App.getContext(), "网络错误，请重试");
                LogUtils.e(e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                Log.d("测试", response);
                ToastUtils.showLong(LoginActivity.this, "验证码已发送");
                try {
                    JSONObject object = new JSONObject(response);
                    verificationCode = object.getString("code");

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

}
