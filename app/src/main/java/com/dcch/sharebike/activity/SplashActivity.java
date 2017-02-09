package com.dcch.sharebike.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;

import com.dcch.sharebike.MainActivity;
import com.dcch.sharebike.R;
import com.dcch.sharebike.base.BaseActivity;

import butterknife.BindView;

public class SplashActivity extends BaseActivity {

    @BindView(R.id.rl_splash_root)
    RelativeLayout mRlSplashRoot;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1001:
                    switchPage();
                    break;
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash ;
    }

    @Override
    protected void initData() {
//        设置全屏显示
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        AlphaAnimation animation = new AlphaAnimation(0, 1);
        animation.setDuration(1000);
        animation.setFillAfter(true);
        mRlSplashRoot.startAnimation(animation);
        handler.sendEmptyMessageDelayed(1001, 2000);
    }

    private void switchPage() {
//		if (SPUtils.isLogin()) {
//			LogUtils.e("已经登录...");
        startActivity(new Intent(this, MainActivity.class));
        finish();
//		} else {
//			LogUtils.e("没有登录...");
//			boolean isStartGuide = (boolean) SPUtils.get(App.getContext(), "isStartGuide", false);
//			if (isStartGuide) {
//				startActivity(new Intent(this, LoginActivity.class));
//				finish();
//			} else {
//				startActivity(new Intent(this, GuideActivity.class));
//				finish();
//			}
//		}
    }

//	@Override
//	protected void onResume() {
//		super.onResume();
//		JPushInterface.onResume(this);
//	}
//
//	@Override
//	protected void onPause() {
//		super.onPause();
//		JPushInterface.onPause(this);
//	}
}
