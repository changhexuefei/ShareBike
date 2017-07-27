package com.dcch.sharebike.moudle.user.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.dcch.sharebike.MainActivity;
import com.dcch.sharebike.R;
import com.dcch.sharebike.app.App;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.base.CodeEvent;
import com.dcch.sharebike.http.Api;
import com.dcch.sharebike.moudle.login.activity.LoginActivity;
import com.dcch.sharebike.moudle.login.activity.OpenLockTipAcitivity;
import com.dcch.sharebike.utils.ClickUtils;
import com.dcch.sharebike.utils.DensityUtils;
import com.dcch.sharebike.utils.LogUtils;
import com.dcch.sharebike.utils.NetUtils;
import com.dcch.sharebike.utils.ToastUtils;
import com.dcch.sharebike.view.CodeInputEditText;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyDialogListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;
import org.simple.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

@SuppressWarnings("ALL")
public class ManualInputActivity extends BaseActivity {

    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.inputFlashLight)
    ToggleButton mOpenFlashLight;
    @BindView(R.id.manualInputArea)
    CodeInputEditText mManualInputArea;
    @BindView(R.id.ensure)
    Button mEnsure;
    @BindView(R.id.manual_help_tip)
    TextView mManualHelpTip;
    private String bikeNo;
    private String mTag;
    private Camera camera = null;
    private Camera.Parameters parameters = null;
    private String mToken;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_manual_input;
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            mTag = intent.getStringExtra("tag");
            mToken = intent.getStringExtra("token");
        }

    }

    //将TextView中的图片转化为规定大小的方法
    private void initDrawable(TextView v) {
        Drawable drawable = v.getCompoundDrawables()[1];
        drawable.setBounds(0, 0, DensityUtils.dp2px(ManualInputActivity.this, 50), DensityUtils.dp2px(ManualInputActivity.this, 50));
        v.setCompoundDrawables(null, drawable, null, null);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        initDrawable(mOpenFlashLight);
        mManualInputArea.initStyle(R.drawable.edit_num_bg, 9, 0.5f, R.color.colorTitle, R.color.lineColor, 15);
        mOpenFlashLight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    camera = Camera.open();
                    parameters = camera.getParameters();
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);// 开启
                    camera.setParameters(parameters);
                    camera.startPreview();
                } else {
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);// 关闭
                    camera.setParameters(parameters);
                    camera.stopPreview();
                    camera.release();
                    camera = null;
                }

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mManualInputArea.setOnTextFinishListener(new CodeInputEditText.OnTextFinishListener() {
            @Override
            public void onFinish(String str) {
                bikeNo = str;
                mEnsure.setEnabled(true);
                mEnsure.setBackgroundColor(Color.parseColor("#F8941D"));
            }
        });

    }

    @OnClick({R.id.back, R.id.ensure, R.id.manual_help_tip})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                if (ClickUtils.isFastClick()) {
                    return;
                }
                startActivity(new Intent(this, MainActivity.class));
                this.finish();
                break;
            case R.id.ensure:
                if (ClickUtils.isFastClick()) {
                    return;
                }
                if (bikeNo != null && mToken != null) {
                    StyledDialog.buildMdAlert(ManualInputActivity.this, "提示", "确定打开编号为" + bikeNo + "的车辆", new MyDialogListener() {
                        @Override
                        public void onFirst() {
                            if (NetUtils.isConnected(App.getContext())) {
                                ensureBikeNo(bikeNo, mToken);
                            } else {
                                ToastUtils.showShort(ManualInputActivity.this, getString(R.string.no_network_tip));
                            }
                        }

                        @Override
                        public void onSecond() {
                        }
                    }).show();

                }
                break;

            case R.id.manual_help_tip:
                if (ClickUtils.isFastClick()) {
                    return;
                }
                startActivity(new Intent(this, OpenLockTipAcitivity.class));
                break;
        }
    }

    private void ensureBikeNo(final String bikeNo, String token) {
        Map<String, String> map = new HashMap<>();
        map.put("lockremark", bikeNo);
        map.put("token", token);
        LogUtils.d("捕获", bikeNo + "\n" + token);
        OkHttpUtils.post().url(Api.BASE_URL + Api.CHECKBICYCLENO).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.showShort(ManualInputActivity.this, getString(R.string.server_tip));
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d("锁号", response);
                //{"resultStatus":"0"}
                try {
                    JSONObject object = new JSONObject(response);
                    String resultStatus = object.optString("resultStatus");
                    switch (resultStatus) {
                        case "1":
                            switch (mTag) {
                                case "main": {
                                    Intent bikeNoIntent = new Intent(ManualInputActivity.this, UnlockProgressActivity.class);
                                    EventBus.getDefault().post(new CodeEvent(bikeNo), "bikeNo");
                                    startActivity(bikeNoIntent);
                                    break;
                                }
                                case "unable": {
                                    Intent bikeNoIntent = new Intent(ManualInputActivity.this, CustomerServiceActivity.class);
                                    startActivity(bikeNoIntent);
                                    EventBus.getDefault().post(new CodeEvent(bikeNo), "unable_bikeNo");
                                    break;
                                }
                                case "reports": {
                                    Intent bikeNoIntent = new Intent(ManualInputActivity.this, CustomerServiceActivity.class);
                                    startActivity(bikeNoIntent);
                                    EventBus.getDefault().post(new CodeEvent(bikeNo), "report_bikeNo");
                                    break;
                                }
                                case "fail": {
                                    Intent bikeNoIntent = new Intent(ManualInputActivity.this, CustomerServiceActivity.class);
                                    startActivity(bikeNoIntent);
                                    EventBus.getDefault().post(new CodeEvent(bikeNo), "fail_bikeNo");
                                    break;
                                }
                            }
                            finish();
                            break;
                        case "0":
                            clearTextView();
                            ToastUtils.showShort(ManualInputActivity.this, getString(R.string.no_has_bikeNo));

                            break;
                        case "2":
                            ToastUtils.showShort(ManualInputActivity.this, getString(R.string.forced_to_logoff));
                            startActivity(new Intent(ManualInputActivity.this, LoginActivity.class));
                            finish();
                            break;
                        case "3":
                            clearTextView();
                            ToastUtils.showLong(ManualInputActivity.this, getString(R.string.using));
                            break;
                        case "4":
                            clearTextView();
                            ToastUtils.showLong(ManualInputActivity.this, getString(R.string.trouble));
                            break;
                        case "5":
                            clearTextView();
                            ToastUtils.showLong(ManualInputActivity.this, getString(R.string.preengaged));
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void clearTextView() {
        mManualInputArea.clearText();
        mEnsure.setEnabled(false);
        mEnsure.setBackgroundColor(getResources().getColor(R.color.input_btn_color));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
