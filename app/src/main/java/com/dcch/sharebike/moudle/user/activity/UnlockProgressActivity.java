package com.dcch.sharebike.moudle.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.dcch.sharebike.MainActivity;
import com.dcch.sharebike.R;
import com.dcch.sharebike.app.App;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.base.MessageEvent;
import com.dcch.sharebike.utils.LogUtils;
import com.dcch.sharebike.utils.ToastUtils;
import com.dcch.sharebike.view.MyProgressBar;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import butterknife.BindView;

public class UnlockProgressActivity extends BaseActivity {

    @BindView(R.id.unlockIcon)
    ImageView mUnlockIcon;
    @BindView(R.id.myProgressBar)
    MyProgressBar mMyProgressBar;
    boolean isDownloading;
    int progress = 0;
    boolean stop;
    private static final int UPDATE_PROGRESS = 0;
    private static final int PAUSE_PROGRESS = 90;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_PROGRESS:
                    mMyProgressBar.setProgress(msg.arg1);
                    if (msg.arg1 == 100) {
                        isDownloading = false;
                        stop = true;
                        startActivity(new Intent(UnlockProgressActivity.this, MainActivity.class));
                        UnlockProgressActivity.this.finish();
                    }
                    break;
                case PAUSE_PROGRESS:

                    break;

                default:
                    break;
            }
        }
    };


    @Override
    protected int getLayoutId() {
        return R.layout.activity_unlock_progress;
    }

    @Override
    protected void initData() {
        mToolbar.setTitle("");
        mTitle.setText(getResources().getString(R.string.unlock_progress));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Animation mAnimation = AnimationUtils.loadAnimation(UnlockProgressActivity.this, R.anim.unlock_lock_anim);
        mUnlockIcon.startAnimation(mAnimation);
        mMyProgressBar.setMax(100);
    }

    private void downloading(MyProgressBar myProgress) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int progress = 0;
                while (!stop) {
                    if (progress >= 100) {
                        break;
                    }
                    Message msg = handler.obtainMessage();
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    progress += 1;
                    msg.what = UPDATE_PROGRESS;
                    msg.arg1 = progress;
                    msg.sendToTarget();
                }
            }
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscriber(tag = "on", mode = ThreadMode.MAIN)
    private void receiveFromMain(MessageEvent info) {
        LogUtils.d("消息", info.toString());
        downloading(mMyProgressBar);
    }

    @Subscriber(tag = "off", mode = ThreadMode.MAIN)
    private void receiveFromMainOther(MessageEvent info) {
        LogUtils.d("消息", info.toString());
        ToastUtils.showShort(App.getContext(), "开锁失败！");
        startActivity(new Intent(UnlockProgressActivity.this, MainActivity.class));
        UnlockProgressActivity.this.finish();
    }


    //    //定义线程
//    Runnable runnable=new Runnable() {
//        @Override
//        public void run() {
//            progress=mMyProgressBar.getProgress()+1;
//            mMyProgressBar.setProgress(progress);
//            setTitle(String.valueOf(progress));
//            //如果进度小于100,则延迟1000毫秒之后重复执行runnable
//            if(progress<100)
//            {
//                handler.postDelayed(runnable, 100);
//            }
//            //否则，都置零，线程重新执行
//            else {
//                mMyProgressBar.setProgress(0);
////                setTitle(String.valueOf(0));
////                handler.post(runnable);
//            }
//        }
//    };


}
