package com.dcch.sharebike.listener;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.route.PlanNode;

/**
 * Created by Administrator on 2017/2/7 0007.
 */

public class MyLocationListener implements BDLocationListener {

    private boolean isFirstLoc = true;
    private MapView mMapView;
    private BaiduMap mBaiduMap = mMapView.getMap();
    private int mXDirection;



    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        // map view 销毁后不在处理新接收的位置
        if (bdLocation == null || mMapView == null) {
            return;
        }
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(bdLocation.getRadius())
                .direction(mXDirection)//设定图标方向     // 此处设置开发者获取到的方向信息，顺时针0-360
                .latitude(bdLocation.getLatitude())
                .longitude(bdLocation.getLongitude()).build();
        mBaiduMap.setMyLocationData(locData);
       Double currentLatitude = bdLocation.getLatitude();
        Double currentLongitude = bdLocation.getLongitude();
//        current_addr.setText(bdLocation.getAddrStr());
      LatLng currentLL = new LatLng(bdLocation.getLatitude(),
                bdLocation.getLongitude());
        PlanNode startNodeStr = PlanNode.withLocation(currentLL);
        //option.setScanSpan(5000)，每隔5000ms这个方法就会调用一次，而有些我们只想调用一次，所以要判断一下isFirstLoc
        if (isFirstLoc) {
            isFirstLoc = false;
            LatLng ll = new LatLng(bdLocation.getLatitude(),
                    bdLocation.getLongitude());
            MapStatus.Builder builder = new MapStatus.Builder();
            //地图缩放比设置为18
            builder.target(ll).zoom(18.0f);
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
          Double  changeLatitude = bdLocation.getLatitude();
            Double changeLongitude = bdLocation.getLongitude();
//            if (!isServiceLive) {
//                addOverLayout(currentLatitude, currentLongitude);
//            }
        }
    }
}
