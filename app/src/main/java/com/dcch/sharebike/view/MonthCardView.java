package com.dcch.sharebike.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dcch.sharebike.R;

/**
 * Created by Administrator on 2017/8/28 0028.
 */

public class MonthCardView extends RelativeLayout {

    private TextView mCurrentPrice;
    private TextView mOriginalPrice;
    private TextView mUsefulLife;
    private TextView mDiscount;
    private String mLeftText;
    private String mBlewText;
    private String mMiddleText;
    private String mRightText;

    public MonthCardView(Context context) {
        super(context);
        initView(context);
    }

    public MonthCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.MonthCardView);
        mLeftText = mTypedArray.getString(R.styleable.MonthCardView_left_text);
        mBlewText = mTypedArray.getString(R.styleable.MonthCardView_blew_text);
        mMiddleText = mTypedArray.getString(R.styleable.MonthCardView_middle_text);
        mRightText = mTypedArray.getString(R.styleable.MonthCardView_right_text);
        //获取资源后要及时回收
        mTypedArray.recycle();
        initView(context);
    }


    public MonthCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View view = View.inflate(context, R.layout.month_card, this);
        mCurrentPrice = (TextView) view.findViewById(R.id.select_one);
        mOriginalPrice = (TextView) view.findViewById(R.id.original_price_one);
        mUsefulLife = (TextView) view.findViewById(R.id.useful_life_one);
        mDiscount = (TextView) view.findViewById(R.id.discount_one);
    }

}
