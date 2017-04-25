package com.dcch.sharebike.moudle.user.fragment;


import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dcch.sharebike.R;
import com.dcch.sharebike.app.App;
import com.dcch.sharebike.base.CodeEvent;
import com.dcch.sharebike.http.Api;
import com.dcch.sharebike.libzxing.zxing.activity.CaptureActivity;
import com.dcch.sharebike.moudle.user.activity.UserGuideActivity;
import com.dcch.sharebike.utils.LogUtils;
import com.dcch.sharebike.utils.SPUtils;
import com.dcch.sharebike.utils.ToastUtils;
import com.louisgeek.multiedittextviewlib.MultiEditInputView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.rxgalleryfinal.RxGalleryFinal;
import cn.finalteam.rxgalleryfinal.imageloader.ImageLoaderType;
import cn.finalteam.rxgalleryfinal.rxbus.RxBusResultSubscriber;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageRadioResultEvent;
import okhttp3.Call;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static android.app.Activity.RESULT_OK;
import static com.dcch.sharebike.R.id.tips;


/**
 * A simple {@link Fragment} subclass.
 */

@RuntimePermissions
public class CycleFailureFragment extends Fragment {

    @BindView(R.id.cf_bike_code)
    TextView mBikeCode;
    @BindView(tips)
    TextView mTips;
    @BindView(R.id.scan_code)
    RelativeLayout mScanCode;
    @BindView(R.id.cycle_photo)
    ImageView mCyclePhoto;
    @BindView(R.id.questionDesc)
    MultiEditInputView mQuestionDesc;
    @BindView(R.id.upload)
    TextView upload;
    @BindView(R.id.questionOne)
    CheckBox questionOne;
    @BindView(R.id.questionTwo)
    CheckBox questionTwo;
    @BindView(R.id.questionThere)
    CheckBox questionThere;
    @BindView(R.id.questionFour)
    CheckBox questionFour;
    @BindView(R.id.questionFive)
    CheckBox questionFive;
    @BindView(R.id.questionSix)
    CheckBox questionSix;
    @BindView(R.id.questionSeven)
    CheckBox questionSeven;
    @BindView(R.id.one)
    LinearLayout one;
    @BindView(R.id.two)
    LinearLayout two;
    @BindView(R.id.there)
    LinearLayout there;
    @BindView(R.id.four)
    LinearLayout four;
    private String uID;
    private String result;
    List<CheckBox> checkBoxes = new ArrayList<>();
    public static final String BOUNDARY = "ZnGpDtePMx0KrHh_G0X99Yef9r8JZsRJSXC";
    private String bikeNo = "";
    private String contentText = "";
    private String mImageResult = "";
    private String selectResult = "";
    String content = "";
    private String mToken;

