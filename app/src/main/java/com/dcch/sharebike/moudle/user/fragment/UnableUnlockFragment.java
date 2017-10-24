package com.dcch.sharebike.moudle.user.fragment;


import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * A simple {@link Fragment} subclass.
 */
@RuntimePermissions
public class UnableUnlockFragment extends Fragment {

    @BindView(R.id.bike_code)
    TextView bikeCode;
    @BindView(R.id.scan_code)
    RelativeLayout scanCode;
    @BindView(R.id.questionDesc)
    MultiEditInputView questionDesc;
    @BindView(R.id.un_confirm)
    TextView confirm;
    @BindView(R.id.tips)
    TextView tips;
    String uID;
    String bikeNo;
    String contentText;
    private String result;
    private String mToken;

    public UnableUnlockFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
//        showCamera();
        if (SPUtils.isLogin()) {
            uID = String.valueOf(SPUtils.get(App.getContext(), "userId", 0));
            mToken = (String) SPUtils.get(App.getContext(), "token", "");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_unable_unlock, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //反注册EventBus
        EventBus.getDefault().unregister(this);
    }

    @OnClick({R.id.scan_code, R.id.un_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.scan_code:
                if (ClickUtils.isFastClick()) {
                    return;
                }
                UnableUnlockFragmentPermissionsDispatcher.showCameraWithCheck(this);
//                String msg = "unable";
//                if (mToken != null) {
//                    goCapture(msg, mToken);
//                }

                break;
            case R.id.un_confirm:
                if (ClickUtils.isFastClick()) {
                    return;
                }
                bikeNo = bikeCode.getText().toString().trim();
                contentText = questionDesc.getContentText().trim();
                if (NetUtils.isConnected(App.getContext())) {
                    if (!uID.equals("") && uID != null && !bikeNo.equals("") && bikeNo != null && mToken != null && !mToken.equals("")) {
                        String selectResult = "";
                        String mImageResult = "";
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
        startActivityForResult(i4, 0);
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
                        ToastUtils.showLong(getActivity(), getResources().getString(R.string.server_tip));
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        //{"resultStatus":"1"}
                        StyledDialog.dismissLoading();
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


    private void changeStatus() {
        tips.setVisibility(View.VISIBLE);
        confirm.setEnabled(true);
        confirm.setBackgroundColor(getResources().getColor(R.color.colorTitle));

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        UnableUnlockFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission(Manifest.permission.CAMERA)
    void showCamera() {
        String msg = "unable";
        if (mToken != null) {
            goCapture(msg, mToken);
        }
    }

    //手工输入页面发来的消息
    @Subscriber(tag = "unable_bikeNo", mode = ThreadMode.MAIN)
    private void receiveFromManual(CodeEvent info) {
        LogUtils.d("自行车", info.getBikeNo());
        if (info != null) {
            result = info.getBikeNo();
            bikeCode.setText(result);
            changeStatus();
        }
    }

    //照相页面发来的消息
    @Subscriber(tag = "unable_bikeNo_cam", mode = ThreadMode.MAIN)
    private void receiveFromCap(CodeEvent info) {
        LogUtils.d("自行车", info.getBikeNo());
        if (info != null) {
            result = info.getBikeNo();
            result = result.substring(result.length() - 9, result.length());
            bikeCode.setText(result);
            changeStatus();
        }
    }
}
