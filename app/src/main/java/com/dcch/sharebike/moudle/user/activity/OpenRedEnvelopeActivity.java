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
    ConstraintLayout mFlContainer;
    @BindView(R.id.merchant_icon)
    ImageView mMerchantIcon;
    @BindView(R.id.merchant_info)
    TextView mMerchantInfo;
    @BindView(R.id.merchant_name)
    TextView mMerchantName;
    @BindView(R.id.telephoneNumber)
    TextView mTelephoneNumber;
    @BindView(R.id.telephoneNumberTwo)
    TextView mTelephoneNumberTwo;
    @BindView(R.id.addressInfo)
    TextView mAddressInfo;
    @BindView(R.id.red_sum_area)
    RelativeLayout mRedSumArea;
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
    private String mImei;
    private String mTelephone;
    private String mAddress;
    private String mMerchatphone;
    private String merchanid;


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
            mImei = intent.getStringExtra("IMEI");
            merchanid = intent.getStringExtra("merchanid");
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
                EventBus.getDefault().post(new MessageEvent(), "unable_click");
                if (isClick) {
                    StyledDialog.buildLoading(OpenRedEnvelopeActivity.this, "", true, false).show();
                    if (mUserId != null && !mUserId.equals("") && mImei != null && !mImei.equals("")
                            && !merchanid.equals("") && merchanid != null) {
                        getRedPacket(mUserId, mImei, merchanid);
                    }
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
                mRedSumArea.setVisibility(View.VISIBLE);

                getRedPacketResult(mAddress, mAmount, mMMerchaninfo, mMerchatphone, mTelephone);

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


    private void getRedPacket(String userId, String imei, String merchanid) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", userId);
        map.put("IMEI", imei);
        map.put("merchanid", merchanid);
        LogUtils.d("用户", userId + "\n" + imei + "\n" + merchanid);
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
                try {
                    JSONObject object = new JSONObject(response);
                    String resultStatus = object.optString("resultStatus");
                    switch (resultStatus) {
                        case "0":
                            ToastUtils.showShort(OpenRedEnvelopeActivity.this, "服务器正忙，请稍后再试！");
                            break;
                        case "1":
                            mAmount = object.optString("amount");
                            mTelephone = object.optString("telephone");
                            mAddress = object.optString("address");
                            mMerchatphone = object.optString("merchatphone");
                            break;
                        case "2":

                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getRedPacketResult(String address, String amount, String mMMerchaninfo, String mMerchatphone, String mTelephone) {
        if (address != null && !address.equals("")) {
            mAddressInfo.setText(address);
        }
        if (mTelephone != null && !mTelephone.equals("")) {
            mTelephoneNumber.setText(mTelephone);
        }
        if (mMerchatphone != null && !mMerchatphone.equals("")) {
            mTelephoneNumberTwo.setText(mMerchatphone);
        } else {
            mTelephoneNumberTwo.setVisibility(View.GONE);
        }
        if (!mMMerchaninfo.equals("") && mMMerchaninfo != null) {
            mMerchantName.setText(mMMerchaninfo);
        }
        mRedSum.setText(amount);
        mCongratulations.setVisibility(View.VISIBLE);
        mRedSum.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        StyledDialog.dismiss();
    }

}
