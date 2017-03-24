package com.dcch.sharebike.utils;

import android.graphics.drawable.Drawable;
import android.widget.TextView;

import com.dcch.sharebike.app.App;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by gao on 2017/3/4.
 */

public class MapUtil {
    //时间转化的方法
    public static String timeFormatter(int minute) {
        if (minute < 60) {
            return minute + "分钟";
        } else if (minute % 60 == 0) {
            return minute / 60 + "小时";
        } else {
            int hour = minute / 60;
            int minute1 = minute % 60;
            return hour + "小时" + minute1 + "分钟";
        }
    }

    //距离转换
    public static String distanceFormatter(int distance) {
        if (distance < 1000) {
            return distance + "米";
        } else if (distance % 1000 == 0) {
            return distance / 1000 + "公里";
        } else {
            DecimalFormat df = new DecimalFormat("0.0");
            int a1 = distance / 1000;   //得到十位
            double a2 = distance % 1000;
            double a3 = a2 / 1000; //得到个位
            String result = df.format(a3);
            double total = Double.parseDouble(result) + a1;
            return total + "公里";
        }
    }
    public static String getDateFromMillisecond(Long millisecond) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(millisecond);
        String dateStr = simpleDateFormat.format(date);
        return dateStr;
    }

    //将TextView中的图片转化为规定大小的方法
    public static void initDrawable(TextView v) {
        Drawable drawable = v.getCompoundDrawables()[1];
        drawable.setBounds(0, 0, DensityUtils.dp2px(App.getContext(), 50), DensityUtils.dp2px(App.getContext(), 50));
        v.setCompoundDrawables(null, drawable, null, null);
    }

}
