package com.dcch.sharebike.moudle.user.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dcch.sharebike.R;
import com.dcch.sharebike.app.App;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.http.Api;
import com.dcch.sharebike.moudle.login.activity.PersonalCenterActivity;
import com.dcch.sharebike.moudle.user.bean.UserInfo;
import com.dcch.sharebike.utils.JsonUtils;
import com.dcch.sharebike.utils.LogUtils;
import com.dcch.sharebike.utils.SPUtils;
import com.dcch.sharebike.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
    public static final String BOUNDARY = "ZnGpDtePMx0KrHh_G0X99Yef9r8JZsRJSXC";
    private String mToken;

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
        Bundle user = intent.getExtras();
        if (user != null) {
            mUserBundle = (UserInfo) user.getSerializable("userBundle");
            assert mUserBundle != null;
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
            if (mUserBundle.getUserimage() != null) {
                Glide.with(App.getContext()).load(mUserBundle.getUserimage()).into(userInfoIcon);
            } else {
                userInfoIcon.setImageResource(R.mipmap.avatar_default_login);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SPUtils.isLogin()) {
            String userDetail = (String) SPUtils.get(App.getContext(), "userDetail", "");
            try {
                JSONObject object = new JSONObject(userDetail);
                int id = object.optInt("id");
                uID = String.valueOf(id);
                mToken = object.optString("token");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @OnClick({R.id.userIcon, R.id.userNickname, R.id.phone})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.userIcon:
                RxGalleryFinal.with(PersonInfoActivity.this)
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
                                Bitmap bitmap = getimage(result);
                                String mImageResult = bitmapToBase64(bitmap);
                                if (result != null && !result.equals("")) {
                                    //将图片赋值给图片控件
                                    Glide.with(App.getContext()).load(result).into(userInfoIcon);
                                    //下一步将选择的图片上传到服务器
                                    if (mImageResult != null && mToken != null && uID != null) {
                                        upLoadIcon(mImageResult, mToken, uID);
                                    }

                                }
                            }
                        }).openGallery();
                break;
            case R.id.userNickname:
                Intent changeNickName = new Intent(this, ChangeUserNickNameActivity.class);
                String trim = nickName.getText().toString().trim();
                Log.d("trim", trim);
                if (!trim.equals(""))
                    changeNickName.putExtra("nickname", trim);
                startActivityForResult(changeNickName, 0);
                break;
            case R.id.phone:
                if (mUserBundle != null) {
                    Intent mobileNum = new Intent(this, MobileNumActivity.class);
                    mobileNum.putExtra("cashStatus", mUserBundle.getCashStatus());
                    mobileNum.putExtra("phone", mUserBundle.getPhone());
                    startActivity(mobileNum);
                }
                break;
        }
    }

    private void upLoadIcon(String mImageResult, String token, String uID) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("imageFile", mImageResult);
        map.put("userId", uID);
        //用户上传头像的方法
        OkHttpUtils.post().url(Api.BASE_URL + Api.UPLOADAVATAR)
                .params(map)
                .addHeader("Content-Type", "multipart/form-data;boundary=" + BOUNDARY)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("错误", e.getMessage());
                        ToastUtils.showShort(PersonInfoActivity.this, "服务器暂时不可用，请稍后再试");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        //根据返回值判断上传成功或者失败
                        Log.d("上传头像", response);
                        //{"code":"1"}
                        if (JsonUtils.isSuccess(response)) {
                            ToastUtils.showShort(PersonInfoActivity.this, "头像上传成功!");
                        } else {
                            ToastUtils.showShort(PersonInfoActivity.this, "头像上传失败!");
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
                        nickName.setText(newName);
                        String userNickName = nickName.getText().toString().trim();
                        try {
                            String encode = URLEncoder.encode(userNickName, "utf-8");//"UTF-8"
                            LogUtils.d("昵称", encode);
                            if (mToken != null && encode != null && uID != null) {
                                updateUserNickName(mToken, encode, uID);
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
                        Log.e("修改昵称的请求失败", e.getMessage());
                        ToastUtils.showShort(PersonInfoActivity.this, "服务器暂时不可用，请稍后再试");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        //根据返回值成功和失败的判断
                        if (JsonUtils.isSuccess(response)) {
                            ToastUtils.showShort(PersonInfoActivity.this, "昵称修改成功!");
                        } else {
                            ToastUtils.showShort(PersonInfoActivity.this, "昵称修改失败!");
                        }
                    }
                });
    }


    /**
     * 将bitmap转换成base64字符串
     *
     * @param bitmap
     * @return base64 字符串
     */
    public static String bitmapToBase64(Bitmap bitmap) {
        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                baos.flush();
                baos.close();
                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 压缩
     *
     * @param image
     * @return
     */
    private Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        return BitmapFactory.decodeStream(isBm, null, null);
    }

    private Bitmap getimage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);//此时返回bm为空
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
    }

}
