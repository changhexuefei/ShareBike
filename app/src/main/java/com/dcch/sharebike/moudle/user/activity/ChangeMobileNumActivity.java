package com.dcch.sharebike.moudle.user.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
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
import com.dcch.sharebike.utils.ClickUtils;
import com.dcch.sharebike.utils.InPutUtils;
import com.dcch.sharebike.utils.LogUtils;
import com.dcch.sharebike.utils.NetUtils;
import com.dcch.sharebike.utils.ToastUtils;
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

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CODE_ING://已发送,倒计时
                    if (TIME > 0) {
                        mGetNewSecurityCode.setText("(" + --TIME + "s)");
                    }
                    if (TIME <= 0) {
                        TIME = 60;
                    }
                    mGetNewSecurityCode.setBackgroundColor(Color.parseColor("#c6bfbf"));
                    mGetNewSecurityCode.setClickable(false);
                    break;
                case CODE_REPEAT://重新发送
                    mGetNewSecurityCode.setText("重新获取验证码");
                    mGetNewSecurityCode.setBackgroundColor(Color.parseColor("#F05B47"));
                    mGetNewSecurityCode.setClickable(true);
                    break;

            }
        }
    };
    private String mToken;
    private String mUserId;


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
                    ToastUtils.showLong(ChangeMobileNumActivity.this, "请检查网络后重试！！");
                }
                break;
            case R.id.change_cell_phone:
                if (mToken != null && mUserId != null) {
                    changeUserCellPhone(mToken,mUserId,phone);
                }
                break;
        }
    }

    private void changeUserCellPhone(String token, String userId, String phone) {
        Map<String,String> map = new HashMap<>();
        map.put("userId",userId);
        map.put("phone",phone);
        map.put("token",token);
        OkHttpUtils.post().url(Api.BASE_URL+Api.EDITUSERPHONE).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.e("错误",e.getMessage());
                ToastUtils.showShort(ChangeMobileNumActivity.this,"服务器忙，请稍后再试！");
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d("更换手机号",response);
                //{"resultStatus":"1"}
                try {
                    JSONObject object = new JSONObject(response);
                    String resultStatus = object.optString("resultStatus");
                    if(resultStatus.equals("0")){
                        ToastUtils.showLong(ChangeMobileNumActivity.this,"更换手机号失败，请重试！");
                    }else if(resultStatus.equals("1")){
                        ToastUtils.showLong(ChangeMobileNumActivity.this,"手机号更换成功！");

                    }else if(resultStatus.equals("2")){
                        ToastUtils.showLong(ChangeMobileNumActivity.this,"您的账号在其他设备登录，您被迫下线！");
                    }else if(resultStatus.equals("3")){
                        ToastUtils.showLong(ChangeMobileNumActivity.this,"该手机号已注册，请重试！");
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
                        mGetNewSecurityCode.setBackgroundColor(Color.parseColor("#c6bfbf"));
                    } else if (!InPutUtils.isMobilePhone(phone)) {
                        mGetNewSecurityCode.setEnabled(false);
                        mGetNewSecurityCode.setBackgroundColor(Color.parseColor("#c6bfbf"));
                    } else if (!TextUtils.isEmpty(phone) && InPutUtils.isMobilePhone(phone)) {
                        mGetNewSecurityCode.setEnabled(true);
                        mGetNewSecurityCode.setBackgroundColor(Color.parseColor("#F8941D"));
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
                        mChangeCellPhone.setBackgroundColor(Color.parseColor("#c6bfbf"));
                    } else if (!TextUtils.isEmpty(phone)) {
                        mChangeCellPhone.setEnabled(true);
                        mChangeCellPhone.setBackgroundColor(Color.parseColor("#F8941D"));
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
//                        SMSSDK.getVerificationCode("86", phone);
                        getSecurityCode(phone);
                        mGetNewSecurityCode.setClickable(false);
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

    public void getSecurityCode(String phone) {
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
                ToastUtils.showLong(ChangeMobileNumActivity.this, "验证码已发送");
                try {
                    JSONObject object = new JSONObject(response);
                    verificationCode = object.getString("code");
                    mNewSecurityCode.setText(verificationCode);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


}
