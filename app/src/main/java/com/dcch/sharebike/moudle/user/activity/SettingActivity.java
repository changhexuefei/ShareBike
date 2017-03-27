package com.dcch.sharebike.moudle.user.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.dcch.sharebike.R;
import com.dcch.sharebike.app.App;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.base.MessageEvent;
import com.dcch.sharebike.moudle.login.activity.PersonalCenterActivity;
import com.dcch.sharebike.utils.SPUtils;
import com.dcch.sharebike.utils.ToastUtils;

import org.simple.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {

    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.usualAddress)
    RelativeLayout mUsualAddress;
    @BindView(R.id.checkVersions)
    RelativeLayout mCheckVersions;
    @BindView(R.id.aboutUs)
    RelativeLayout mAboutUs;
    @BindView(R.id.userAgreement)
    RelativeLayout mUserAgreement;
    @BindView(R.id.cashPledgeExplain)
    RelativeLayout mCashPledgeExplain;
    @BindView(R.id.rechargeAgreement)
    RelativeLayout mRechargeAgreement;
    @BindView(R.id.signOut)
    Button mSignOut;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.back, R.id.usualAddress, R.id.checkVersions, R.id.aboutUs, R.id.userAgreement, R.id.cashPledgeExplain, R.id.rechargeAgreement, R.id.signOut})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.usualAddress:
                Intent address = new Intent(this, UsualAddressActivity.class);
                startActivity(address);
                break;
            case R.id.checkVersions:
                ToastUtils.showShort(SettingActivity.this,"未发现新版本");
                break;
            case R.id.aboutUs:
                startActivity(new Intent(SettingActivity.this,AboutUsActivity.class));
                break;
            case R.id.userAgreement:
                break;
            case R.id.cashPledgeExplain:
                break;
            case R.id.rechargeAgreement:
                break;
            case R.id.signOut:
                new AlertDialog.Builder(this)
                        .setTitle("退出登录")
                        .setMessage("确定退出登录吗？")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ToastUtils.showShort(SettingActivity.this, "" + which);
                                Intent i1 = new Intent(SettingActivity.this, PersonalCenterActivity.class);
                                i1.putExtra("name", "unLogin");
                                //用EventBus发送消息给个人中心
                                EventBus.getDefault().post(new MessageEvent(), "unLogin");
                                //给主页发送消息
                                EventBus.getDefault().post(new MessageEvent(), "visible");
                                startActivity(i1);
                                SPUtils.clear(App.getContext());

                                SPUtils.put(App.getContext(), "islogin", false);
                                SPUtils.put(App.getContext(),"isfirst",false);
                                SPUtils.put(App.getContext(), "isStartGuide", true);
                                finish();
                            }
                        }).create()
                        .show();
                break;
        }
    }
}
