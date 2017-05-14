package com.dcch.sharebike.service;


import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.dcch.sharebike.listener.MyOrientationListener;
import com.dcch.sharebike.moudle.home.bean.RoutePoint;
import com.google.gson.Gson;

import java.util.ArrayList;


/**
 * Created by Administrator on 2017/3/9 0009.
 */

public class RouteService extends Service {
    private double currentLatitude, currentLongitude;

    private LocationClient mlocationClient = null;
    private MylocationListener mlistener;
    private BitmapDescriptor mIconLocation;
    private MyOrientationListener myOrientationListener;
    private float mCurrentX;
    //定位图层显示方式
    private MyLocationConfiguration.LocationMode locationMode;
    //    AllInterface.IUpdateLocation iUpdateLocation;
    public ArrayList<RoutePoint> routPointList = new ArrayList<>();
    public int totalDistance = 0;
    public float totalPrice = 0;
    public long beginTime = 0, totalTime = 0;
    Notification notification;
    RemoteViews contentView;

//    public void setiUpdateLocation(AllInterface.IUpdateLocation iUpdateLocation) {
//        this.iUpdateLocation = iUpdateLocation;
//    }

    public void onCreate() {
        Log.d("gaolei", "RouteService--------onCreate-------------");
        super.onCreate();
        beginTime = System.currentTimeMillis();
//        RouteDBHelper dbHelper = new RouteDBHelper(this);
//        // 只有调用了DatabaseHelper的getWritableDatabase()方法或者getReadableDatabase()方法之后，才会创建或打开一个连接
//        SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();
        totalTime = 0;
        totalDistance = 0;
        totalPrice = 0;
        routPointList.clear();

    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("gaolei", "RouteService--------onStartCommand---------------");
        initLocation();//初始化LocationgClient
//        initNotification();
//        Utils.acquireWakeLock(this);
        // 开启轨迹记录线程
        return super.onStartCommand(intent, flags, startId);
    }

//    private void initNotification() {
//        int icon = R.mipmap.bike_icon2;
//        contentView = new RemoteViews(getPackageName(), R.layout.notification_layout);
//        notification = new NotificationCompat.Builder(this).setContent(contentView).setSmallIcon(icon).build();
//        Intent notificationIntent = new Intent(this, MainActivity.class);
//        notificationIntent.putExtra("flag", "notification");
//        notification.contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
//    }

    private void initLocation() {
//        mIconLocation = BitmapDescriptorFactory
//                .fromResource(R.mipmap.location_marker);
        locationMode = MyLocationConfiguration.LocationMode.NORMAL;

        //定位服务的客户端。宿主程序在客户端声明此类，并调用，目前只支持在主线程中启动
        mlocationClient = new LocationClient(this);
        mlistener = new MylocationListener();
//        initMarkerClickEvent();
        //注册监听器
        mlocationClient.registerLocationListener(mlistener);
        //配置定位SDK各配置参数，比如定位模式、定位时间间隔、坐标系类型等
        LocationClientOption mOption = new LocationClientOption();
        //设置坐标类型
        mOption.setCoorType("bd09ll");
        //设置是否需要地址信息，默认为无地址
        mOption.setIsNeedAddress(true);
        //设置是否打开gps进行定位
        mOption.setOpenGps(true);
        //设置扫描间隔，单位是毫秒 当<1000(1s)时，定时定位无效
        int span = 10000;
        mOption.setScanSpan(span);
        //设置 LocationClientOption
        mlocationClient.setLocOption(mOption);

//        //初始化图标,BitmapDescriptorFactory是bitmap 描述信息工厂类.
//        mIconLocation = BitmapDescriptorFactory
//                .fromResource(R.mipmap.location_marker);

        myOrientationListener = new MyOrientationListener(this);
        //通过接口回调来实现实时方向的改变
        myOrientationListener.setOnOrientationListener(new MyOrientationListener.OnOrientationListener() {
            @Override
            public void onOrientationChanged(float x) {
                mCurrentX = x;
            }
        });
//        mSearch = RoutePlanSearch.newInstance();
//        mSearch.setOnGetRoutePlanResultListener(this);
//        //开启定位
//        mBaiduMap.setMyLocationEnabled(true);
        if (!mlocationClient.isStarted()) {
            mlocationClient.start();
        }
        myOrientationListener.start();
    }

//    private void startNotifi(String time, String distance, String price) {
//        startForeground(1, notification);
//        contentView.setTextViewText(R.id.bike_time, time);
//        contentView.setTextViewText(R.id.bike_distance, distance);
//        contentView.setTextViewText(R.id.bike_price, price);
//    }


    public IBinder onBind(Intent intent) {
        Log.d("gaolei", "onBind-------------");
        return null;
    }

