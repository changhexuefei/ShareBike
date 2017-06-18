package com.dcch.sharebike.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;

import com.dcch.sharebike.MainActivity;
import com.dcch.sharebike.R;
import com.dcch.sharebike.app.App;
import com.dcch.sharebike.base.AppManager;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.base.MessageEvent;
import com.dcch.sharebike.utils.LogUtils;
import com.dcch.sharebike.utils.NetUtils;
import com.dcch.sharebike.utils.SPUtils;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import butterknife.BindView;

public class SplashActivity extends BaseActivity {

    @BindView(R.id.rl_splash_root)
    RelativeLayout mRlSplashRoot;

    private final static int SWITCH_MAINACTIVITY = 1000;
    private final static int SWITCH_GUIDACTIVITY = 1001;

//    private Handler handler = new Handler() {
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case SWITCH_MAINACTIVITY:
//                   goMain();
//                    break;
//
//                case SWITCH_GUIDACTIVITY:
//                    goGuide();
//                    break;
//            }
//            super.handleMessage(msg);
//        }
//    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initData() {
//        //设置全屏显示
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        AlphaAnimation animation = new AlphaAnimation(0, 1);
        animation.setDuration(2000);
        animation.setFillAfter(true);
        mRlSplashRoot.startAnimation(animation);
        LogUtils.d("网络", NetUtils.isNetworkAvailable(App.getContext()) + "");


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
    protected void onResume() {
        super.onResume();
        if (NetUtils.isNetworkAvailable(App.getContext())) {
            if (SPUtils.isFirst()) {
//                handler.sendEmptyMessageDelayed(SWITCH_GUIDACTIVITY, 1500);
                EventBus.getDefault().post(new MessageEvent(), "guide");
            } else {
//                handler.sendEmptyMessageDelayed(SWITCH_MAINACTIVITY, 1500);
                EventBus.getDefault().post(new MessageEvent(), "main");
            }
        } else {
//            ToastUtils.showLong(SplashActivity.this, getString(R.string.coupon_tip_two));
            NetUtils.checkNetwork(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
//        handler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        AppManager.finishActivity(this);
//        App.getRefWatcher().watch(this);
    }


    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }


    //gotoguide
    @Subscriber(tag = "guide", mode = ThreadMode.POST)
    private void receiveMessageToGuide(MessageEvent info) {
        LogUtils.d("输入", info.toString() + "guide");
        goGuide();
    }

    //gotoMain
    @Subscriber(tag = "main", mode = ThreadMode.POST)
    private void receiveMessageToMain(MessageEvent info) {
        LogUtils.d("输入", info.toString() + "main");
        goMain();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

    }
}
