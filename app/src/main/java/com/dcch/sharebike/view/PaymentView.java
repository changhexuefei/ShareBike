package com.dcch.sharebike.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
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
    private View mView;

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
        mView = inflate(context, R.layout.payment_mode, this);
        mPayInfo = (TextView) mView.findViewById(R.id.pay_info);
        mPaySelect = (CheckBox) mView.findViewById(R.id.select_checkbox);
        mPayImage = (ImageView) mView.findViewById(R.id.pay_image);
        mView.setFocusable(false);

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

    public void setClickListener(OnClickListener clickListener){
        mView.setOnClickListener(clickListener);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_MOVE:   //表示父类需要
                return false;
            case MotionEvent.ACTION_UP:
                return true;
            default:
                break;
        }
        return true;    //如果设置拦截，除了down,其他都是父类处理
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
