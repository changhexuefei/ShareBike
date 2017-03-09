package com.dcch.sharebike.moudle.user.activity;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;
import com.dcch.sharebike.R;
import com.dcch.sharebike.alipay.AliPay;
import com.dcch.sharebike.alipay.PayResult;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.http.Api;
import com.dcch.sharebike.utils.LogUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

public class RechargeDepositActivity extends BaseActivity {


    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.figure)
    TextView figure;
    @BindView(R.id.rd_ali_checkbox)
    CheckBox rdAliCheckbox;
    @BindView(R.id.rd_aliArea)
    RelativeLayout rdAliArea;
    @BindView(R.id.rd_weixin_checkbox)
    CheckBox rdWeixinCheckbox;
    @BindView(R.id.rd_weixinArea)
    RelativeLayout rdWeixinArea;
    @BindView(R.id.btn_rd_recharge)
    Button btnRdRecharge;

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_recharge_deposit;
    }

    @Override
    protected void initData() {
        rdAliCheckbox.setChecked(true);
    }


    @OnClick({R.id.back, R.id.rd_aliArea, R.id.rd_weixinArea, R.id.btn_rd_recharge})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.rd_aliArea:
                rdAliCheckbox.setChecked(true);
                rdWeixinCheckbox.setChecked(false);
                break;
            case R.id.rd_weixinArea:
                rdAliCheckbox.setChecked(false);
                rdWeixinCheckbox.setChecked(true);
                break;
            case R.id.btn_rd_recharge:
                final AliPay aliPay = new AliPay(this);
                String outTradeNo = aliPay.getOutTradeNo();
                String moneySum = figure.getText().toString().trim();
                Map<String, String> map = new HashMap<>();
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
                                PayTask task = new PayTask(RechargeDepositActivity.this);
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
                        Toast.makeText(RechargeDepositActivity.this, "支付成功",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(RechargeDepositActivity.this, "支付结果确认中",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RechargeDepositActivity.this, "支付失败",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                    break;
                }
            }
        }
    };

}
