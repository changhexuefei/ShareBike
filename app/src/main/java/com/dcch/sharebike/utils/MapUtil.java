package com.dcch.sharebike.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

import com.dcch.sharebike.app.App;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TimeZone;

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

    //double 类型保留3位位小数
    public static double changeDouble(Double dou) {
        NumberFormat nf = new DecimalFormat("0.000");
        dou = Double.parseDouble(nf.format(dou));
        return dou;
    }

    public static double changeOneDouble(Double dou) {
        NumberFormat nf = new DecimalFormat("0.0");
        dou = Double.parseDouble(nf.format(dou));
        return dou;
    }


    public static int stringToInt(String string) {
        String str = string.substring(0, string.indexOf("."));
        int intgeo = Integer.parseInt(str);
        return intgeo;
    }

    //获取系统的北京时间
    public static String getStringDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    //计算系统时间和车辆预定时间的时间差
    public static long countTime(String stringDate, String bookingCarDate) {
        long diff = 0;
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date1 = df.parse(stringDate);
            Date date2 = df.parse(bookingCarDate);
            //这样得到的差值是微秒级别
            diff = date1.getTime() - date2.getTime();
            LogUtils.d("时间差", diff + "");
//            long days = diff / (1000 * 60 * 60 * 24);
//            long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
//            long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return diff;
    }


    /**
     * 方法名称:transMapToString
     * 传入参数:map
     * 返回值:String 形如 username:chenziwen;password:1234
     */
    public static String transMapToString(Map map) {
        java.util.Map.Entry entry;
        StringBuffer sb = new StringBuffer();
        for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext(); ) {
            entry = (java.util.Map.Entry) iterator.next();
            sb.append(entry.getKey().toString()).append(":").append(null == entry.getValue() ? "" :
                    entry.getValue().toString()).append(iterator.hasNext() ? ";" : "");
        }
        return sb.toString();
    }

    /*
      将dp转化为px
       */
    public static int Dp2Px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
