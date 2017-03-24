package com.dcch.sharebike.moudle.user.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.dcch.sharebike.MainActivity;
import com.dcch.sharebike.R;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.base.CodeEvent;
import com.dcch.sharebike.utils.DensityUtils;
import com.dcch.sharebike.utils.ToastUtils;
import com.dcch.sharebike.view.CodeInputEditText;

import org.simple.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

public class ManualInputActivity extends BaseActivity {

    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.openFlashLight)
    ToggleButton mOpenFlashLight;
    @BindView(R.id.manualInputArea)
    CodeInputEditText mManualInputArea;
    @BindView(R.id.ensure)
    Button mEnsure;
    private String bikeNo="";


    @Override
    protected int getLayoutId() {
        return R.layout.activity_manual_input;
    }

    @Override
    protected void initData() {

    }

    //将TextView中的图片转化为规定大小的方法
    public void initDrawable(TextView v) {
        Drawable drawable = v.getCompoundDrawables()[1];
        drawable.setBounds(0, 0, DensityUtils.dp2px(ManualInputActivity.this, 50), DensityUtils.dp2px(ManualInputActivity.this, 50));
        v.setCompoundDrawables(null, drawable, null, null);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        initDrawable(mOpenFlashLight);
        mManualInputArea.initStyle(R.drawable.edit_num_bg, 11, 0.2f, R.color.colorTitle, R.color.lineColor, 15);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mManualInputArea.setOnTextFinishListener(new CodeInputEditText.OnTextFinishListener() {
            @Override
            public void onFinish(String str) {
                Toast.makeText(ManualInputActivity.this, str, Toast.LENGTH_SHORT).show();
                bikeNo=str;
                mEnsure.setEnabled(true);
                mEnsure.setBackgroundColor(Color.parseColor("#F8941D"));
            }
        });

    }

    @OnClick({R.id.back, R.id.ensure})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.ensure:
                ToastUtils.showShort(this, "您点击的是确认按钮");
                Intent bikeNoIntent = new Intent(this, MainActivity.class);
                EventBus.getDefault().post(new CodeEvent(bikeNo), "bikeNo");
                startActivity(bikeNoIntent);
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
