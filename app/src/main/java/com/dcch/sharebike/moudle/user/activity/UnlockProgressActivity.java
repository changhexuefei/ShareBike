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
    int progress = 0;
    boolean stop;
    private static final int UPDATE_PROGRESS = 0;
    private static final int PAUSE_PROGRESS = 90;
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
//        Intent intent = getIntent();
//        if (intent != null) {
//            String bikeNo = intent.getStringExtra("bikeNo");
//            String userId = intent.getStringExtra("userId");
//            String phone = intent.getStringExtra("phone");
//            openScan(phone, bikeNo, userId);
//        }
        Animation mAnimation = AnimationUtils.loadAnimation(UnlockProgressActivity.this, R.anim.unlock_lock_anim);
        mUnlockIcon.startAnimation(mAnimation);
        mMyProgressBar.setMax(100);

    }

//    private void openScan(String phone, String bikeNo, String userId) {
//        Map<String, String> map = new HashMap<>();
//        map.put("userId", userId);
//        map.put("phone", phone);
//        map.put("bicycleNo", bikeNo);
//
//        OkHttpUtils.post().url(Api.BASE_URL + Api.OPENSCAN).params(map).build().execute(new StringCallback() {
//            @Override
//            public void onError(Call call, Exception e, int id) {
//
//            }
//
//            @Override
//            public void onResponse(String response, int id) {
//                if (JsonUtils.isSuccess(response)) {
//                    handler.post(runnable);
////                    startActivity(new Intent(UnlockProgressActivity.this, MainActivity.class));
//                } else {
//                    handler.removeCallbacks(runnable);
////                    mMyProgressBar.setProgress(90);
//                }
//            }
//        });
//    }


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
                        Thread.sleep(100);
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

    @Override
    protected void onResume() {
        super.onResume();
        downloading(mMyProgressBar);
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
