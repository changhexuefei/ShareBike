package com.dcch.sharebike.moudle.user.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dcch.sharebike.R;
import com.dcch.sharebike.app.App;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.http.Api;
import com.dcch.sharebike.moudle.login.activity.PersonalCenterActivity;
import com.dcch.sharebike.moudle.user.bean.UserInfo;
import com.dcch.sharebike.utils.SPUtils;
import com.dcch.sharebike.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
    private String uID;
    private String result;
    public static final String BOUNDARY = "ZnGpDtePMx0KrHh_G0X99Yef9r8JZsRJSXC";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_person_info;
    }

    @Override
    protected void initData() {

        if (SPUtils.isLogin()) {
            String userDetail = (String) SPUtils.get(App.getContext(), "userDetail", "");
            try {
                JSONObject object = new JSONObject(userDetail);
                int id = object.getInt("id");
                uID = String.valueOf(id);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            Intent intent = getIntent();
            Bundle user = intent.getExtras();
            mUserBundle = (UserInfo) user.getSerializable("userBundle");
            Log.d("你是谁", mUserBundle + "");
            if (mUserBundle != null) {
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
                if(mUserBundle.getUserimage()!=null){
                    Glide.with(App.getContext()).load(mUserBundle.getUserimage()).into(userInfoIcon);
                }else {
                    userInfoIcon.setImageResource(R.mipmap.avatar_default_login);
                }
            }
        }
    }

    @OnClick({R.id.back, R.id.userIcon, R.id.userNickname, R.id.phone})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                startActivity(new Intent(this, PersonalCenterActivity.class));
                finish();
                break;
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
                                Log.d("图片地址", result);
                                Bitmap bitmap = getimage(result);
                                String mImageResult = bitmapToBase64(bitmap);

                                if (result != null && !result.equals("")) {
                                    //将图片赋值给图片控件
                                    Glide.with(App.getContext()).load(result).into(userInfoIcon);
                                    //下一步将选择的图片上传到服务器
                                    Map<String, String> map = new HashMap<>();
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
                                                    try {
                                                        JSONObject object = new JSONObject(response);
                                                        String code = object.optString("code");
                                                        if (code.equals("1")) {
                                                            ToastUtils.showShort(PersonInfoActivity.this, "头像上传成功!");
                                                        } else if (code.equals("0")) {
                                                            ToastUtils.showShort(PersonInfoActivity.this, "头像上传失败!");
                                                        }
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }

                                                }
                                            });
                                }
                            }
                        }).openGallery();
                break;
            case R.id.userNickname:
                ToastUtils.showLong(this, "昵称");
                Intent changeNickName = new Intent(this, ChangeUserNickNameActivity.class);
                String trim = nickName.getText().toString().trim();
                Log.d("trim", trim);
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
        if (data != null && !data.equals("")) {
            String newName = data.getStringExtra("newName");
            switch (requestCode) {
                case 0:
                    if (newName != null && !newName.equals("")) {
                        nickName.setText(newName);
                        String userNickName = nickName.getText().toString().trim();
                        if (!userNickName.equals("") && userNickName != null) {
                            Map<String, String> map = new HashMap<>();
                            map.put("userId", uID);
                            map.put("nickName", userNickName);
                            /**
                             * 上传用户昵称的方法
                             *
                             */
                            OkHttpUtils.post().url(Api.BASE_URL + Api.EDITUSER).params(map).build().execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    Log.e("修改昵称的请求失败", e.getMessage());
                                    ToastUtils.showShort(PersonInfoActivity.this, "服务器暂时不可用，请稍后再试");
                                }

                                @Override
                                public void onResponse(String response, int id) {
                                    //根据返回值成功和失败的判断
                                    try {
                                        JSONObject object = new JSONObject(response);
                                        String code = object.optString("code");
                                        if (code.equals("1")) {
                                            ToastUtils.showShort(PersonInfoActivity.this, "昵称修改成功!");
                                        } else if (code.equals("0")) {
                                            ToastUtils.showShort(PersonInfoActivity.this, "昵称修改失败!");
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    }
                    break;
            }
        }
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
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
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
