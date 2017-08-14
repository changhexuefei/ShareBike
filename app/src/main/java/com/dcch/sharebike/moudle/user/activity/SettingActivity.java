package com.dcch.sharebike.moudle.user.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dcch.sharebike.R;
import com.dcch.sharebike.app.App;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.base.MessageEvent;
import com.dcch.sharebike.moudle.login.activity.PersonalCenterActivity;
import com.dcch.sharebike.utils.AppUtils;
import com.dcch.sharebike.utils.ClickUtils;
import com.dcch.sharebike.utils.NetUtils;
import com.dcch.sharebike.utils.SPUtils;
import com.dcch.sharebike.utils.ToastUtils;
import com.qihoo.appstore.common.updatesdk.lib.UpdateHelper;

import org.simple.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

import static com.dcch.sharebike.R.id.userAgreement;

public class SettingActivity extends BaseActivity {

    //    @BindView(R.id.usualAddress)
//    RelativeLayout mUsualAddress;
    @BindView(R.id.checkVersions)
    RelativeLayout mCheckVersions;
    @BindView(R.id.aboutUs)
    RelativeLayout mAboutUs;
    @BindView(userAgreement)
    RelativeLayout mUserAgreement;
    @BindView(R.id.cashPledgeExplain)
    RelativeLayout mCashPledgeExplain;
    @BindView(R.id.rechargeAgreement)
    RelativeLayout mRechargeAgreement;
    @BindView(R.id.signOut)
    Button mSignOut;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;



    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initData() {
        mToolbar.setTitle("");
        mTitle.setText(getResources().getString(R.string.setting));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    //R.id.usualAddress,
    @OnClick({R.id.checkVersions, R.id.aboutUs, userAgreement, R.id.cashPledgeExplain, R.id.rechargeAgreement, R.id.signOut})
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.usualAddress:
//                Intent address = new Intent(this, UsualAddressActivity.class);
//                startActivity(address);
//                break;
            case R.id.checkVersions:
                if (ClickUtils.isFastClick()) {
                    return;
                }
                if (NetUtils.isConnected(this)) {
                    checkUpDate();
//                    UpdateManager updateManager = new UpdateManager(this);
//                    updateManager.checkVersion();
                } else {
                    ToastUtils.showShort(this, getString(R.string.no_network_tip));
                }

                break;
            case R.id.aboutUs:
                if (ClickUtils.isFastClick()) {
                    return;
                }
                startActivity(new Intent(SettingActivity.this, AboutUsActivity.class));
                break;
            case userAgreement:
                if (ClickUtils.isFastClick()) {
                    return;
                }
                startActivity(new Intent(SettingActivity.this, UserAgreementActivity.class));
                break;
            case R.id.cashPledgeExplain:
                if (ClickUtils.isFastClick()) {
                    return;
                }
                startActivity(new Intent(SettingActivity.this, CashPledgeExplainActivity.class));
                break;
            case R.id.rechargeAgreement:
                if (ClickUtils.isFastClick()) {
                    return;
                }
                startActivity(new Intent(SettingActivity.this, RechargeAgreementActivity.class));
                break;
            case R.id.signOut:
                if (ClickUtils.isFastClick()) {
                    return;
                }
                signout();
                break;
        }
    }

    private void signout() {
        new AlertDialog.Builder(this)
                .setTitle("退出登录")
                .setMessage("确定退出登录吗？")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i1 = new Intent(SettingActivity.this, PersonalCenterActivity.class);
                        i1.putExtra("name", "unLogin");
                        //用EventBus发送消息给个人中心
//                        EventBus.getDefault().post(new MessageEvent(), "unLogin");
                        //给主页发送消息
                        EventBus.getDefault().post(new MessageEvent(), "visible");
                        SPUtils.clear(App.getContext());
                        SPUtils.put(App.getContext(), "islogin", false);
                        SPUtils.put(App.getContext(), "isfirst", false);
                        startActivity(i1);
                        finish();
                    }
                }).create()
                .show();
    }

    private void checkUpDate() {
        UpdateHelper.getInstance().init(getApplicationContext(), Color.parseColor("#0A93DB"));
        UpdateHelper.getInstance().manualUpdate(AppUtils.getPackageName(this));

    }

    private void showTip(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showLong(SettingActivity.this, str);
            }
        });
    }
}
