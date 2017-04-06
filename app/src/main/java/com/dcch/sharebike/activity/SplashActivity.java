package com.dcch.sharebike.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;

import com.dcch.sharebike.MainActivity;
import com.dcch.sharebike.R;
import com.dcch.sharebike.app.App;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.utils.LogUtils;
import com.dcch.sharebike.utils.SPUtils;

import butterknife.BindView;
import cn.jpush.android.api.JPushInterface;

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
        if (SPUtils.isFirst()) {
            handler.sendEmptyMessageDelayed(SWITCH_GUIDACTIVITY, 3000);
        } else {
            handler.sendEmptyMessageDelayed(SWITCH_MAINACTIVITY, 3000);
        }
    }


    private void switchPage() {
        boolean isStartGuide = (boolean) SPUtils.get(App.getContext(), "isStartGuide", false);
        Log.d("知道也", isStartGuide + "");
        if (SPUtils.isLogin()) {
            LogUtils.e("已经登录...");
            Intent login = new Intent(App.getContext(), MainActivity.class);
            startActivity(login);
            SplashActivity.this.finish();
        } else {
            LogUtils.e("没有登录...");
            if (isStartGuide) {
                startActivity(new Intent(App.getContext(), MainActivity.class));
                finish();
            } else {
                startActivity(new Intent(App.getContext(), GuideActivity.class));
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    private void goGuide() {
        Intent intent = new Intent(SplashActivity.this, GuideActivity.class);
        SplashActivity.this.startActivity(intent);
        SplashActivity.this.finish();
    }
}
