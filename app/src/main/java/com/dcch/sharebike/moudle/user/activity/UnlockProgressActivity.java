package com.dcch.sharebike.moudle.user.activity;

import android.app.AlarmManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.dcch.sharebike.MainActivity;
import com.dcch.sharebike.R;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.base.MessageEvent;
import com.dcch.sharebike.utils.LogUtils;
import com.dcch.sharebike.utils.ToastUtils;
import com.dcch.sharebike.view.MarqueeTextView;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;

public class UnlockProgressActivity extends BaseActivity {

    //    @BindView(R.id.myProgressBar)
//    MyProgressBar mMyProgressBar;
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
    @BindView(R.id.unlockIcon)
    ImageView mUnlockIcon;
    @BindView(R.id.menu_desc)
    MarqueeTextView mMenuDesc;

    private Animation mAnimation;
    private Timer mTimer;
    private AlarmManager mAlarmManager;

//    MyHandler handler = new MyHandler(this);

//    private class MyHandler extends Handler {
//        WeakReference<Activity> weakReference;
//
//        MyHandler(Activity activity) {
//            weakReference = new WeakReference<>(activity);
//        }
//
//        @Override
//        public void handleMessage(Message msg) {
//            if (weakReference.get() != null) {
//                switch (msg.what) {
//                    case WHAT:
//                        num++;
//                        if (num <= mMyProgressBar.getMax()) {
//                            mMyProgressBar.setProgress(num);
//                            handler.sendEmptyMessageDelayed(WHAT, 20);
//                        }
//                        break;
//                }
//            }
//        }
//    }

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
//                mTimer.cancel();
//                finish();
                ToastUtils.showShort(UnlockProgressActivity.this, "正在开锁中，请稍后.....");
            }
        });

        mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//        mMyProgressBar.setEndSuccessBackgroundColor(Color.parseColor("#66A269"))//设置进度完成时背景颜色
//                .setEndSuccessDrawable(R.drawable.ic_done_white_36dp, null)//设置进度完成时背景图片
//                .setCanEndSuccessClickable(false)//设置进度完成后是否可以再次点击开始
//                .setProgressBarColor(Color.WHITE)//进度条颜色
//                .setCanDragChangeProgress(false)//是否进度条是否可以拖拽
//                .setCanReBack(true)//是否在进度成功后返回初始状态
//                .setProgressBarBgColor(Color.parseColor("#491C14"))//进度条背景颜色
//                .setProgressBarHeight(mMyProgressBar.dip2px(this, 4))//进度条宽度
//                .setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()))//设置字体大小
//                .setStartDrawable(R.drawable.ic_file_upload_white_36dp, null)//设置开始时背景图片
//                .setTextColorSuccess(Color.parseColor("#66A269"))//设置成功时字体颜色
//                .setTextColorNormal(Color.parseColor("#491C14"))//设置默认字体颜色
//                .setTextColorError(Color.parseColor("#BC5246"));//设置错误时字体颜色
//
//
//        mMyProgressBar.setOnAnimationEndListener(new MyProgressBar.AnimationEndListener() {
//            @Override
//            public void onAnimationEnd() {
//                mMyProgressBar.setMax(100);
//                mMyProgressBar.setProgress(num);//初次进入在动画结束时设置进度
//                Log.d("实验", "结束了+++++++" + "onCreate");
//                handler.sendEmptyMessage(WHAT);
//
//                new Handler().postDelayed(new Runnable() {
//                    public void run() {
//                        //你需要跳转的地方的代码
//                        startActivity(new Intent(UnlockProgressActivity.this, MainActivity.class));
//                        EventBus.getDefault().post(new MessageEvent(), "order_show");
//                        UnlockProgressActivity.this.finish();
//                    }
//                }, 5000); //延迟5秒跳转
//
//            }
//        });
//        mMyProgressBar.setOntextChangeListener(new MyProgressBar.OntextChangeListener() {
//            @Override
//            public String onProgressTextChange(MyProgressBar specialProgressBarView, int max, int progress) {
//                return progress * 100 / max + "%";
//
//            }
//
//            @Override
//            public String onErrorTextChange(MyProgressBar specialProgressBarView, int max, int progress) {
//                return "error";
//            }
//
//            @Override
//            public String onSuccessTextChange(MyProgressBar specialProgressBarView, int max, int progress) {
//                return "done";
//            }
//        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.d("实验", "我已经创建好了");
        EventBus.getDefault().register(this);
        mAnimation = AnimationUtils.loadAnimation(this, R.anim.unlock_lock_anim);
        mUnlockIcon.startAnimation(mAnimation);
        mMenuDesc.setTextColor(getResources().getColor(R.color.white));
        mMenuDesc.setSpeed(5);
        mMenuDesc.startScroll();
        final Intent main = new Intent(this, MainActivity.class); // 要转向的Activity
        mTimer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                startActivity(main); // 启动新的Activity      //或不写该段仅让当前Activity消失
                EventBus.getDefault().post(new MessageEvent(), "stopNetty");
                UnlockProgressActivity.this.finish();
            }
        };
        mTimer.schedule(task, 1000 * 30); // 40秒后执行跳转页面的操作
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("实验", "onResume+2");
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("实验", "onPause+2");

    }

    @Subscriber(tag = "on", mode = ThreadMode.MAIN)
    private void receiveFromNettyService(MessageEvent info) {
        LogUtils.d("NettyService", "成功" + info.toString());
        if (info != null) {

            startActivity(new Intent(UnlockProgressActivity.this, MainActivity.class));
            EventBus.getDefault().post(new MessageEvent(), "order_show");
            mTimer.cancel();
            UnlockProgressActivity.this.finish();
//            mMyProgressBar.beginStarting();//启动开始动画
        }
    }

    @Subscriber(tag = "off", mode = ThreadMode.MAIN)
    private void receiveFromNettyServiceOther(MessageEvent info) {
        LogUtils.d("NettyServiceOther", "失败" + info.toString());
//        mMyProgressBar.setError();//进度失败 发生错误
        mTimer.cancel();
        startActivity(new Intent(UnlockProgressActivity.this, MainActivity.class));
        EventBus.getDefault().post(new MessageEvent(), "lose");
        this.finish();
    }

    //接收到关锁成功命令Nettyservice发来的消息
    @Subscriber(tag = "close", mode = ThreadMode.MAIN)
    private void receiveCloseFromNettyService(MessageEvent info) {
        LogUtils.d("输入", info.toString());
//        mMyProgressBar.setError();
//        mMyProgressBar.changeStateError();
//        handler.removeCallbacksAndMessages(null);
//        mMyProgressBar.setProgress(0);
        mTimer.cancel();
        this.finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
