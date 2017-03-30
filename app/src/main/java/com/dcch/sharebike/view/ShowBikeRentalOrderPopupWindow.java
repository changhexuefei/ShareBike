package com.dcch.sharebike.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.dcch.sharebike.R;
import com.dcch.sharebike.moudle.home.bean.ShowBikeRentalOrderInfo;

/**
 * Created by Administrator on 2017/3/2 0002.
 */

public class ShowBikeRentalOrderPopupWindow extends PopupWindow {
    private ShowBikeRentalOrderInfo mShowBikeRentalOrderInfo;
    public TextView rideTime;
    public TextView rideDistance;
    public TextView consumeEnergy;
    public TextView bikeNo;
    public TextView costCycling;
    private View mShowBikeRentalOrderView;

    public ShowBikeRentalOrderPopupWindow(Context context, ShowBikeRentalOrderInfo mShowBikeRentalOrderInfo) {
        super(context);
        this.mShowBikeRentalOrderInfo = mShowBikeRentalOrderInfo;

        //创建布局反射器
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //加载布局
        mShowBikeRentalOrderView = inflater.inflate(R.layout.item_riding_order, null);
        //初始化控件
        rideTime = (TextView) mShowBikeRentalOrderView.findViewById(R.id.ride_time);
        rideDistance = (TextView) mShowBikeRentalOrderView.findViewById(R.id.ride_distance);
        consumeEnergy = (TextView) mShowBikeRentalOrderView.findViewById(R.id.consume_energy);
        bikeNo = (TextView) mShowBikeRentalOrderView.findViewById(R.id.bike_number);
        costCycling = (TextView) mShowBikeRentalOrderView.findViewById(R.id.cost_cycling);
//
//        // 设置按钮监听
//        mOrder.setOnClickListener(itemsOnClick);
//          为控件赋值
        if (mShowBikeRentalOrderInfo != null && !mShowBikeRentalOrderInfo.equals("")) {
            rideTime.setText(String.valueOf(mShowBikeRentalOrderInfo.getTripTime()) + "分钟");
            rideDistance.setText(String.valueOf(mShowBikeRentalOrderInfo.getTripDist()) + "米");
            bikeNo.setText(mShowBikeRentalOrderInfo.getBicycleNo());
            costCycling.setText(String.valueOf(mShowBikeRentalOrderInfo.getRideCost()));
        }

        // 设置SelectPicPopupWindow的View
        this.setContentView(mShowBikeRentalOrderView);

        // 设置SelectPicPopupWindow的View
        this.setContentView(mShowBikeRentalOrderView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(false);
        // 设置SelectPicPopupWindow弹出窗体动画效果
//        this.setAnimationStyle(R.style.PopupAnimation);
        // 实例化一个ColorDrawable颜色为半透明
//        ColorDrawable dw = new ColorDrawable(0x00000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
//        this.setBackgroundDrawable(dw);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
//        mBikeRentalOrderView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            @SuppressLint("ClickableViewAccessibility")
//            public boolean onTouch(View v, MotionEvent event) {
//
//                int height = mBikeRentalOrderView.findViewById(R.id.pop_order_layout).getTop();
//                int y = (int) event.getY();
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    if (y < height) {
//                        dismiss();
//                    }
//                }
//                return true;
//            }
//        });
    }


}
