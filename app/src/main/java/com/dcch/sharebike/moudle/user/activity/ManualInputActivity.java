package com.dcch.sharebike.moudle.user.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.dcch.sharebike.R;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.utils.DensityUtils;
import com.dcch.sharebike.utils.ToastUtils;
import com.dcch.sharebike.view.CodeInputEditText;

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
        initDrawable(mOpenFlashLight);
        mManualInputArea.initStyle(0,11,0.2f,R.color.colorTitle,R.color.lineColor,15);
        mManualInputArea.setOnTextFinishListener(new CodeInputEditText.OnTextFinishListener() {
            @Override
            public void onFinish(String str) {
                Toast.makeText(ManualInputActivity.this, str, Toast.LENGTH_SHORT).show();
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
                ToastUtils.showShort(this,"您点击的是确认按钮");
                break;
        }
    }
}
