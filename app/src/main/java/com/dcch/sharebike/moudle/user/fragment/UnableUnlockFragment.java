package com.dcch.sharebike.moudle.user.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dcch.sharebike.R;
import com.dcch.sharebike.app.App;
import com.dcch.sharebike.http.Api;
import com.dcch.sharebike.utils.SPUtils;
import com.dcch.sharebike.utils.ToastUtils;
import com.louisgeek.multiedittextviewlib.MultiEditInputView;
import com.xys.libzxing.zxing.activity.CaptureActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * A simple {@link Fragment} subclass.
 */
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
    public static final String BOUNDARY = "ZnGpDtePMx0KrHh_G0X99Yef9r8JZsRJSXC";

    public UnableUnlockFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SPUtils.isLogin()) {
            String userDetail = (String) SPUtils.get(App.getContext(), "userDetail", "");
            Log.d("用户明细", userDetail);
            try {
                JSONObject object = new JSONObject(userDetail);
                int id = object.getInt("id");
                uID = String.valueOf(id);
                Log.d("用户ID", uID);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_unable_unlock, container, false);
        ButterKnife.bind(this, view);


        return view;
    }

    @OnClick({R.id.scan_code, R.id.un_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.scan_code:
                Intent i4 = new Intent(App.getContext(), CaptureActivity.class);
                startActivityForResult(i4, 0);
                break;
            case R.id.un_confirm:
                bikeNo = bikeCode.getText().toString().trim();
                contentText = questionDesc.getContentText().toString().trim();
                if (!uID.equals("") && uID != null && !bikeNo.equals("") && bikeNo != null) {
                    Map<String, String> map = new HashMap<>();
                    map.put("userId", uID);
                    map.put("bicycleNo", bikeNo);
                    map.put("faultDescription", contentText);
                    map.put("selectFaultDescription", "1");
                    map.put("imageFile", "");
                    OkHttpUtils.post()
                            .url(Api.BASE_URL + Api.ADDTROUBLEORDER)
                            .addHeader("Content-Type", "multipart/form-data;boundary=" + BOUNDARY)
                            .params(map)
                            .build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    Log.d("错误", e.getMessage());
                                    ToastUtils.showLong(getActivity(), "上传失败！");
                                }

                                @Override
                                public void onResponse(String response, int id) {
                                    Log.d("上传成功", response);
                                    try {
                                        JSONObject object = new JSONObject(response);
                                        String resultStatus = object.optString("resultStatus");
                                        if (resultStatus.equals("1")) {
                                            ToastUtils.showLong(getActivity(), "上传成功！");
                                        } else if (resultStatus.equals("0")) {
                                            ToastUtils.showLong(getActivity(), "上传失败！");
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                }else {
                    ToastUtils.showShort(getActivity(),"请输入车辆编号！");
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == -1 && requestCode == 0) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                String result = bundle.getString("result");
                bikeCode.setText(result);
                tips.setVisibility(View.VISIBLE);
                confirm.setEnabled(true);
                confirm.setBackgroundColor(getResources().getColor(R.color.colorTitle));
            }
        }
    }
}
