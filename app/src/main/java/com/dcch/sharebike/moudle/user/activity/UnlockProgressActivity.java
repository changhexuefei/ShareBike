package com.dcch.sharebike.moudle.user.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.dcch.sharebike.MainActivity;
import com.dcch.sharebike.R;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.base.MessageEvent;
import com.dcch.sharebike.utils.LogUtils;
import com.dcch.sharebike.view.MyProgressBar;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.lang.ref.WeakReference;

import butterknife.BindView;

public class UnlockProgressActivity extends BaseActivity {

    //    @BindView(R.id.unlockIcon)
//    ImageView mUnlockIcon;
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
    private static final int WHAT = 1;
    int num = 0;
    MyHandler handler = new MyHandler(this);

    private class MyHandler extends Handler {
        WeakReference<Activity> weakReference;

        MyHandler(Activity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (weakReference.get() != null) {
                switch (msg.what) {
                    case WHAT:
                        num++;
                        if (num <= mMyProgressBar.getMax()) {
                            mMyProgressBar.setProgress(num);
                            handler.sendEmptyMessageDelayed(WHAT, 20);
                        }
                        break;
                }
            }
        }
    }

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("实验", "onResume+2");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        mMyProgressBar.setEndSuccessBackgroundColor(Color.parseColor("#66A269"))//设置进度完成时背景颜色
                .setEndSuccessDrawable(R.drawable.ic_done_white_36dp, null)//设置进度完成时背景图片
                .setCanEndSuccessClickable(false)//设置进度完成后是否可以再次点击开始
                .setProgressBarColor(Color.WHITE)//进度条颜色
                .setCanDragChangeProgress(false)//是否进度条是否可以拖拽
                .setCanReBack(true)//是否在进度成功后返回初始状态
                .setProgressBarBgColor(Color.parseColor("#491C14"))//进度条背景颜色
                .setProgressBarHeight(mMyProgressBar.dip2px(this, 4))//进度条宽度
                .setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()))//设置字体大小
                .setStartDrawable(R.drawable.ic_file_upload_white_36dp, null)//设置开始时背景图片
                .setTextColorSuccess(Color.parseColor("#66A269"))//设置成功时字体颜色
                .setTextColorNormal(Color.parseColor("#491C14"))//设置默认字体颜色
                .setTextColorError(Color.parseColor("#BC5246"));//设置错误时字体颜色

        mMyProgressBar.setOnAnimationEndListener(new MyProgressBar.AnimationEndListener() {
            @Override
            public void onAnimationEnd() {
                mMyProgressBar.setMax(100);
                mMyProgressBar.setProgress(num);//初次进入在动画结束时设置进度
                Log.d("实验", "结束了+++++++");
                handler.sendEmptyMessage(WHAT);
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        //你需要跳转的地方的代码
                        startActivity(new Intent(UnlockProgressActivity.this, MainActivity.class));
                        EventBus.getDefault().post(new MessageEvent(),"create");
                        UnlockProgressActivity.this.finish();
                    }
                }, 6000); //延迟6秒跳转

            }
        });
        mMyProgressBar.setOntextChangeListener(new MyProgressBar.OntextChangeListener() {
            @Override
            public String onProgressTextChange(MyProgressBar specialProgressBarView, int max, int progress) {
                return progress * 100 / max + "%";

            }

            @Override
            public String onErrorTextChange(MyProgressBar specialProgressBarView, int max, int progress) {
                return "error";
            }

            @Override
            public String onSuccessTextChange(MyProgressBar specialProgressBarView, int max, int progress) {
                Log.d("实验", "结束了");
                return "done";
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("实验", "onPause+2");
    }

    @Subscriber(tag = "on", mode = ThreadMode.POST)
    private void receiveFromMain(MessageEvent info) {
        LogUtils.d("给后台", info.toString());
        if (info != null) {
            num = 0;
            mMyProgressBar.beginStarting();//启动开始动画

        } else {
            startActivity(new Intent(UnlockProgressActivity.this, MainActivity.class));
            UnlockProgressActivity.this.finish();
        }
    }

    @Subscriber(tag = "off", mode = ThreadMode.POST)
    private void receiveFromMainOther(MessageEvent info) {
        LogUtils.d("给后台", info.toString());
        mMyProgressBar.setError();//进度失败 发生错误
        startActivity(new Intent(UnlockProgressActivity.this, MainActivity.class));
        UnlockProgressActivity.this.finish();
    }

}
