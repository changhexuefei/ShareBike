package com.dcch.sharebike.moudle.login.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;
import com.anton46.stepsview.StepsView;
import com.dcch.sharebike.MainActivity;
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

public class RechargeActivity extends BaseActivity {

    @BindView(R.id.btn_recharge)
    Button btnRecharge;
    @BindView(R.id.ali_checkbox)
    CheckBox aliCheckbox;
    @BindView(R.id.aliArea)
    RelativeLayout aliArea;
    @BindView(R.id.weixin_checkbox)
    CheckBox weixinCheckbox;
    @BindView(R.id.weixinArea)
    RelativeLayout weixinArea;
    @BindView(R.id.money)
    TextView money;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private static final int SDK_PAY_FLAG = 1;
//    private static final int SDK_AUTH_FLAG = 2;
    @BindView(R.id.stepsView)
    StepsView mStepsView;
    private String userID;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_recharge;
    }

    @Override
    protected void initData() {
        mToolbar.setTitle("");
        mTitle.setText(getResources().getString(R.string.title_recharge));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backToLoginMain = new Intent(RechargeActivity.this, MainActivity.class);
                startActivity(backToLoginMain);
                finish();
            }
        });

        //默认微信选中
        weixinCheckbox.setChecked(true);
        String userDetail = (String) SPUtils.get(App.getContext(), "userDetail", "");
        Log.d("用户明细", userDetail);
        try {
            JSONObject object = new JSONObject(userDetail);
            int id = object.getInt("id");
            Log.d("手机号", id + "");
            userID = String.valueOf(id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mStepsView.setLabels(MyContent.steps)
                .setBarColorIndicator(getResources().getColor(R.color.colorHeading))
                .setProgressColorIndicator(getResources().getColor(R.color.colorTitle))
                .setLabelColorIndicator(getResources().getColor(R.color.colorTitle))
                .setCompletedPosition(1)
                .drawView();

    }

    @OnClick({R.id.btn_recharge, R.id.aliArea, R.id.weixinArea})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.aliArea:
                aliCheckbox.setChecked(true);
                weixinCheckbox.setChecked(false);
                break;

            case R.id.weixinArea:
                weixinCheckbox.setChecked(true);
                aliCheckbox.setChecked(false);
                break;

            case R.id.btn_recharge:
                if(ClickUtils.isFastClick()){
                    return;
                }
                String moneySum = money.getText().toString().trim();
                String outTradeNo;
                if (aliCheckbox.isChecked()) {
                    final AliPay aliPay = new AliPay(this);
                    outTradeNo = aliPay.getOutTradeNo();
                    if (moneySum != null && userID != null && outTradeNo != null) {
                        Map<String, String> map = new HashMap<>();
                        map.put("passback_params", userID);
                        map.put("outtradeno", outTradeNo);
                        map.put("orderbody", "交押金");
                        map.put("subject", "押金");
                        map.put("money", moneySum);
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
                                        PayTask task = new PayTask(RechargeActivity.this);
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
                    } else {
                        ToastUtils.showShort(App.getContext(), "服务忙，请稍后重试");
                    }
                } else if (weixinCheckbox.isChecked()) {
                    //调取微信的支付方式
                    final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, MyContent.APP_ID);
                    WeixinPay weixinPay = new WeixinPay(this);
                    if (NetUtils.isConnected(App.getContext())) {
                        String ipAddress;
                        if (NetUtils.isWifi(App.getContext())) {
                            ipAddress = weixinPay.getLocalIpAddress();
                        } else {
                            ipAddress = weixinPay.getIpAddress();
                        }
                        outTradeNo = weixinPay.getOutTradeNo();
                        if (outTradeNo != null && userID != null && ipAddress != null) {
                            Map<String, String> map = new HashMap<>();
                            map.put("out_trade_no", outTradeNo);
                            map.put("body", "押金");
                            map.put("attach", userID);
                            map.put("total_price", "0.01");
                            map.put("spbill_create_ip", ipAddress);
                            LogUtils.d("微信支付", ipAddress);
                            OkHttpUtils.post().url(Api.BASE_URL + Api.WEIXINCASHPAY).params(map).build().execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    ToastUtils.showShort(RechargeActivity.this, "服务器正忙请稍后！");
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
                                        req.nonceStr = weixinReturnInfo.getNonce_str();
                                        req.timeStamp = weixinReturnInfo.getTimestamp();
                                        req.packageValue = weixinReturnInfo.getPackageid();
                                        req.sign = weixinReturnInfo.getSign();
                                        req.extData = "app data"; // optional
                                        msgApi.sendReq(req);
                                    } else {
                                        ToastUtils.showShort(RechargeActivity.this, "服务器正忙请稍后！");
                                    }
                                }
                            });
                        } else {
                            ToastUtils.showShort(App.getContext(), "服务忙，请稍后重试");
                        }
                    } else {
                        ToastUtils.showShort(RechargeActivity.this, "网络环境差，请稍后重试");
                    }
                }
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
                        Toast.makeText(RechargeActivity.this, "支付成功",
                                Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RechargeActivity.this, IdentityAuthentication.class));
                        finish();

                    } else {
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(RechargeActivity.this, "支付结果确认中",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RechargeActivity.this, "支付失败",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                    break;
                }
            }
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent backToLoginMain = new Intent(RechargeActivity.this, MainActivity.class);
        startActivity(backToLoginMain);
        finish();
    }

}