    public boolean onUnBind(Intent intent) {
        Log.d("gaolei", "onBind-------------");
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mlocationClient != null) {
            mlocationClient.unRegisterLocationListener(mlistener);
            mlocationClient.stop();
        }
        myOrientationListener.stop();
        Log.d("gaolei", "RouteService----0nDestroy---------------");
        Gson gson = new Gson();
        String routeListStr = gson.toJson(routPointList);
        Log.d("gaolei", "RouteService----routeListStr-------------" + routeListStr);
        Bundle bundle = new Bundle();
        bundle.putString("totalTime", totalTime + "");
        bundle.putString("totalDistance", totalDistance + "");
        bundle.putString("totalPrice", totalPrice + "");
        bundle.putString("routePoints", routeListStr);
//        Intent intent = new Intent(this, RouteDetailActivity.class);
//        intent.putExtras(bundle);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//        if (routPointList.size() > 2)
//            insertData(routeListStr);
//        Utils.releaseWakeLock();
        stopForeground(true);
    }


    //所有的定位信息都通过接口回调来实现
    private class MylocationListener implements BDLocationListener {
        //定位请求回调接口
        private boolean isFirstIn = true;

        //定位请求回调函数,这里面会得到定位信息
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (null == bdLocation) return;
            //"4.9E-324"表示目前所处的环境（室内或者是网络状况不佳）造成无法获取到经纬度
            if ("4.9E-324".equals(String.valueOf(bdLocation.getLatitude())) || "4.9E-324".equals(String.valueOf(bdLocation.getLongitude()))) {
                return;
            }//过滤百度定位失败

            Log.d("gaolei", "RouteService---------getAddrStr()-------------" + bdLocation.getAddrStr());
            double routeLat = bdLocation.getLatitude();
            double routeLng = bdLocation.getLongitude();
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
                    if (routeLat > 0 && routeLng > 0) {//经纬度都不能为0
                        double distantce = DistanceUtil.getDistance(lastLatLng, currentLatLng);
//                       大于5米才加入列表
                        if (distantce > 5) {
                            routPointList.add(routePoint);
                            totalDistance += distantce;
                        }
                    }
                }
            }

            totalTime = (int) (System.currentTimeMillis() - beginTime) / 1000 / 60;
            totalPrice = (float) (Math.floor(totalTime / 30) * 0.5 + 0.5);
//            Log.d("gaolei", "biginTime--------------" + beginTime);
            Log.d("gaolei", "totalTime--------------" + totalTime);
            Log.d("gaolei", "totalDistance--------------" + totalDistance);
//            startNotifi(totalTime + "分钟", totalDistance + "米", totalPrice + "元");
            Intent intent = new Intent("com.locationreceiver");
            Bundle bundle = new Bundle();
            bundle.putString("totalTime", totalTime + "分钟");
            bundle.putString("totalDistance", totalDistance + "米");
            bundle.putString("totalPrice", totalPrice + "元");
            intent.putExtras(bundle);
            sendBroadcast(intent);
        }
    }

    public static class NetWorkReceiver extends BroadcastReceiver {
        public NetWorkReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            NetworkInfo.State wifiState;
            NetworkInfo.State mobileState;
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            wifiState = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
            mobileState = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
            if (wifiState != null && mobileState != null
                    && NetworkInfo.State.CONNECTED != wifiState
                    && NetworkInfo.State.CONNECTED == mobileState) {
//                Toast.makeText(context, context.getString(R.string.net_mobile), Toast.LENGTH_SHORT).show();
                // 手机网络连接成功
            } else if (wifiState != null && mobileState != null
                    && NetworkInfo.State.CONNECTED != wifiState
                    && NetworkInfo.State.CONNECTED != mobileState) {
//                Toast.makeText(context, context.getString(R.string.net_none), Toast.LENGTH_SHORT).show();

                // 手机没有任何的网络
            } else if (wifiState != null && NetworkInfo.State.CONNECTED == wifiState) {
                // 无线网络连接成功
//                Toast.makeText(context, context.getString(R.string.net_wifi), Toast.LENGTH_SHORT).show();

            }
        }
    }

//    public void insertData(String routeListStr) {
//        ContentValues values = new ContentValues();
//        // 向该对象中插入键值对，其中键是列名，值是希望插入到这一列的值，值必须和数据当中的数据类型一致
//        values.put("cycle_date", Utils.getDateFromMillisecond(beginTime));
//        values.put("cycle_time", totalTime);
//        values.put("cycle_distance", totalDistance);
//        values.put("cycle_price", totalPrice);
//        values.put("cycle_points", routeListStr);
//        // 创建DatabaseHelper对象
//        RouteDBHelper dbHelper = new RouteDBHelper(this);
//        // 得到一个可写的SQLiteDatabase对象
//        SQLiteDatabase sqliteDatabase = dbHelper.getWritableDatabase();
//        // 调用insert方法，就可以将数据插入到数据库当中
//        // 第一个参数:表名称
//        // 第二个参数：SQl不允许一个空列，如果ContentValues是空的，那么这一列被明确的指明为NULL值
//        // 第三个参数：ContentValues对象
//        sqliteDatabase.insert("cycle_route", null, values);
//        sqliteDatabase.close();
//    }
}
