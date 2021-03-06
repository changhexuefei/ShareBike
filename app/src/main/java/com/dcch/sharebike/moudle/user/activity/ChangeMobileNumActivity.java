package com.dcch.sharebike.moudle.user.activity;

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
import android.widget.EditText;
import android.widget.TextView;

import com.dcch.sharebike.R;
import com.dcch.sharebike.app.App;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.base.MessageEvent;
import com.dcch.sharebike.http.Api;
import com.dcch.sharebike.moudle.home.content.MyContent;
import com.dcch.sharebike.moudle.login.activity.LoginActivity;
import com.dcch.sharebike.utils.AES;
import com.dcch.sharebike.utils.ClickUtils;
import com.dcch.sharebike.utils.InPutUtils;
import com.dcch.sharebike.utils.LogUtils;
import com.dcch.sharebike.utils.NetUtils;
import com.dcch.sharebike.utils.SPUtils;
import com.dcch.sharebike.utils.ToastUtils;
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


@SuppressWarnings("ALL")
public class ChangeMobileNumActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.userNewPhone)
    EditText mUserNewPhone;
    @BindView(R.id.newSecurityCode)
    EditText mNewSecurityCode;
    @BindView(R.id.getNewSecurityCode)
    TextView mGetNewSecurityCode;
    @BindView(R.id.change_cell_phone)
    TextView mChangeCellPhone;
    private String phone;
    private String seCode;
    private static final int CODE_ING = 1;   //已发送，倒计时
    private static final int CODE_REPEAT = 2;  //重新发送
    private static final int SMSDDK_HANDLER = 3;  //短信回调
    private int TIME = 60;//倒计时60s
    private String verificationCode;
    private String mToken;
    private String mUserId;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CODE_ING://已发送,倒计时
                    if (TIME > 0) {
                        mGetNewSecurityCode.setText("(" + --TIME + "s)");
                    }
                    if (TIME <= 0) {
                        TIME = 60;
                    }
                    mGetNewSecurityCode.setBackgroundColor(getResources().getColor(R.color.btn_bg));
                    mGetNewSecurityCode.setClickable(false);
                    break;
                case CODE_REPEAT://重新发送
                    mGetNewSecurityCode.setText(getString(R.string.regain_verifyCode));
                    mNewSecurityCode.setText("");
                    mGetNewSecurityCode.setBackgroundColor(getResources().getColor(R.color.btn_bg_other));
                    mGetNewSecurityCode.setClickable(true);
                    break;

            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_change_mobile_num;
    }

    @Override
    protected void initData() {
        mToolbar.setTitle("");
        mTitle.setText(getResources().getString(R.string.change_cell_phone_number));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new MessageEvent(), "show");
                finish();
            }
        });
        Intent intent = getIntent();
        if (intent != null) {
            mToken = intent.getStringExtra("token");
            mUserId = intent.getStringExtra("userId");
        }
    }


    @OnClick({R.id.getNewSecurityCode, R.id.change_cell_phone})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.getNewSecurityCode:
                if (ClickUtils.isFastClick()) {
                    return;
                }
                if (NetUtils.isConnected(ChangeMobileNumActivity.this)) {
                    getseCode(phone);
                } else {
                    ToastUtils.showLong(ChangeMobileNumActivity.this, getString(R.string.no_network_tip));
                }
                break;
            case R.id.change_cell_phone:
                if (NetUtils.isConnected(ChangeMobileNumActivity.this)) {
                    StyledDialog.buildLoading(ChangeMobileNumActivity.this, "变更中", true, false).show();
                    if (mToken != null && mUserId != null) {
                        changeUserCellPhone(mToken, mUserId, phone);
                    }
                } else {
                    ToastUtils.showLong(ChangeMobileNumActivity.this, getString(R.string.no_network_tip));
                }
                break;
        }
    }

    private void changeUserCellPhone(String token, String userId, String phone) {
        phone = AES.encrypt(phone.getBytes(), MyContent.key);
        Map<String, String> map = new HashMap<>();
        map.put("userId", userId);
        map.put("phone", phone);
        map.put("token", token);
        OkHttpUtils.post().url(Api.BASE_URL + Api.EDITUSERPHONE).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                StyledDialog.dismissLoading();
                ToastUtils.showShort(ChangeMobileNumActivity.this, getString(R.string.server_tip));
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d("更换手机号", response);
                StyledDialog.dismissLoading();
                //{"resultStatus":"1"}
                try {
                    JSONObject object = new JSONObject(response);
                    String resultStatus = object.optString("resultStatus");
                    switch (resultStatus) {
                        case "0":
                            ToastUtils.showLong(ChangeMobileNumActivity.this, "更换手机号失败，请重试！");
                            break;
                        case "1":
                            ToastUtils.showLong(ChangeMobileNumActivity.this, "手机号更换成功！");
                            //执行退出登录，让用户重新登录
                            Intent i1 = new Intent(ChangeMobileNumActivity.this, LoginActivity.class);
                            startActivity(i1);
                            SPUtils.clear(App.getContext());
                            SPUtils.put(App.getContext(), "islogin", false);
                            SPUtils.put(App.getContext(), "isfirst", false);
                            finish();
                            break;
                        case "2":
                            ToastUtils.showLong(ChangeMobileNumActivity.this, "您的账号在其他设备登录，您被迫下线！");
                            break;
                        case "3":
                            ToastUtils.showLong(ChangeMobileNumActivity.this, "该手机号已注册，请重试！");
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }


    @Override
    protected void initListener() {
        super.initListener();
        mUserNewPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.equals("") && s != null) {
                    phone = s.toString().trim();
                    if (TextUtils.isEmpty(phone)) {
                        mGetNewSecurityCode.setEnabled(false);
                        mGetNewSecurityCode.setBackgroundColor(getResources().getColor(R.color.btn_bg));
                    } else if (!InPutUtils.isMobilePhone(phone)) {
                        mGetNewSecurityCode.setEnabled(false);
                        mGetNewSecurityCode.setBackgroundColor(getResources().getColor(R.color.btn_bg));
                    } else if (!TextUtils.isEmpty(phone) && InPutUtils.isMobilePhone(phone)) {
                        mGetNewSecurityCode.setEnabled(true);
                        mGetNewSecurityCode.setBackgroundColor(getResources().getColor(R.color.btn_bg_other));
                    }
                }
            }
        });
        mNewSecurityCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.equals("") && s != null) {
                    seCode = s.toString().trim();
                    if (TextUtils.isEmpty(seCode)) {
                        mChangeCellPhone.setEnabled(false);
                        mChangeCellPhone.setBackgroundColor(getResources().getColor(R.color.btn_bg));
                    } else if (!TextUtils.isEmpty(phone)) {
                        mChangeCellPhone.setEnabled(true);
                        mChangeCellPhone.setBackgroundColor(getResources().getColor(R.color.btn_bg_other));
                    }
                }
            }
        });

    }

    private void getseCode(final String phone) {
        new AlertDialog.Builder(ChangeMobileNumActivity.this)
                .setTitle("发送短信")
                .setMessage("我们将把验证码发送到以下号码:\n" + "+86:" + phone)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getSecurityCode(phone);
                        mGetNewSecurityCode.setClickable(false);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                for (int i = 60; i > 0; i--) {
//                                    EventBus.getDefault().post(new MessageEvent(), "change_continue");
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
//                                EventBus.getDefault().post(new MessageEvent(), "change_repeat");
                            }
                        }).start();
                    }
                })
                .create()
                .show();
    }

    private void getSecurityCode(String phone) {
        phone = AES.encrypt(phone.getBytes(), MyContent.key);
        Map<String, String> map = new HashMap<>();
        map.put("phone", phone);
        OkHttpUtils.post().url(Api.BASE_URL + Api.REGISTER).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.showLong(ChangeMobileNumActivity.this, getString(R.string.server_tip));
            }

            @Override
            public void onResponse(String response, int id) {
                Log.d("测试", response);
                ToastUtils.showLong(ChangeMobileNumActivity.this, "验证码已发送");
                try {
                    JSONObject object = new JSONObject(response);
                    verificationCode = object.getString("code");
//                    if (mNewSecurityCode != null && !mNewSecurityCode.equals("")) {
//                        mNewSecurityCode.setText(verificationCode);
//                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //接收到继续倒计时的消息
    @Subscriber(tag = "change_continue", mode = ThreadMode.MAIN)
    private void receiveContinue(MessageEvent info) {
        LogUtils.d("输入", info.toString());
        if (TIME > 0) {
            mGetNewSecurityCode.setText("(" + --TIME + "s)");
        }
        if (TIME <= 0) {
            TIME = 60;
        }
        mGetNewSecurityCode.setBackgroundColor(getResources().getColor(R.color.btn_bg));
        mGetNewSecurityCode.setClickable(false);
    }

//    //接收到继续倒计时的消息
//    @Subscriber(tag = "change_repeat", mode = ThreadMode.MAIN)
//    private void receiveRepeat(MessageEvent info) {
//        LogUtils.d("输入", info.toString());
//        mGetNewSecurityCode.setText("重新获取验证码");
//        mGetNewSecurityCode.setBackgroundColor(getResources().getColor(R.color.btn_bg_other));
//        mGetNewSecurityCode.setClickable(true);
//    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
//        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        StyledDialog.dismiss();
//        EventBus.getDefault().clear();
    }
}
