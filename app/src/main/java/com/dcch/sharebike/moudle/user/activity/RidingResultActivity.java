package com.dcch.sharebike.moudle.user.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dcch.sharebike.MainActivity;
import com.dcch.sharebike.R;
import com.dcch.sharebike.app.App;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.base.MessageEvent;
import com.dcch.sharebike.http.Api;
import com.dcch.sharebike.moudle.home.bean.RideResultInfo;
import com.dcch.sharebike.utils.ClickUtils;
import com.dcch.sharebike.utils.LogUtils;
import com.dcch.sharebike.utils.MapUtil;
import com.dcch.sharebike.utils.NetUtils;
import com.dcch.sharebike.utils.ToastUtils;
import com.google.gson.Gson;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyDialogListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

@SuppressWarnings("ALL")
public class RidingResultActivity extends BaseActivity {

    @BindView(R.id.moneyResult_show)
    TextView mMoneyResultShow;
    @BindView(R.id.ride_cost)
    TextView mRideCost;
    @BindView(R.id.rideTime)
    TextView mRideTime;
    @BindView(R.id.balance)
    TextView mBalance;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.coupon_cost)
    TextView mCouponCost;
    @BindView(R.id.ride_dis)
    TextView mRideDis;
    @BindView(R.id.calorimeter)
    TextView mCalorimeter;
    @BindView(R.id.callCenter)
    TextView mCallCenter;
    @BindView(R.id.red_packet)
    ImageView mRedPacket;
    @BindView(R.id.cyclingTimes)
    TextView mCyclingTimes;

    private String mImei;
    private String mUserId;
    private boolean isFirst = true;
    private String mMerchantinfo;
    private String mMerchantimageurl;
    private String mMerchant;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_riding_result;
    }

    @Override
    protected void initData() {
        mToolbar.setTitle("");
        mTitle.setText(getResources().getString(R.string.ride_result));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RidingResultActivity.this, MainActivity.class));
                RidingResultActivity.this.finish();
            }
        });
    }

    private void getResult(String imei, String userId) {
        Map<String, String> map = new HashMap<>();
        map.put("IMEI", imei);
        map.put("userId", userId);
        OkHttpUtils.post().url(Api.BASE_URL + Api.ORDERBALANCE).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.showShort(RidingResultActivity.this, getString(R.string.server_tip));
                StyledDialog.dismissLoading();
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d("骑行结果", response);
                StyledDialog.dismissLoading();
                Gson gson = new Gson();
                RideResultInfo rideResultInfo = gson.fromJson(response, RideResultInfo.class);
                String resultStatus = rideResultInfo.getResultStatus();
                String resultGift = rideResultInfo.getResultGift();
                RideResultInfo.CarRentalInfoBean carRentalInfo = rideResultInfo.getCarRentalInfo();

                switch (resultStatus) {
                    case "0":
                        ToastUtils.showShort(RidingResultActivity.this, getString(R.string.server_tip));
                        RidingResultActivity.this.finish();
                        break;
                    case "1":
                        switch (resultGift) {
                            case "1":
                                showResult(carRentalInfo);
                                mMerchantinfo = rideResultInfo.getMerchantinfo();
                                mMerchantimageurl = rideResultInfo.getMerchantimageurl();
                                mMerchant = rideResultInfo.getMerchantid();
                                mRedPacket.setVisibility(View.VISIBLE);
                                break;
                            case "0":
                                showResult(carRentalInfo);
                                mRedPacket.setVisibility(View.GONE);
                                break;
                        }
                        break;
                }
            }
        });
    }

    private void showResult(RideResultInfo.CarRentalInfoBean carRentalInfo) {
        mMoneyResultShow.setText(String.valueOf(carRentalInfo.getFinalCast()));
        mRideCost.setText(String.valueOf(carRentalInfo.getRideCost()) + "元");
        mRideTime.setText(String.valueOf(carRentalInfo.getTripTime()) + "分钟");
        mBalance.setText(String.valueOf(carRentalInfo.getFinalCast()) + "元");
        if (carRentalInfo.getCouponno().equals("nocoupon")) {
            mCouponCost.setText(R.string.no_coupons);
        } else {
            mCouponCost.setText(R.string.coupons);
        }
        if (carRentalInfo.getCardFlag() == 1) {
            mCyclingTimes.setVisibility(View.VISIBLE);
            if (carRentalInfo.getRidetimes() > 0) {
                mCyclingTimes.setText("今天您的免费骑行次数还剩"+carRentalInfo.getRidetimes()+"次");
            } else if (carRentalInfo.getRidetimes() == 0) {
                mCyclingTimes.setText("今天您的免费骑行次数已用完！");
            }

        } else {
            mCyclingTimes.setVisibility(View.GONE);
        }
        mCalorimeter.setText(String.valueOf(MapUtil.changeDouble(carRentalInfo.getCalorie())) + "大卡");
        mRideDis.setText(String.valueOf(carRentalInfo.getTripDist()) + "千米");
    }

    @Override
    protected void initListener() {
        super.initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        LogUtils.d("周期", "onCreate");
        StyledDialog.buildLoading(RidingResultActivity.this, "结算中.....", true, false).show();
        Intent intent = getIntent();
        mImei = intent.getStringExtra("IMEI");
        mUserId = intent.getStringExtra("userId");
//        mImei = "091609999";
//        mUserId = "214";
        if (NetUtils.isConnected(App.getContext())) {
            if (mImei != null && !mImei.equals("") && mUserId != null && !mUserId.equals("")) {
                LogUtils.d("骑行结果", mImei + "\n" + mUserId);
                getResult(mImei, mUserId);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.d("周期", "onPause");
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        StyledDialog.dismiss();
        super.onDestroy();
        LogUtils.d("周期", "onDestroy");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(RidingResultActivity.this, MainActivity.class));
        RidingResultActivity.this.finish();
    }

    @OnClick({R.id.callCenter, R.id.red_packet})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.callCenter:
                if (ClickUtils.isFastClick()) {
                    return;
                }
                StyledDialog.buildIosAlert(RidingResultActivity.this, "提示", getString(R.string.service_tel), new MyDialogListener() {
                    @Override
                    public void onFirst() {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "400-660-6215"));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }

                    @Override
                    public void onSecond() {
                    }
                }).setMsgColor(R.color.colorHeading).setMsgSize(16).show();
                break;
            case R.id.red_packet:
                if (ClickUtils.isFastClick()) {
                    return;
                }
                LogUtils.d("骑行结果", "点击我了！");
