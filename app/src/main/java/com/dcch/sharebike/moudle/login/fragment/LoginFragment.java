package com.dcch.sharebike.moudle.login.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dcch.sharebike.R;
import com.dcch.sharebike.app.App;
import com.dcch.sharebike.http.Api;
import com.dcch.sharebike.moudle.home.content.MyContent;
import com.dcch.sharebike.moudle.login.activity.LoginActivity;
import com.dcch.sharebike.moudle.user.activity.CouponListActivity;
import com.dcch.sharebike.moudle.user.activity.CreditIntegralActivity;
import com.dcch.sharebike.moudle.user.activity.InviteFriendsActivity;
import com.dcch.sharebike.moudle.user.activity.MyJourneyActivity;
import com.dcch.sharebike.moudle.user.activity.MyMessageActivity;
import com.dcch.sharebike.moudle.user.activity.PersonInfoActivity;
import com.dcch.sharebike.moudle.user.activity.SettingActivity;
import com.dcch.sharebike.moudle.user.activity.UserGuideActivity;
import com.dcch.sharebike.moudle.user.activity.WalletInfoActivity;
import com.dcch.sharebike.moudle.user.bean.UserInfo;
import com.dcch.sharebike.utils.AES;
import com.dcch.sharebike.utils.ClickUtils;
import com.dcch.sharebike.utils.LogUtils;
import com.dcch.sharebike.utils.MapUtil;
import com.dcch.sharebike.utils.NetUtils;
import com.dcch.sharebike.utils.SPUtils;
import com.dcch.sharebike.utils.ToastUtils;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
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
    @BindView(R.id.achievement_show)
    LinearLayout mAchievementShow;
    @BindView(R.id.no_network_tip)
    LinearLayout mNoNetworkTip;
    private UserInfo mInfo;
    private String uID;
    private String mPhone;
    private String mToken;
    private String mNickName;
    private String mUserimage;
    private String mImageURL;
    private String mURL;

    public LoginFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uID = String.valueOf(SPUtils.get(App.getContext(), "userId", 0));
        mToken = (String) SPUtils.get(App.getContext(), "token", "");
        mPhone = AES.decrypt((String) SPUtils.get(App.getContext(), "phone", ""), MyContent.key);
