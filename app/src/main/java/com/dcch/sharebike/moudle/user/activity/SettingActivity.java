package com.dcch.sharebike.moudle.user.activity;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dcch.sharebike.R;
import com.dcch.sharebike.app.App;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.base.MessageEvent;
import com.dcch.sharebike.base.ServiceAndroidContact;
import com.dcch.sharebike.base.UpdateManager;
import com.dcch.sharebike.moudle.login.activity.PersonalCenterActivity;
import com.dcch.sharebike.utils.ClickUtils;
import com.dcch.sharebike.utils.NetUtils;
import com.dcch.sharebike.utils.SPUtils;
import com.dcch.sharebike.utils.ToastUtils;

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

    public static Boolean IS = false;
    private ServiceAndroidContact serviceAndroidContact = new ServiceAndroidContact();

    ServiceConnection coon = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            serviceAndroidContact.Log();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };


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
                    if (NetUtils.isWifi(this)) {
                        UpdateManager updateManager = new UpdateManager(this);
                        updateManager.checkVersion();
                    } else {
                        ToastUtils.showShort(this, "你当前使用的手机网络,请切换成无线网络进行下载");
                    }

                } else {
                    ToastUtils.showShort(this, "网络无法连接，请检查网络连接");
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
                                EventBus.getDefault().post(new MessageEvent(), "unLogin");
                                //给主页发送消息
                                EventBus.getDefault().post(new MessageEvent(), "visible");
                                startActivity(i1);
                                SPUtils.clear(App.getContext());
                                SPUtils.put(App.getContext(), "islogin", false);
                                SPUtils.put(App.getContext(), "isfirst", false);
                                SPUtils.put(App.getContext(), "isStartGuide", true);
                                finish();
                            }
                        }).create()
                        .show();
                break;
        }
    }

    // 获取当前应用的版本号
    public int getVerCode() {
        int verCode = -1;
        try {
            verCode = getPackageManager().getPackageInfo("com.dcch.sharebike", 0).versionCode;
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return verCode;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent();
//        intent.setAction("com.gao.startService");
        intent.setPackage(getPackageName());
        bindService(intent, coon, BIND_AUTO_CREATE);
        if (IS) {
            /*String path = Environment.getExternalStorageDirectory() + "/DateApp.apk";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive");
            startActivity(intent);*/
            IS = false;
        }
    }

    /***
     * 检查是否存在某个App存在
     * @param pkgName
     * @return
     */
    private boolean isPkgInstalled(String pkgName) {
        PackageInfo packageInfo;
        try {
            packageInfo = this.getPackageManager().getPackageInfo(pkgName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo == null) {
            return false;
        } else {
            return true;
        }
    }
}
