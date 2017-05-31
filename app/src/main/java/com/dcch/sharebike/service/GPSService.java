package com.dcch.sharebike.service;

import android.app.Notification;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.RemoteViews;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.dcch.sharebike.app.App;
import com.dcch.sharebike.db.DatabaseHelper;
import com.dcch.sharebike.http.Api;
import com.dcch.sharebike.listener.MyOrientationListener;
import com.dcch.sharebike.moudle.home.bean.RidingInfo;
import com.dcch.sharebike.moudle.home.bean.RoutePoint;
import com.dcch.sharebike.utils.JsonUtils;
import com.dcch.sharebike.utils.LogUtils;
import com.dcch.sharebike.utils.MapUtil;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

import static cn.jiguang.api.utils.ByteBufferUtils.ERROR_CODE;

/**
 * Created by gao on 2017/3/11.
 */

public class GPSService extends Service {
    private static final int minTime = 5000;
    private LocationClient locationClient;
    private BDGpsServiceListener locationListener;
    private LocationClientOption lco;
    private MyOrientationListener myOrientationListener;
    private Context context;
    public ArrayList<RoutePoint> routPointList = new ArrayList<RoutePoint>();
    private final String ROUTE_PATH = "/sdcard/Route/"; //"Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Route/""
    DatabaseHelper mHelper;
    public double totalDistance = 0;
    private String mUserId;
    private String mBicycleNo;
    private String mCarRentalOrderDate;
    private String mCarRentalOrderId;
    private double mRouteLat;
    private double mRouteLng;
    private int startId = 1; // 轨迹点初始ID
    Notification notification;
    RemoteViews contentView;
    private String startTime = "";
    private String stopTime = "";
    private double routeLng;
    private double routeLat;
//    private RouteAdapter adapter = new RouteAdapter();
    public float totalPrice = 0;
    public long beginTime = 0, totalTime = 0;

    private boolean isEncrypt = false; // true:读取百度加密经纬度 false:读取设备提供经纬度
    private boolean isDebug = true;
    private LocationManager mLocationManager;
    private String mToken;

    // 设备定位经纬度
    private enum DeviceLocType {
        LATITUDE, LONGITUDE
    }


    @Override
    public void onCreate() {
        super.onCreate();
//        initNotification();
        // 初始化路径
//        File filestoreMusic = new File(ROUTE_PATH);
//        LogUtils.d("路径",ROUTE_PATH);
//        if (!filestoreMusic.exists()) {
//            filestoreMusic.mkdir();
//        }
//        startTime = getTimeStr();
//        if (isDebug) {
//            Toast.makeText(getApplicationContext(), "Start Record Route",
//                    Toast.LENGTH_SHORT).show();
//        }
        beginTime = System.currentTimeMillis();
        totalTime = 0;
        totalDistance = 0;
        totalPrice = 0;
        routPointList.clear();
        Log.i("BDGpsService", "********BDGpsService onCreate*******");
        lco = new LocationClientOption();
        lco.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        lco.setScanSpan(minTime);
        lco.setCoorType("bd09ll");
        lco.setOpenGps(true);
        lco.setIsNeedAddress(true);
        locationListener = new BDGpsServiceListener();
        locationClient = new LocationClient(getApplicationContext());
        locationClient.setLocOption(lco);
        locationClient.registerLocationListener(locationListener);
    }

//    private void initNotification() {
//        int icon = R.mipmap.bike_info_board_location;
//        contentView = new RemoteViews(getPackageName(), R.layout.item_riding_order);
//        notification = new NotificationCompat.Builder(this).setContent(contentView).setSmallIcon(icon).build();
//        Intent notificationIntent = new Intent(this, MainActivity.class);
//        notificationIntent.putExtra("flag", "notification");
//        notification.contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
//    }


//    private void startNotifi(String time, String distance, String price) {
//        startForeground(1, notification);
//        contentView.setTextViewText(R.id.ride_time, time);
//        contentView.setTextViewText(R.id.ride_distance, distance);
//        contentView.setTextViewText(R.id.cost_cycling, price);
//    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            mToken = intent.getStringExtra("token");
            mUserId = intent.getStringExtra("userId");
            mBicycleNo = intent.getStringExtra("bicycleNo");
            mCarRentalOrderDate = intent.getStringExtra("carRentalOrderDate");
            mCarRentalOrderId = intent.getStringExtra("carRentalOrderId");
            Log.d("大神", mUserId + "\n" + mBicycleNo + "\n" + mCarRentalOrderDate + "\n" + mCarRentalOrderId);
            // 开启轨迹记录线程
//            new Thread(new RouteRecordThread()).start();
        }
        Log.d("BDGpsService", "********BDGpsService onStartCommand*******");
        if (locationClient != null && !locationClient.isStarted()) {
            locationClient.start();
        }
        return START_REDELIVER_INTENT;
    }

    public class RouteRecordThread implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(minTime);
                    Message message = new Message();
                    message.what = 1;
                    recordHandler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    final Handler recordHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
