package com.dcch.sharebike.moudle.user.activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dcch.sharebike.R;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.utils.ClickUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class MobileNumActivity extends BaseActivity {

    @BindView(R.id.phone_show)
    TextView mPhoneShow;
    @BindView(R.id.replace_mobile_num)
    Button mReplaceMobileNum;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private int mCashStatus;
    private String mUserId;
    private String mToken;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_mobile_num;
    }

    @Override
    protected void initData() {
        mToolbar.setTitle("");
        mTitle.setText(getResources().getString(R.string.mobile_title));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MobileNumActivity.this.finish();
            }
        });
        Intent intent = getIntent();
        String phone = intent.getStringExtra("phone");
        mCashStatus = intent.getIntExtra("cashStatus", 0);
        mUserId = intent.getStringExtra("userId");
        mToken = intent.getStringExtra("token");
        if (phone != null && !phone.equals("")) {
            mPhoneShow.setText("您当前的手机号为" + phone);
        }
    }


    @OnClick(R.id.replace_mobile_num)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.replace_mobile_num:
                if (ClickUtils.isFastClick()) {
                    return;
                }
//                if (mCashStatus == 1) {
//                    new AlertDialog.Builder(this)
//                            .setMessage("更换手机号需先退押金您还未退押金，是否前去退押金?")
//                            .setNegativeButton("取消", null)
//                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    startActivity(new Intent(MobileNumActivity.this, WalletInfoActivity.class));
//                                }
//                            }).create().show();
//                } else if (mCashStatus == 0) {
                    if (mToken != null && mUserId != null) {
                        Intent changeMobileNum = new Intent(this, ChangeMobileNumActivity.class);
                        changeMobileNum.putExtra("token", mToken);
                        changeMobileNum.putExtra("userId", mUserId);
                        startActivity(changeMobileNum);
//                    }

                }
                break;
        }
    }

}
