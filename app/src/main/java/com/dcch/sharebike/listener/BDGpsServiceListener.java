package com.dcch.sharebike.listener;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.dcch.sharebike.http.Api;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by gao on 2017/3/10.
 */

public class BDGpsServiceListener implements BDLocationListener {

    private Context context;

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
        // TODO Auto-generated method stub
        Log.i("Listener", "********BDGpsServiceListener onReceiveLocation*******");
        if(location == null){return;}
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

        sendToActivity(sb.toString());

        try {
            Map<String,String> map = new HashMap<String, String>();
            map.put("longi", location.getLongitude()+"");
            map.put("lati", location.getLatitude()+"");
            map.put("time", location.getTime());
            String url = Api.BASE_URL+"coords.do?method=addCoords";
//            HttpUtil.postRequest(url, map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
