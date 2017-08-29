package com.dcch.sharebike.moudle.user.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
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
    //    @BindView(R.id.select_one)
//    TextView mSelectOne;
//    @BindView(R.id.useful_life_one)
//    TextView mUsefulLifeOne;
//    @BindView(R.id.original_price_one)
//    TextView mOriginalPriceOne;
//    @BindView(R.id.oneMonth)
//    RelativeLayout mOneMonth;
    //    @BindView(R.id.select_two)
//    TextView mSelectTwo;
//    @BindView(R.id.useful_life_two)
//    TextView mUsefulLifeTwo;
//    @BindView(R.id.original_price_two)
//    TextView mOriginalPriceTwo;
//    @BindView(R.id.thereMonth)
//    RelativeLayout mThereMonth;
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
    //    @BindView(R.id.discount_one)
//    TextView mDiscountOne;
    //    @BindView(R.id.discount_two)
//    TextView mDiscountTwo;
//    @BindView(R.id.select_there)
//    TextView mSelectThere;
//    @BindView(R.id.useful_life_there)
//    TextView mUsefulLifeThere;
//    @BindView(R.id.original_price_there)
//    TextView mOriginalPriceThere;
//    @BindView(R.id.discount_there)
//    TextView mDiscountThere;
//    @BindView(R.id.sixMonth)
//    RelativeLayout mSixMonth;
//    @BindView(R.id.select_four)
//    TextView mSelectFour;
//    @BindView(R.id.useful_life_four)
//    TextView mUsefulLifeFour;
//    @BindView(R.id.original_price_four)
//    TextView mOriginalPriceFour;
//    @BindView(R.id.discount_four)
//    TextView mDiscountFour;
//    @BindView(R.id.twelveMonth)
//    RelativeLayout mTwelveMonth;
    @BindView(R.id.one_area)
    LinearLayout mOneArea;
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

    @OnClick({R.id.buy_month_card,
            R.id.month_weixinArea, R.id.month_aliArea})
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
                            mTradeDialog = StyledDialog.buildLoading(BuyMonthlyActivity.this, "交易中...", true, false).show();
                            //这里要分两种情况，调取微信和支付宝的支付方式
                            if (mMonthAliCheckbox.isChecked()) {
                                //选择支付宝，调取支付宝的支付方法
                                AliPay aliPay = new AliPay(this);
                                String outTradeNo = aliPay.getOutTradeNo();
                                if (mUserId != null && payNumber != null && outTradeNo != null) {
                                    aliCardPayWay(mUserId, outTradeNo, payNumber, orderbody, subject, payMode);

                                } else {
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
                            }
                        }
                    }
                } else {
                    ToastUtils.showShort(BuyMonthlyActivity.this, getString(R.string.no_network_tip));
                }

                break;


            case R.id.month_weixinArea:
                mMonthWeixinCheckbox.setChecked(true);
                mMonthAliCheckbox.setChecked(false);
                break;

            case R.id.month_aliArea:
                mMonthAliCheckbox.setChecked(true);
                mMonthWeixinCheckbox.setChecked(false);
                break;


        }
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
        LogUtils.d("微信支付", ipAddress + "\n" + userId + "\n" + mOutTradeNo);
        OkHttpUtils.post().url(Api.BASE_URL + Api.WEIXINPAY).params(map).build().execute(new StringCallback() {
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
        queryMonthCardType();
//        initAutoLL();
        mCashStatus = (Integer) SPUtils.get(App.getContext(), "cashStatus", 0);
        mStatus = (Integer) SPUtils.get(App.getContext(), "status", 0);
        if (SPUtils.isLogin()) {
            if (mCashStatus == 0) {
                mBuyMonthCard.setText("交押金后，方可购买月卡");
            } else if (mCashStatus == 1 && mStatus == 0) {
                mBuyMonthCard.setText("实名验证后，方可购买月卡");
            } else {
                mBuyMonthCard.setText("购买");
            }
            Intent intent = getIntent();
            if (intent != null) {
                mUserId = intent.getStringExtra("userId");
                mToken = intent.getStringExtra("token");
            }
            mMonthWeixinCheckbox.setChecked(true);
            payMode = "月卡";
        }
    }

    private void queryMonthCardType() {
        OkHttpUtils.post().url(Api.BASE_URL + Api.CHECKCARD).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                StyledDialog.dismiss(mTradeDialog);
                ToastUtils.showShort(BuyMonthlyActivity.this, getResources().getString(R.string.server_tip));
//                BuyMonthlyActivity.this.finish();
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d("结果", response);
                if (JsonUtils.isSuccess(response)) {
                    StyledDialog.dismiss(mTradeDialog);
                    Gson gson = new Gson();
                    MonthCardInfo monthCardInfo = gson.fromJson(response, MonthCardInfo.class);
                    mCardHolder = monthCardInfo.getCardHolder();
                    initAutoLL(mCardHolder);

                } else {
                    ToastUtils.showShort(BuyMonthlyActivity.this, getResources().getString(R.string.server_tip));
                }
            }
        });
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

    //    绘制自动换行的线性布局
    private void initAutoLL(List<MonthCardInfo.CardHolderBean> cardHolder) {
//        每一行的布局，初始化第一行布局
        LinearLayout rowLL = new LinearLayout(this);
        LinearLayout.LayoutParams rowLP =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        float rowMargin = dipToPx(10);
        rowLP.setMargins(0, (int) rowMargin, 0, 0);
        rowLL.setLayoutParams(rowLP);
        boolean isNewLayout = false;
        float maxWidth = getScreenWidth() - dipToPx(30);
//        剩下的宽度
        float elseWidth = maxWidth;
        LinearLayout.LayoutParams textViewLP =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        textViewLP.setMargins((int) dipToPx(8), 0, 0, 0);
        for (int i = 0; i < cardHolder.size(); i++) {
//            若当前为新起的一行，先添加旧的那行
//            然后重新创建布局对象，设置参数，将isNewLayout判断重置为false
            if (isNewLayout) {
                mOneArea.addView(rowLL);
                rowLL = new LinearLayout(this);
                rowLL.setLayoutParams(rowLP);
                isNewLayout = false;
            }
//            计算是否需要换行
            final MonthCardView monthCardView = new MonthCardView(this);
            LogUtils.d("结果", monthCardView + "\n");
            monthCardView.setLeftText(String.valueOf(cardHolder.get(i).getCardprice()) + "元");
            monthCardView.setBlewText(cardHolder.get(i).getCardType());
            monthCardView.setMiddleText(String.valueOf(cardHolder.get(i).getCardprice()) + "元");
            monthCardView.setRightText(String.valueOf(cardHolder.get(i).getCardprice()) + "折");
            monthCardView.setOnSelectedListener(new MonthCardView.ISelectedListener() {
                @Override
                public void onSelectedListener() {
                    monthCardView.setSelected(true);
                    ToastUtils.showShort(BuyMonthlyActivity.this, "选中了\n" + monthCardView.getLeftText());
                }
            });
            monthCardView.measure(0, 0);
//            若是一整行都放不下这个文本框，添加旧的那行，新起一行添加这个文本框
            if (maxWidth < monthCardView.getMeasuredWidth()) {
                mOneArea.addView(rowLL);
                rowLL = new LinearLayout(this);
                rowLL.setLayoutParams(rowLP);
                rowLL.addView(monthCardView);
                isNewLayout = true;
                continue;
            }
//            若是剩下的宽度小于文本框的宽度（放不下了）
//            添加旧的那行，新起一行，但是i要-1，因为当前的文本框还未添加
            if (elseWidth < monthCardView.getMeasuredWidth()) {
                isNewLayout = true;
                i--;
//                重置剩余宽度
                elseWidth = maxWidth;
                continue;
            } else {
//                剩余宽度减去文本框的宽度+间隔=新的剩余宽度
                elseWidth -= monthCardView.getMeasuredWidth() + dipToPx(8);
                if (rowLL.getChildCount() == 0) {
                    rowLL.addView(monthCardView);
                } else {
                    monthCardView.setLayoutParams(textViewLP);
                    rowLL.addView(monthCardView);
                }
            }
        }
//        添加最后一行，但要防止重复添加
        mOneArea.removeView(rowLL);
        mOneArea.addView(rowLL);
    }

    //    dp转px
    private float dipToPx(int dipValue) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dipValue,
                this.getResources().getDisplayMetrics());
    }

    //  获得评论宽度
    private float getScreenWidth() {
        return this.getResources().getDisplayMetrics().widthPixels;
    }

}
