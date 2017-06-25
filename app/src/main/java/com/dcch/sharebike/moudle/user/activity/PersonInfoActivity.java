package com.dcch.sharebike.moudle.user.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;
import com.dcch.sharebike.R;
import com.dcch.sharebike.app.App;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.http.Api;
import com.dcch.sharebike.moudle.home.content.MyContent;
import com.dcch.sharebike.moudle.login.activity.PersonalCenterActivity;
import com.dcch.sharebike.moudle.user.bean.UserInfo;
import com.dcch.sharebike.utils.ClickUtils;
import com.dcch.sharebike.utils.JsonUtils;
import com.dcch.sharebike.utils.LogUtils;
import com.dcch.sharebike.utils.NetUtils;
import com.dcch.sharebike.utils.PictureProcessingUtils;
import com.dcch.sharebike.utils.SPUtils;
import com.dcch.sharebike.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.finalteam.rxgalleryfinal.RxGalleryFinal;
import cn.finalteam.rxgalleryfinal.imageloader.ImageLoaderType;
import cn.finalteam.rxgalleryfinal.rxbus.RxBusResultSubscriber;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageRadioResultEvent;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;

public class PersonInfoActivity extends BaseActivity {

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
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private UserInfo mUserBundle;
    private String uID;
    private String result;
    private String mToken;
    private String mImageURL;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_person_info;
    }

    @Override
    protected void initData() {
        mToolbar.setTitle("");
        mTitle.setText(getResources().getString(R.string.person_info));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PersonInfoActivity.this, PersonalCenterActivity.class));
                finish();
            }
        });
        Intent intent = getIntent();
