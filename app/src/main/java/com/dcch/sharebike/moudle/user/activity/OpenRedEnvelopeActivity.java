package com.dcch.sharebike.moudle.user.activity;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dcch.sharebike.R;
import com.dcch.sharebike.app.App;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.http.Api;
import com.dcch.sharebike.utils.ClickUtils;
import com.dcch.sharebike.utils.JsonUtils;
import com.dcch.sharebike.utils.LogUtils;
import com.dcch.sharebike.utils.NetUtils;
import com.dcch.sharebike.utils.SPUtils;
import com.dcch.sharebike.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;


public class OpenRedEnvelopeActivity extends BaseActivity {

    @BindView(R.id.imageView)
    ImageView mImageView;
    @BindView(R.id.Congratulations)
    TextView mCongratulations;
    @BindView(R.id.red_sum)
    TextView mRedSum;
    @BindView(R.id.close_redPacket)
    ImageView mCloseRedPacket;
    private String mUserId;
    private String mAmount;
    private String mTitle;
    private String mContent;
    private boolean isClick = true;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_open_red_envelope;
    }

    @Override
    protected void initData() {
//        Intent intent = getIntent();
//        if (intent != null && !intent.equals("")) {
//            mUserId = intent.getStringExtra("userId");
//        }
        mUserId = String.valueOf(SPUtils.get(App.getContext(), "userId", 0));
    }


    @OnClick({R.id.imageView, R.id.close_redPacket})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageView:
                if (ClickUtils.isFastClick()) {
                    return;
                }
                LogUtils.d("用户","点击了我");
                if (mUserId != null && !mUserId.equals("")) {
                    if (NetUtils.isConnected(App.getContext())) {
                        if (isClick) {
                            isClick = false;
                            getRedPacket(mUserId);
                        }
                    } else {
                        ToastUtils.showShort(OpenRedEnvelopeActivity.this, getResources().getString(R.string.no_network_tip));
                    }
                }
                break;
            case R.id.close_redPacket:
                if (ClickUtils.isFastClick()) {
                    return;
                }
                finish();
                break;
        }

    }

    private void getRedPacket(String userId) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", "214");
        OkHttpUtils.post().url(Api.BASE_URL + Api.MERCHANTGIFT).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.showShort(OpenRedEnvelopeActivity.this, getString(R.string.server_tip));
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d("用户", response);
                Animator animator = AnimatorInflater.loadAnimator(OpenRedEnvelopeActivity.this, R.animator.red_animator);
                animator.setTarget(mImageView);
                animator.start();
                if (JsonUtils.isSuccess(response)) {
                    try {
                        JSONObject object = new JSONObject(response);
                        mAmount = object.optString("amount");
                        String merchantinfo = object.optString("merchantinfo");
                        mTitle = "恭喜您";
                        mContent = "获得爱尚KTV发放的" + mAmount + "元红包";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    mTitle = "很遗憾！";
                    mContent = "没有红包，下次骑行再来试试手气吧！";
                }
                animator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mImageView.setImageResource(R.drawable.red_y);
                        mImageView.setEnabled(false);
                        mCongratulations.setText(mTitle);
                        mCongratulations.setTextColor(getResources().getColor(R.color.white));
                        mCongratulations.setTextSize(20);
                        mRedSum.setText(mContent);
                        mRedSum.setTextColor(getResources().getColor(R.color.white));
                        mRedSum.setTextSize(12);
                        mCongratulations.setVisibility(View.VISIBLE);
                        mRedSum.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
            }
        });
    }

}
