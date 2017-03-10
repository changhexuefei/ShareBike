package com.dcch.sharebike.moudle.user.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dcch.sharebike.R;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.utils.ToastUtils;

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
    @BindView(R.id.unFindBike)
    LinearLayout unFindBike;
    @BindView(R.id.allQuestion)
    LinearLayout allQuestion;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_guide;
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.back, R.id.lock, R.id.breakdown, R.id.depositInstructions, R.id.topUpInstructions, R.id.report, R.id.unFindBike, R.id.allQuestion})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.lock:
                Intent unLock = new Intent(this,CustomerServiceActivity.class);
                unLock.putExtra("name","0");
                startActivity(unLock);
                break;
            case R.id.breakdown:
                Intent bikeTrouble = new Intent(this,CustomerServiceActivity.class);
                bikeTrouble.putExtra("name","1");
                startActivity(bikeTrouble);

                break;
            case R.id.depositInstructions:
                break;
            case R.id.topUpInstructions:
                break;
            case R.id.report:
                Intent reportIllegalParking = new Intent(this,CustomerServiceActivity.class);
                reportIllegalParking.putExtra("name","2");
                startActivity(reportIllegalParking);
                break;
            case R.id.unFindBike:
                break;
            case R.id.allQuestion:
                ToastUtils.showShort(this,"全部问题");
//               Intent allQuestionPage = new Intent(this,CustomerServiceActivity.class);
//                allQuestionPage.putExtra("name","all");
//                startActivity(allQuestionPage);
                break;
        }
    }
}
