package com.dcch.sharebike.moudle.user.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dcch.sharebike.R;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class UsualAddressActivity extends BaseActivity {
    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.starTargetOne)
    ImageView mStarTargetOne;
    @BindView(R.id.placeNameOne)
    TextView mPlaceNameOne;
    @BindView(R.id.addressNameOne)
    TextView mAddressNameOne;
    @BindView(R.id.addressOne)
    RelativeLayout mAddressOne;
    @BindView(R.id.starTargetTwo)
    ImageView mStarTargetTwo;
    @BindView(R.id.placeNameTwo)
    TextView mPlaceNameTwo;
    @BindView(R.id.addressNameTwo)
    TextView mAddressNameTwo;
    @BindView(R.id.addressTwo)
    RelativeLayout mAddressTwo;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_usual_address;
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.back, R.id.addressOne, R.id.addressTwo})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.addressOne:
                ToastUtils.showLong(this,"地址1");
                break;
            case R.id.addressTwo:
                ToastUtils.showLong(this,"地址2");
                break;
        }
    }
}
