package com.dcch.sharebike.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.dcch.sharebike.R;

/**
 * Created by Administrator on 2017/3/9 0009.
 */

public class RefundPopuwindow extends PopupWindow {

    private TextView tv_tips;
    private Button btn_cancle;
    private Button btn_confirm;
    private View refundView;

    public RefundPopuwindow(Context context, View.OnClickListener refundViewOnClick) {
        super(context);
        //创建布局反射器
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //加载布局
        refundView = inflater.inflate(R.layout.item_refund, null);
        //初始化控件

        tv_tips = (TextView) refundView.findViewById(R.id.tv_tip);
        btn_cancle = (Button) refundView.findViewById(R.id.btn_cancel);
        btn_confirm = (Button) refundView.findViewById(R.id.btn_confirm);


        // 设置按钮监听
        btn_confirm.setOnClickListener(refundViewOnClick);
        btn_cancle.setOnClickListener(refundViewOnClick);
        // 设置SelectPicPopupWindow的View
        this.setContentView(refundView);
        // 设置SelectPicPopupWindow的View
        this.setContentView(refundView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(800);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
//             设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.PopupAnimation);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        refundView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            @SuppressLint("ClickableViewAccessibility")
            public boolean onTouch(View v, MotionEvent event) {
                int height = refundView.findViewById(R.id.refund_area).getTop();
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
