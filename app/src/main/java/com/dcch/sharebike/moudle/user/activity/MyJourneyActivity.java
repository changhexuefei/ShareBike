package com.dcch.sharebike.moudle.user.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dcch.sharebike.R;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class MyJourneyActivity extends BaseActivity {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.help)
    TextView help;
    @BindView(R.id.journey_list)
    ListView journeyList;
    @BindView(R.id.no_journey)
    TextView noJourney;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_journey;
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.back, R.id.help})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.help:
                ToastUtils.showShort(this,"我是帮助");
                break;
        }
    }
}
