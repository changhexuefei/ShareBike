package com.dcch.sharebike;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.dcch.sharebike.app.App;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.listener.MyOrientationListener;
import com.dcch.sharebike.moudle.home.bean.MarkerInfoUtil;
import com.dcch.sharebike.moudle.login.activity.PersonalCenterActivity;
import com.dcch.sharebike.moudle.search.activity.SeekActivity;
import com.dcch.sharebike.overlayutil.OverlayManager;
import com.dcch.sharebike.overlayutil.WalkingRouteOverlay;
import com.dcch.sharebike.utils.ToastUtils;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.bottomsheet.BottomSheetBean;
import com.hss01248.dialog.interfaces.MyItemDialogListener;
import com.xys.libzxing.zxing.activity.CaptureActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements OnGetGeoCoderResultListener {
    @BindView(R.id.mapView)
    MapView mMapView;
    BaiduMap mMap;
    /**
     * 定位的监听器
     */
    public MyLocationListener mMyLocationListener;

    @BindView(R.id.MyCenter)
    ImageView mMyCenter;
    @BindView(R.id.seek)
    ImageView mSeek;
    @BindView(R.id.btn_my_location)
    ImageButton mBtnMyLocation;
    @BindView(R.id.instructions)
    Button mInstructions;
    @BindView(R.id.btn_my_help)
    ImageButton mBtnMyHelp;
    @BindView(R.id.scan)
    TextView mScan;
    @BindView(R.id.top)
    FrameLayout top;
    @BindView(R.id.allBike)
    RadioButton allBike;
    @BindView(R.id.one)
    RadioButton one;
    @BindView(R.id.two)
    RadioButton two;
    @BindView(R.id.classify)
    RadioGroup classify;
    private final int SDK_PERMISSION_REQUEST = 127;
    private List<MarkerInfoUtil> infos;
    //显示marker
    private boolean showMarker = false;
    private String permissionInfo;
    boolean useDefaultIcon = false;
    /**
     * 该类提供一个能够显示和管理多个Overlay的基类
     */
    OverlayManager routeOverlay = null;

    /**
     * 定位的客户端
     */
    private LocationClient mLocationClient;

    /**
     * 当前定位的模式
     */
    private MyLocationConfiguration.LocationMode mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
    /***
     * 是否是第一次定位
     */
    private volatile boolean isFristLocation = true;
    /**
     * 最新一次的经纬度
     */
    private double mCurrentLantitude;
    private double mCurrentLongitude;
    /**
     * 当前的精度
     */
    private float mCurrentAccracy;
    /**
     * 方向传感器的监听器
     */
    private MyOrientationListener myOrientationListener;
    /**
     * 方向传感器X方向的值
     */
    private int mXDirection;
    private GeoCoder mSearch = null;
    private Marker mMarker;
    private double latitude;
    private double longitude;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        classify.check(R.id.allBike);
        ButterKnife.bind(this);
        // 初始化GeoCoder模块，注册事件监听
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);
        mMap = mMapView.getMap();
        mMapView.showZoomControls(false);
        //隐藏logo和缩放图标
        View child = mMapView.getChildAt(1);
        if (child != null && (child instanceof ImageView || child instanceof ZoomControls)) {
            child.setVisibility(View.INVISIBLE);
        }
        //隐藏地图上比例尺
        mMapView.showScaleControl(false);
        // 初始化定位
        initMyLocation();
        // 初始化传感器
        initOritationListener();
