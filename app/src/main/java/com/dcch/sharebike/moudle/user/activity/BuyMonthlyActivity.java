package com.dcch.sharebike.moudle.user.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.dcch.sharebike.R;
import com.dcch.sharebike.alipay.AliPay;
import com.dcch.sharebike.alipay.PayResult;
import com.dcch.sharebike.alipay.WeixinPay;
import com.dcch.sharebike.app.App;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.base.MessageEvent;
import com.dcch.sharebike.http.Api;
import com.dcch.sharebike.moudle.home.content.MyContent;
import com.dcch.sharebike.moudle.login.activity.IdentityAuthenticationActivity;
import com.dcch.sharebike.moudle.login.activity.RechargeActivity;
import com.dcch.sharebike.moudle.user.bean.MonthCardInfo;
import com.dcch.sharebike.moudle.user.bean.WeixinReturnInfo;
import com.dcch.sharebike.utils.ClickUtils;
import com.dcch.sharebike.utils.JsonUtils;
import com.dcch.sharebike.utils.LogUtils;
import com.dcch.sharebike.utils.NetUtils;
import com.dcch.sharebike.utils.SPUtils;
import com.dcch.sharebike.utils.ToastUtils;
import com.dcch.sharebike.view.MonthCardView;
import com.google.android.flexbox.FlexboxLayout;
import com.google.gson.Gson;
import com.hss01248.dialog.StyledDialog;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

public class BuyMonthlyActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.buy_month_card)
    Button mBuyMonthCard;
    @BindView(R.id.month_weixin)
    TextView mMonthWeixin;
    @BindView(R.id.month_weixin_checkbox)
    CheckBox mMonthWeixinCheckbox;
    @BindView(R.id.month_weixinArea)
    RelativeLayout mMonthWeixinArea;
    @BindView(R.id.month_ali_checkbox)
    CheckBox mMonthAliCheckbox;
    @BindView(R.id.month_aliArea)
    RelativeLayout mMonthAliArea;
    @BindView(R.id.month_scrollView)
    ScrollView mMonthScrollView;
    @BindView(R.id.month_balance_checkbox)
    CheckBox mMonthBalanceCheckbox;
    @BindView(R.id.month_balance_Area)
    RelativeLayout mMonthBalanceArea;

    @BindView(R.id.one_area)
    FlexboxLayout mOneArea;
    @BindView(R.id.oneMonth)
    MonthCardView mOneMonth;
    @BindView(R.id.thereMonth)
    MonthCardView mThereMonth;
    @BindView(R.id.sixMonth)
    MonthCardView mSixMonth;
    @BindView(R.id.twelveMonth)
    MonthCardView mTwelveMonth;
    @BindView(R.id.balance_sum)
    TextView mBalanceSum;

    //@BindView(R.id.month_weixinArea)
