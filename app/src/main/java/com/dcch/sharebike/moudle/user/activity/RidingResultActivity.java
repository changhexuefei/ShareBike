package com.dcch.sharebike.moudle.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
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
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_riding_result;
    }

    @Override
    protected void initData() {
        mToolbar.setTitle("");
        mTitle.setText(getResources().getString(R.string.agreement));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        MapUtil.initDrawable(mTakePhone);
        MapUtil.initDrawable(mShareJourney);
    }

    @Override
    protected void initListener() {
        super.initListener();
    }

    //R.id.back,
    @OnClick({R.id.myCountry, R.id.take_phone, R.id.share_journey})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.myCountry:
                ToastUtils.showShort(RidingResultActivity.this, "您点击的是行程详情");
                break;
            case R.id.take_phone:
                ToastUtils.showShort(RidingResultActivity.this, "您点击的是拍照");
                break;
            case R.id.share_journey:
                ToastUtils.showShort(RidingResultActivity.this, "您点击的是分享行程");
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.d("周期", "onCreate");
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            RidingInfo ridingInfo = (RidingInfo) extras.getSerializable("result");
            mMoneyResultShow.setText(String.valueOf(ridingInfo.getRideCost()));
            mRideCost.setText(String.valueOf(ridingInfo.getRideCost()) + "元");
            mRideTime.setText(String.valueOf(ridingInfo.getTripTime()) + "分钟");
            mBalance.setText(String.valueOf(ridingInfo.getAmount()) + "元");
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

}
