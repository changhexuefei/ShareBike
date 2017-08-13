package com.dcch.sharebike.moudle.user.activity;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dcch.sharebike.R;
import com.dcch.sharebike.app.App;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.base.MessageEvent;
import com.dcch.sharebike.http.Api;
import com.dcch.sharebike.utils.ClickUtils;
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
    RelativeLayout mFlContainer;
    @BindView(R.id.merchant_icon)
    ImageView mMerchantIcon;
    @BindView(R.id.merchant_info)
    TextView mMerchantInfo;
    @BindView(R.id.red_sum_area)
    RelativeLayout mRedSumArea;
    @BindView(R.id.merchant_info_image)
    ImageView mMerchantInfoImage;
    @BindView(R.id.showInfoArea)
    RelativeLayout mShowInfoArea;
    private String mUserId;
    private String mAmount;
    private boolean isClick = true;
    private AnimatorSet mFrontAnimator;
    private AnimatorSet mBackAnimator;
    private boolean isShowFront = true;
    private String mMMerchaninfo;
    private String mMMerchantimageurl;
    private String mImei;
    private String merchantid;
    private String mMerchantInfoImageURl;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_open_red_envelope;
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            mMMerchaninfo = intent.getStringExtra("mMerchantinfo");
            mMMerchantimageurl = intent.getStringExtra("mMerchantimageurl");
            mImei = intent.getStringExtra("IMEI");
            merchantid = intent.getStringExtra("merchantid");
            if (mMMerchaninfo != null && !mMMerchaninfo.equals("")) {
                mMerchantInfo.setText(mMMerchaninfo + "给您发来一份惊喜红包！");
            }
            if (!mMMerchantimageurl.equals("") && mMMerchantimageurl != null) {
                Glide.with(App.getContext()).load(mMMerchantimageurl).into(mMerchantIcon);
            }
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
                if (isClick) {
                    StyledDialog.buildLoading(OpenRedEnvelopeActivity.this, "领取中...", true, false).show();
                    EventBus.getDefault().post(new MessageEvent(), "unable_click");
                    startAnimation(mFlContainer);
                    mMerchantIcon.setVisibility(View.GONE);
                    mMerchantInfo.setVisibility(View.GONE);
                }
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
                if (mUserId != null && !mUserId.equals("") && mImei != null && !mImei.equals("")
                        && !merchantid.equals("") && merchantid != null) {
                    getRedPacket(mUserId, mImei, merchantid);
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


    private void getRedPacket(String userId, String imei, final String merchantid) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", userId);
        map.put("IMEI", imei);
        map.put("merchantid", merchantid);
        LogUtils.d("用户", userId + "\n" + imei + "\n" + merchantid);
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
                EventBus.getDefault().post(new MessageEvent(), "unable_click");

                try {
                    JSONObject object = new JSONObject(response);
                    String resultStatus = object.optString("resultStatus");
                    LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT
                    );

                    switch (resultStatus) {
                        case "0":

                            mCongratulations.setText("很遗憾，红包抢完了");
                            mCongratulations.setLayoutParams(params);
                            break;
                        case "1":
                            mAmount = object.optString("amount");
                            mMerchantInfoImageURl = object.optString("merchantinfoimageurl");
                            mCongratulations.setText("恭喜您");
                            if (mAmount != null && mMerchantInfoImageURl != null) {
                                getRedPacketResult(mAmount, mMerchantInfoImageURl);
                            }
                            break;
                        case "2":
                        
                            mCongratulations.setLayoutParams(params);
                            mCongratulations.setText("领过红包，下次再来");
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getRedPacketResult(String amount, String mMerchantInfoImageURl) {
        mShowInfoArea.setVisibility(View.VISIBLE);
        mMerchantInfoImage.setVisibility(View.VISIBLE);
        mRedSum.setText(amount);
        Glide.with(App.getContext()).load(mMerchantInfoImageURl).into(mMerchantInfoImage);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        StyledDialog.dismiss();
    }

}
