package com.dcch.sharebike.moudle.user.activity;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dcch.sharebike.R;
import com.dcch.sharebike.app.App;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.base.MessageEvent;
import com.dcch.sharebike.base.ServiceAndroidContact;
import com.dcch.sharebike.moudle.login.activity.PersonalCenterActivity;
import com.dcch.sharebike.utils.SPUtils;
import com.dcch.sharebike.utils.ToastUtils;

import org.simple.eventbus.EventBus;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {

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

    @OnClick({R.id.usualAddress, R.id.checkVersions, R.id.aboutUs, R.id.userAgreement, R.id.cashPledgeExplain, R.id.rechargeAgreement, R.id.signOut})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.usualAddress:
                Intent address = new Intent(this, UsualAddressActivity.class);
                startActivity(address);
                break;
            case R.id.checkVersions:
//                int verCode = getVerCode();
//                Log.d("版本",verCode+"");
//                if (2 > verCode) {
////                    // 这里来检测版本是否需要更新
////                    UpdateManager mUpdateManager = new UpdateManager(this);
////                    mUpdateManager.checkUpdateInfo();
//                    ToastUtils.showShort(SettingActivity.this, "有新版本，记得更新哟");
//                } else {
//                    ToastUtils.showShort(SettingActivity.this, "已经是最新版本了无需更新");
//                }

                //测试是否安装安装某个软件
                Boolean IIII = isPkgInstalled("com.dcch.sharebike");
                Log.d("@@@@@",IIII+"");
                Toast.makeText(getApplicationContext(),IIII+"",Toast.LENGTH_SHORT).show();

                //安装指定的软件
                String path = Environment.getExternalStorageDirectory() + "/app-release.apk";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive");
                startActivity(intent);
                break;
            case R.id.aboutUs:
                startActivity(new Intent(SettingActivity.this, AboutUsActivity.class));
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
        } catch (PackageManager.NameNotFoundException e) {
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
        bindService(intent,coon,BIND_AUTO_CREATE);
        if (IS == true) {
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
        PackageInfo packageInfo = null;
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
