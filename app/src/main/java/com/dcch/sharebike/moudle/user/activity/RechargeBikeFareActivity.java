package com.dcch.sharebike.moudle.user.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;
import com.dcch.sharebike.R;
import com.dcch.sharebike.alipay.AliPay;
import com.dcch.sharebike.alipay.PayResult;
import com.dcch.sharebike.alipay.WeixinPay;
import com.dcch.sharebike.app.App;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.http.Api;
import com.dcch.sharebike.utils.LogUtils;
import com.dcch.sharebike.utils.NetUtils;
import com.dcch.sharebike.utils.SPUtils;
import com.dcch.sharebike.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

public class RechargeBikeFareActivity extends BaseActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {


    @BindView(R.id.moneySum)
    EditText moneySum;
    @BindView(R.id.rb_rg1_10)
    RadioButton rbRg110;
    @BindView(R.id.rb_rg1_20)
    RadioButton rbRg120;
    @BindView(R.id.rg_rec_rg1)
    RadioGroup rgRecRg1;
    @BindView(R.id.rb_rg2_50)
    RadioButton rbRg250;
    @BindView(R.id.rb_rg2_100)
    RadioButton rbRg2100;
    @BindView(R.id.rg_rec_rg2)
    RadioGroup rgRecRg2;
    @BindView(R.id.rbf_ali_checkbox)
    CheckBox rbfAliCheckbox;
    @BindView(R.id.rbf_aliArea)
    RelativeLayout rbfAliArea;
    @BindView(R.id.rbf_weixin_checkbox)
    CheckBox rbfWeixinCheckbox;
    @BindView(R.id.rbf_weixinArea)
    RelativeLayout rbfWeixinArea;
    @BindView(R.id.pay)
    LinearLayout pay;
    @BindView(R.id.btn_rbf_recharge)
    Button btnRbfRecharge;
    String rechargeNumber = "";
    String uID;
    String ipAddress = "";

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_recharge_bike_fare;
    }

    @Override
    protected void initData() {

        mToolbar.setTitle("");
        mTitle.setText(getResources().getString(R.string.recharge_bike_fare));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        rbfAliCheckbox.setChecked(true);
        rbRg110.setChecked(true);
        String s1 = rbRg110.getText().toString().trim();
        rechargeNumber = s1.substring(1, s1.length());
        if (SPUtils.isLogin()) {

            String userDetail = (String) SPUtils.get(App.getContext(), "userDetail", "");
            Log.d("ooooo", userDetail);
            if (userDetail != null) {
                try {
                    JSONObject object = new JSONObject(userDetail);
                    int userId = object.getInt("id");
                    uID = String.valueOf(userId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    @Override
    protected void initListener() {
        rgRecRg1.setOnCheckedChangeListener(this);
        rgRecRg2.setOnCheckedChangeListener(this);

    }

    @OnClick({R.id.rbf_aliArea, R.id.rbf_weixinArea, R.id.btn_rbf_recharge})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.rbf_aliArea:
                rbfAliCheckbox.setChecked(true);
                rbfWeixinCheckbox.setChecked(false);
                break;
            case R.id.rbf_weixinArea:
                rbfAliCheckbox.setChecked(false);
                rbfWeixinCheckbox.setChecked(true);
                break;
            case R.id.btn_rbf_recharge:

                //这里要分两种情况，调取微信和支付宝的支付方式
                if (rbfAliCheckbox.isChecked()) {
                    //选择支付宝，调取支付宝的支付方法
                    AliPay aliPay = new AliPay(this);
                    String outTradeNo = aliPay.getOutTradeNo();
                    Map<String, String> map = new HashMap<>();
                    map.put("outtradeno", outTradeNo);
                    map.put("orderbody", "交车费");
                    map.put("subject", "车费");
                    map.put("money", rechargeNumber);
                    ToastUtils.showShort(RechargeBikeFareActivity.this, rechargeNumber);
                    OkHttpUtils.post().url(Api.BASE_URL + Api.ALIPAY).params(map).build().execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            LogUtils.d(e.getMessage());
                        }

                        @Override
                        public void onResponse(final String response, int id) {
                            LogUtils.d("支付", response);
                            Runnable payRunnable = new Runnable() {
                                @Override
                                public void run() {
                                    EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
                                    PayTask task = new PayTask(RechargeBikeFareActivity.this);
                                    Map<String, String> stringStringMap = task.payV2(response, true);
                                    Message msg = new Message();
                                    msg.what = SDK_PAY_FLAG;
                                    msg.obj = stringStringMap;
                                    handler.sendMessage(msg);
                                }
                            };
                            // 必须异步调用
                            Thread payThread = new Thread(payRunnable);
                            payThread.start();
                        }
                    });
                } else if (rbfWeixinCheckbox.isChecked()) {
                    //调取微信的支付方式
//                    final IWXAPI msgApi = WXAPIFactory.createWXAPI(context, null);
//                    msgApi.registerApp("wxd4b5d5e34b0dd095");
                    WeixinPay weixinPay = new WeixinPay(this);

                    if (NetUtils.isConnected(App.getContext())) {
                        if (NetUtils.isWifi(App.getContext())) {
                            ipAddress = weixinPay.getLocalIpAddress();

                        } else {
                            ipAddress = weixinPay.getIpAddress();
                        }
                        String outTradeNo = weixinPay.getOutTradeNo();

                        Map<String, String> map = new HashMap<>();
                        map.put("out_trade_no", outTradeNo);
                        map.put("body", "充值");
                        map.put("total_price", "0.01");
                        map.put("spbill_create_ip", ipAddress);

                        OkHttpUtils.post().url(Api.BASE_URL + Api.WEIXINPAY).params(map).build().execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {

                            }

                            @Override
                            public void onResponse(String response, int id) {
                                LogUtils.d("微信支付", response);
                            }
                        });


                    } else {


                    }


                }
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        switch (checkedId) {
            case R.id.rb_rg1_10:
                rgRecRg2.clearCheck();
                radioGroup.check(R.id.rb_rg1_10);
                String s1 = rbRg110.getText().toString().trim();
                rechargeNumber = s1.substring(1, s1.length());
                break;
            case R.id.rb_rg1_20:
                rgRecRg2.clearCheck();
                radioGroup.check(R.id.rb_rg1_20);
                String s2 = rbRg120.getText().toString().trim();
                rechargeNumber = s2.substring(1, s2.length());
                break;

            case R.id.rb_rg2_50:
                rgRecRg1.clearCheck();
                radioGroup.check(R.id.rb_rg2_50);
                String s3 = rbRg250.getText().toString().trim();
                rechargeNumber = s3.substring(1, s3.length());
                break;
            case R.id.rb_rg2_100:
                rgRecRg1.clearCheck();
                radioGroup.check(R.id.rb_rg2_100);
                String s4 = rbRg2100.getText().toString().trim();
                rechargeNumber = s4.substring(1, s4.length());
                break;

        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    Log.d("问题原因", payResult.toString());
                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    // String resultInfo = payResult.getResult();
                    String resultStatus = payResult.getResultStatus();
                    if (TextUtils.equals(resultStatus, "9000")) {
                        updateRechargeInfo(uID, rechargeNumber);
                        Toast.makeText(RechargeBikeFareActivity.this, "支付成功",
                                Toast.LENGTH_SHORT).show();
                        returnData(rechargeNumber);
                    } else {
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(RechargeBikeFareActivity.this, "支付结果确认中",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RechargeBikeFareActivity.this, "支付失败",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                    break;
                }
            }
        }
    };

    private void returnData(String rechargeNumber) {
        Intent mIntent = new Intent();
        mIntent.putExtra("recherge", rechargeNumber);
        // 设置结果，并进行传送
        this.setResult(0, mIntent);
        this.finish();
    }


    private void updateRechargeInfo(String uID, final String rechargeNumber) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", uID);
        map.put("amount", rechargeNumber);
        OkHttpUtils.post().url(Api.BASE_URL + Api.RECHARGE).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e("修改充值信息", e.getMessage());
                ToastUtils.showShort(RechargeBikeFareActivity.this, "服务器正忙！");
            }

            @Override
            public void onResponse(String response, int id) {
                Log.d("修改的结果", response);
                //{"code":"1"}
                try {
                    JSONObject object = new JSONObject(response);
                    String code = object.optString("code");
                    if (code.equals("1")) {
                        ToastUtils.showShort(RechargeBikeFareActivity.this, "修改用户资料成功！");
                    } else if (code.equals("0")) {
                        ToastUtils.showShort(RechargeBikeFareActivity.this, "服务器正忙！");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
