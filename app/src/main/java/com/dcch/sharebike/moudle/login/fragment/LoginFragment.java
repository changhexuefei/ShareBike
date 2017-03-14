package com.dcch.sharebike.moudle.login.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dcch.sharebike.R;
import com.dcch.sharebike.app.App;
import com.dcch.sharebike.moudle.user.activity.CreditIntegralActivity;
import com.dcch.sharebike.moudle.user.activity.InviteFriendsActivity;
import com.dcch.sharebike.moudle.user.activity.MyJourneyActivity;
import com.dcch.sharebike.moudle.user.activity.MyMessageActivity;
import com.dcch.sharebike.moudle.user.activity.PersonInfoActivity;
import com.dcch.sharebike.moudle.user.activity.SettingActivity;
import com.dcch.sharebike.moudle.user.activity.UserGuideActivity;
import com.dcch.sharebike.moudle.user.activity.WalletInfoActivity;
import com.dcch.sharebike.moudle.user.bean.UserInfo;
import com.dcch.sharebike.utils.SPUtils;
import com.dcch.sharebike.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

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
    public LoginFragment() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);
        if (SPUtils.isLogin()) {
            //从服务端拿到客户信息
            String userDetail = (String) SPUtils.get(App.getContext(), "userDetail", "");
            Log.d("ooooo", userDetail);
            if (userDetail != null) {
                try {
                    JSONObject object = new JSONObject(userDetail);
                    mInfo = new UserInfo();
                    mInfo.setNickName(object.getString("nickName"));
                    mInfo.setName(object.getString("name"));
                    mInfo.setPhone(object.getString("phone"));
                    mInfo.setStatus(object.getInt("status"));
                    mInfo.setPledgeCash(object.getInt("pledgeCash"));
                    mInfo.setCashStatus(object.getInt("cashStatus"));
                    //用户头像应该为string类型的图像路径

//                        mInfo.setUserimage(object.getString("userimage"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //手机号中间四位数字用*号代替的做法
                if (mInfo!=null) {
                    String nn = mInfo.getNickName().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
                    nickName.setText(nn);
                    remainSum.setText(String.valueOf(mInfo.getPledgeCash()));
                    //骑行距离
//                    person_distance.setText();
                    //节约碳排放
//                    discharge.setText();
                    //运动成就
//                    sportsAchievement.setText();
                    //用户头像
                    if(mInfo.getUserimage()==null){
                        userIcon.setImageResource(R.mipmap.avatar_default_login);
                    }else {
                        //使用用户自定义的头像
                    }
                }
            }
        }
        return view;
    }

    @OnClick({R.id.creditScore, R.id.userIcon, R.id.wallet, R.id.favorable, R.id.journey, R.id.message, R.id.friend, R.id.guide, R.id.setting})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.creditScore:
                ToastUtils.showLong(getContext(), "信用积分");
                Intent credit = new Intent(App.getContext(), CreditIntegralActivity.class);
                startActivity(credit);
                break;
            case R.id.userIcon:
                ToastUtils.showLong(getContext(), "用户头像");
                Intent personInfo = new Intent(App.getContext(), PersonInfoActivity.class);
                Bundle userBundle = new Bundle();
                userBundle.putSerializable("userBundle",mInfo);
                personInfo.putExtras(userBundle);
                startActivity(personInfo);
                break;
            case R.id.wallet:
                ToastUtils.showLong(getContext(), "钱包");
                String remainsum = remainSum.getText().toString().trim();
                Intent walletInfo = new Intent(App.getContext(), WalletInfoActivity.class);
                walletInfo.putExtra("remainSum", remainsum);
                startActivity(walletInfo);
                break;
            case R.id.favorable:
                ToastUtils.showLong(getContext(), "优惠");
                break;
            case R.id.journey:
                ToastUtils.showLong(getContext(), "行程");
                Intent myJourney = new Intent(App.getContext(), MyJourneyActivity.class);
                startActivity(myJourney);
                break;
            case R.id.message:
                ToastUtils.showLong(getContext(), "消息");
                Intent myMessage = new Intent(App.getContext(), MyMessageActivity.class);
                startActivity(myMessage);
                break;
            case R.id.friend:
                ToastUtils.showLong(getContext(), "邀请好友");
                startActivity(new Intent(getActivity(),InviteFriendsActivity.class));

                break;
            case R.id.guide:
                ToastUtils.showLong(getContext(), "用户指南");
                Intent userGuide = new Intent(App.getContext(), UserGuideActivity.class);
                startActivity(userGuide);
                break;
            case R.id.setting:
                ToastUtils.showLong(getContext(), "设置");
                Intent setting = new Intent(App.getContext(), SettingActivity.class);
                startActivity(setting);
//                getActivity().finish();
                break;
        }
    }

}
