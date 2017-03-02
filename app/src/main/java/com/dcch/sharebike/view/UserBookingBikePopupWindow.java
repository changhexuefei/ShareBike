package com.dcch.sharebike.view;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.dcch.sharebike.R;
import com.dcch.sharebike.moudle.home.bean.UserBookingBikeInfo;

/**
 * Created by Administrator on 2017/3/2 0002.
 */

public class UserBookingBikePopupWindow extends PopupWindow{
    private UserBookingBikeInfo userBookingBikeInfo;
    TextView mBookBikeLocationInfo;
    TextView mBikeNumber;
    public TextView mHoldTime;
    Button mCancel;
    private View mUserBookBikeWindow;

    public UserBookingBikePopupWindow(Context context, UserBookingBikeInfo userBookingBikeInfo, View.OnClickListener bookBikeItemsOnClick) {
        super(context);
        this.userBookingBikeInfo =userBookingBikeInfo;
        //创建布局反射器
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //加载布局
        mUserBookBikeWindow = inflater.inflate(R.layout.item_book_bike, null);
        //初始化控件
        mBookBikeLocationInfo = (TextView) mUserBookBikeWindow.findViewById(R.id.book_bike_location_info);
        mBikeNumber = (TextView) mUserBookBikeWindow.findViewById(R.id.bikeNumber);
        mHoldTime = (TextView) mUserBookBikeWindow.findViewById(R.id.hold_time);
        mCancel = (Button) mUserBookBikeWindow.findViewById(R.id.cancel_book);

        //为控件赋值
        if(userBookingBikeInfo!=null && !userBookingBikeInfo.equals("")){
            mBookBikeLocationInfo.setText(userBookingBikeInfo.getAddress());
            mBikeNumber.setText(String.valueOf(userBookingBikeInfo.getBicycleNo()));
        }
        // 设置按钮监听
        mCancel.setOnClickListener(bookBikeItemsOnClick);
        // 设置SelectPicPopupWindow的View
        this.setContentView(mUserBookBikeWindow);
        // 设置SelectPicPopupWindow的View
        this.setContentView(mUserBookBikeWindow);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
//            this.setFocusable(false);
////             设置SelectPicPopupWindow弹出窗体动画效果
//            this.setAnimationStyle(R.style.PopupAnimation);
        // 实例化一个ColorDrawable颜色为半透明
//        ColorDrawable dw = new ColorDrawable(0x00000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
//        this.setBackgroundDrawable(dw);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mUserBookBikeWindow.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    @SuppressLint("ClickableViewAccessibility")
                    public boolean onTouch(View v, MotionEvent event) {

                int height = mUserBookBikeWindow.findViewById(R.id.book_bike_pop_layout).getTop();
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
