package com.dcch.sharebike.moudle.user.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dcch.sharebike.R;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class PersonInfoActivity extends BaseActivity {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.userInfoIcon)
    CircleImageView userInfoIcon;
    @BindView(R.id.userIcon)
    RelativeLayout userIcon;
    @BindView(R.id.nickName)
    TextView nickName;
    @BindView(R.id.userNickname)
    RelativeLayout userNickname;
    @BindView(R.id.realName)
    TextView realName;
    @BindView(R.id.authority)
    TextView authority;
    @BindView(R.id.telephone)
    TextView telephone;
    @BindView(R.id.phone)
    RelativeLayout phone;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_person_info;
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.back, R.id.userIcon, R.id.userNickname, R.id.phone})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.userIcon:
                ToastUtils.showLong(this,"选择头像");
                break;
            case R.id.userNickname:
                ToastUtils.showLong(this,"昵称");
                break;
            case R.id.phone:
                ToastUtils.showLong(this,"手机号");
                break;
        }
    }
}
