package com.dcch.sharebike.moudle.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dcch.sharebike.MainActivity;
import com.dcch.sharebike.R;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.http.Api;
import com.dcch.sharebike.moudle.home.bean.RidingInfo;
import com.dcch.sharebike.utils.ClickUtils;
import com.dcch.sharebike.utils.JsonUtils;
import com.dcch.sharebike.utils.LogUtils;
import com.dcch.sharebike.utils.ToastUtils;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

public class RidingResultActivity extends BaseActivity {

    @BindView(R.id.moneyResult_show)
    TextView mMoneyResultShow;
    @BindView(R.id.ride_cost)
    TextView mRideCost;
    @BindView(R.id.rideTime)
    TextView mRideTime;
    @BindView(R.id.balance)
    TextView mBalance;
    @BindView(R.id.take_phone)
    TextView mTakePhone;
    @BindView(R.id.share_journey)
    TextView mShareJourney;
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
    @BindView(R.id.pay_now)
    Button mPayNow;
    private String mImei;
    private String mUserId;


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
//        MapUtil.initDrawable(mTakePhone);
//        MapUtil.initDrawable(mShareJourney);
    }

    private void getResult(String imei, String userId) {
        Map<String, String> map = new HashMap<>();
        map.put("IMEI", imei);
        map.put("userId", userId);
        OkHttpUtils.post().url(Api.BASE_URL + Api.ORDERBALANCE).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d("骑行结果", response);
                if (JsonUtils.isSuccess(response)) {
                    Gson gson = new Gson();
                    RidingInfo ridingInfo = gson.fromJson(response, RidingInfo.class);
                    LogUtils.d("骑行结果", ridingInfo.getFinalCast() + "\n" + ridingInfo.getOrderCast() + "\n" + ridingInfo.getTripTime() + "\n" + ridingInfo.getTripDist());
                    mMoneyResultShow.setText(String.valueOf(ridingInfo.getFinalCast()));
                    mRideCost.setText(String.valueOf(ridingInfo.getOrderCast()) + "元");
                    mRideTime.setText(String.valueOf(ridingInfo.getTripTime()) + "分钟");
                    if (ridingInfo.getAmount() < 0) {
                        mBalance.setText("您的余额不足，请前往充值");
                        mBalance.setTextColor(R.color.colorTitle);
                    } else {
                        mBalance.setText(String.valueOf(ridingInfo.getAmount()) + "元");
                    }
                    mCouponCost.setText(String.valueOf(ridingInfo.getCouponAmount()) + "元");
                    mCalorimeter.setText(String.valueOf(ridingInfo.getCalorie()) + "(kCal)");
                    mRideDis.setText(String.valueOf(ridingInfo.getTripDist()) + "(km)");
                } else {
                    ToastUtils.showShort(RidingResultActivity.this, "请求失败");
                }
            }
        });
    }

    @Override
    protected void initListener() {
        super.initListener();
    }

    @OnClick({R.id.take_phone, R.id.share_journey, R.id.pay_now})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.take_phone:
                if (ClickUtils.isFastClick()) {
                    return;
                }
                ToastUtils.showShort(RidingResultActivity.this, "您点击的是拍照");
                break;
            case R.id.share_journey:
                if (ClickUtils.isFastClick()) {
                    return;
                }
                ToastUtils.showShort(RidingResultActivity.this, "您点击的是分享行程");
                break;

            case R.id.pay_now:

                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.d("周期", "onCreate");
        Intent intent = getIntent();
        mImei = intent.getStringExtra("IMEI");
        mUserId = intent.getStringExtra("userId");
//        Bundle extras = intent.getExtras();
//        if (extras != null) {
//            RidingInfo ridingInfo = (RidingInfo) extras.getSerializable("result");
//            mMoneyResultShow.setText(String.valueOf(ridingInfo.getFinalCast()));
//            mRideCost.setText(String.valueOf(ridingInfo.getOrderCast()) + "元");
//            mRideTime.setText(String.valueOf(ridingInfo.getTripTime()) + "分钟");
//            if (ridingInfo.getAmount() <= 0) {
//                mBalance.setText("您的余额不足，请前往充值");
//                mBalance.setTextColor(R.color.colorTitle);
//            } else {
//                mBalance.setText(String.valueOf(ridingInfo.getAmount()) + "元");
//            }
//            mCouponCost.setText(String.valueOf(ridingInfo.getCouponAmount() + "元"));
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mImei != null && mUserId != null) {
            LogUtils.d("骑行结果", mImei + "\n" + mUserId);
            getResult(mImei, mUserId);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.d("周期", "onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.d("周期", "onDestroy");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(RidingResultActivity.this, MainActivity.class));
        RidingResultActivity.this.finish();
    }
}
