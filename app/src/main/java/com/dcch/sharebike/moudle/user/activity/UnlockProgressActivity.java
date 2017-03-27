package com.dcch.sharebike.moudle.user.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.dcch.sharebike.MainActivity;
import com.dcch.sharebike.R;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.view.MyProgressBar;

import butterknife.BindView;
import butterknife.OnClick;

public class UnlockProgressActivity extends BaseActivity {

    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.unlockIcon)
    ImageView mUnlockIcon;
    @BindView(R.id.myProgressBar)
    MyProgressBar mMyProgressBar;
    boolean isDownloading;
    boolean stop;
    private static final int UPDATE_PROGRESS = 0;
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_PROGRESS:
                    mMyProgressBar.setProgress(msg.arg1);
                    if (msg.arg1 == 100) {
                        isDownloading = false;
                        stop = true;
                        startActivity(new Intent(UnlockProgressActivity.this, MainActivity.class));
                    }
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
        Animation mAnimation = AnimationUtils.loadAnimation(this, R.anim.unlock_lock_anim);
        mUnlockIcon.startAnimation(mAnimation);
        mMyProgressBar.setMax(100);
        downloading(mMyProgressBar);
    }


    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();

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
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    progress += 1;
                    msg.what = UPDATE_PROGRESS;
                    msg.arg1 = progress;
                    msg.sendToTarget();
                }
                progress = 0;
            }
        }).start();

    }


}
