package com.dcch.sharebike.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dcch.sharebike.R;

/**
 * Created by Administrator on 2017/9/5 0005.
 */

public class PaymentView extends LinearLayout {

    private TextView mPayInfo;
    private CheckBox mPaySelect;
    private ImageView mPayImage;

    public PaymentView(Context context) {
        super(context);
        if (!isInEditMode()) {
            init(context);
        }
    }

    public PaymentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            init(context);
        }
        //获取属性值
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.PaymentView);
        String text = mTypedArray.getString(R.styleable.PaymentView_text);
        Drawable image = mTypedArray.getDrawable(R.styleable.PaymentView_src);

        //将获取的的值设置到相应的位置上
        if (!isInEditMode()) {
            mPayInfo.setText(text);
            mPayImage.setImageDrawable(image);
        }
        //获取资源后要及时回收
        mTypedArray.recycle();
    }

    public PaymentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode()) {
            init(context);
        }
    }


    private void init(Context context) {
        View view = inflate(context, R.layout.payment_mode, this);
        mPayInfo = (TextView) view.findViewById(R.id.pay_info);
        mPaySelect = (CheckBox) view.findViewById(R.id.select_checkbox);
        mPayImage = (ImageView) view.findViewById(R.id.pay_image);
        view.setFocusable(false);
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mPaySelect.setChecked(true);
            }
        });
    }

    public boolean isChecked() {
        return mPaySelect.isChecked();
    }

    public void setChecked(boolean checked) {
        if (checked) {
            mPaySelect.setChecked(checked);
        }
    }

    public void setText(String text) {
        mPayInfo.setText(text);
    }

    public void setPayImage(int id) {
        mPayImage.setImageResource(id);
    }
}
