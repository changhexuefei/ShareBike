package com.dcch.sharebike.moudle.user.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.dcch.sharebike.utils.ClickUtils;
import com.dcch.sharebike.utils.NetUtils;
import com.dcch.sharebike.utils.SPUtils;
import com.dcch.sharebike.utils.ToastUtils;
import com.iflytek.autoupdate.IFlytekUpdate;
import com.iflytek.autoupdate.IFlytekUpdateListener;
import com.iflytek.autoupdate.UpdateConstants;
import com.iflytek.autoupdate.UpdateErrorCode;
import com.iflytek.autoupdate.UpdateInfo;
import com.iflytek.autoupdate.UpdateType;

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

    private IFlytekUpdate updManager;


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
                ToastUtils.showShort(this, "正在检查更新");
                if (NetUtils.isConnected(this)) {
                    checkUpDate();
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
//                        SPUtils.put(App.getContext(), "isStartGuide", true);
                        startActivity(i1);
                        finish();
                    }
                }).create()
                .show();
    }

    private void checkUpDate() {
        //初始化自动更新对象
        final IFlytekUpdate updManager = IFlytekUpdate.getInstance(App.getContext());
        //开启调试模式，默认不开启
        updManager.setDebugMode(true);
        //开启wifi环境下检测更新，仅对自动更新有效，强制更新则生效
        updManager.setParameter(UpdateConstants.EXTRA_WIFIONLY, "true");
        //设置通知栏使用应用icon，详情请见示例
        updManager.setParameter(UpdateConstants.EXTRA_NOTI_ICON, "true");
        //设置更新提示类型，默认为通知栏提示
        updManager.setParameter(UpdateConstants.EXTRA_STYLE, UpdateConstants.UPDATE_UI_DIALOG);
        //自动更新回调方法，详情参考demo
        IFlytekUpdateListener updateListener = new IFlytekUpdateListener() {
            @Override
            public void onResult(int errorcode, final UpdateInfo result) {

                if (errorcode == UpdateErrorCode.OK && result != null) {
                    if (result.getUpdateType() == UpdateType.NoNeed) {
                        showTip("当前为最新版本");
                        return;
                    }
                    updManager.showUpdateInfo(SettingActivity.this, result);
                } else {
                    showTip("请求更新失败！更新错误码：" + errorcode);
                }
            }
        };
// 启动自动更新
        updManager.autoUpdate(this, updateListener);

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
