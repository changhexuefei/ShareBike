package com.dcch.sharebike.view;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.dcch.sharebike.R;
import com.dcch.sharebike.moudle.home.bean.BookingBikeInfo;


/**
 * Created by Administrator on 2017/3/2 0002.
 */

public class BookBikePopupWindow extends PopupWindow {
    private BookingBikeInfo bookingBikeInfo;
    public TextView mBookBikeLocationInfo;
    private TextView mBikeNumber;
    public TextView mHoldTime;
    public ImageView forBellIcon;
    private Button mCancel;
    private View mCancelBookBikeWindow;

    public BookBikePopupWindow(Context context, BookingBikeInfo bookingBikeInfo, View.OnClickListener bookBikeItemsOnClick) {
        super(context);
        this.bookingBikeInfo = bookingBikeInfo;
        //创建布局反射器
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //加载布局
        mCancelBookBikeWindow = inflater.inflate(R.layout.item_book_bike, null);
        //初始化控件
        mBookBikeLocationInfo = (TextView) mCancelBookBikeWindow.findViewById(R.id.book_bike_location_info);
        mBikeNumber = (TextView) mCancelBookBikeWindow.findViewById(R.id.bikeNumber);
        mHoldTime = (TextView) mCancelBookBikeWindow.findViewById(R.id.hold_time);
        mCancel = (Button) mCancelBookBikeWindow.findViewById(R.id.cancel_book);
        forBellIcon = (ImageView) mCancelBookBikeWindow.findViewById(R.id.forBellIcon);
        //为控件赋值
//        String address = String.valueOf(bookingBikeInfo.getAddress());
//        if (address != null) {
//            mBookBikeLocationInfo.setText(address);
//        } else {
//            mBookBikeLocationInfo.setText("未知地址");
//        }
        mBikeNumber.setText(String.valueOf(bookingBikeInfo.getBicycleNo()));

        // 设置按钮监听
        mCancel.setOnClickListener(bookBikeItemsOnClick);
        forBellIcon.setOnClickListener(bookBikeItemsOnClick);
        // 设置SelectPicPopupWindow的View
        this.setContentView(mCancelBookBikeWindow);
        // 设置SelectPicPopupWindow的View
        this.setContentView(mCancelBookBikeWindow);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setOutsideTouchable(false);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(false);
////             设置SelectPicPopupWindow弹出窗体动画效果
//            this.setAnimationStyle(R.style.PopupWindowAnimation);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mCancelBookBikeWindow.setOnTouchListener(new View.OnTouchListener() {
            @Override
            @SuppressLint("ClickableViewAccessibility")
            public boolean onTouch(View v, MotionEvent event) {

                int height = mCancelBookBikeWindow.findViewById(R.id.book_bike_pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }

}