    public CycleFailureFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        CycleFailureFragmentPermissionsDispatcher.initPermissionWithCheck(this);
        showCamera();
        EventBus.getDefault().register(this);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        //反注册EventBus
        EventBus.getDefault().unregister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cycle_failure, container, false);
        ButterKnife.bind(this, view);
        checkBoxes.add(questionOne);
        checkBoxes.add(questionTwo);
        checkBoxes.add(questionThere);
        checkBoxes.add(questionFour);
        checkBoxes.add(questionFive);
        checkBoxes.add(questionSix);
        checkBoxes.add(questionSeven);

        return view;
    }

    public String getTag(List<CheckBox> checkBoxes) {
        for (CheckBox cbx : checkBoxes) {
            if (cbx.isChecked()) {
                content += String.valueOf(cbx.getTag()) + ";";
            }
        }
        return content;
    }

    @OnClick({R.id.scan_code, R.id.cycle_photo, R.id.upload})
    public void onClick(View view) {


        switch (view.getId()) {
            case R.id.scan_code:
               CycleFailureFragmentPermissionsDispatcher.showCameraWithCheck(this);
                Intent i4 = new Intent(getActivity(), CaptureActivity.class);
                i4.putExtra("msg","fail");
                i4.putExtra("token",mToken);
                startActivityForResult(i4, 0);
                break;

            case R.id.cycle_photo:

                RxGalleryFinal.with(getActivity())
                        .cropHideBottomControls(true)
                        .cropFreeStyleCropEnabled(false)
                        .image()
                        .radio()
                        .crop()
                        .imageLoader(ImageLoaderType.GLIDE)
                        .subscribe(new RxBusResultSubscriber<ImageRadioResultEvent>() {
                            @Override
                            protected void onEvent(ImageRadioResultEvent imageRadioResultEvent) throws Exception {
                                //得到图片的完整路径
                                result = imageRadioResultEvent.getResult().getOriginalPath();
                                if (result != null && !result.equals("")) {
                                    //将图片赋值给图片控件
                                    Glide.with(App.getContext()).load(result).into(mCyclePhoto);
                                    Bitmap bitmap = getimage(result);
                                    mImageResult = bitmapToBase64(bitmap);
                                } else {
                                    mImageResult = "";
                                }
                            }
                        }).openGallery();
                break;
            case R.id.upload:
                bikeNo = mBikeCode.getText().toString().trim();
                contentText = mQuestionDesc.getContentText().trim();
                String tag = getTag(checkBoxes);
                if (!tag.equals("") && tag != null) {
                    selectResult = tag.substring(0, tag.length() - 1);
                } else {
                    selectResult = "";
                }

                if (!uID.equals("") && uID != null && !bikeNo.equals("") && bikeNo != null) {
                    Map<String, String> map = new HashMap<>();
                    map.put("userId", uID);
                    map.put("bicycleNo", bikeNo);
                    map.put("faultDescription", contentText);
                    map.put("selectFaultDescription", selectResult);
                    map.put("imageFile", mImageResult);
                    map.put("token",mToken);
                    OkHttpUtils.post()
                            .url(Api.BASE_URL + Api.ADDTROUBLEORDER)
                            .addHeader("Content-Type", "multipart/form-data;boundary=" + BOUNDARY)
                            .params(map)
                            .build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    Log.d("错误", e.getMessage());
                                    ToastUtils.showLong(getActivity(), "服务器正忙！");
                                }

                                @Override
                                public void onResponse(String response, int id) {

                                    //{"resultStatus":"1"}
                                    try {
                                        JSONObject object = new JSONObject(response);
                                        String resultStatus = object.optString("resultStatus");

                                        if (resultStatus.equals("1")) {
                                            ToastUtils.showLong(getActivity(), "上传成功！");
                                            startActivity(new Intent(getActivity(), UserGuideActivity.class));
                                            getActivity().finish();
                                        } else if (resultStatus.equals("0")) {
                                            ToastUtils.showLong(getActivity(), "上传失败！");
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                } else {
                    ToastUtils.showShort(getActivity(), "请输入车辆编号！");
                    return;
                }
                break;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        CycleFailureFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission(Manifest.permission.CAMERA)
    void showCamera() {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 0) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                result = bundle.getString("result");
                mBikeCode.setText(result);
                changeStatus();
            }
        }
    }

    //手工输入页面发来的消息
    @Subscriber(tag = "fail_bikeNo", mode = ThreadMode.MAIN)
    private void receiveFromManual(CodeEvent info) {
        if(info!=null){
            LogUtils.d("自行车", info.getBikeNo());
            result = info.getBikeNo();
            mBikeCode.setText(result);
            changeStatus();
        }

    }
    private void changeStatus() {
        mTips.setVisibility(View.VISIBLE);
        upload.setEnabled(true);
        upload.setBackgroundColor(getResources().getColor(R.color.colorTitle));

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

    public boolean isEnable(String bikeNo){
        if(!bikeNo.equals("")&& bikeNo!=null){
            upload.setEnabled(true);
            upload.setBackgroundColor(getResources().getColor(R.color.colorTitle));
            return true;
        }

        return false;
    }


}
