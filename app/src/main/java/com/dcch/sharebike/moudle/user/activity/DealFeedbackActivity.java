package com.dcch.sharebike.moudle.user.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dcch.sharebike.MainActivity;
import com.dcch.sharebike.R;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.utils.ClickUtils;
import com.dcch.sharebike.utils.MapUtil;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyDialogListener;
import com.sefford.circularprogressdrawable.CircularProgressDrawable;

import butterknife.BindView;
import butterknife.OnClick;

public class DealFeedbackActivity extends BaseActivity {

    Animator currentAnimation;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.iv_drawable)
    ImageView mIvDrawable;
    @BindView(R.id.feedback_time)
    TextView mFeedbackTime;
    @BindView(R.id.select_cost)
    TextView mSelectCost;
    @BindView(R.id.feedback_detail)
    TextView mFeedbackDetail;
    @BindView(R.id.customer_service)
    Button mCustomerService;
    CircularProgressDrawable drawable;
    @BindView(R.id.reminder)
    TextView mReminder;
    private String mRechargeNumber;
    private String mContentText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_deal_feedback;
    }

    @Override
    protected void initData() {
        mToolbar.setTitle("");
        mTitle.setText(getResources().getString(R.string.feedback_detail));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DealFeedbackActivity.this, MainActivity.class));
                DealFeedbackActivity.this.finish();
            }
        });
        Intent intent = getIntent();
        if (intent != null && !intent.equals("")) {
            mRechargeNumber = intent.getStringExtra("rechargeNumber");
            mContentText = intent.getStringExtra("contentText");
            mSelectCost.setText(mRechargeNumber);
            mFeedbackDetail.setText(mContentText);
            mFeedbackTime.setText(MapUtil.getStringDate());
        }
    }

    @OnClick(R.id.customer_service)
    public void onViewClicked() {
        if (ClickUtils.isFastClick()) {
            return;
        }
        StyledDialog.buildIosAlert(DealFeedbackActivity.this, "提示", "拨打电话 400-660-6215", new MyDialogListener() {
            @Override
            public void onFirst() {
//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_CALL);//指定意图动作
//                intent.setData(Uri.parse("tel:400-660-6215"));//指定电话号码
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "400-660-6215"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }

            @Override
            public void onSecond() {
                return;
            }
        }).setMsgColor(R.color.colorHeading).setMsgSize(16).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        drawable = new CircularProgressDrawable.Builder()
                .setRingWidth(getResources().getDimensionPixelSize(R.dimen.drawable_ring_size))
                .setOutlineColor(getColor(android.R.color.darker_gray))
                .setRingColor(getColor(android.R.color.holo_green_light))
                .setCenterColor(getColor(android.R.color.holo_blue_dark))
                .create();
        mIvDrawable.setImageDrawable(drawable);
        if (currentAnimation != null) {
            currentAnimation.cancel();
        }
        currentAnimation = prepareStyle3Animation();
        currentAnimation.start();
        currentAnimation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
//                mReminder.setText("处理中.......");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
//                mReminder.setText("");
//                mReminder.setText("结束了");
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private Animator prepareStyle3Animation() {
        AnimatorSet animation = new AnimatorSet();

        ObjectAnimator progressAnimation = ObjectAnimator.ofFloat(drawable, CircularProgressDrawable.PROGRESS_PROPERTY, 1f, 0f);
        progressAnimation.setDuration(4000);
        progressAnimation.setInterpolator(new AnticipateInterpolator());

        Animator innerCircleAnimation = ObjectAnimator.ofFloat(drawable, CircularProgressDrawable.CIRCLE_SCALE_PROPERTY, 0.75f, 0f);
        innerCircleAnimation.setDuration(4000);
        innerCircleAnimation.setInterpolator(new AnticipateInterpolator());

        ObjectAnimator invertedProgress = ObjectAnimator.ofFloat(drawable, CircularProgressDrawable.PROGRESS_PROPERTY, 0f, 1f);
        invertedProgress.setDuration(4000);
        invertedProgress.setStartDelay(4000);
        invertedProgress.setInterpolator(new OvershootInterpolator());

        Animator invertedCircle = ObjectAnimator.ofFloat(drawable, CircularProgressDrawable.CIRCLE_SCALE_PROPERTY, 0f, 0.75f);
        invertedCircle.setDuration(4000);
        invertedCircle.setStartDelay(4000);
        invertedCircle.setInterpolator(new OvershootInterpolator());

        animation.playTogether(progressAnimation, innerCircleAnimation, invertedProgress, invertedCircle);
        return animation;
    }


}
