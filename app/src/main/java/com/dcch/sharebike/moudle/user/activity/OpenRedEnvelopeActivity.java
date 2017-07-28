package com.dcch.sharebike.moudle.user.activity;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dcch.sharebike.R;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.http.Api;
import com.dcch.sharebike.utils.JsonUtils;
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
    private String mUserId;
    private String mAmount;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_open_red_envelope;
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        if (intent != null && !intent.equals("")) {
            mUserId = intent.getStringExtra("userId");
        }
    }


    @OnClick(R.id.imageView)
    public void onViewClicked() {
        Animator animator = AnimatorInflater.loadAnimator(this, R.animator.red_animator);
        animator.setTarget(mImageView);
        animator.start();
        if (mUserId != null && !mUserId.equals("")) {
            getRedPacket(mUserId);
        }
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mImageView.setImageResource(R.drawable.red_y);
                mImageView.setEnabled(false);
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

    private void getRedPacket(String userId) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", userId);
        OkHttpUtils.post().url(Api.BASE_URL + Api.MERCHANTGIFT).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                if(JsonUtils.isSuccess(response)){
                    try {
                        JSONObject object = new JSONObject(response);
                        mAmount = object.optString("amount");
                        String merchantinfo = object.optString("merchantinfo");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else {


                }
            }
        });
    }

}
