package com.dcch.sharebike.moudle.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dcch.sharebike.R;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.moudle.home.bean.RidingInfo;
import com.dcch.sharebike.utils.LogUtils;
import com.dcch.sharebike.utils.MapUtil;
import com.dcch.sharebike.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class RidingResultActivity extends BaseActivity {

    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.moneyResult_show)
    TextView mMoneyResultShow;
    @BindView(R.id.ride_cost)
    TextView mRideCost;
    @BindView(R.id.rideTime)
    TextView mRideTime;
    @BindView(R.id.balance)
    TextView mBalance;
    @BindView(R.id.myCountry)
    CardView mMyCountry;
    @BindView(R.id.take_phone)
    TextView mTakePhone;
    @BindView(R.id.share_journey)
    TextView mShareJourney;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_riding_result;
    }

    @Override
    protected void initData() {
        MapUtil.initDrawable(mTakePhone);
        MapUtil.initDrawable(mShareJourney);
    }

    @Override
    protected void initListener() {
        super.initListener();
    }

    @OnClick({R.id.back, R.id.myCountry, R.id.take_phone, R.id.share_journey})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.myCountry:
                ToastUtils.showShort(RidingResultActivity.this,"您点击的是行程详情");
                break;
            case R.id.take_phone:
                ToastUtils.showShort(RidingResultActivity.this,"您点击的是拍照");
                break;
            case R.id.share_journey:
                ToastUtils.showShort(RidingResultActivity.this,"您点击的是分享行程");
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.d("周期","onCreate");
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras!=null){
            RidingInfo ridingInfo= (RidingInfo) extras.getSerializable("result");
            mMoneyResultShow.setText(String.valueOf(ridingInfo.getRideCost()));
            mRideCost.setText(String.valueOf(ridingInfo.getRideCost())+"元");
            mRideTime.setText(String.valueOf(ridingInfo.getTripTime())+"分钟");
            mBalance.setText(String.valueOf(ridingInfo.getAmount())+"元");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.d("周期","onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.d("周期","onDestroy");
    }

}