//                    startRecordRoute();
            }
            super.handleMessage(msg);
        }
    };

//    private void startRecordRoute() {
//        // 获取设备经纬度
//        if (!isEncrypt) {
//            routeLat = getDeviceLocation(DeviceLocType.LATITUDE);
//            routeLng = getDeviceLocation(DeviceLocType.LONGITUDE);
//            if (isDebug)
//                Toast.makeText(getApplicationContext(),
//                        "Device Loc:" + routeLat + "," + routeLng,
//                        Toast.LENGTH_SHORT).show();
//        }
//
//        RoutePoint routePoint = new RoutePoint();
//        if (routeLng != 5.55 && routeLat != 5.55) {
//            if (routPointList.size() > 0
//                    && routPointList.get(routPointList.size() - 1).getRouteLat() == routeLat
//                    && (routPointList.get(routPointList.size() - 1).getRouteLng() == routeLng)) {
//                if (isDebug) {
//                     Toast.makeText(getApplicationContext(),
//                            "Route not change",
//                            Toast.LENGTH_SHORT).show();
//                }
//            } else {
//                routePoint.setId(startId++);
//                routePoint.setRouteLng(routeLng);
//                routePoint.setRouteLat(routeLat);
//                routPointList.add(routePoint);
//            }
//        }
//    }

