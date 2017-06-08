package com.dcch.sharebike.moudle.user.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.dcch.sharebike.MainActivity;
import com.dcch.sharebike.R;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.http.Api;
import com.dcch.sharebike.moudle.home.bean.RideResultInfo;
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
                    RideResultInfo rideResultInfo = gson.fromJson(response, RideResultInfo.class);
                    LogUtils.d("骑行结果", rideResultInfo.getCarRentalInfo().getFinalCast() + "\n" + rideResultInfo.getCarRentalInfo().getOrderCast() + "\n" + rideResultInfo.getCarRentalInfo().getTripTime() + "\n" + rideResultInfo.getCarRentalInfo().getTripDist());
                    mMoneyResultShow.setText(String.valueOf(rideResultInfo.getCarRentalInfo().getFinalCast()));
                    mRideCost.setText(String.valueOf(rideResultInfo.getCarRentalInfo().getFinalCast()) + "元");
                    mRideTime.setText(String.valueOf(rideResultInfo.getCarRentalInfo().getTripTime()) + "分钟");
                    mBalance.setText(String.valueOf(rideResultInfo.getCarRentalInfo().getFinalCast()) + "元");
                    mCouponCost.setText(String.valueOf(rideResultInfo.getCarRentalInfo().getCouponAmount()) + "元");
                    mCalorimeter.setText(String.valueOf(rideResultInfo.getCarRentalInfo().getCalorie()) + "(kCal)");
                    mRideDis.setText(String.valueOf(rideResultInfo.getCarRentalInfo().getTripDist()) + "(km)");
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.d("周期", "onCreate");
        Intent intent = getIntent();
        mImei = intent.getStringExtra("IMEI");
        mUserId = intent.getStringExtra("userId");
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

    @OnClick(R.id.callCenter)
    public void onViewClicked() {
        String phoneNumber = "400-660-6215";
        mCallCenter.setText(phoneNumber);
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