//                if (mUserId != null && !mUserId.equals("")) {
                if (NetUtils.isConnected(App.getContext())) {
                    if (isFirst) {
                        if (mMerchantinfo != null && !mMerchantinfo.equals("")
                                && !mMerchantimageurl.equals("") && mMerchantimageurl != null
                                && !mImei.equals("") && mImei != null && !mMerchant.equals("")
                                && mMerchant != null
                                ) {
                            Intent redPacket = new Intent(this, OpenRedEnvelopeActivity.class);
                            redPacket.putExtra("merchantid", mMerchant);
                            redPacket.putExtra("mMerchantinfo", mMerchantinfo);
                            redPacket.putExtra("IMEI", mImei);
                            redPacket.putExtra("mMerchantimageurl", mMerchantimageurl);
                            startActivity(redPacket);
                        }

                    } else {
                        ToastUtils.showShort(RidingResultActivity.this, "下次骑行再来领红包吧！");
                    }
                } else {
                    ToastUtils.showShort(RidingResultActivity.this, getResources().getString(R.string.no_network_tip));
                }
//                }
                break;
        }
    }

    //抢红包页面带来的消息,表示用户已经拆了红包的，提醒用户下次骑行再来
    @Subscriber(tag = "unable_click", mode = ThreadMode.MAIN)
    private void receiveFromOpenRedEnvelope(MessageEvent info) {
        if (info != null) {
            isFirst = false;
        }
    }

}
