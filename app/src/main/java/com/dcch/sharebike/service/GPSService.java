package com.dcch.sharebike.service;

import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.util.Log;

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
import com.dcch.sharebike.moudle.home.bean.RoutePoint;
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
    private static final int minTime = 30000;
    private LocationClient locationClient;
    private BDGpsServiceListener locationListener;
    private LocationClientOption lco;
    private MyOrientationListener myOrientationListener;
    private Context context;
    public ArrayList<RoutePoint> routPointList = new ArrayList<RoutePoint>();
    DatabaseHelper mHelper;
    public int totalDistance = 0;
    private String mUserId;
    private String mBicycleNo;
    private String mCarRentalOrderDate;
    private String mCarRentalOrderId;

    @Override
    public void onCreate() {
        super.onCreate();
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

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            mUserId = intent.getStringExtra("userId");
            Log.d("我是",mUserId);

            mBicycleNo = intent.getStringExtra("bicycleNo");
            Log.d("我是",mBicycleNo);
            mCarRentalOrderDate = intent.getStringExtra("carRentalOrderDate");
            Log.d("我是",mCarRentalOrderDate);
            mCarRentalOrderId = intent.getStringExtra("carRentalOrderId");
            Log.d("我是",mCarRentalOrderId);
        }
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
            Gson gson = new Gson();
            String routeListStr = gson.toJson(routPointList);
            Log.d("gao", "RouteService----routeListStr-------------" + routeListStr);
            if (routPointList.size() > 2) {
                insertData(routeListStr);
            }
        }
        locationClient.unRegisterLocationListener(locationListener);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    public class BDGpsServiceListener implements BDLocationListener {
        //发送广播，提示更新界面
        private void sendToActivity(String str) {
            Log.d("hhhh", str);
            Intent intent = new Intent();
            intent.putExtra("newLoca", str);
            intent.setAction("NEW LOCATION SENT");
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

            double routeLat = location.getLatitude();
            double routeLng = location.getLongitude();
            RoutePoint routePoint = new RoutePoint();
            routePoint.setRouteLat(routeLat);
            routePoint.setRouteLng(routeLng);
            if (routPointList.size() == 0)
                routPointList.add(routePoint);
            else {
                RoutePoint lastPoint = routPointList.get(routPointList.size() - 1);
                if (routeLat == lastPoint.getRouteLat() && routeLng == lastPoint.getRouteLng()) {

                } else {
                    LatLng lastLatLng = new LatLng(lastPoint.getRouteLat(),
                            lastPoint.getRouteLng());
                    LatLng currentLatLng = new LatLng(routeLat, routeLng);
                    if (routeLat > 0 && routeLng > 0) {
                        double distantce = DistanceUtil.getDistance(lastLatLng, currentLatLng);
                        Log.d("gao", "distantce--------------" + distantce);
                        if (distantce > 5) {
                            routPointList.add(routePoint);
                            totalDistance += distantce;
                        }
                    }
                }
                StringBuffer sb = new StringBuffer();
                sb.append("经度=").append(location.getLongitude());
                sb.append("\n纬度=").append(location.getLatitude());
                sb.append("\n时间=").append(location.getTime());
                sb.append("\nERR Code=").append(location.getLocType());
                if (location.hasRadius()) {
                    sb.append("\n定位精度=").append(location.getRadius());
                }
                if (location.getLocType() == BDLocation.TypeGpsLocation) {
                    sb.append("\n速度=");
                    sb.append(location.getSpeed());
                    sb.append("\n卫星=");
                    sb.append(location.getSatelliteNumber());
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                    sb.append("\n位置=").append(location.getAddrStr());
                    sb.append("\n省=").append(location.getProvince());
                    sb.append("\n市=").append(location.getCity());
                    sb.append("\n区县=").append(location.getDistrict());
                }

                sendToActivity(sb.toString());

                try {
                    Map<String, String> map = new HashMap<String, String>();
                    String url = Api.BASE_URL + Api.ORDERCAST;
                    map.put("carRentalOrderDate", mCarRentalOrderDate);
                    map.put("bicycleNo", mBicycleNo);
                    map.put("carRentalOrderId", mCarRentalOrderId);
                    map.put("userId", mUserId);
                    map.put("lng", location.getLongitude() + "");
                    map.put("lat", location.getLatitude() + "");
                    map.put("mile", totalDistance + "");
                    Log.d("历程", totalDistance + "");
                    OkHttpUtils.post().url(url).params(map).build().execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Log.d("给后台", response);

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void insertData(String routeListStr) {
        ContentValues values = new ContentValues();
        // 向该对象中插入键值对，其中键是列名，值是希望插入到这一列的值，值必须和数据当中的数据类型一致
        values.put("cycle_points", routeListStr);
        values.put("cycle_distance", totalDistance);
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
