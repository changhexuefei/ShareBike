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

import com.alipay.sdk.app.PayTask;
import com.dcch.sharebike.R;
import com.dcch.sharebike.alipay.AliPay;
import com.dcch.sharebike.alipay.PayResult;
import com.dcch.sharebike.alipay.WeixinPay;
import com.dcch.sharebike.app.App;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.http.Api;
import com.dcch.sharebike.moudle.home.content.MyContent;
import com.dcch.sharebike.moudle.user.bean.WeixinReturnInfo;
import com.dcch.sharebike.utils.ClickUtils;
import com.dcch.sharebike.utils.JsonUtils;
import com.dcch.sharebike.utils.LogUtils;
import com.dcch.sharebike.utils.NetUtils;
import com.dcch.sharebike.utils.SPUtils;
import com.dcch.sharebike.utils.ToastUtils;
import com.google.gson.Gson;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
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
    @BindView(R.id.input_moneySum)
    EditText input_moneySum;
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
    //    private static final int SDK_AUTH_FLAG = 2;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.weixin)
    TextView mWeixin;
    @BindView(R.id.recharge_agreement)
    TextView mRechargeAgreement;
    @BindView(R.id.rb_rg_1)
    RadioButton mRbRg1;
    @BindView(R.id.rb_rg_5)
    RadioButton mRbRg5;
    @BindView(R.id.rg_rec_rg)
    RadioGroup mRgRecRg;
    private String orderbody = "交车费";
    private String subject = "车费";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_recharge_bike_fare;
    }

    @Override
    protected void initData() {
        mToolbar.setTitle("");
        mTitle.setText(getString(R.string.recharge_bike_fare));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        rbfWeixinCheckbox.setChecked(true);
        mRbRg1.setChecked(true);
        String s1 = mRbRg1.getText().toString().trim();
        rechargeNumber = s1.substring(1, s1.length());
    }

    @Override
    protected void initListener() {
        rgRecRg1.setOnCheckedChangeListener(this);
        rgRecRg2.setOnCheckedChangeListener(this);
        mRgRecRg.setOnCheckedChangeListener(this);
    }

    @OnClick({R.id.recharge_agreement, R.id.rbf_aliArea, R.id.rbf_weixinArea, R.id.btn_rbf_recharge})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.recharge_agreement:
                if (ClickUtils.isFastClick()) {
                    return;
                }
                startActivity(new Intent(this, RechargeAgreementActivity.class));
                break;
            case R.id.rbf_aliArea:
                if (ClickUtils.isFastClick()) {
                    return;
                }
                rbfAliCheckbox.setChecked(true);
                rbfWeixinCheckbox.setChecked(false);
                break;
            case R.id.rbf_weixinArea:
                if (ClickUtils.isFastClick()) {
                    return;
                }
                rbfAliCheckbox.setChecked(false);
                rbfWeixinCheckbox.setChecked(true);
                break;
            case R.id.btn_rbf_recharge:
                if (ClickUtils.isFastClick()) {
                    return;
                }
                if (NetUtils.isConnected(App.getContext())) {
                    //这里要分两种情况，调取微信和支付宝的支付方式
                    if (rbfAliCheckbox.isChecked()) {
                        //选择支付宝，调取支付宝的支付方法
                        AliPay aliPay = new AliPay(this);
                        String outTradeNo = aliPay.getOutTradeNo();

                        if (uID != null && rechargeNumber != null && outTradeNo != null) {
                            aliPayWay(uID, outTradeNo, rechargeNumber, orderbody, subject);

                        } else {
                            ToastUtils.showShort(RechargeBikeFareActivity.this, getString(R.string.server_tip));
                        }
                    } else if (rbfWeixinCheckbox.isChecked()) {
                        //调取微信的支付方式

                        WeixinPay weixinPay = new WeixinPay(this);
                        if (NetUtils.isWifi(App.getContext())) {
                            ipAddress = weixinPay.getLocalIpAddress();
                        } else {
                            ipAddress = weixinPay.getIpAddress();
                        }
                        String mOutTradeNo = weixinPay.getOutTradeNo();
                        if (uID != null && mOutTradeNo != null && ipAddress != null) {
                            weiXinPayWay(mOutTradeNo, subject, uID, ipAddress, rechargeNumber);
                        } else {
                            ToastUtils.showShort(RechargeBikeFareActivity.this, getString(R.string.server_tip));
                        }
                    }
                } else {
                    ToastUtils.showShort(RechargeBikeFareActivity.this, getString(R.string.no_network_tip));
                }
                break;
        }
    }

    private void weiXinPayWay(String mOutTradeNo, String subject, String uID, String ipAddress, String rechargeNumber) {
        final IWXAPI mMsgApi = WXAPIFactory.createWXAPI(this, MyContent.APP_ID);
        Map<String, String> map = new HashMap<>();
        map.put("out_trade_no", mOutTradeNo);
        map.put("attach", uID);
        map.put("body", subject);
        map.put("total_price", rechargeNumber);
        map.put("spbill_create_ip", ipAddress);
        LogUtils.d("微信支付", ipAddress + "\n" + uID + "\n" + mOutTradeNo);
        OkHttpUtils.post().url(Api.BASE_URL + Api.WEIXINPAY).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.showShort(RechargeBikeFareActivity.this, getString(R.string.server_tip));
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d("微信支付", response);
                if (JsonUtils.isSuccess(response)) {
                    PayReq req = new PayReq();
                    Gson gson = new Gson();
                    WeixinReturnInfo weixinReturnInfo = gson.fromJson(response, WeixinReturnInfo.class);
                    req.appId = weixinReturnInfo.getAppid();
                    req.partnerId = weixinReturnInfo.getMch_id();
                    req.prepayId = weixinReturnInfo.getPrepay_id();
                    req.packageValue = weixinReturnInfo.getPackageid();
                    req.nonceStr = weixinReturnInfo.getNonce_str();
                    req.timeStamp = weixinReturnInfo.getTimestamp();
                    req.sign = weixinReturnInfo.getSign();
                    req.extData = "app data"; // optional
                    LogUtils.d("微信支付", req.appId + "\n" + req.partnerId + "\n" + req.prepayId + "\n" + req.nonceStr + "\n" + req.timeStamp + "\n" + req.packageValue + "\n" + req.sign + "\n" + req.extData);
                    mMsgApi.sendReq(req);
                } else {
                    ToastUtils.showShort(RechargeBikeFareActivity.this, getString(R.string.server_tip));
                }
            }
        });

    }

    private void aliPayWay(String uID, String outTradeNo, String rechargeNumber, String orderbody, String subject) {
        Map<String, String> map = new HashMap<>();
        map.put("passback_params", uID);
        map.put("outtradeno", outTradeNo);
        map.put("orderbody", orderbody);
        map.put("subject", subject);
        map.put("money", rechargeNumber);
        OkHttpUtils.post().url(Api.BASE_URL + Api.ALIPAY).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.showShort(RechargeBikeFareActivity.this, getString(R.string.server_tip));
            }

            @Override
            public void onResponse(final String response, int id) {
                LogUtils.d("支付", response);
                Runnable payRunnable = new Runnable() {
                    @Override
                    public void run() {
//                        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);沙箱测试
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
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        switch (checkedId) {
            case R.id.rb_rg1_10:
                rgRecRg2.clearCheck();
                mRgRecRg.clearCheck();
                radioGroup.check(R.id.rb_rg1_10);
                String s1 = rbRg110.getText().toString().trim();
                rechargeNumber = "";
                rechargeNumber = s1.substring(1, s1.length());

                break;
            case R.id.rb_rg1_20:
                rgRecRg2.clearCheck();
                mRgRecRg.clearCheck();
                radioGroup.check(R.id.rb_rg1_20);
                String s2 = rbRg120.getText().toString().trim();
                rechargeNumber = "";
                rechargeNumber = s2.substring(1, s2.length());
                break;

            case R.id.rb_rg2_50:
                rgRecRg1.clearCheck();
                mRgRecRg.clearCheck();
                radioGroup.check(R.id.rb_rg2_50);
                String s3 = rbRg250.getText().toString().trim();
                rechargeNumber = "";
                rechargeNumber = s3.substring(1, s3.length());

                break;
            case R.id.rb_rg2_100:
                rgRecRg1.clearCheck();
                mRgRecRg.clearCheck();
                radioGroup.check(R.id.rb_rg2_100);
                String s4 = rbRg2100.getText().toString().trim();
                rechargeNumber = "";
                rechargeNumber = s4.substring(1, s4.length());

                break;
            case R.id.rb_rg_1:
                rgRecRg1.clearCheck();
                rgRecRg2.clearCheck();
                radioGroup.check(R.id.rb_rg_1);
                String s5 = mRbRg1.getText().toString().trim();
                rechargeNumber = "";
                rechargeNumber = s5.substring(1, s5.length());

                break;
            case R.id.rb_rg_5:
                rgRecRg1.clearCheck();
                rgRecRg2.clearCheck();
                radioGroup.check(R.id.rb_rg_5);
                String s6 = mRbRg5.getText().toString().trim();
                rechargeNumber = "";
                rechargeNumber = s6.substring(1, s6.length());
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
//                        updateRechargeInfo(uID, rechargeNumber);
                        Toast.makeText(RechargeBikeFareActivity.this, "支付成功",
                                Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RechargeBikeFareActivity.this, WalletInfoActivity.class));
                        finish();
//                        returnData(rechargeNumber);
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

    @Override
    protected void onResume() {
        super.onResume();
        if (SPUtils.isLogin()) {
            String userDetail = (String) SPUtils.get(App.getContext(), "userDetail", "");
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
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

}
