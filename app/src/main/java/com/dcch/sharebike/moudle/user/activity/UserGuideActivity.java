package com.dcch.sharebike.moudle.user.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.dcch.sharebike.R;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.utils.ClickUtils;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyDialogListener;

import butterknife.BindView;
import butterknife.OnClick;


public class UserGuideActivity extends BaseActivity {


    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.lock)
    TextView mLock;
    @BindView(R.id.breakdown)
    TextView mBreakdown;
    @BindView(R.id.depositInstructions)
    TextView mDepositInstructions;
    @BindView(R.id.topUpInstructions)
    TextView mTopUpInstructions;
    @BindView(R.id.report)
    TextView mReport;

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
                if (ClickUtils.isFastClick()) {
                    return;
                }
                Intent unLock = new Intent(this, CustomerServiceActivity.class);
                unLock.putExtra("name", "0");
                startActivity(unLock);
                break;
            case R.id.breakdown:
                if (ClickUtils.isFastClick()) {
                    return;
                }
                Intent bikeTrouble = new Intent(this, CustomerServiceActivity.class);
                bikeTrouble.putExtra("name", "1");
                startActivity(bikeTrouble);
                break;
            case R.id.depositInstructions:
                if (ClickUtils.isFastClick()) {
                    return;
                }
                startActivity(new Intent(this, CashPledgeExplainActivity.class));
                break;
            case R.id.topUpInstructions:
                if (ClickUtils.isFastClick()) {
                    return;
                }
                startActivity(new Intent(this, RechargeAgreementActivity.class));
                break;
            case R.id.report:
                if (ClickUtils.isFastClick()) {
                    return;
                }
                StyledDialog.buildIosAlert(UserGuideActivity.this, "提示", getResources().getString(R.string.customserver_tip), new MyDialogListener() {
                    @Override
                    public void onFirst() {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "400-660-6215"));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }

                    @Override
                    public void onSecond() {
                    }
                }).setMsgColor(R.color.colorHeading).setMsgSize(14).show();
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