//        byte[] decryptFrom = AESUtil.parseHexStr2Byte((String) SPUtils.get(App.getContext(), "phone", ""));
//        byte[] decryptResult = AESUtil.decrypt(decryptFrom, MyContent.key);
//        mPhone = new String(decryptResult);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mURL = (String) SPUtils.get(App.getContext(), "imageURL", "");
        if (!mURL.equals("")) {
            LogUtils.d("状态", mURL);
            if (getActivity() != null) {
                Glide.with(getActivity())
                        .load(Uri.fromFile(new File(mURL)))
                        .error(R.mipmap.avatar_default_login)
                        .thumbnail(0.1f)// 加载缩略图
                        .into(userIcon);
            }

        }
        if (uID != null && mToken != null) {
            if (NetUtils.isConnected(App.getContext())) {
                getUserInfo(uID, mToken);
            } else {
                ToastUtils.showShort(getActivity(), getString(R.string.no_network_tip));
            }
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
                ToastUtils.showShort(getActivity(), getString(R.string.server_tip));
            }

            @Override
            public void onResponse(String response, int id) {
                Log.d("用户的信息", response);
                try {
                    JSONObject userObject = new JSONObject(response);
                    String resultStatus = userObject.optString("resultStatus");
                    switch (resultStatus) {
                        case "0":
                            ToastUtils.showShort(getActivity(), getString(R.string.server_tip));
                            break;
                        case "1":
                            Gson gson = new Gson();
                            mInfo = gson.fromJson(response, UserInfo.class);
                            //手机号中间四位数字用*号代替的做法
                            if (mInfo.getNickName() != null && !mInfo.getNickName().equals("")) {
                                String nn = mInfo.getNickName().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
                                nickName.setText(nn);
                            }
                            if (mInfo.getIntegral() != 0) {
                                creditScore.setText("信用积分 " + String.valueOf(mInfo.getIntegral()));
                            }
                            remainSum.setText(String.valueOf(mInfo.getAggregateAmount()));
                            //骑行距离
                            person_distance.setText(String.valueOf(mInfo.getMileage()));
                            //运动成就
                            sportsAchievement.setText(String.valueOf(MapUtil.changeOneDouble(mInfo.getCalorie())));
                            //用户头像
                            mUserimage = mInfo.getUserimage();
                            if (mURL.equals("")) {
                                if (mUserimage != null) {
                                    //使用用户自定义的头像
                                    LogUtils.d("状态", mUserimage);
                                    if (getActivity() != null) {
                                        Glide.with(LoginFragment.this).load(mUserimage)
                                                .error(R.mipmap.avatar_default_login)
                                                .thumbnail(0.1f)// 加载缩略图
                                                .into(userIcon);
                                    }
                                } else {
                                    if (getActivity() != null && isAdded()) {
                                        userIcon.setImageResource(R.mipmap.avatar_default_login);
                                    }
                                }
                            }
                            break;
                        case "2":
                            goToLogin();
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void goToLogin() {
        ToastUtils.showShort(getActivity(), getString(R.string.logged_in_other_devices));
        startActivity(new Intent(getActivity(), LoginActivity.class));
        SPUtils.put(App.getContext(), "islogin", false);
        SPUtils.put(App.getContext(), "cashStatus", 0);
        SPUtils.put(App.getContext(), "status", 0);
        getActivity().finish();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick({R.id.no_network_tip, R.id.creditScore, R.id.userIcon, R.id.wallet, R.id.favorable, R.id.journey, R.id.message, R.id.friend, R.id.guide, R.id.setting})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.creditScore:
                if (ClickUtils.isFastClick()) {
                    return;
                }
                String mCreditScore = creditScore.getText().toString().trim();
                mCreditScore = mCreditScore.substring(5, mCreditScore.length());
                if (mCreditScore != null) {
                    goCreditIntegral(mCreditScore);
                }
                break;
            case R.id.userIcon:
                if (ClickUtils.isFastClick()) {
                    return;
                }
                if (NetUtils.isConnected(App.getContext())) {
                    if (mInfo != null) {
                        goPersonInfo(mInfo);
                    }
                } else {
                    ToastUtils.showShort(getActivity(), getString(R.string.no_network_tip));
                }
                break;
            case R.id.wallet:
                if (ClickUtils.isFastClick()) {
                    return;
                }
                LogUtils.d("点击", "钱包");
                if (NetUtils.isConnected(App.getContext())) {
//                    if (mInfo != null) {mInfo
                    goWalletInfo();
//                    }
                } else {
                    ToastUtils.showShort(getActivity(), getString(R.string.no_network_tip));
                }
                break;
            case R.id.favorable:
                if (ClickUtils.isFastClick()) {
                    return;
                }
                if (uID != null && mToken != null) {
                    goCouponList(uID, mToken);
                }
                break;
            case R.id.journey:
                if (ClickUtils.isFastClick()) {
                    return;
                }
                if (mPhone != null && mToken != null) {
                    goMyJourney(mPhone, mToken);
                }
                break;
            case R.id.message:
                if (ClickUtils.isFastClick()) {
                    return;
                }
                LogUtils.d("点击", "消息");
                if (uID != null && mToken != null) {
                    goMyMessage(uID, mToken);
                }
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
                startActivity(new Intent(getActivity(), UserGuideActivity.class));
                break;
            case R.id.setting:
                if (ClickUtils.isFastClick()) {
                    return;
                }
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;

            case R.id.no_network_tip:
                if (ClickUtils.isFastClick()) {
                    return;
                }
                if (NetUtils.isConnected(getContext())) {
                    onResume();
                    if (mNoNetworkTip != null) {
                        mNoNetworkTip.setVisibility(View.GONE);
                    }
                } else {
                    ToastUtils.showShort(getActivity(), getString(R.string.no_network_tip));
                }
                break;
        }
    }

    private void goMyMessage(String uID, String token) {
        Intent myMessage = new Intent(getActivity(), MyMessageActivity.class);
        myMessage.putExtra("userId", uID);
        myMessage.putExtra("token", token);
        startActivity(myMessage);
    }

    private void goMyJourney(String phone, String token) {
        Intent myJourney = new Intent(getActivity(), MyJourneyActivity.class);
        myJourney.putExtra("phone", phone);
        myJourney.putExtra("token", token);
        startActivity(myJourney);
    }

    private void goCouponList(String uID, String token) {
        Intent coupon = new Intent(getActivity(), CouponListActivity.class);
        coupon.putExtra("userId", uID);
        coupon.putExtra("token", token);
        startActivity(coupon);
    }

    //UserInfo info
    private void goWalletInfo() {
        Intent walletInfo = new Intent(getActivity(), WalletInfoActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("bundle", info);
//        walletInfo.putExtras(bundle);
        startActivity(walletInfo);
    }

    private void goPersonInfo(UserInfo info) {
        Intent personInfo = new Intent(getActivity(), PersonInfoActivity.class);
        Bundle mUserBundle = new Bundle();
        mUserBundle.putSerializable("userBundle", info);
        personInfo.putExtras(mUserBundle);
        if (mImageURL != null) {
            personInfo.putExtra("imageURL", mImageURL);
        }
        startActivity(personInfo);
    }

    private void goCreditIntegral(String mCreditScore) {
        Intent credit = new Intent(getActivity(), CreditIntegralActivity.class);
        credit.putExtra("score", mCreditScore);
        startActivity(credit);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("用户头像路径1111111", mURL + "onStart");
        if (SPUtils.isLogin()) {
            if (NetUtils.isConnected(App.getContext())) {
                if (mNickName != null && !mNickName.equals("")) {
                    nickName.setText(mNickName);
                }
                mAchievementShow.setVisibility(View.VISIBLE);
                mNoNetworkTip.setVisibility(View.GONE);
            } else {
                mAchievementShow.setVisibility(View.GONE);
                if (mNoNetworkTip != null) {
                    mNoNetworkTip.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