//    PaymentView mMonthWeixinArea;
//    @BindView(R.id.month_aliArea)
//    PaymentView mMonthAliArea;
//    @BindView(R.id.month_balance_Area)
//    PaymentView mMonthBalanceArea;
    private String payNumber;
    private String orderbody = "购买月卡";
    private String subject = "月卡费用";
    private String mUserId;
    private String mToken;
    private static final int SDK_PAY_FLAG = 1;
    private Dialog mTradeDialog;
    private String payMode;
    private int mCashStatus;
    private int mStatus;
    private List<MonthCardInfo.CardHolderBean> mCardHolder;
    private double mMAggregateAmount;
    private Double mADouble;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_buy_monthly;
    }

    @Override
    protected void initData() {
        mToolbar.setTitle("");
        mTitle.setText(getString(R.string.month_card));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initListener() {
        super.initListener();

    }

    @OnClick({R.id.buy_month_card, R.id.month_weixinArea, R.id.month_aliArea,
            R.id.oneMonth, R.id.thereMonth, R.id.sixMonth, R.id.twelveMonth,
            R.id.month_balance_Area
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buy_month_card:
                if (ClickUtils.isFastClick()) {
                    return;
                }
                if (NetUtils.isConnected(App.getContext())) {
                    if (SPUtils.isLogin()) {
                        if (mCashStatus == 0 && mStatus == 0) {
                            startActivity(new Intent(BuyMonthlyActivity.this, RechargeActivity.class));
                        } else if (mCashStatus == 1 && mStatus == 0) {
                            startActivity(new Intent(BuyMonthlyActivity.this, IdentityAuthenticationActivity.class));
                        } else if (mCashStatus == 0 && mStatus == 1) {
                            startActivity(new Intent(BuyMonthlyActivity.this, RechargeDepositActivity.class));
                        } else {
                            mTradeDialog = StyledDialog.buildLoading(BuyMonthlyActivity.this, "加载中...", true, false).show();
                            //这里要分两种情况，调取微信和支付宝的支付方式
                            if (mMonthAliCheckbox.isChecked()) {
                                //选择支付宝，调取支付宝的支付方法
                                AliPay aliPay = new AliPay(this);
                                String outTradeNo = aliPay.getOutTradeNo();
                                if (mUserId != null && payNumber != null && outTradeNo != null) {
                                    LogUtils.d("支付", mUserId + "\n" + outTradeNo + "\n" + payNumber + "\n" + payMode);
                                    aliCardPayWay(mUserId, outTradeNo, payNumber, orderbody, subject, payMode);
                                } else {
                                    StyledDialog.dismissLoading();
                                    ToastUtils.showShort(BuyMonthlyActivity.this, getString(R.string.server_tip));
                                }
                            } else if (mMonthWeixinCheckbox.isChecked()) {
                                //调取微信的支付方式
                                WeixinPay weixinPay = new WeixinPay(this);
                                String ipAddress;
                                if (NetUtils.isWifi(App.getContext())) {
                                    ipAddress = weixinPay.getLocalIpAddress();
                                } else {
                                    ipAddress = weixinPay.getIpAddress();
                                }
                                String mOutTradeNo = weixinPay.getOutTradeNo();
                                if (mUserId != null && mOutTradeNo != null && ipAddress != null && payNumber != null) {
                                    weiXinCardPayWay(mOutTradeNo, subject, mUserId, ipAddress, payNumber, payMode);
                                } else {
                                    ToastUtils.showShort(BuyMonthlyActivity.this, getString(R.string.server_tip));
                                }
                            } else if (mMonthBalanceCheckbox.isChecked()) {
                                if (mADouble >= Double.valueOf(payNumber)) {
                                    String tradeType = "APP";
                                    if (payNumber != null && !payNumber.equals("") && mUserId != null && !mUserId.equals("")
                                            && payMode != null && !payMode.equals("")
                                            ) {
                                        balancePayWay(payNumber, mUserId, payMode, tradeType);
                                    }

                                } else {
                                    StyledDialog.dismissLoading();
                                    ToastUtils.showShort(BuyMonthlyActivity.this, "余额不足，请选择其他的充值方式");
                                }
                            }
                        }
                    }
                } else {
                    ToastUtils.showShort(BuyMonthlyActivity.this, getString(R.string.no_network_tip));
                }
                break;


            case R.id.month_weixinArea:
                mMonthAliCheckbox.setChecked(false);
                mMonthWeixinCheckbox.setChecked(true);
                mMonthBalanceCheckbox.setChecked(false);
                break;

            case R.id.month_aliArea:
                LogUtils.d("测试", "2");
                mMonthAliCheckbox.setChecked(true);
                mMonthWeixinCheckbox.setChecked(false);
                mMonthBalanceCheckbox.setChecked(false);

                break;

            case R.id.month_balance_Area:
                LogUtils.d("测试", "3");
                mMonthAliCheckbox.setChecked(false);
                mMonthWeixinCheckbox.setChecked(false);
                mMonthBalanceCheckbox.setChecked(true);
                break;
            case R.id.oneMonth:
                mOneMonth.setSelected(true);
                mThereMonth.setSelected(false);
                mSixMonth.setSelected(false);
                mTwelveMonth.setSelected(false);
                payNumber = mOneMonth.getLeftText().trim().substring(0,
                        mOneMonth.getLeftText().trim().length() - 1);
                payMode = mOneMonth.getBlewText().trim().substring(0, mOneMonth.getBlewText().trim().length() - 1);
                break;

            case R.id.thereMonth:
                mOneMonth.setSelected(false);
                mThereMonth.setSelected(true);
                mSixMonth.setSelected(false);
                mTwelveMonth.setSelected(false);
                payNumber = mThereMonth.getLeftText().trim().substring(0, mThereMonth.getLeftText().trim().length() - 1);
                payMode = mThereMonth.getBlewText().trim().substring(0, mThereMonth.getBlewText().trim().length() - 1);

                break;
            case R.id.sixMonth:
                mOneMonth.setSelected(false);
                mThereMonth.setSelected(false);
                mSixMonth.setSelected(true);
                mTwelveMonth.setSelected(false);
                payNumber = mSixMonth.getLeftText().trim().substring(0, mSixMonth.getLeftText().trim().length() - 1);
                payMode = mSixMonth.getBlewText().trim().substring(0, mSixMonth.getBlewText().trim().length() - 1);
                break;

            case R.id.twelveMonth:
                mOneMonth.setSelected(false);
                mThereMonth.setSelected(false);
                mSixMonth.setSelected(false);
                mTwelveMonth.setSelected(true);
                payNumber = mTwelveMonth.getLeftText().trim().substring(0, mTwelveMonth.getLeftText().trim().length() - 1);
                payMode = mTwelveMonth.getBlewText().trim().substring(0, mTwelveMonth.getBlewText().trim().length() - 1);
                break;
        }
    }

    private void balancePayWay(String payNumber, String userId, String payMode, String tradeType) {
        Map<String, String> map = new HashMap<>();
        map.put("total_price", payNumber);
        map.put("userId", userId);
        map.put("paymode", payMode);
        map.put("trade_type", tradeType);
        LogUtils.d("天数",payMode);
        OkHttpUtils.post().url(Api.BASE_URL + Api.BALANCECARDPAY).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                StyledDialog.dismissLoading();
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d("余额支付", response);
                if(response.equals("success")){
                    StyledDialog.dismissLoading();
                    BuyMonthlyActivity.this.finish();
                    ToastUtils.showShort(BuyMonthlyActivity.this,"支付成功！");
                }else{
                    StyledDialog.dismissLoading();
                    ToastUtils.showShort(BuyMonthlyActivity.this,"支付失败，请重试");
                }
            }
        });
    }

    private void weiXinCardPayWay(String mOutTradeNo, String subject, String userId, String ipAddress, String payNumber, String payMode) {
        final IWXAPI mMsgApi = WXAPIFactory.createWXAPI(this, MyContent.APP_ID);
        Map<String, String> map = new HashMap<>();
        map.put("out_trade_no", mOutTradeNo);
        map.put("attach", userId);
        map.put("body", subject);
        map.put("total_price", payNumber);
        map.put("spbill_create_ip", ipAddress);
        map.put("paymode", payMode);
        LogUtils.d("微信支付", ipAddress + "\n" + userId + "\n" + mOutTradeNo + "\n" + payMode);
        OkHttpUtils.post().url(Api.BASE_URL + Api.WEIXINCARDPAY).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                StyledDialog.dismiss(mTradeDialog);
                ToastUtils.showShort(BuyMonthlyActivity.this, getString(R.string.server_tip));
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d("微信支付", response);
                StyledDialog.dismiss(mTradeDialog);
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
                    ToastUtils.showShort(BuyMonthlyActivity.this, getString(R.string.server_tip));
                }
            }
        });
    }

    private void aliCardPayWay(String userId, String outTradeNo, String payNumber, String orderbody, String subject, String payMode) {
        Map<String, String> map = new HashMap<>();
        map.put("passback_params", userId);
        map.put("outtradeno", outTradeNo);
        map.put("orderbody", orderbody);
        map.put("subject", subject);
        map.put("money", payNumber);
        map.put("paymode", payMode);
        OkHttpUtils.post().url(Api.BASE_URL + Api.ALICARDPAY).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                StyledDialog.dismiss(mTradeDialog);
                ToastUtils.showShort(BuyMonthlyActivity.this, getString(R.string.server_tip));
            }

            @Override
            public void onResponse(final String response, int id) {
                StyledDialog.dismiss(mTradeDialog);
                LogUtils.d("支付", response);
                Runnable payRunnable = new Runnable() {
                    @Override
                    public void run() {
//                        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);沙箱测试
                        PayTask task = new PayTask(BuyMonthlyActivity.this);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        mTradeDialog = StyledDialog.buildLoading(BuyMonthlyActivity.this, "加载中...", true, false).show();
        if (NetUtils.isConnected(App.getContext())) {
            queryMonthCardType();
        } else {
            ToastUtils.showShort(BuyMonthlyActivity.this, getString(R.string.no_network_tip));
            this.finish();
        }
        mCashStatus = (Integer) SPUtils.get(App.getContext(), "cashStatus", 0);
        mStatus = (Integer) SPUtils.get(App.getContext(), "status", 0);
        LogUtils.d("看看", mCashStatus + "\n" + mStatus + "\n" + SPUtils.isLogin());
        if (SPUtils.isLogin()) {
            if ((mCashStatus == 0 && mStatus == 0) || (mCashStatus == 0 && mStatus == 1)) {
                mBuyMonthCard.setText("交押金后，方可购买月卡");
            } else if (mCashStatus == 1 && mStatus == 0) {
                mBuyMonthCard.setText("实名验证后，方可购买月卡");
            } else if (mCashStatus == 1 && mStatus == 1) {
                mBuyMonthCard.setText("购买");
            }
            Intent intent = getIntent();
            if (intent != null) {
                mUserId = intent.getStringExtra("userId");
                mToken = intent.getStringExtra("token");
                mMAggregateAmount = intent.getDoubleExtra("mAggregateAmount", 0);
                mADouble = mMAggregateAmount;
                if (mADouble != null && !mADouble.equals("")) {
                    mBalanceSum.setText(String.valueOf("(" + mMAggregateAmount + ")"));
                }
            }
            mMonthWeixinCheckbox.setChecked(true);
        }
    }

    private void queryMonthCardType() {
        OkHttpUtils.post().url(Api.BASE_URL + Api.CHECKCARD).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
//                ToastUtils.showShort(BuyMonthlyActivity.this, getResources().getString(R.string.server_tip));
                StyledDialog.dismiss(mTradeDialog);
                BuyMonthlyActivity.this.finish();
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d("结果", response);
                if (JsonUtils.isSuccess(response)) {
                    StyledDialog.dismiss(mTradeDialog);
                    Gson gson = new Gson();
                    MonthCardInfo monthCardInfo = gson.fromJson(response, MonthCardInfo.class);
                    mCardHolder = monthCardInfo.getCardHolder();
                    if (mCardHolder.size() > 0) {
                        showMonthCardNumber(mCardHolder.size(), mCardHolder);
                    }

                } else {
                    BuyMonthlyActivity.this.finish();
                    ToastUtils.showShort(BuyMonthlyActivity.this, getResources().getString(R.string.server_tip));
                }
            }
        });
    }

    private void showMonthCardNumber(int size, List<MonthCardInfo.CardHolderBean> cardHolder) {
        switch (size) {
            case 1:
                mOneMonth.setVisibility(View.VISIBLE);
                mOneMonth.setSelected(true);
                mOneMonth.setLeftText(String.valueOf(cardHolder.get(0).getCardprice()) + "元");
                mOneMonth.setBlewText(String.valueOf(cardHolder.get(0).getCardType()) + "天");
                mOneMonth.setMiddleText(String.valueOf(cardHolder.get(0).getDiscountbefore()) + "元");
                mOneMonth.setRightText(String.valueOf(cardHolder.get(0).getDiscount()) + "折");
                payNumber = mOneMonth.getLeftText().trim().substring(0,
                        mOneMonth.getLeftText().trim().length() - 1);
                payMode = mOneMonth.getBlewText().trim().substring(0, mOneMonth.getBlewText().trim().length() - 1);
                break;
            case 2:
                mOneMonth.setVisibility(View.VISIBLE);
                mThereMonth.setVisibility(View.VISIBLE);
                mOneMonth.setSelected(true);
                mOneMonth.setLeftText(String.valueOf(cardHolder.get(0).getCardprice()) + "元");
                mOneMonth.setBlewText(String.valueOf(cardHolder.get(0).getCardType()) + "天");
                mOneMonth.setMiddleText(String.valueOf(cardHolder.get(0).getDiscountbefore()) + "元");
                mOneMonth.setRightText(String.valueOf(cardHolder.get(0).getDiscount()) + "折");
                mThereMonth.setLeftText(String.valueOf(cardHolder.get(1).getCardprice()) + "元");
                mThereMonth.setBlewText(String.valueOf(cardHolder.get(1).getCardType()) + "天");
                mThereMonth.setMiddleText(String.valueOf(cardHolder.get(1).getDiscountbefore()) + "元");
                mThereMonth.setRightText(String.valueOf(cardHolder.get(1).getDiscount()) + "折");
                payNumber = mOneMonth.getLeftText().trim().substring(0,
                        mOneMonth.getLeftText().trim().length() - 1);
                payMode = mOneMonth.getBlewText().trim().substring(0, mOneMonth.getBlewText().trim().length() - 1);
                break;
            case 3:
                mOneMonth.setVisibility(View.VISIBLE);
                mThereMonth.setVisibility(View.VISIBLE);
                mSixMonth.setVisibility(View.VISIBLE);
                mOneMonth.setSelected(true);
                mOneMonth.setLeftText(String.valueOf(cardHolder.get(0).getCardprice()) + "元");
                mOneMonth.setBlewText(String.valueOf(cardHolder.get(0).getCardType()) + "天");
                mOneMonth.setMiddleText(String.valueOf(cardHolder.get(0).getDiscountbefore()) + "元");
                mOneMonth.setRightText(String.valueOf(cardHolder.get(0).getDiscount()) + "折");
                mThereMonth.setLeftText(String.valueOf(cardHolder.get(1).getCardprice()) + "元");
                mThereMonth.setBlewText(String.valueOf(cardHolder.get(1).getCardType()) + "天");
                mThereMonth.setMiddleText(String.valueOf(cardHolder.get(1).getDiscountbefore()) + "元");
                mThereMonth.setRightText(String.valueOf(cardHolder.get(1).getDiscount()) + "折");
                mSixMonth.setLeftText(String.valueOf(cardHolder.get(2).getCardprice()) + "元");
                mSixMonth.setBlewText(String.valueOf(cardHolder.get(2).getCardType()) + "天");
                mSixMonth.setMiddleText(String.valueOf(cardHolder.get(2).getDiscountbefore()) + "元");
                mSixMonth.setRightText(String.valueOf(cardHolder.get(2).getDiscount()) + "折");
                String leftText = mOneMonth.getLeftText();
                payNumber = mOneMonth.getLeftText().trim().substring(0,
                        mOneMonth.getLeftText().trim().length() - 1);
                payMode = mOneMonth.getBlewText().trim().substring(0, mOneMonth.getBlewText().trim().length() - 1);
                break;
            case 4:
                mOneMonth.setVisibility(View.VISIBLE);
                mThereMonth.setVisibility(View.VISIBLE);
                mSixMonth.setVisibility(View.VISIBLE);
                mTwelveMonth.setVisibility(View.VISIBLE);
                mOneMonth.setSelected(true);
                mOneMonth.setLeftText(String.valueOf(cardHolder.get(0).getCardprice()) + "元");
                mOneMonth.setBlewText(String.valueOf(cardHolder.get(0).getCardType()) + "天");
                mOneMonth.setMiddleText(String.valueOf(cardHolder.get(0).getDiscountbefore()) + "元");
                mOneMonth.setRightText(String.valueOf(cardHolder.get(0).getDiscount()) + "折");
                mThereMonth.setLeftText(String.valueOf(cardHolder.get(1).getCardprice()) + "元");
                mThereMonth.setBlewText(String.valueOf(cardHolder.get(1).getCardType()) + "天");
                mThereMonth.setMiddleText(String.valueOf(cardHolder.get(1).getDiscountbefore()) + "元");
                mThereMonth.setRightText(String.valueOf(cardHolder.get(1).getDiscount()) + "折");
                mSixMonth.setLeftText(String.valueOf(cardHolder.get(2).getCardprice()) + "元");
                mSixMonth.setBlewText(String.valueOf(cardHolder.get(2).getCardType()) + "天");
                mSixMonth.setMiddleText(String.valueOf(cardHolder.get(2).getDiscountbefore()) + "元");
                mSixMonth.setRightText(String.valueOf(cardHolder.get(2).getDiscount()) + "折");
                mTwelveMonth.setLeftText(String.valueOf(cardHolder.get(3).getCardprice()) + "元");
                mTwelveMonth.setBlewText(String.valueOf(cardHolder.get(3).getCardType()) + "天");
                mTwelveMonth.setMiddleText(String.valueOf(cardHolder.get(3).getDiscountbefore()) + "元");
                mTwelveMonth.setRightText(String.valueOf(cardHolder.get(3).getDiscount()) + "折");
                payNumber = mOneMonth.getLeftText().trim().substring(0,
                        mOneMonth.getLeftText().trim().length() - 1);
                payMode = mOneMonth.getBlewText().trim().substring(0, mOneMonth.getBlewText().trim().length() - 1);
                break;
        }
    }

    private Handler handler = new Handler() {
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
                        Toast.makeText(BuyMonthlyActivity.this, "支付成功",
                                Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(BuyMonthlyActivity.this, WalletInfoActivity.class));
                        finish();
//                        returnData(rechargeNumber);
                    } else {
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(BuyMonthlyActivity.this, "支付结果确认中",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(BuyMonthlyActivity.this, "支付失败",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                    break;
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (mTradeDialog != null) {
            StyledDialog.dismiss(mTradeDialog);
        }
        handler.removeCallbacksAndMessages(null);
    }

    //微信支付传过来的消息
    @Subscriber(tag = "page_disappear", mode = ThreadMode.MAIN)
    private void receiveFromWXPayEntry(MessageEvent info) {
        if (info != null) {
            this.finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
