package com.dcch.sharebike.moudle.user.activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.dcch.sharebike.R;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.http.Api;
import com.dcch.sharebike.moudle.home.bean.RoutePoint;
import com.dcch.sharebike.utils.JsonUtils;
import com.dcch.sharebike.utils.LogUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import okhttp3.Call;

public class JourneyDetailActivity extends BaseActivity {

    @BindView(R.id.journey_mapView)
    MapView mJourneyMapView;
    public ArrayList<RoutePoint> routePoints;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private BaiduMap mRouteBaiduMap;
    private BitmapDescriptor startBmp, endBmp;
    //    private MylocationListener mlistener;
    LocationClient mlocationClient;
    private List<LatLng> mPoints;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_journey_detail;
    }

    @Override
    protected void initData() {
        mToolbar.setTitle("");
        mTitle.setText(getResources().getString(R.string.journey_detail));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();
        if (intent != null) {
            String bicycleNo = intent.getStringExtra("bicycleNo");
            String carRentalOrderId = intent.getStringExtra("carRentalOrderId");
            String userId = intent.getStringExtra("userId");
            String token = intent.getStringExtra("token");
            checkTrip(bicycleNo, carRentalOrderId, userId,token);
        }

        mPoints = new ArrayList<>();
        mRouteBaiduMap = mJourneyMapView.getMap();
        mJourneyMapView.showZoomControls(false);
        startBmp = BitmapDescriptorFactory.fromResource(R.mipmap.route_start);
        endBmp = BitmapDescriptorFactory.fromResource(R.mipmap.route_end);
        initMap();
    }

    private void addOverLayout(LatLng startPosition, LatLng endPosition) {
        //先清除图层
//        mBaiduMap.clear();
        // 定义Maker坐标点
        // 构建MarkerOption，用于在地图上添加Marker
        MarkerOptions options = new MarkerOptions().position(startPosition)
                .icon(startBmp);
        // 在地图上添加Marker，并显示
        mRouteBaiduMap.addOverlay(options);
        MarkerOptions options2 = new MarkerOptions().position(endPosition)
                .icon(endBmp);
        // 在地图上添加Marker，并显示
        mRouteBaiduMap.addOverlay(options2);

    }

    private void initMap() {
        mlocationClient = new LocationClient(this);
//        mlistener = new MylocationListener();
//        mlocationClient.registerLocationListener(mlistener);

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
        if (!mlocationClient.isStarted()) {
            mlocationClient.start();
        }
        UiSettings settings = mRouteBaiduMap.getUiSettings();
        settings.setScrollGesturesEnabled(true);
    }


    public class MylocationListener implements BDLocationListener {
        //定位请求回调接口
        private boolean isFirstIn = true;

        //定位请求回调函数,这里面会得到定位信息
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            //判断是否为第一次定位,是的话需要定位到用户当前位置
            if (isFirstIn) {
                Log.d("gao", "onReceiveLocation----------RouteDetail-----" + bdLocation.getAddrStr());
//                LatLng currentLL = new LatLng(bdLocation.getLatitude(),
//                        bdLocation.getLongitude());
////                startNodeStr = PlanNode.withLocation(currentLL);
//                MapStatus.Builder builder = new MapStatus.Builder();
//                builder.target(currentLL).zoom(18.0f);
//                routeBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                isFirstIn = false;

            }
        }
    }

    public void onDestroy() {
        super.onDestroy();
        mRouteBaiduMap.setMyLocationEnabled(false);
        mlocationClient.stop();
    }

    private void checkTrip(String bicycleNo, String carRentalOrderId, String userId,String token) {
        Map<String, String> map = new HashMap<>();
        map.put("bicycleNo", bicycleNo);
        map.put("carRentalOrderId", carRentalOrderId);
        map.put("userId", userId);
        map.put("token",token);
        LogUtils.d("参数", bicycleNo + "\n" + carRentalOrderId + "\n" + userId);
        OkHttpUtils.post().url(Api.BASE_URL + Api.TRIPRECORD).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d("卡卡", response);
                if (JsonUtils.isSuccess(response)) {
                //{"resultStatus":"1","records":[{"lat":"39.977552","lng":"116.301934"},{"lat":"39.919141","lng":"116.508328"},{"lat":"39.949141","lng":"116.528328"},{"lat":"40.051023","lng":"116.308589"}]}
                    try {
                        JSONObject object = new JSONObject(response);
                        JSONArray records = object.getJSONArray("records");
                        routePoints = new ArrayList<>();
                        for (int i = 0; i < records.length(); i++) {
                            JSONObject jsonObject = records.getJSONObject(i);
                            RoutePoint routePoint = new RoutePoint();
                            routePoint.setRouteLat(jsonObject.optDouble("GPSY"));
                            routePoint.setRouteLng(jsonObject.optDouble("GPSX"));
//                            routePoint.setRouteLat(Double.valueOf(jsonObject.optString("lat")));
//                            routePoint.setRouteLng(Double.valueOf(jsonObject.optString("lng")));
                            routePoints.add(routePoint);
                        }
                        for (int i = 0; i < routePoints.size(); i++) {
                            RoutePoint point = routePoints.get(i);
                            LatLng latLng = new LatLng(point.getRouteLat(), point.getRouteLng());
                            Log.d("gao", "point.getRouteLat()----show-----" + point.getRouteLat());
                            Log.d("gao", "point.getRouteLng()----show-----" + point.getRouteLng());
                            mPoints.add(latLng);
                        }
                        if (mPoints.size() > 2) {
                            OverlayOptions ooPolyline = new PolylineOptions().width(10).color(0xFF36D19D).points(mPoints);
                            mRouteBaiduMap.addOverlay(ooPolyline);
                            RoutePoint startPoint = routePoints.get(0);
                            LatLng startPosition = new LatLng(startPoint.getRouteLat(), startPoint.getRouteLng());
                            MapStatus.Builder builder = new MapStatus.Builder();
                            builder.target(startPosition).zoom(18.0f);
                            mRouteBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                            RoutePoint endPoint = routePoints.get(routePoints.size() - 1);
                            LatLng endPosition = new LatLng(endPoint.getRouteLat(), endPoint.getRouteLng());
                            addOverLayout(startPosition, endPosition);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

}
