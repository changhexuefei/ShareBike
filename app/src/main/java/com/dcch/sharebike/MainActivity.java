package com.dcch.sharebike;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
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
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.listener.MyOrientationListener;
import com.dcch.sharebike.moudle.home.bean.MarkerInfoUtil;
import com.dcch.sharebike.moudle.login.activity.PersonalCenterActivity;
import com.dcch.sharebike.moudle.search.activity.SeekActivity;
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

public class MainActivity extends BaseActivity  {
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

    private List infos;
    //显示marker
    private boolean showMarker = false;

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
    private MapStatus.Builder mBuilder;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        classify.check(R.id.allBike);
        ButterKnife.bind(this);
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
    }

    public void showMain() {
        Intent intent = getIntent();
        String result = intent.getStringExtra("result");
        if (result.equals("correct") && result != null) {
            mInstructions.setVisibility(View.GONE);
        }

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
                LatLng mlLatLng = new LatLng(mCurrentLantitude, mCurrentLongitude);
                MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(mlLatLng);
                mMap.animateMapStatus(msu);

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
//                addOverlay(infos);

                break;
        }
    }

//    private void addOverlay(List infos) {
//
//        //清空地图
//        mMap.clear();
//        //创建marker的显示图标
//        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.booking_bike_marker);
//        LatLng latLng = null;
//        Marker marker;
//        OverlayOptions options;
//        for (MarkerInfoUtil info : infos) {
//            //获取经纬度
//            latLng = new LatLng(info.getLatitude(), info.getLongitude());
//            //设置marker
//            options = new MarkerOptions()
//                    .position(latLng)//设置位置
//                    .icon(bitmap)//设置图标样式
//                    .zIndex(9) // 设置marker所在层级
//                    .draggable(true); // 设置手势拖拽;
//            //添加marker
//            marker = (Marker) mMap.addOverlay(options);
//        }
//        //将地图显示在最后一个marker的位置
//        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
//        mMap.setMapStatus(msu);
//
//    }

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
//            setBaiduMapMark();
////             设置自定义图标
//            BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
//                    .fromResource(R.mipmap.search_center_ic);
            MyLocationConfiguration config = new MyLocationConfiguration(
                    mCurrentMode, true, null);
            mMap.setMyLocationConfigeration(config);
            // 第一次定位时，将地图位置移动到当前位置
            if (isFristLocation) {
                isFristLocation = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                mBuilder = new MapStatus.Builder();
                mBuilder.target(ll).zoom(15.0f);
                MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(18.0f);
                mMap.setMapStatus(msu);
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                mMap.animateMapStatus(u);
            }
        }
    }
//
//    //移动后，手机地图的中心的地理坐标
//    public void setBaiduMapCenterPoint() {
////        mBuilder.target()
//
//        MapStatusUpdate update = MapStatusUpdateFactory.newMapStatus(mBuilder.build());
//        mMap.setMapStatus(update);
//
//    }

    private void setBaiduMapMark() {
        LatLng point = new LatLng(mCurrentLantitude, mCurrentLongitude);
        //设置覆盖物图标
        BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
                .fromResource(R.mipmap.search_center_ic);
        OverlayOptions options = new MarkerOptions()
                .position(point)
                .icon(mCurrentMarker);
        mMap.addOverlay(options);
    }

    private void setMarkerInfo() {
        infos = new ArrayList();
        infos.add(new MarkerInfoUtil(116.551593, 39.821720));
        infos.add(new MarkerInfoUtil(116.551593, 39.821725));
        infos.add(new MarkerInfoUtil(116.551593, 39.821730));
    }
}
