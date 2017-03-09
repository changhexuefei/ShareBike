package com.dcch.sharebike.moudle.user.activity;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
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

public class RechargeBikeFareActivity extends BaseActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {


    @BindView(R.id.back)
    ImageView back;
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

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_recharge_bike_fare;
    }

    @Override
    protected void initData() {
        rbfAliCheckbox.setChecked(true);
    }

    @Override
    protected void initListener() {
        super.initListener();
        rgRecRg1.setOnCheckedChangeListener(this);
        rgRecRg2.setOnCheckedChangeListener(this);

    }

    @OnClick({R.id.back, R.id.rbf_aliArea, R.id.rbf_weixinArea, R.id.btn_rbf_recharge})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;

            case R.id.rbf_aliArea:
                rbfAliCheckbox.setChecked(true);
                rbfWeixinCheckbox.setChecked(false);
                break;
            case R.id.rbf_weixinArea:
                rbfAliCheckbox.setChecked(false);
                rbfWeixinCheckbox.setChecked(true);
                break;
            case R.id.btn_rbf_recharge:
                AliPay aliPay = new AliPay(this);
                String outTradeNo = aliPay.getOutTradeNo();
//                String moneySum = money.getText().toString().trim();
                Map<String, String> map = new HashMap<>();
                map.put("outtradeno", outTradeNo);
                map.put("orderbody", "交车费");
                map.put("subject", "车费");
                map.put("money", "0.01");
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
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        switch (checkedId){
            case R.id.rb_rg1_10:
                rgRecRg2.clearCheck();
                radioGroup.check(R.id.rb_rg1_10);

                break;
            case R.id.rb_rg1_20:
                rgRecRg2.clearCheck();
                radioGroup.check(R.id.rb_rg1_20);
                break;

            case R.id.rb_rg2_50:
                rgRecRg1.clearCheck();
                radioGroup.check(R.id.rb_rg2_50);
                break;
            case R.id.rb_rg2_100:
                rgRecRg1.clearCheck();
                radioGroup.check(R.id.rb_rg2_100);
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

                        Toast.makeText(RechargeBikeFareActivity.this, "支付成功",
                                Toast.LENGTH_SHORT).show();

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

}
