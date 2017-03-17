package com.dcch.sharebike.listener;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.dcch.sharebike.app.App;
import com.dcch.sharebike.db.DatabaseHelper;
import com.dcch.sharebike.http.Api;
import com.dcch.sharebike.moudle.home.bean.RoutePoint;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;


/**
 * Created by gao on 2017/3/10.
 */

public class BDGpsServiceListener implements BDLocationListener {

    private Context context;
    public ArrayList<RoutePoint> routPointList = new ArrayList<RoutePoint>();
    private RoutePoint mPoint;
    DatabaseHelper mHelper ;
    public  int totalDistance = 0;
    public  float totalPrice = 0;
    public  long beginTime = 0, totalTime = 0;

    public BDGpsServiceListener(){
        super();
    }
    public BDGpsServiceListener(Context context){
        super();
        this.context = context;
    }

    //发送广播，提示更新界面
    private void sendToActivity(String str){
        Log.d("hhhh",str);
        Intent intent = new Intent();
        intent.putExtra("newLoca", str);
        intent.setAction("NEW LOCATION SENT");
        context.sendBroadcast(intent);
    }
    @Override
    public void onReceiveLocation(BDLocation location) {
        mPoint = new RoutePoint();
        mHelper= new DatabaseHelper(App.getContext());
        // TODO Auto-generated method stub
        Log.i("Listener", "********BDGpsServiceListener onReceiveLocation*******");
        if(location == null){return;}
        //过滤百度定位失败
        if ("4.9E-324".equals(String.valueOf(location.getLatitude())) || "4.9E-324".equals(String.valueOf(location.getLongitude()))) {
            return;
        }

        StringBuffer sb = new StringBuffer();
        sb.append("经度=").append(location.getLongitude());
        sb.append("\n纬度=").append(location.getLatitude());
        sb.append("\n时间=").append(location.getTime());
        sb.append("\nERR Code=").append(location.getLocType());
        if (location.hasRadius()){
            sb.append("\n定位精度=").append(location.getRadius());
        }
        if (location.getLocType() == BDLocation.TypeGpsLocation){
            sb.append("\n速度=");
            sb.append(location.getSpeed());
            sb.append("\n卫星=");
            sb.append(location.getSatelliteNumber());
        } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
            sb.append("\n位置=").append(location.getAddrStr());
            sb.append("\n省=").append(location.getProvince());
            sb.append("\n市=").append(location.getCity());
            sb.append("\n区县=").append(location.getDistrict());
        }
        mPoint.setRouteLng(location.getLongitude());
        mPoint.setRouteLat(location.getLatitude());
        mPoint.setDistance(0);
        Log.d("数据库",mPoint.getRouteLat()+"\n"+mPoint.getId()+"\n"+mPoint.getRouteLng());
        mHelper.insert(mPoint);
        sendToActivity(sb.toString());

        try {
            Map<String,String> map = new HashMap<String, String>();
            String url = Api.BASE_URL+Api.ORDERCAST;
            map.put("carRentalOrderDate","2017-03-17 14:50");
            map.put("bicycleNo","1000800020");
            map.put("carRentalOrderId","");
            map.put("userId","24");
            map.put("lng",location.getLongitude()+"");
            map.put("lat", location.getLatitude()+"");
            OkHttpUtils.post().url(url).params(map).build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {

                }

                @Override
                public void onResponse(String response, int id) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