//        setMarkerInfo();
        clickBaiduMapMark();
        getPersimmions();


    }

    private void getPersimmions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<String>();
            /***
             * 定位权限为必须权限，用户如果禁止，则每次进入都会申请
             */
            // 定位精确位置
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            /*
             * 读写权限和电话状态权限非必要权限(建议授予)只会申请一次，用户同意或者禁止，只会弹一次
			 */
            // 读写权限
            if (addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissionInfo += "Manifest.permission.WRITE_EXTERNAL_STORAGE Deny \n";
            }
            // 读取电话状态权限
            if (addPermission(permissions, Manifest.permission.READ_PHONE_STATE)) {
                permissionInfo += "Manifest.permission.READ_PHONE_STATE Deny \n";
            }

            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION_REQUEST);
            }
        }
    }

    private boolean addPermission(ArrayList<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) { // 如果应用没有获得对应权限,则添加到列表中,准备批量申请
            if (shouldShowRequestPermissionRationale(permission)) {
                return true;
            } else {
                permissionsList.add(permission);
                return false;
            }

        } else {
            return true;
        }
    }

    public void reverseGeoCoder(LatLng latlng) {
        //反向地理编码的功能
        mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(latlng));
    }


    private void initOritationListener() {
        myOrientationListener = new MyOrientationListener(
                getApplicationContext());
        myOrientationListener.setOnOrientationListener(new MyOrientationListener.OnOrientationListener() {
            @Override
            public void onOrientationChanged(float x) {
                mXDirection = (int) x;
                // 构造定位数据
                MyLocationData locData = new MyLocationData.Builder()
                        .accuracy(mCurrentAccracy)
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(mXDirection)
                        .latitude(mCurrentLantitude)
                        .longitude(mCurrentLongitude)
                        .build();
                // 设置定位数据
                mMap.setMyLocationData(locData);
//                 设置自定义图标
//                BitmapDescriptor mCurrentMarker =
//                        fromResource(R.mipmap.search_center_ic);
                MyLocationConfiguration config = new MyLocationConfiguration(
                        mCurrentMode, true, null);
                mMap.setMyLocationConfigeration(config);
            }
        });
    }

    private void initMyLocation() {
        // 定位初始化
        mLocationClient = new LocationClient(this);
        // 开启定位图层
        mMap.setMyLocationEnabled(true);
        mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);
        // 设置定位的相关配置
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setIsNeedLocationDescribe(true);//可选，设置是否需要地址描述
        option.setNeedDeviceDirect(false);//可选，设置是否需要设备方向结果
        option.setLocationNotify(false);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        mLocationClient.setLocOption(option);
    }


    @OnClick({R.id.MyCenter, R.id.seek, R.id.btn_my_location, R.id.instructions, R.id.btn_my_help, R.id.scan, R.id.allBike, R.id.one, R.id.two})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.MyCenter:
                ToastUtils.showLong(this, "我是个人中心");
                Intent i = new Intent(this, PersonalCenterActivity.class);
                i.putExtra("name", "login");
                startActivity(i);
                break;
            case R.id.seek:
                ToastUtils.showLong(this, "我是搜索");
                Intent i1 = new Intent(this, SeekActivity.class);
//                Log.d("11111",resultAddress);
//                i1.putExtra("address",resultAddress);
                startActivity(i1);
                break;
            case R.id.btn_my_location:
                //点击定位按钮，回到当前的位置
                ToastUtils.showLong(this, "我是位置");
                MyLocationData data = new MyLocationData.Builder()
                        .accuracy(1000)//范围半径，单位：米
                        .latitude(mCurrentLantitude)//
                        .longitude(mCurrentLongitude).build();
                mMap.setMyLocationData(data);
                setUserMapCenter();

                break;
            case R.id.instructions:
                ToastUtils.showLong(this, "我是说明");
                Intent i2 = new Intent(this, PersonalCenterActivity.class);
                i2.putExtra("name", "unLogin");
                startActivity(i2);
                break;
            case R.id.btn_my_help:
                ToastUtils.showLong(this, "我是帮助");
