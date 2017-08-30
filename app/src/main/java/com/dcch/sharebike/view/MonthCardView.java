package com.dcch.sharebike.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dcch.sharebike.R;

/**
 * Created by Administrator on 2017/8/28 0028.
 */

public class MonthCardView extends LinearLayout {

    private TextView mCurrentPrice;
    private TextView mOriginalPrice;
    private TextView mUsefulLife;
    private TextView mDiscount;
    private View mView;

    private String mLeftText;
    private String mBlewText;
    private String mMiddleText;
    private String mRightText;

    public String getLeftText() {
        return mLeftText;
    }

    public void setLeftText(String leftText) {
        mLeftText = leftText;
        mCurrentPrice.setText(leftText);
    }

    public String getBlewText() {
        return mBlewText;
    }

    public void setBlewText(String blewText) {
        mBlewText = blewText;
        mUsefulLife.setText(blewText);
    }

    public String getMiddleText() {
        return mMiddleText;
    }

    public void setMiddleText(String middleText) {
        mMiddleText = middleText;
        mOriginalPrice.setText(middleText);
    }

    public String getRightText() {
        return mRightText;
    }

    public void setRightText(String rightText) {
        mRightText = rightText;
        mDiscount.setText(rightText);
    }

    public interface ISelectedListener {
        void onSelectedListener();
    }

    /*初始化接口变量*/
    ISelectedListener iListener = null;

    /*自定义事件*/
    public void setOnSelectedListener(ISelectedListener iSelectedListener) {
        iListener = iSelectedListener;
    }

    public MonthCardView(Context context) {
        super(context);
        if (!isInEditMode()) {
            initView(context);
        }
    }

    public MonthCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {//解决可视化编辑器无法自动识别控件的问题
            initView(context);
        }
        //获取属性值
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.MonthCardView);
        mLeftText = mTypedArray.getString(R.styleable.MonthCardView_left_text);
        mBlewText = mTypedArray.getString(R.styleable.MonthCardView_blew_text);
        mMiddleText = mTypedArray.getString(R.styleable.MonthCardView_middle_text);
        mRightText = mTypedArray.getString(R.styleable.MonthCardView_right_text);
        //将获取的的值设置到相应的位置上
        if (!isInEditMode()) {
            mCurrentPrice.setText(mLeftText);
            mUsefulLife.setText(mBlewText);
            mOriginalPrice.setText(mMiddleText);
            mDiscount.setText(mRightText);
        }
        //获取资源后要及时回收
        mTypedArray.recycle();
    }


    private void initView(Context context) {
        mView = View.inflate(context, R.layout.month_card, this);
        mCurrentPrice = (TextView) mView.findViewById(R.id.select_one);
        mOriginalPrice = (TextView) mView.findViewById(R.id.original_price_one);
        mUsefulLife = (TextView) mView.findViewById(R.id.useful_life_one);
        mDiscount = (TextView) mView.findViewById(R.id.discount_one);
        mOriginalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); //中划线
        mView.setFocusable(false);
        mView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                iListener.onSelectedListener();
            }
        });
    }

}
