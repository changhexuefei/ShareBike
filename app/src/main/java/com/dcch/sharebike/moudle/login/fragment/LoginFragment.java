package com.dcch.sharebike.moudle.login.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dcch.sharebike.R;
import com.dcch.sharebike.app.App;
import com.dcch.sharebike.http.Api;
import com.dcch.sharebike.moudle.login.activity.LoginActivity;
import com.dcch.sharebike.moudle.user.activity.CouponListActivity;
import com.dcch.sharebike.moudle.user.activity.CreditIntegralActivity;
import com.dcch.sharebike.moudle.user.activity.InviteFriendsActivity;
import com.dcch.sharebike.moudle.user.activity.MyJourneyActivity;
import com.dcch.sharebike.moudle.user.activity.PersonInfoActivity;
import com.dcch.sharebike.moudle.user.activity.SettingActivity;
import com.dcch.sharebike.moudle.user.activity.UserGuideActivity;
import com.dcch.sharebike.moudle.user.activity.WalletInfoActivity;
import com.dcch.sharebike.moudle.user.bean.UserInfo;
import com.dcch.sharebike.utils.ClickUtils;
import com.dcch.sharebike.utils.JsonUtils;
import com.dcch.sharebike.utils.LogUtils;
import com.dcch.sharebike.utils.MapUtil;
import com.dcch.sharebike.utils.SPUtils;
import com.dcch.sharebike.utils.ToastUtils;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;


public class LoginFragment extends Fragment {

    @BindView(R.id.nickName)
    TextView nickName;
    @BindView(R.id.creditScore)
    TextView creditScore;
    @BindView(R.id.userIcon)
    CircleImageView userIcon;
    @BindView(R.id.person_distance)
    TextView person_distance;
    @BindView(R.id.discharge)
    TextView discharge;
    @BindView(R.id.sportsAchievement)
    TextView sportsAchievement;
    @BindView(R.id.wallet)
    RelativeLayout wallet;
    @BindView(R.id.favorable)
    RelativeLayout favorable;
    @BindView(R.id.journey)
    RelativeLayout journey;
    @BindView(R.id.message)
    RelativeLayout message;
    @BindView(R.id.friend)
    RelativeLayout friend;
    @BindView(R.id.guide)
    RelativeLayout guide;
    @BindView(R.id.setting)
    RelativeLayout setting;
    @BindView(R.id.remainSum)
    TextView remainSum;
    private UserInfo mInfo;
    private String uID;
    private String mPhone;
    private String mToken;