//                Intent i3 = new Intent(this, PersonalCenterActivity.class);
//                startActivity(i3);
                popupDialog();
                break;
            case R.id.scan:
                ToastUtils.showLong(this, "我是扫描");
                Intent i4 = new Intent(this, CaptureActivity.class);
                startActivityForResult(i4, 0);
                break;

            case R.id.allBike:
                break;
            case R.id.one:

                break;
            case R.id.two:
                break;
        }
    }

    private void addOverlay(List infos) {
        //清空地图
//        mMap.clear();
        //创建marker的显示图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.ease_icon_marka);
        LatLng latLng = null;
        OverlayOptions options;

        for (int i = 0; i < infos.size(); i++) {
            MarkerInfoUtil info = (MarkerInfoUtil) infos.get(i);
            latLng = new LatLng(info.getLatitude(), info.getLongitude());
            //设置marker
            options = new MarkerOptions()
                    .position(latLng)//设置位置
                    .icon(bitmap)//设置图标样式
                    .zIndex(9) // 设置marker所在层级
                    .draggable(true); // 设置手势拖拽;
            //添加marker
            mMarker = (Marker) mMap.addOverlay(options);
        }
    }

    private void clickBaiduMapMark() {

        mMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                LatLng latlng = marker.getPosition();
                reverseGeoCoder(latlng);
                return false;
            }
        });
    }

    private void popupDialog() {
        final List<BottomSheetBean> strings = new ArrayList<>();
        strings.add(new BottomSheetBean(R.mipmap.locking, "开不了锁"));
        strings.add(new BottomSheetBean(R.mipmap.trouble, "发现车辆故障"));
        strings.add(new BottomSheetBean(R.mipmap.report, "举报违停"));
        strings.add(new BottomSheetBean(R.mipmap.other, "其他问题"));

        StyledDialog.buildBottomSheetGv(this, "客户服务", strings, "", 2, new MyItemDialogListener() {
            @Override
            public void onItemClick(CharSequence text, int position) {
                ToastUtils.showLong(MainActivity.this, text + "-----" + position);
            }
        }).show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK && requestCode == 0) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                String result = bundle.getString("result");
                ToastUtils.showLong(this, result);
            }
        }
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(MainActivity.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        Toast.makeText(MainActivity.this, result.getAddress(),
                Toast.LENGTH_LONG).show();
        ToastUtils.showLong(MainActivity.this, result.getLocation().latitude + "------" + result.getLocation().longitude);
        latitude = result.getLocation().latitude;
        longitude = result.getLocation().longitude;
        countDistance(mCurrentLantitude, mCurrentLongitude, latitude, longitude);

    }

    private void countDistance(double mCurrentLantitude, double mCurrentLongitude, double latitude, double longitude) {
        RoutePlanSearch search = RoutePlanSearch.newInstance();        //百度的搜索路线的类
//        DrivingRoutePlanOption drivingRoutePlanOption = new DrivingRoutePlanOption();
        //步行路线参数类
        WalkingRoutePlanOption walkingRoutePlanOption = new WalkingRoutePlanOption();

        //起始坐标和终点坐标
        PlanNode startPlanNode = PlanNode.withLocation(new LatLng(mCurrentLantitude, mCurrentLongitude));  // lat  long
        PlanNode endPlanNode = PlanNode.withLocation(new LatLng(latitude, longitude));
        walkingRoutePlanOption.from(startPlanNode);
        walkingRoutePlanOption.to(endPlanNode);
        search.walkingSearch(walkingRoutePlanOption);
        search.setOnGetRoutePlanResultListener(new OnGetRoutePlanResultListener() {
            @Override
            public void onGetWalkingRouteResult(WalkingRouteResult result) {
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    Toast.makeText(MainActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
                }
                if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                    //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
                    //result.getSuggestAddrInfo()
                    return;
                }
                WalkingRouteLine walkingRouteLine = result.getRouteLines().get(0);
                int duration = walkingRouteLine.getDuration();
                Log.d("距离", duration + "米");
                Toast.makeText(App.getContext(), "你距离目标" + duration + "米", Toast.LENGTH_SHORT).show();

/**
 * public java.util.List<WalkingRouteLine> getRouteLines()
 * 获取所有步行规划路线
 * 返回:所有步行规划路线
 * */

                WalkingRouteOverlay overlay = new MyWalkingRouteOverlay(mMap);
                /**
                 * 设置地图 Marker 覆盖物点击事件监听者
                 * 需要实现的方法：     onMarkerClick(Marker marker)
                 * */
                mMap.setOnMarkerClickListener(overlay);
                routeOverlay = overlay;

                /**
                 * public void setData(WalkingRouteLine line)设置路线数据。
                 * 参数:line - 路线数据
                 * */
                overlay.setData(result.getRouteLines().get(0));

                /**
                 * public final void addToMap()将所有Overlay 添加到地图上
                 * */
                overlay.addToMap();

                /**
                 * public void zoomToSpan()
                 * 缩放地图，使所有Overlay都在合适的视野内
                 * 注： 该方法只对Marker类型的overlay有效
                 * */
                overlay.zoomToSpan();


            }

            @Override
            public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

            }

            @Override
            public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

            }

            @Override
            public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {

            }

            @Override
            public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

            }

            @Override
            public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

            }
        });


    }


    /**
     * 实现定位回调监听
     */
    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {

            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null)
                return;
            // 构造定位数据
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mXDirection).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mCurrentAccracy = location.getRadius();
            // 设置定位数据
            mMap.setMyLocationData(locData);
            mCurrentLantitude = location.getLatitude();
            mCurrentLongitude = location.getLongitude();

