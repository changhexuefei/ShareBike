package com.dcch.sharebike.moudle.user.activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dcch.sharebike.R;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.utils.ClickUtils;

import butterknife.BindView;
import butterknife.OnClick;


public class UserGuideActivity extends BaseActivity {

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
//    @BindView(R.id.unFindBike)
//    LinearLayout unFindBike;
    @BindView(R.id.allQuestion)
    LinearLayout allQuestion;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_guide;
    }

    @Override
    protected void initData() {
        mToolbar.setTitle("");
        mTitle.setText(getResources().getString(R.string.user_guide));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
    //, R.id.allQuestion, R.id.unFindBike
    @OnClick({R.id.lock, R.id.breakdown, R.id.depositInstructions, R.id.topUpInstructions, R.id.report})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lock:
                if(ClickUtils.isFastClick()){
                    return;
                }
                Intent unLock = new Intent(this, CustomerServiceActivity.class);
                unLock.putExtra("name", "0");
                startActivity(unLock);
                break;
            case R.id.breakdown:
                if(ClickUtils.isFastClick()){
                    return;
                }
                Intent bikeTrouble = new Intent(this, CustomerServiceActivity.class);
                bikeTrouble.putExtra("name", "1");
                startActivity(bikeTrouble);
                break;
            case R.id.depositInstructions:
                if(ClickUtils.isFastClick()){
                    return;
                }
                startActivity(new Intent(this,CashPledgeExplainActivity.class));
                break;
            case R.id.topUpInstructions:
                if(ClickUtils.isFastClick()){
                    return;
                }
                startActivity(new Intent(this,RechargeAgreementActivity.class));
                break;
            case R.id.report:
                if(ClickUtils.isFastClick()){
                    return;
                }
                Intent reportIllegalParking = new Intent(this, CustomerServiceActivity.class);
                reportIllegalParking.putExtra("name", "2");
                startActivity(reportIllegalParking);
                break;
//            case R.id.unFindBike:
//                break;
//            case R.id.allQuestion:
//                ToastUtils.showShort(this, "全部问题");
////               Intent allQuestionPage = new Intent(this,CustomerServiceActivity.class);
////                allQuestionPage.putExtra("name","all");
////                startActivity(allQuestionPage);
//                break;
        }
    }

}
