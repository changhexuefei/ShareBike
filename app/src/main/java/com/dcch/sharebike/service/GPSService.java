package com.dcch.sharebike.service;

import android.app.Notification;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.IBinder;
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
import com.dcch.sharebike.utils.ToastUtils;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

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
    DatabaseHelper mHelper;
    public double totalDistance = 0;
    private String mUserId;
    private String mBicycleNo;
    private String mCarRentalOrderDate;
    private String mCarRentalOrderId;
    private double mRouteLat;
    private double mRouteLng;
    Notification notification;
    RemoteViews contentView;
    public float totalPrice = 0;
    public long beginTime = 0, totalTime = 0;

    @Override
    public void onCreate() {
        super.onCreate();
//        initNotification();
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
            mUserId = intent.getStringExtra("userId");
            mBicycleNo = intent.getStringExtra("bicycleNo");
            mCarRentalOrderDate = intent.getStringExtra("carRentalOrderDate");
            mCarRentalOrderId = intent.getStringExtra("carRentalOrderId");
            Log.d("大神",mUserId+"\n"+mBicycleNo+"\n"+mCarRentalOrderDate+"\n"+mCarRentalOrderId);
            //开启子线程和后台进行通信

//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    LogUtils.d("进行中","记录经纬度");
//                    Map<String, String> map = new HashMap<String, String>();
//                    String url = Api.BASE_URL + Api.ORDERCAST;
//                    map.put("carRentalOrderDate", mCarRentalOrderDate);
//                    map.put("bicycleNo", mBicycleNo);
//                    map.put("carRentalOrderId", mCarRentalOrderId);
//                    map.put("userId", mUserId);
//                    map.put("lng", mRouteLng + "");
//                    map.put("lat", mRouteLat + "");
//                    map.put("mile", totalDistance / 1000 + "");
//
//                    OkHttpUtils.post().url(url).params(map).build().execute(new StringCallback() {
//                        @Override
//                        public void onError(Call call, Exception e, int id) {
//                            ToastUtils.showShort(App.getContext(), "服务正忙！");
//                        }
//
//                        @Override
//                        public void onResponse(String response, int id) {
//                            Log.d("给后台", response);
//                            if (JsonUtils.isSuccess(response)) {
//                                Gson gson = new Gson();
//                                RidingInfo ridingInfo = gson.fromJson(response, RidingInfo.class);
//                                Bundle bundle = new Bundle();
//                                bundle.putSerializable("ridingInfo", ridingInfo);
//                                if (bundle != null) {
////                                    sendToActivity(bundle);
//                                }
//                            }
//
//                        }
//                    });
//                }
//            }).start();
//            AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
//            int second = 30*1000;
//            long triggerAtTime = SystemClock.elapsedRealtime()+second;
//            Intent i = new Intent(this,MainActivity.class);
//            PendingIntent pi = PendingIntent.getBroadcast(this,0,i,0);
//            manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);
        }
        Log.d("BDGpsService", "********BDGpsService onStartCommand*******");
        if (locationClient != null && !locationClient.isStarted()) {
            locationClient.start();
        }
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("BDGpsService", "********BDGpsService onDestroy*******");
        if (locationClient != null && locationClient.isStarted()) {
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

    public class BDGpsServiceListener implements BDLocationListener {
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

//                Bundle bundle = new Bundle();
//                bundle.putLong("totalTime", totalTime);
//                bundle.putDouble("totalDistance", totalDistance);
//                bundle.putFloat("totalPrice", totalPrice);
//                bundle.putDouble("calorie", (totalDistance / 1000) * 29);
//                LogUtils.d("距离", totalDistance + "");
//                sendToActivity(bundle);

//                StringBuffer sb = new StringBuffer();
//                sb.append("经度=").append(location.getLongitude());
//                sb.append("\n纬度=").append(location.getLatitude());
//                sb.append("\n时间=").append(location.getTime());
//                sb.append("\nERR Code=").append(location.getLocType());
//                if (location.hasRadius()) {
//                    sb.append("\n定位精度=").append(location.getRadius());
//                }
//                if (location.getLocType() == BDLocation.TypeGpsLocation) {
//                    sb.append("\n速度=");
//                    sb.append(location.getSpeed());
//                    sb.append("\n卫星=");
//                    sb.append(location.getSatelliteNumber());
//                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
//                    sb.append("\n位置=").append(location.getAddrStr());
//                    sb.append("\n省=").append(location.getProvince());
//                    sb.append("\n市=").append(location.getCity());
//                    sb.append("\n区县=").append(location.getDistrict());
//                }
//                sendToActivity(sb.toString());

                Map<String, String> map = new HashMap<String, String>();
                String url = Api.BASE_URL + Api.ORDERCAST;
                map.put("carRentalOrderDate", mCarRentalOrderDate);

                map.put("bicycleNo", mBicycleNo);

                map.put("carRentalOrderId", mCarRentalOrderId);

                map.put("userId", mUserId);

                map.put("lng", mRouteLng + "");
                LogUtils.d("距离",mRouteLng + "");
                map.put("lat", mRouteLat + "");
                LogUtils.d("距离",mRouteLat + "");
                map.put("mile", totalDistance / 1000 + "");
                LogUtils.d("距离",totalDistance / 1000 + "");

                OkHttpUtils.post().url(url).params(map).build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showShort(App.getContext(), "服务正忙！");
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
        sqliteDatabase.close();
    }
}