//            BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
//                    .fromResource(R.mipmap.search_center_ic);
            //不设置bitmapDescriptor时代表默认使用百度地图图标
            MyLocationConfiguration config = new MyLocationConfiguration(
                    mCurrentMode, true, null);
            mMap.setMyLocationConfigeration(config);
            // 第一次定位时，将地图位置移动到当前位置
            if (isFristLocation) {
                isFristLocation = false;
                setBaiduMapMark();
                setUserMapCenter();
                setMarkerInfo();
                addOverlay(infos);
            }
        }
    }

    private void setBaiduMapMark() {
        LatLng point = new LatLng(mCurrentLantitude, mCurrentLongitude);
        //设置Mark(覆盖物)图标
        BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
                .fromResource(R.mipmap.search_center_ic);
        OverlayOptions options = new MarkerOptions()
                .position(point)
                .icon(mCurrentMarker)
                .draggable(true);
        mMarker = (Marker) mMap.addOverlay(options);
    }

    private void setMarkerInfo() {
        infos = new ArrayList();
        infos.add(new MarkerInfoUtil(mCurrentLantitude - 0.001, mCurrentLongitude - 0.001));
        infos.add(new MarkerInfoUtil(mCurrentLantitude - 0.002, mCurrentLongitude - 0.002));
        infos.add(new MarkerInfoUtil(mCurrentLantitude - 0.003, mCurrentLongitude - 0.003));
        infos.add(new MarkerInfoUtil(mCurrentLantitude + 0.002, mCurrentLongitude + 0.002));
        infos.add(new MarkerInfoUtil(mCurrentLantitude + 0.003, mCurrentLongitude + 0.003));
        infos.add(new MarkerInfoUtil(mCurrentLantitude - 0.008, mCurrentLongitude - 0.008));
        infos.add(new MarkerInfoUtil(mCurrentLantitude + 0.01, mCurrentLongitude + 0.01));
        infos.add(new MarkerInfoUtil(mCurrentLantitude + 0.005, mCurrentLongitude + 0.005));
    }

    /**
     * 设置中心点
     */
    private void setUserMapCenter() {
//        Log.v("pcw","setUserMapCenter : lat : "+ lat+" lon : " + lon);

        LatLng cenpt = new LatLng(mCurrentLantitude, mCurrentLongitude);
        //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(cenpt)
                .zoom(18)
                .build();
//        reverseGeoCoder(cenpt);

        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
                .newMapStatus(mMapStatus);
        //改变地图状态
        mMap.animateMapStatus(mMapStatusUpdate);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        mMapView = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onStop() {
        // 关闭图层定位
        mMap.setMyLocationEnabled(false);
        mLocationClient.stop();
        // 关闭方向传感器
        myOrientationListener.stop();
        super.onStop();
    }


    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    protected void onStart() {
        // 开启图层定位
        mMap.setMyLocationEnabled(true);
        if (!mLocationClient.isStarted()) {
            mLocationClient.start();
        }
        // 开启方向传感器
        myOrientationListener.start();
        super.onStart();
    }

    /**
     * WalkingRouteOverlay已经实现了BaiduMap.OnMarkerClickListener接口
     */
    private class MyWalkingRouteOverlay extends WalkingRouteOverlay {

        public MyWalkingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        /**
         * public BitmapDescriptor getStartMarker()
         * 覆写此方法以改变默认起点图标
         * 返回:起点图标
         */
        @Override
        public BitmapDescriptor getStartMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.start_point);
            }
            return null;
        }

        /**
         * public BitmapDescriptor getTerminalMarker()
         * 覆写此方法以改变默认终点图标
         * 返回:终点图标
         */
        @Override
        public BitmapDescriptor getTerminalMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.stop_point);
            }
            return null;
        }
    }

}
