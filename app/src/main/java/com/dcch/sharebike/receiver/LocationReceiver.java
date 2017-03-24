package com.dcch.sharebike.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.dcch.sharebike.service.GPSService;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by Administrator on 2017/3/24 0024.
 */

public class LocationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent start = new Intent(context, GPSService.class);
        context.startService(start);
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            double totalDistance = bundle.getDouble("totalDistance");
            double changeDouble = changeDouble(totalDistance);
            String s = String.valueOf(changeDouble);
            int i = stringToInt(s);
//            orderPopupWindow.rideDistance.setText(MapUtil.distanceFormatter(i));
//            orderPopupWindow.rideTime.setText(String.valueOf(bundle.getLong("totalTime")) + "分钟");
//            orderPopupWindow.consumeEnergy.setText(changeDouble(bundle.getDouble("calorie")) + "大卡");
//            orderPopupWindow.costCycling.setText(String.valueOf(bundle.getFloat("totalPrice")));
        }
//            locationMsg = intent.getStringExtra("newLoca");
//            RidingInfo ridingInfo = (RidingInfo) intent.getSerializableExtra("ridingInfo");
//            if (ridingInfo != null) {
//                double tripDist = changeDouble(ridingInfo.getTripDist());
//                double calorie = changeDouble(ridingInfo.getCalorie());
//                String dist = String.valueOf(tripDist*1000);
//                int i = stringToInt(dist);
//                String s = MapUtil.distanceFormatter(i);
//                orderPopupWindow.rideDistance.setText(s);
//                orderPopupWindow.rideTime.setText(String.valueOf(ridingInfo.getTripTime()) + "分钟");
//                orderPopupWindow.consumeEnergy.setText(String.valueOf(calorie) + "大卡");
//                orderPopupWindow.costCycling.setText(String.valueOf(ridingInfo.getRideCost()));
//            }
    }

    //double 类型保留一位小数
    public double changeDouble(Double dou) {
        NumberFormat nf = new DecimalFormat("0.0");
        dou = Double.parseDouble(nf.format(dou));
        return dou;
    }

    public int stringToInt(String string) {
        String str = string.substring(0, string.indexOf("."));
        int intgeo = Integer.parseInt(str);
        return intgeo;
    }

}
