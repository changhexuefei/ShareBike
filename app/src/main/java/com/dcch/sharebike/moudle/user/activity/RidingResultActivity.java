package com.dcch.sharebike.moudle.user.activity;

import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dcch.sharebike.R;
import com.dcch.sharebike.base.BaseActivity;
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
}
