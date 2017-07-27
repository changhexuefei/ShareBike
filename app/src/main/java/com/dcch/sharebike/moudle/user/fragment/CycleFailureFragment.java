package com.dcch.sharebike.moudle.user.fragment;


import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.dcch.sharebike.moudle.home.content.MyContent;
import com.dcch.sharebike.moudle.user.activity.UserGuideActivity;
import com.dcch.sharebike.utils.ClickUtils;
import com.dcch.sharebike.utils.LogUtils;
import com.dcch.sharebike.utils.NetUtils;
import com.dcch.sharebike.utils.PictureProcessingUtils;
import com.dcch.sharebike.utils.SPUtils;
import com.dcch.sharebike.utils.ToastUtils;
import com.hss01248.dialog.StyledDialog;
import com.louisgeek.multiedittextviewlib.MultiEditInputView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

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
    private List<CheckBox> checkBoxes = new ArrayList<>();
    private String bikeNo = "";
    private String contentText = "";
    private String mImageResult = "";
    private String selectResult = "";
    private String content = "";
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
            uID = String.valueOf(SPUtils.get(App.getContext(), "userId", 0));
            mToken = (String) SPUtils.get(App.getContext(), "token", "");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //反注册EventBus
        StyledDialog.dismiss();
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

    private String getTag(List<CheckBox> checkBoxes) {
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
                if (ClickUtils.isFastClick()) {
                    return;
                }
                CycleFailureFragmentPermissionsDispatcher.showCameraWithCheck(this);
                String msg = "fail";
                goCapture(msg, mToken);

                break;

            case R.id.cycle_photo:
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
                                //得到图片的完整路径
                                result = imageRadioResultEvent.getResult().getOriginalPath();
                                if (result != null && !result.equals("")) {
                                    //将图片赋值给图片控件
                                    Glide.with(App.getContext()).load(result).into(mCyclePhoto);
                                    Bitmap bitmap = PictureProcessingUtils.getimage(result);
                                    mImageResult = PictureProcessingUtils.bitmapToBase64(bitmap);
                                } else {
                                    mImageResult = "";
                                }
                            }
                        }).openGallery();
                break;
            case R.id.upload:
                if (ClickUtils.isFastClick()) {
                    return;
                }
                bikeNo = mBikeCode.getText().toString().trim();
                contentText = mQuestionDesc.getContentText().trim();
                String tag = getTag(checkBoxes);
                if (!tag.equals("") && tag != null) {
                    selectResult = tag.substring(0, tag.length() - 1);
                } else {
                    selectResult = "";
                }
                if (NetUtils.isConnected(App.getContext())) {
                    if (!uID.equals("") && uID != null && !bikeNo.equals("") && bikeNo != null && mToken != null && !mToken.equals("")) {
                        StyledDialog.buildLoading(getContext(), "提交中", true, false).show();
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
        startActivity(i4);
    }


    private void upLoad(String uID, String bikeNo, String token, String contentText, String selectResult, String imageResult) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", uID);
        map.put("bicycleNo", bikeNo);
        map.put("faultDescription", contentText);
        map.put("selectFaultDescription", selectResult);
        map.put("imageFile", imageResult);
        map.put("token", token);
        LogUtils.d("错误", uID + "\n" + bikeNo + "\n" + token);
        OkHttpUtils.post()
                .url(Api.BASE_URL + Api.ADDTROUBLEORDER)
                .addHeader("Content-Type", "multipart/form-data;boundary=" + MyContent.BOUNDARY)
                .params(map)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        StyledDialog.dismissLoading();
                        ToastUtils.showLong(getContext(), getResources().getString(R.string.server_tip));
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        StyledDialog.dismissLoading();
                        //{"resultStatus":"1"}
                        try {
                            JSONObject object = new JSONObject(response);
                            String resultStatus = object.optString("resultStatus");

                            if (resultStatus.equals("1")) {
                                ToastUtils.showLong(getActivity(), "提交成功！");
                                startActivity(new Intent(getActivity(), UserGuideActivity.class));
                                getActivity().finish();
                            } else if (resultStatus.equals("0")) {
                                ToastUtils.showLong(getActivity(), "提交失败！");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        CycleFailureFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission(Manifest.permission.CAMERA)
    void showCamera() {
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK && requestCode == 0) {
//            Bundle bundle = data.getExtras();
//            if (bundle != null) {
//                result = bundle.getString("result");
//                result = result.substring(result.length() - 9, result.length());
//                mBikeCode.setText(result);
//                changeStatus();
//            }
//        }
//    }

    //手工输入页面发来的消息
    @Subscriber(tag = "fail_bikeNo", mode = ThreadMode.MAIN)
    private void receiveFromManual(CodeEvent info) {
        if (info != null) {
            LogUtils.d("自行车", info.getBikeNo());
            result = info.getBikeNo();
            mBikeCode.setText(result);
            changeStatus();
        }

    }

    //从照相页面发来的消息
    @Subscriber(tag = "fail_bikeNo_cam", mode = ThreadMode.MAIN)
    private void receiveFromCap(CodeEvent info) {
        if (info != null) {
            LogUtils.d("自行车", info.getBikeNo());
            result = info.getBikeNo();
            result = result.substring(result.length() - 9, result.length());
            LogUtils.d("自行车", result);
            mBikeCode.setText(result);
            changeStatus();
        }

    }

    private void changeStatus() {
        mTips.setVisibility(View.VISIBLE);
        upload.setEnabled(true);
        upload.setBackgroundColor(getResources().getColor(R.color.colorTitle));
    }


}
