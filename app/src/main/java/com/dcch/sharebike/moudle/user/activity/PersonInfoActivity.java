package com.dcch.sharebike.moudle.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dcch.sharebike.R;
import com.dcch.sharebike.app.App;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.moudle.user.bean.UserInfo;
import com.dcch.sharebike.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;
import cn.finalteam.rxgalleryfinal.RxGalleryFinal;
import cn.finalteam.rxgalleryfinal.imageloader.ImageLoaderType;
import cn.finalteam.rxgalleryfinal.rxbus.RxBusResultSubscriber;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageRadioResultEvent;
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
    private UserInfo mUserBundle;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_person_info;
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        Bundle user = intent.getExtras();
        Log.d("user", user + "");
        if (user != null) {
            mUserBundle = (UserInfo) user.getSerializable("userBundle");
            Log.d("用户", mUserBundle + "");
            nickName.setText(mUserBundle.getNickName().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
            telephone.setText(mUserBundle.getPhone().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));

            if (mUserBundle.getStatus() == 0) {
                authority.setText("未认证");
                realName.setText("未认证");
            }
            if (mUserBundle.getStatus() == 1) {
                authority.setText("已认证");
                realName.setText(mUserBundle.getName());
            }
            if (mUserBundle.getUserimage() == null) {
                userInfoIcon.setImageResource(R.mipmap.avatar_default_login);
            }
        }

    }

    @OnClick({R.id.back, R.id.userIcon, R.id.userNickname, R.id.phone})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.userIcon:
                RxGalleryFinal.with(PersonInfoActivity.this)
                        .cropHideBottomControls(true)
                        .cropOvalDimmedLayer(false)
                        .cropAllowedGestures(-1,-1,-1)
                        .image()
                        .radio()
                        .crop()
                        .cropFreeStyleCropEnabled(false)
                        .imageLoader(ImageLoaderType.GLIDE)
                        .subscribe(new RxBusResultSubscriber<ImageRadioResultEvent>() {
                            @Override
                            protected void onEvent(ImageRadioResultEvent imageRadioResultEvent) throws Exception {
                                //得到图片的路径
                                String result = imageRadioResultEvent.getResult().getOriginalPath();
                                Log.d("图片地址",result);

                                if (result != null && !result.equals("")) {
                                    //将图片赋值给图片控件
                                    Glide.with(App.getContext()).load(result).into(userInfoIcon);
                                    //下一步将选择的图片上传到服务器
                                }
                            }
                        }).openGallery();
                break;
            case R.id.userNickname:
                ToastUtils.showLong(this, "昵称");
                Intent changeNickName = new Intent(this, ChangeUserNickNameActivity.class);
                String trim = nickName.getText().toString().trim();
                Log.d("trim",trim);
                if (!trim.equals("") && trim != null)
                    changeNickName.putExtra("nickname", trim);
                startActivityForResult(changeNickName, 0);
                break;
            case R.id.phone:
                ToastUtils.showLong(this, "手机号");
                Intent mobileNum = new Intent(this, MobileNumActivity.class);
                mobileNum.putExtra("cashStatus", mUserBundle.getCashStatus());
                mobileNum.putExtra("phone", mUserBundle.getPhone());
                startActivity(mobileNum);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null&&!data.equals("")){
            String newName = data.getStringExtra("newName");
            switch (requestCode) {
                case 0:
                    if(newName!=null && !newName.equals("")) {
                        nickName.setText(newName);
                    }
                    break;
            }


        }



    }
}
