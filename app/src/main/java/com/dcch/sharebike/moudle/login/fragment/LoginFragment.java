package com.dcch.sharebike.moudle.login.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dcch.sharebike.R;
import com.dcch.sharebike.app.App;
import com.dcch.sharebike.moudle.user.activity.CreditIntegralActivity;
import com.dcch.sharebike.moudle.user.activity.PersonInfoActivity;
import com.dcch.sharebike.moudle.user.activity.SettingActivity;
import com.dcch.sharebike.moudle.user.activity.WalletInfoActivity;
import com.dcch.sharebike.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;


public class LoginFragment extends Fragment {


    @BindView(R.id.nickName)
    TextView nickName;
    @BindView(R.id.creditScore)
    TextView creditScore;
    @BindView(R.id.userIcon)
    CircleImageView userIcon;
    @BindView(R.id.distance)
    TextView distance;
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

    public LoginFragment() {

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
                ToastUtils.showLong(getContext(),"信用积分");
                Intent credit = new Intent(App.getContext(),CreditIntegralActivity.class);
                startActivity(credit);
                break;
            case R.id.userIcon:
                ToastUtils.showLong(getContext(),"用户头像");
                Intent personInfo = new Intent(App.getContext(),PersonInfoActivity.class);
                startActivity(personInfo);
                break;
            case R.id.wallet:
                ToastUtils.showLong(getContext(),"钱包");
                Intent walletInfo = new Intent(App.getContext(),WalletInfoActivity.class);
                startActivity(walletInfo);
                break;
            case R.id.favorable:
                ToastUtils.showLong(getContext(),"优惠");
                break;
            case R.id.journey:
                ToastUtils.showLong(getContext(),"行程");
                break;
            case R.id.message:
                ToastUtils.showLong(getContext(),"消息");
                break;
            case R.id.friend:
                ToastUtils.showLong(getContext(),"邀请好友");
                break;
            case R.id.guide:
                ToastUtils.showLong(getContext(),"用户指南");
                break;
            case R.id.setting:
                ToastUtils.showLong(getContext(),"设置");
                Intent setting = new Intent(App.getContext(),SettingActivity.class);
                startActivity(setting);
                break;
        }
    }
}
