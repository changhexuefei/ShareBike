package com.dcch.sharebike.moudle.user.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dcch.sharebike.R;
import com.dcch.sharebike.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class UserGuideActivity extends BaseActivity {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.lock)
    LinearLayout lock;
    @BindView(R.id.breakdown)
    LinearLayout breakdown;
    @BindView(R.id.depositInstructions)
    LinearLayout depositInstructions;
    @BindView(R.id.topUpInstructions)
    LinearLayout topUpInstructions;
    @BindView(R.id.report)
    LinearLayout report;
    @BindView(R.id.unFindCar)
    LinearLayout unFindCar;
    @BindView(R.id.allQuestion)
    LinearLayout allQuestion;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_guide;
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.back, R.id.lock, R.id.breakdown, R.id.depositInstructions, R.id.topUpInstructions, R.id.report, R.id.unFindCar, R.id.allQuestion})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.lock:
                Intent customerService = new Intent(this,CustomerServiceActivity.class);
                startActivity(customerService);
                break;
            case R.id.breakdown:
                break;
            case R.id.depositInstructions:
                break;
            case R.id.topUpInstructions:
                break;
            case R.id.report:
                break;
            case R.id.unFindCar:
                break;
            case R.id.allQuestion:
                break;
        }
    }
}
