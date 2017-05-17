package com.dcch.sharebike.moudle.user.fragment;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dcch.sharebike.R;
import com.dcch.sharebike.app.App;
import com.dcch.sharebike.base.CodeEvent;
import com.dcch.sharebike.http.Api;
import com.dcch.sharebike.libzxing.zxing.activity.CaptureActivity;
import com.dcch.sharebike.moudle.user.activity.UserGuideActivity;
import com.dcch.sharebike.utils.ClickUtils;
import com.dcch.sharebike.utils.JsonUtils;
import com.dcch.sharebike.utils.LogUtils;
import com.dcch.sharebike.utils.NetUtils;
import com.dcch.sharebike.utils.PictureProcessingUtils;
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

import java.util.HashMap;
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
public class ReportIllegalParkingFragment extends Fragment {

    @BindView(R.id.bike_code)
    TextView mBikeCode;
    @BindView(tips)
    TextView mTips;
    @BindView(R.id.scan_code)
    RelativeLayout mScanCode;
    @BindView(R.id.select_photo)
    ImageView mSelectPhoto;
    @BindView(R.id.questionDesc)
    MultiEditInputView mQuestionDesc;
    @BindView(R.id.mconfirm)
    TextView mMconfirm;
    private String uID;
    private String bikeNo;
    private String contentText;
    public static final String BOUNDARY = "ZnGpDtePMx0KrHh_G0X99Yef9r8JZsRJSXC";
    private String result;
    private String mImageResult = "";
    private String mToken;

    public ReportIllegalParkingFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        showCamera();
        if (SPUtils.isLogin()) {
            String userDetail = (String) SPUtils.get(App.getContext(), "userDetail", "");
            Log.d("用户明细", userDetail);
            try {
                JSONObject object = new JSONObject(userDetail);
                int id = object.optInt("id");
                uID = String.valueOf(id);
                mToken = object.optString("token");
                Log.d("用户ID", uID);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report_illegal_parking, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick({R.id.scan_code, R.id.select_photo, R.id.mconfirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.scan_code:
                if (ClickUtils.isFastClick()) {
                    return;
                }
                ReportIllegalParkingFragmentPermissionsDispatcher.showCameraWithCheck(this);
                String msg = "reports";
                if (mToken != null) {
                    goCapture(msg, mToken);
                }
                break;
            case R.id.select_photo:
                if (ClickUtils.isFastClick()) {
                    return;
                }
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
                                //得到图片的路径
                                result = imageRadioResultEvent.getResult().getOriginalPath();
                                if (result != null && !result.equals("")) {
                                    //将图片赋值给图片控件
                                    Glide.with(App.getContext()).load(result).into(mSelectPhoto);
                                    Bitmap bitmap = PictureProcessingUtils.getimage(result);
                                    mImageResult = PictureProcessingUtils.bitmapToBase64(bitmap);
                                } else {
                                    mImageResult = "";
                                }
                            }
                        }).openGallery();
                break;
            case R.id.mconfirm:
                if (ClickUtils.isFastClick()) {
                    return;
                }
                bikeNo = mBikeCode.getText().toString().trim();
                contentText = mQuestionDesc.getContentText().trim();
                if (NetUtils.isConnected(App.getContext())) {
                    if (!uID.equals("") && uID != null && !bikeNo.equals("") && bikeNo != null && mToken != null && !mToken.equals("")) {
                        String selectResult = "";
                        upLoad(uID, bikeNo, mToken, contentText, selectResult, mImageResult);
                    } else {
                        ToastUtils.showShort(getContext(), getString(R.string.input_tip));
                    }
                } else {
                    ToastUtils.showShort(getContext(), getString(R.string.no_network_tip));
                }
                break;
        }
    }

    private void goCapture(String msg, String token) {
        Intent i4 = new Intent(getActivity(), CaptureActivity.class);
        i4.putExtra("msg", msg);
        i4.putExtra("token", token);
        startActivityForResult(i4, 0);
    }

    private void upLoad(String uID, String bikeNo, String token, String contentText, String selectResult, String imageResult) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", uID);
        map.put("bicycleNo", bikeNo);
        map.put("faultDescription", contentText);
        map.put("selectFaultDescription", selectResult);
        map.put("imageFile", mImageResult);
        map.put("token", mToken);
        OkHttpUtils.post()
                .url(Api.BASE_URL + Api.ADDTROUBLEORDER)
                .addHeader("Content-Type", "multipart/form-data;boundary=" + BOUNDARY)
                .params(map)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("错误", e.getMessage());
                        ToastUtils.showLong(getContext(), "服务器正忙！");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (JsonUtils.isSuccess(response)) {
                            ToastUtils.showLong(getContext(), "提交成功！");
                            startActivity(new Intent(getActivity(), UserGuideActivity.class));
                            getActivity().finish();
                        } else {
                            ToastUtils.showLong(getContext(), "提交失败！");
                        }

                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ReportIllegalParkingFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
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
    @Subscriber(tag = "report_bikeNo", mode = ThreadMode.MAIN)
    private void receiveFromManual(CodeEvent info) {
        LogUtils.d("自行车", info.getBikeNo());
        if (info != null) {
            result = info.getBikeNo();
            mBikeCode.setText(result);
            changeStatus();

        }

    }

    private void changeStatus() {
        mTips.setVisibility(View.VISIBLE);
        mMconfirm.setEnabled(true);
        mMconfirm.setBackgroundColor(getResources().getColor(R.color.colorTitle));

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //反注册EventBus
        EventBus.getDefault().unregister(this);
    }


}