    public LoginFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SPUtils.isLogin()) {
            String userDetail = (String) SPUtils.get(App.getContext(), "userDetail", "");
            if (userDetail != null) {
                try {
                    JSONObject object = new JSONObject(userDetail);
                    int userId = object.optInt("id");
                    mToken = object.optString("token");
                    uID = String.valueOf(userId);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (uID != null && mToken != null) {
            getUserInfo(uID, mToken);
        }
    }

    //从服务端拿到客户信息
    private void getUserInfo(String uID, String mToken) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", uID);
        map.put("token", mToken);
        LogUtils.d("状态", uID + "\n" + mToken);
        OkHttpUtils.post().url(Api.BASE_URL + Api.INFOUSER).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
//                Log.e("获取用户信息", e.getMessage());
                ToastUtils.showShort(App.getContext(), "服务器正忙");
            }

            @Override
            public void onResponse(String response, int id) {
                Log.d("用户的信息", response);
                if (JsonUtils.isSuccess(response)) {
                    Gson gson = new Gson();
                    mInfo = gson.fromJson(response, UserInfo.class);
                    //手机号中间四位数字用*号代替的做法
                    mPhone = mInfo.getPhone();
                    if (mPhone != null) {
                        String nn = mInfo.getNickName().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
                        nickName.setText(nn);
                    }
                    if (mInfo.getIntegral() != 0) {
                        creditScore.setText("信用积分 " + String.valueOf(mInfo.getIntegral()));
                    }
                    remainSum.setText(String.valueOf(mInfo.getAggregateAmount()));

//              骑行距离
                    person_distance.setText(String.valueOf(mInfo.getMileage()));

//              运动成就
                    sportsAchievement.setText(String.valueOf(MapUtil.changeOneDouble(mInfo.getCalorie())));
                    //用户头像
                    String userimage = mInfo.getUserimage();
                    if (userimage != null) {
                        Log.d("用户头像路径", userimage);
                        //使用用户自定义的头像
                        Glide.with(App.getContext()).load(userimage)
                                .error(R.mipmap.avatar_default_login)
                                .into(userIcon);
                    } else {
                        userIcon.setImageResource(R.mipmap.avatar_default_login);
                    }
                } else {
                    LogUtils.d("状态", "您被迫下线了");
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    getActivity().finish();
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick({R.id.creditScore, R.id.userIcon, R.id.wallet, R.id.favorable, R.id.journey, R.id.message, R.id.friend, R.id.guide, R.id.setting})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.creditScore:
                if (ClickUtils.isFastClick()) {
                    return;
                }
                String mCreditScore = creditScore.getText().toString().trim();
                mCreditScore = mCreditScore.substring(5, mCreditScore.length());
                LogUtils.d("积分", mCreditScore);
                if (mCreditScore != null) {
                    Intent credit = new Intent(App.getContext(), CreditIntegralActivity.class);
                    credit.putExtra("score", mCreditScore);
                    startActivity(credit);
                }
                break;
            case R.id.userIcon:
                if (ClickUtils.isFastClick()) {
                    return;
                }
                if (mInfo != null) {
                    Intent personInfo = new Intent(App.getContext(), PersonInfoActivity.class);
                    Bundle mUserBundle = new Bundle();
                    mUserBundle.putSerializable("userBundle", mInfo);
                    personInfo.putExtras(mUserBundle);
                    startActivity(personInfo);
                }
                break;
            case R.id.wallet:
                if (ClickUtils.isFastClick()) {
                    return;
                }
                if (mInfo != null) {
                    Intent walletInfo = new Intent(App.getContext(), WalletInfoActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("bundle", mInfo);
                    walletInfo.putExtras(bundle);
                    startActivity(walletInfo);
                }
                break;
            case R.id.favorable:
                if (ClickUtils.isFastClick()) {
                    return;
                }
                if (uID != null && mToken != null) {
                    Intent coupon = new Intent(App.getContext(), CouponListActivity.class);
                    coupon.putExtra("userId", uID);
                    coupon.putExtra("token", mToken);
                    startActivity(coupon);
                }
                break;
            case R.id.journey:
                if (ClickUtils.isFastClick()) {
                    return;
                }
                if (mPhone != null && mToken != null) {
                    Intent myJourney = new Intent(App.getContext(), MyJourneyActivity.class);
                    myJourney.putExtra("phone", mPhone);
                    myJourney.putExtra("token", mToken);
                    startActivity(myJourney);
                }
                break;
            case R.id.message:
                if (ClickUtils.isFastClick()) {
                    return;
                }
                ToastUtils.showLong(getContext(), "敬请期待");
//                Intent myMessage = new Intent(App.getContext(), MyMessageActivity.class);
//                startActivity(myMessage);
                break;
            case R.id.friend:
                if (ClickUtils.isFastClick()) {
                    return;
                }
                startActivity(new Intent(getActivity(), InviteFriendsActivity.class));
                break;
            case R.id.guide:
                if (ClickUtils.isFastClick()) {
                    return;
                }
                startActivity(new Intent(App.getContext(), UserGuideActivity.class));
                break;
            case R.id.setting:
                if (ClickUtils.isFastClick()) {
                    return;
                }
                startActivity(new Intent(App.getContext(), SettingActivity.class));
                break;
        }
    }

}
