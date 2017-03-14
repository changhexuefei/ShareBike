package com.dcch.sharebike.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.dcch.sharebike.listener.BDGpsServiceListener;

/**
 * Created by gao on 2017/3/11.
 */

public class GPSService extends Service {
    private static final int minTime = 60000;
    private LocationClient locationClient;
    private BDGpsServiceListener locationListener;
    private LocationClientOption lco;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("BDGpsService", "********BDGpsService onCreate*******");
        lco = new LocationClientOption();
        lco.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        lco.setScanSpan(minTime);
        lco.setCoorType("bd09ll");
        lco.setOpenGps(true);
        lco.setIsNeedAddress(true);
        locationListener = new BDGpsServiceListener(getApplicationContext());
        locationClient = new LocationClient(getApplicationContext());
        locationClient.setLocOption(lco);
        locationClient.registerLocationListener(locationListener);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("BDGpsService", "********BDGpsService onStartCommand*******");
        if (locationClient != null && !locationClient.isStarted()) {
            locationClient.start();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("BDGpsService", "********BDGpsService onDestroy*******");
        if (locationClient != null && locationClient.isStarted()) {
            locationClient.stop();
        }
        locationClient.unRegisterLocationListener(locationListener);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
}
