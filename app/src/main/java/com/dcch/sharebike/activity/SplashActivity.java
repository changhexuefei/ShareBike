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
    // 判断应用是否初次加载，读取SharedPreferences中的guide_activity字段
//    private static final String SHAREDPREFERENCES_NAME = "my_pref";
//    private static final String KEY_GUIDE_ACTIVITY = "guide_activity";
    private final static int SWITCH_MAINACTIVITY = 1000;
    private final static int SWITCH_GUIDACTIVITY = 1001;


    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SWITCH_MAINACTIVITY:
                    switchPage();
                    break;

                case SWITCH_GUIDACTIVITY:
                    Intent mIntent = new Intent();
                    mIntent.setClass(SplashActivity.this, GuideActivity.class);
                    SplashActivity.this.startActivity(mIntent);
                    SplashActivity.this.finish();
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
            handler.sendEmptyMessageDelayed(SWITCH_GUIDACTIVITY, 5000);
        }
        else {
            handler.sendEmptyMessageDelayed(SWITCH_MAINACTIVITY, 5000);
        }
        SPUtils.put(App.getContext(),"isfirst",false);
    }


    private void switchPage() {
        if (SPUtils.isLogin()) {
            LogUtils.e("已经登录...");
            Intent login = new Intent(this, MainActivity.class);
            startActivity(login);
            SplashActivity.this.finish();
        } else {
            LogUtils.e("没有登录...");
            boolean isStartGuide = (boolean) SPUtils.get(App.getContext(), "isStartGuide", false);
            Log.d("知道也",isStartGuide+"");
            if (isStartGuide) {
				startActivity(new Intent(this, MainActivity.class));
				finish();
			} else {
				startActivity(new Intent(this, GuideActivity.class));
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
}