// *获取设备提供的经纬度，Network或GPS

    private double getDeviceLocation(DeviceLocType type) {
        double deviceLat = ERROR_CODE;
        double deviceLng = ERROR_CODE;

        mLocationManager = (LocationManager) getSystemService(getApplicationContext().LOCATION_SERVICE);
        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                deviceLat = location.getLatitude();
                deviceLng = location.getLongitude();
            } else {
                mLocationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER, 1000, 0, new deviceLocationListener());
                Location location1 = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (location1 != null) {
                    deviceLat = location1.getLatitude(); // 经度
                    deviceLng = location1.getLongitude(); // 纬度
                }
            }
        } else {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 1000, 0, new deviceLocationListener());

            Location location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                deviceLat = location.getLatitude(); // 经度
                deviceLng = location.getLongitude(); // 纬度
            }
        }
        if (type == DeviceLocType.LATITUDE)
            return deviceLat;
        else if (type == DeviceLocType.LONGITUDE)
            return deviceLng;
        else
            return ERROR_CODE;
    }


      //* 设备位置监听器
      private class deviceLocationListener implements LocationListener {

        // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        // Provider被enable时触发此函数，比如GPS被打开
        @Override
        public void onProviderEnabled(String provider) {

        }

        // Provider被disable时触发此函数，比如GPS被关闭
        @Override
        public void onProviderDisabled(String provider) {
        }

        // 当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
        @Override
        public void onLocationChanged(Location location) {
            // routeLat = location.getLatitude(); // 经度
            // routeLng = location.getLongitude(); // 纬度
        }
    }

    private String getTimeStr() {
        long nowTime = System.currentTimeMillis();
        Date date = new Date(nowTime);
        String strs = "" + ERROR_CODE;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            strs = sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strs;
    }


     // 初始化轨迹文件路径和名称
    private String getFilePath() {
        stopTime = getTimeStr();
        String format = ".json";
        if (isDebug)
            format = ".txt";
        return ROUTE_PATH + startTime + "-" + stopTime + format;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        String filePath = getFilePath();
        Log.d("BDGpsService", "********BDGpsService onDestroy*******");
        Log.d("BDGpsService", "filePath");
        if (locationClient != null && locationClient.isStarted()) {
            locationClient.unRegisterLocationListener(locationListener);
            locationClient.stop();
            Gson gson = new Gson();
            String routeListStr = gson.toJson(routPointList);
            Log.d("gao", "RouteService----routeListStr-------------" + routeListStr);
            if (routPointList.size() > 2) {
                insertData(routeListStr);
            }
        }
//        stopForeground(true);
        locationClient.unRegisterLocationListener(locationListener);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    private class BDGpsServiceListener implements BDLocationListener {
        //发送广播，提示更新界面
        private void sendToActivity(Bundle bundle) {
            Log.d("hhhh", bundle + "");
            Intent intent = new Intent();
            intent.putExtras(bundle);
            intent.setAction("NEW LOCATION SENT");
            sendBroadcast(intent);
        }

        //发送广播，提示跳转到结束界面
        private void sendToRidingResult(Bundle bundle) {
            Log.d("AAAAA", bundle + "");
            Intent intent = new Intent();
            intent.putExtras(bundle);
            intent.setAction("RESULT SENT");
            sendBroadcast(intent);
        }

        @Override
        public void onReceiveLocation(BDLocation location) {
            mHelper = new DatabaseHelper(App.getContext());
            Log.i("Listener", "********BDGpsServiceListener onReceiveLocation*******");
            if (location == null) {
                return;
            }
            //过滤百度定位失败
            if ("4.9E-324".equals(String.valueOf(location.getLatitude())) || "4.9E-324".equals(String.valueOf(location.getLongitude()))) {
                return;
            }
            mRouteLat = location.getLatitude();
            mRouteLng = location.getLongitude();
            RoutePoint routePoint = new RoutePoint();
            routePoint.setRouteLat(mRouteLat);
            routePoint.setRouteLng(mRouteLng);
            if (routPointList.size() == 0) {
                routPointList.add(routePoint);
                Log.d("看看", routPointList.size() + "");
            } else {
                RoutePoint lastPoint = routPointList.get(routPointList.size() - 1);
                if (mRouteLat == lastPoint.getRouteLat() && mRouteLng == lastPoint.getRouteLng()) {

                } else {
                    LatLng lastLatLng = new LatLng(lastPoint.getRouteLat(),
                            lastPoint.getRouteLng());
                    LatLng currentLatLng = new LatLng(mRouteLat, mRouteLng);
                    if (mRouteLat > 0 && mRouteLng > 0) {
                        double distantce = DistanceUtil.getDistance(lastLatLng, currentLatLng);
                        Log.d("gao", "distantce--------------" + distantce);
                        if (distantce > 5) {
                            routPointList.add(routePoint);
                            totalDistance += distantce;
                        }
                    }
                }
                totalTime = (int) (System.currentTimeMillis() - beginTime) / 1000 / 60;
                totalPrice = (float) (Math.floor(totalTime / 30) * 0.5 + 0.5);
//                startNotifi(totalTime + "分钟", totalDistance + "米", totalPrice + "元");
//                sendToActivity(sb.toString());

                Map<String, String> map = new HashMap<>();
                map.put("token",mToken);
                map.put("carRentalOrderDate", mCarRentalOrderDate);
                map.put("bicycleNo", mBicycleNo);
                map.put("carRentalOrderId", mCarRentalOrderId);
                map.put("userId", mUserId);
                map.put("lng", mRouteLng + "");
                map.put("lat", mRouteLat + "");
                map.put("mile", totalDistance / 1000 + "");
                LogUtils.d("看看数据",mToken+"\n"+mCarRentalOrderDate+"\n"+mBicycleNo+"\n"+mCarRentalOrderId+"\n"+mUserId);

                OkHttpUtils.post().url(Api.BASE_URL + Api.ORDERCAST).params(map).build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
//                        LogUtils.d("后台",e.getMessage());
//                        ToastUtils.showShort(App.getContext(), "服务正忙！");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.d("给后台", response);
                        if (JsonUtils.isSuccess(response)) {
                                    Gson gson = new Gson();
                                    RidingInfo ridingInfo = gson.fromJson(response, RidingInfo.class);
                                    if(ridingInfo.getStatus()==0){
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("ridingInfo", ridingInfo);
                                        if (bundle != null) {
                                            sendToActivity(bundle);
                                        }
                                    } else if(ridingInfo.getStatus()==1){
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("ridingInfo", ridingInfo);
                                        sendToRidingResult(bundle);
                                        stopSelf();
                                }
                        }
                    }
                });
            }
        }
    }

    public void insertData(String routeListStr) {
        ContentValues values = new ContentValues();
        // 向该对象中插入键值对，其中键是列名，值是希望插入到这一列的值，值必须和数据当中的数据类型一致
        values.put("cycle_date", MapUtil.getDateFromMillisecond(beginTime));
        values.put("cycle_time", totalTime);
        values.put("cycle_distance", totalDistance);
        values.put("cycle_price", totalPrice);
        values.put("cycle_points", routeListStr);
        // 创建DatabaseHelper对象
        DatabaseHelper dbHelper = new DatabaseHelper(App.getContext());
        // 得到一个可写的SQLiteDatabase对象
        SQLiteDatabase sqliteDatabase = dbHelper.getWritableDatabase();
        // 调用insert方法，就可以将数据插入到数据库当中
        // 第一个参数:表名称
        // 第二个参数：SQl不允许一个空列，如果ContentValues是空的，那么这一列被明确的指明为NULL值
        // 第三个参数：ContentValues对象
        sqliteDatabase.insert("routePoint", null, values);
        Cursor cursor = sqliteDatabase.query("routePoint", null, null, null, null, null, null);
        while (cursor.moveToNext()){
            String route_id = cursor.getString(cursor.getColumnIndex("route_id"));
            String cycle_date = cursor.getString(cursor.getColumnIndex("cycle_date"));
            String cycle_time = cursor.getString(cursor.getColumnIndex("cycle_time"));
            String cycle_distance = cursor.getString(cursor.getColumnIndex("cycle_distance"));
            String cycle_price = cursor.getString(cursor.getColumnIndex("cycle_price"));
            String cycle_points = cursor.getString(cursor.getColumnIndex("cycle_points"));
            LogUtils.d("数据库",route_id+"\n"+cycle_date+"\n"+cycle_time+"\n"+cycle_distance+"\n"+cycle_price+"\n"+cycle_points);
        }
        cursor.close();
        sqliteDatabase.close();
    }
}
