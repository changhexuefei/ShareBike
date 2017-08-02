package com.dcch.sharebike.moudle.user.activity;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dcch.sharebike.R;
import com.dcch.sharebike.app.App;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.base.MessageEvent;
import com.dcch.sharebike.http.Api;
import com.dcch.sharebike.utils.ClickUtils;
import com.dcch.sharebike.utils.JsonUtils;
import com.dcch.sharebike.utils.LogUtils;
import com.dcch.sharebike.utils.SPUtils;
import com.dcch.sharebike.utils.ToastUtils;
import com.hss01248.dialog.StyledDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;
import org.simple.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;


public class OpenRedEnvelopeActivity extends BaseActivity {

    @BindView(R.id.Congratulations)
    TextView mCongratulations;
    @BindView(R.id.red_sum)
    TextView mRedSum;
    @BindView(R.id.close_redPacket)
    ImageView mCloseRedPacket;
    @BindView(R.id.lly_back)
    LinearLayout mLlyBack;
    @BindView(R.id.lly_front)
    LinearLayout mLlyFront;
    @BindView(R.id.fl_container)
    ConstraintLayout mFlContainer;
    @BindView(R.id.merchant_icon)
    ImageView mMerchantIcon;
    @BindView(R.id.merchant_info)
    TextView mMerchantInfo;
    private String mUserId;
    private String mAmount;
    private String mTitle;
    private String mContent;
    private boolean isClick = true;
    private AnimatorSet mFrontAnimator;
    private AnimatorSet mBackAnimator;
    private boolean isShowFront = true;
    private String mMMerchaninfo;
    private String mMMerchantimageurl;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_open_red_envelope;
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            mMMerchaninfo = intent.getStringExtra("mMerchaninfo");
            mMMerchantimageurl = intent.getStringExtra("mMerchantimageurl");
            mMerchantInfo.setText(mMMerchaninfo + "给您发来一份惊喜红包！");
            Glide.with(App.getContext()).load(mMMerchantimageurl).into(mMerchantIcon);
        }
        mUserId = String.valueOf(SPUtils.get(App.getContext(), "userId", 0));
        initAnimator();
        setAnimatorListener();
        setCameraDistance();
    }

    //R.id.imageView,
    @OnClick({R.id.close_redPacket, R.id.lly_front})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.close_redPacket:
                if (ClickUtils.isFastClick()) {
                    return;
                }
                finish();
                break;

            case R.id.lly_front:
                if (ClickUtils.isFastClick()) {
                    return;
                }
                EventBus.getDefault().post(new MessageEvent(), "unable_click");
                if (isClick) {
                    StyledDialog.buildLoading(OpenRedEnvelopeActivity.this, "", true, false).show();
                    getRedPacket(mUserId);
                } else {
                    ToastUtils.showShort(OpenRedEnvelopeActivity.this, "您已经领过红包了，下次骑行再来吧");
                }
                break;
        }

    }

    /**
     * 1.初始化动画
     */
    private void initAnimator() {
        mFrontAnimator = (AnimatorSet) AnimatorInflater.loadAnimator(this,
                R.animator.anim_in);
        mBackAnimator = (AnimatorSet) AnimatorInflater.loadAnimator(this,
                R.animator.anim_out);
    }

    /**
     * 2.设置视角间距，防止旋转时超出边界区域
     */

    private void setCameraDistance() {
        int distance = 6000;
        float scale = getResources().getDisplayMetrics().density * distance;
        mLlyFront.setCameraDistance(scale);
        mLlyBack.setCameraDistance(scale);
    }

    /**
     * 3.设置动画监听事件
     */
    private void setAnimatorListener() {

        mFrontAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                mFlContainer.setClickable(false);
            }
        });

        mBackAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mFlContainer.setClickable(true);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (mTitle != null && !mTitle.equals("") && !mContent.equals("") && mContent != null) {
                    getRedPacketResult(mTitle, mContent);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }

    /**
     * 4.开启动画
     *
     * @param view
     */
    public void startAnimation(View view) {

        //显示正面
        if (!isShowFront) {
            mFrontAnimator.setTarget(mLlyFront);
            mBackAnimator.setTarget(mLlyBack);
            mFrontAnimator.start();
            mBackAnimator.start();
            isShowFront = true;
        } else {
            mFrontAnimator.setTarget(mLlyBack);
            mBackAnimator.setTarget(mLlyFront);
            mFrontAnimator.start();
            mBackAnimator.start();
            isShowFront = false;
        }
    }


    private void getRedPacket(String userId) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", userId);
        LogUtils.d("用户", userId);
        OkHttpUtils.post().url(Api.BASE_URL + Api.MERCHANTGIFT).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                StyledDialog.dismissLoading();
                ToastUtils.showShort(OpenRedEnvelopeActivity.this, getString(R.string.server_tip));
            }

            @Override
            public void onResponse(String response, int id) {
                isClick = false;
                LogUtils.d("用户", response);
                StyledDialog.dismissLoading();
                startAnimation(mFlContainer);
                EventBus.getDefault().post(new MessageEvent(), "unable_click");
                mMerchantIcon.setVisibility(View.GONE);
                mMerchantInfo.setVisibility(View.GONE);
                if (JsonUtils.isSuccess(response)) {
                    try {
                        JSONObject object = new JSONObject(response);
                        mAmount = object.optString("amount");
                        mTitle = "恭喜您！";
                        mContent = "获得由" + mMMerchaninfo + "发放的" + mAmount + "元红包";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    mTitle = "很遗憾！";
                    mContent = "下次骑行再来试试手气吧！";
                }
            }
        });
    }

    private void getRedPacketResult(String title, String content) {
        mCongratulations.setText(title);
        mCongratulations.setTextColor(getResources().getColor(R.color.white));
        mCongratulations.setTextSize(20);
        mRedSum.setText(content);
        mRedSum.setTextColor(getResources().getColor(R.color.white));
        mRedSum.setTextSize(12);
        mCongratulations.setVisibility(View.VISIBLE);
        mRedSum.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        StyledDialog.dismiss();
    }
}