//        if (intent != null) {
//            mImageURL = intent.getStringExtra("imageURL");
//        }
        Bundle user = intent.getExtras();
        if (user != null) {
            mUserBundle = (UserInfo) user.getSerializable("userBundle");
            if (mUserBundle.getPhone() != null && !mUserBundle.getPhone().equals("")
                    && !mUserBundle.getPhone().equals("") && mUserBundle.getPhone() != null) {
                nickName.setText(mUserBundle.getNickName().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
                telephone.setText(mUserBundle.getPhone().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
            }
            if (mUserBundle.getStatus() == 0) {
                authority.setText(R.string.unverified);
                realName.setText(R.string.unverified);
            } else if (mUserBundle.getStatus() == 1) {
                authority.setText(R.string.verified);
                realName.setText(mUserBundle.getName());
            }
            if (mUserBundle.getUserimage() != null) {
                LogUtils.d("图片", mUserBundle.getUserimage());
                if (Util.isOnMainThread()) {
                    Glide.with(this).load(mUserBundle.getUserimage()).error(R.mipmap.avatar_default_login).thumbnail(0.1f).into(userInfoIcon);
                }
            } else {
                userInfoIcon.setImageResource(R.mipmap.avatar_default_login);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SPUtils.isLogin()) {
            try {
                String userDetail = (String) SPUtils.get(App.getContext(), "userDetail", "");
                JSONObject object = new JSONObject(userDetail);
//                if (mImageURL != null && !mImageURL.equals("")) {
//                    Glide.with(PersonInfoActivity.this)
//                            .load(Uri.fromFile(new File(mImageURL)))
//                            .error(R.mipmap.avatar_default_login)
//                            .thumbnail(0.1f)// 加载缩略图
//                            .into(userInfoIcon);
//                } else {
//                    userInfoIcon.setImageResource(R.mipmap.avatar_default_login);
//                }
                int id = object.optInt("id");
                uID = String.valueOf(id);
                mToken = (String)SPUtils.get(App.getContext(), "token", "");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @OnClick({R.id.userIcon, R.id.userNickname, R.id.phone})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.userIcon:
                if (ClickUtils.isFastClick()) {
                    return;
                }
                RxGalleryFinal.with(App.getContext())
                        .cropHideBottomControls(true)
                        .cropOvalDimmedLayer(false)
                        .cropAllowedGestures(-1, -1, -1)
                        .image()
                        .radio()
                        .crop()
                        .cropFreeStyleCropEnabled(false)
                        .imageLoader(ImageLoaderType.GLIDE)
                        .subscribe(new RxBusResultSubscriber<ImageRadioResultEvent>() {
                            @Override
                            protected void onEvent(ImageRadioResultEvent imageRadioResultEvent) throws Exception {
                                //得到图片的路径
                                result = imageRadioResultEvent.getResult().getOriginalPath();
                                Bitmap bitmap = PictureProcessingUtils.getimage(result);
                                String mImageResult = PictureProcessingUtils.bitmapToBase64(bitmap);
                                //将图片赋值给图片控件
                                if (result != null && !result.equals("")) {
                                    Log.d("图片", result);

                                    Glide.with(PersonInfoActivity.this)
                                            .load(Uri.fromFile(new File(result)))
                                            .error(R.mipmap.avatar_default_login)
                                            .thumbnail(0.1f)// 加载缩略图
                                            .into(userInfoIcon);
                                }

                                //下一步将选择的图片上传到服务器
                                if (NetUtils.isConnected(App.getContext())) {
                                    if (mImageResult != null && mToken != null && uID != null) {
                                        upLoadIcon(mImageResult, mToken, uID);
                                    }
                                } else {
                                    ToastUtils.showShort(PersonInfoActivity.this, getString(R.string.no_network_tip));
                                }

                            }
                        }).openGallery();
                break;
            case R.id.userNickname:
                if (ClickUtils.isFastClick()) {
                    return;
                }
                String trim = nickName.getText().toString().trim();
                Intent changeNickName = new Intent(this, ChangeUserNickNameActivity.class);
                Log.d("trim", trim);
                if (!trim.equals("") && trim != null)
                    changeNickName.putExtra("nickname", trim);
                startActivityForResult(changeNickName, 0);
                break;
            case R.id.phone:
                if (ClickUtils.isFastClick()) {
                    return;
                }
                if (mUserBundle != null) {
                    if (mUserBundle.getCashStatus() == 1 && !mUserBundle.getPhone().equals("")
                            && mUserBundle.getPhone() != null && !mToken.equals("") && mToken != null
                            && !uID.equals("") && uID != null) {
                        Intent mobileNum = new Intent(this, MobileNumActivity.class);
                        mobileNum.putExtra("cashStatus", mUserBundle.getCashStatus());
                        mobileNum.putExtra("phone", mUserBundle.getPhone());
                        mobileNum.putExtra("token", mToken);
                        mobileNum.putExtra("userId", uID);
                        startActivity(mobileNum);
                    }
                }
                break;
        }
    }

    private void upLoadIcon(String mImageResult, String token, String uID) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("userId", uID);
        map.put("imageFile", mImageResult);
        LogUtils.d("用户", uID + token + mImageResult);
        //用户上传头像的方法
        OkHttpUtils.post().url(Api.BASE_URL + Api.UPLOADAVATAR)
                .params(map)
                .addHeader("Content-Type", "multipart/form-data;boundary=" + MyContent.BOUNDARY)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                        ToastUtils.showShort(PersonInfoActivity.this, getString(R.string.server_tip));
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        //根据返回值判断上传成功或者失败
                        Log.d("上传头像", response);
                        //{"code":"1"}
                        if (JsonUtils.isSuccess(response)) {
                            SPUtils.put(App.getContext(), "imageURL", result);
                            ToastUtils.showShort(PersonInfoActivity.this, getString(R.string.changeIcon_success));
                        } else {
                            ToastUtils.showShort(PersonInfoActivity.this, getString(R.string.changeIcon_fail));
                        }
                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && !data.equals("")) {
            String newName = data.getStringExtra("newName");
            switch (requestCode) {
                case 0:
                    if (newName != null && !newName.equals("")) {
                        try {
                            nickName.setText(newName);
                            String userNickName = nickName.getText().toString().trim();
                            String encode = URLEncoder.encode(userNickName, "utf-8");//"UTF-8"
                            LogUtils.d("昵称", encode);
                            if (NetUtils.isConnected(App.getContext())) {
                                if (mToken != null && encode != null && uID != null) {
                                    updateUserNickName(mToken, encode, uID);
                                }
                            } else {
                                ToastUtils.showShort(PersonInfoActivity.this, getString(R.string.no_network_tip));

                            }
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
    }

    private void updateUserNickName(String token, String encode, String uID) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("userId", uID);
        map.put("nickName", encode);
        OkHttpUtils.post()
                .url(Api.BASE_URL + Api.EDITUSER)
                .params(map)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showShort(PersonInfoActivity.this, getString(R.string.server_tip));
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        //根据返回值成功和失败的判断
                        if (JsonUtils.isSuccess(response)) {
                            ToastUtils.showShort(PersonInfoActivity.this, getString(R.string.updateNickName_success));
                        } else {
                            ToastUtils.showShort(PersonInfoActivity.this, getString(R.string.updateNickName_fail));
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        Glide.with(this).pauseRequests();
    }
}
