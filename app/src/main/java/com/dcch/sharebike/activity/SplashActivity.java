package com.dcch.sharebike.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;

import com.dcch.sharebike.MainActivity;
import com.dcch.sharebike.R;
import com.dcch.sharebike.app.App;
import com.dcch.sharebike.base.AppManager;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.utils.LogUtils;
import com.dcch.sharebike.utils.NetUtils;
import com.dcch.sharebike.utils.SPUtils;

import butterknife.BindView;

public class SplashActivity extends BaseActivity {

    @BindView(R.id.rl_splash_root)
    RelativeLayout mRlSplashRoot;

    private final static int SWITCH_MAINACTIVITY = 1000;
    private final static int SWITCH_GUIDACTIVITY = 1001;


    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SWITCH_MAINACTIVITY:
                    switchPage();
                    break;

                case SWITCH_GUIDACTIVITY:
                    goGuide();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initData() {
        //设置全屏显示
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        AlphaAnimation animation = new AlphaAnimation(0, 1);
        animation.setDuration(1000);
        animation.setFillAfter(true);
        mRlSplashRoot.startAnimation(animation);
        if(NetUtils.isConnected(App.getContext())){
            if (SPUtils.isFirst()) {
                handler.sendEmptyMessageDelayed(SWITCH_GUIDACTIVITY, 1000);
            } else {
                handler.sendEmptyMessageDelayed(SWITCH_MAINACTIVITY, 1000);
            }
        }else{
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("")
                    .setMessage(getString(R.string.no_network_tip))
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            AppManager.finishCurrentActivity();
                        }
                    })
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            NetUtils.openSetting(SplashActivity.this);
                        }
                    })
                    .setCancelable(false)
                    .show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.d("设置",data.toString());
    }

    private void switchPage() {
        boolean isStartGuide = (boolean) SPUtils.get(this, "isStartGuide", false);
        if (SPUtils.isLogin()) {
            LogUtils.e("已经登录...");
            goMain();
        } else {
            LogUtils.e("没有登录...");
            if (isStartGuide) {
                goMain();
            } else {
                goGuide();
            }
        }
    }

    private void goGuide() {
        Intent intent = new Intent(this, GuideActivity.class);
        startActivity(intent);
        finish();
    }

    private void goMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        if (handler != null) {
            handler = null;
        }
        AppManager.finishActivity(this);
//        App.getRefWatcher().watch(this);
    }
}
