package com.dcch.sharebike;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.baidu.location.Address;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
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
import com.baidu.mapapi.search.poi.PoiSearch;
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
import com.dcch.sharebike.base.MessageEvent;
import com.dcch.sharebike.http.Api;
import com.dcch.sharebike.listener.MyOrientationListener;
import com.dcch.sharebike.moudle.home.bean.BikeInfo;
import com.dcch.sharebike.moudle.login.activity.PersonalCenterActivity;
import com.dcch.sharebike.moudle.search.activity.SeekActivity;
import com.dcch.sharebike.moudle.user.activity.CustomerServiceActivity;
import com.dcch.sharebike.moudle.user.bean.UserInfo;
import com.dcch.sharebike.overlayutil.OverlayManager;
import com.dcch.sharebike.overlayutil.WalkingRouteOverlay;
import com.dcch.sharebike.utils.LogUtils;
import com.dcch.sharebike.utils.SPUtils;
import com.dcch.sharebike.utils.ToastUtils;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.bottomsheet.BottomSheetBean;
import com.hss01248.dialog.interfaces.MyItemDialogListener;
import com.xys.libzxing.zxing.activity.CaptureActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

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
    //    @BindView(R.id.allBike)
//    RadioButton allBike;
//    @BindView(R.id.one)
//    RadioButton one;
//    @BindView(R.id.two)
//    RadioButton two;
//    @BindView(R.id.classify)
//    RadioGroup classify;
    private final int SDK_PERMISSION_REQUEST = 127;

    //    private List<MarkerInfoUtil> infos;
    //显示marker
    private boolean showMarker = false;
    private String permissionInfo;
    boolean useDefaultIcon = false;
    private long mExitTime; //退出时间
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
    private Address locationDescribe;
    //POI搜索相关
    public PoiSearch mPoiSearch = null;
    private SelectPicPopupWindow menuWindow; // 自定义的头像编辑弹出框
    private int mDuration;
    private String resultAddress;
    private String address1;
    private List<BikeInfo> bikeInfos;
    private MapStatus mMapStatus;
    private MapStatusUpdate mMapStatusUpdate;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
//        classify.check(R.id.allBike);
        ButterKnife.bind(this);

        if (SPUtils.isLogin()) {
            mInstructions.setVisibility(View.GONE);
        } else {
            mInstructions.setVisibility(View.VISIBLE);
        }

        bikeInfos = new ArrayList<BikeInfo>();
        // 初始化GeoCoder模块，注册事件监听
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);
//        //POI搜索相关
//        mPoiSearch = PoiSearch.newInstance();
//        mPoiSearch.setOnGetPoiSearchResultListener(this);
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
        clickDismissOverlay();
    }

    private void clickDismissOverlay() {
        mMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
//                ToastUtils.showShort(MainActivity.this,"我是latlng"+latLng);
                mMap.clear();
                addOverlay(bikeInfos);
                //由于menuWindow会和地图抢夺焦点，所以在设置他的属性时设置为不能获得焦点
                //就能够满足一起消失的功能
                if (menuWindow != null) {
                    menuWindow.dismiss();
                }
                if (SPUtils.isLogin()) {
                    mInstructions.setVisibility(View.GONE);
                } else if (!SPUtils.isLogin()) {
                    mInstructions.setVisibility(View.VISIBLE);
                }

                setUserMapCenter();
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
//                ToastUtils.showShort(MainActivity.this, "我是mapPoi" + mapPoi);
//                Log.d("信息",mapPoi.getName()+"/////"+mapPoi.getUid()+"////"+mapPoi.getPosition());
                return false;
            }
        });


    }

    @Override
    protected void initListener() {
        //地图状态改变相关监听
//        mMap.setOnMapStatusChangeListener(this);


    }

    //询问手机权限的方法
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
            // 打开照相机的权限
            if (addPermission(permissions, Manifest.permission.CAMERA)) {
                permissionInfo += "Manifest.permission.CAMERA Deny \n";
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


    @OnClick({R.id.MyCenter, R.id.seek, R.id.btn_my_location, R.id.instructions, R.id.btn_my_help, R.id.scan})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.MyCenter:
                ToastUtils.showLong(this, "我是个人中心");
                if (SPUtils.isLogin()) {
                    Intent i = new Intent(this, PersonalCenterActivity.class);
                    i.putExtra("name", "login");
                    startActivity(i);
                } else {
                    Intent i = new Intent(this, PersonalCenterActivity.class);
                    i.putExtra("name", "unLogin");
                    startActivity(i);
                }

                break;
            case R.id.seek:
                ToastUtils.showLong(this, "我是搜索");
                Intent i1 = new Intent(this, SeekActivity.class);
//                Log.d("11111",resultAddress);
                i1.putExtra("address", address1);
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
                if (SPUtils.isLogin()) {
//                    mInstructions.setVisibility(View.GONE);
                } else {
                    Intent i2 = new Intent(this, PersonalCenterActivity.class);
                    i2.putExtra("name", "unLogin");
                    startActivity(i2);
                }
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
        }
    }

    private void addOverlay(List bikeInfos) {

        if (bikeInfos.size() > 0) {
            //清空地图
            mMap.clear();
            //创建marker的显示图标
            BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.ease_icon_marka);
            LatLng latLng = null;
            OverlayOptions options;

            for (int i = 0; i < bikeInfos.size(); i++) {
                BikeInfo info = (BikeInfo) bikeInfos.get(i);
                String lat = info.getLatitude();
                Log.d("&&&&&&&", lat);
                String lng = info.getLongitude();
                Log.d("&&&&&&&", lng);
                double lat1 = Double.parseDouble(lat);
                Log.d("(((((((", lat1 + "");
                double lng1 = Double.parseDouble(lng);
                Log.d("))))))", lng1 + "");

                latLng = new LatLng(lat1, lng1);
                Log.d("$$$$$$", latLng + "");
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
//      else {
//            ToastUtils.showLong(this, "当前周围没有车辆");
//        }
    }

    private void clickBaiduMapMark() {
        mMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker != null) {
                    LatLng latlng = marker.getPosition();
                    mInstructions.setVisibility(View.GONE);
//                    ToastUtils.showShort(MainActivity.this, "我是marker" + marker);
                    addOverlay(bikeInfos);//
                    reverseGeoCoder(latlng);
                    return true;
                }

                return false;
            }
        });

    }

    View.OnClickListener itemsOnClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                case R.id.order:
                    if (SPUtils.isLogin()) {
                        mInstructions.setVisibility(View.GONE);
                    } else if (!SPUtils.isLogin()) {
                        mInstructions.setVisibility(View.VISIBLE);
                    }

                    mMap.clear();
                    menuWindow.setOutsideTouchable(true);

                    addOverlay(bikeInfos);
                    setUserMapCenter();
                    ToastUtils.showShort(MainActivity.this, "预约车辆");
            }
        }
    };

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
                if (position == 0) {
                    Intent unlock = new Intent(MainActivity.this, CustomerServiceActivity.class);
                    unlock.putExtra("name", "0");
                    startActivity(unlock);
                }

                if (position == 1) {
                    Intent bikeTrouble = new Intent(MainActivity.this, CustomerServiceActivity.class);
                    bikeTrouble.putExtra("name", "1");
                    startActivity(bikeTrouble);

                }
                if (position == 2) {
                    Intent reportIllegalParking = new Intent(MainActivity.this, CustomerServiceActivity.class);
                    reportIllegalParking.putExtra("name", "3");
                    startActivity(reportIllegalParking);

                }
                if (position == 3) {
                    Intent reportIllegalParking = new Intent(MainActivity.this, CustomerServiceActivity.class);
                    reportIllegalParking.putExtra("name", "4");
                    startActivity(reportIllegalParking);
                }
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
        resultAddress = result.getAddress();
        LatLng latLng = result.getLocation();
        latitude = latLng.latitude;
        longitude = latLng.longitude;
        countDistance(mCurrentLantitude, mCurrentLongitude, latLng, resultAddress);

    }

    private void countDistance(double mCurrentLantitude, double mCurrentLongitude, final LatLng latLng, final String address) {
        RoutePlanSearch search = RoutePlanSearch.newInstance();        //百度的搜索路线的类
        //步行路线参数类
        WalkingRoutePlanOption walkingRoutePlanOption = new WalkingRoutePlanOption();

        //起始坐标和终点坐标
        PlanNode startPlanNode = PlanNode.withLocation(new LatLng(mCurrentLantitude, mCurrentLongitude));  // lat  long
        PlanNode endPlanNode = PlanNode.withLocation(latLng);
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
//                    result.getSuggestAddrInfo();
                    return;
                }

                if (result != null && result.error == SearchResult.ERRORNO.NO_ERROR) {
                    WalkingRouteLine walkingRouteLine = result.getRouteLines().get(0);
//                    ToastUtils.showLong(MainActivity.this,walkingRouteLine+"");
                    mDuration = walkingRouteLine.getDuration();
                    Log.d("距离", mDuration + "米");
//                    Toast.makeText(App.getContext(), "你距离目标" + mDuration + "米", Toast.LENGTH_SHORT).show();

                    menuWindow = new SelectPicPopupWindow(MainActivity.this, itemsOnClick);
                    //指定父视图，显示在父控件的某个位置（Gravity.TOP,Gravity.RIGHT等）
                    //  menuWindow.showAtLocation(findViewById(R.id.mapView), Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, -48);

                    //设置显示在某个指定控件的下方
                    menuWindow.showAsDropDown(findViewById(R.id.top));
                }
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


//    @Override
//    public void onGetPoiResult(PoiResult poiResult) {
//
//    }
//
//    @Override
//    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
//
//        if (poiDetailResult.error != SearchResult.ERRORNO.NO_ERROR) {
//            // 检索失败
//        } else {
//            //1:
//            BitmapDescriptor descriptor;
//            Button btn = new Button(this);
//            btn.setBackgroundColor(0xAA00FF00);
//            btn.setText(poiDetailResult.getName() + "\r\n" + poiDetailResult.getAddress());
//
//            //2:
//            descriptor = BitmapDescriptorFactory.fromView(btn);
//
//            //3:
//            InfoWindow mInfoWindow = new InfoWindow(descriptor, poiDetailResult.getLocation(), -100, new InfoWindow.OnInfoWindowClickListener() {
//                @Override
//                public void onInfoWindowClick() {
//                    // 隐藏弹窗！
//                    mMap.hideInfoWindow();
//                }
//            });
//
//            //4:
//            mMap.showInfoWindow(mInfoWindow);
//        }
//
//    }
//
//    @Override
//    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {
//
//    }

//    @Override
//    public void onMapStatusChangeStart(MapStatus mapStatus) {
//
//    }
//
//    @Override
//    public void onMapStatusChange(MapStatus mapStatus) {
//
//    }
//
//    @Override
//    public void onMapStatusChangeFinish(MapStatus mapStatus) {
//        //地图操作的中心点
//        LatLng cenpt = mapStatus.target;
//        Log.d("移动到",cenpt+"");
//        setMarkerInfo(cenpt.longitude,cenpt.latitude);
////        ToastUtils.showLong(this,cenpt+"");
//
//        mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(cenpt));
//    }


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
//                setBaiduMapMark();
                setUserMapCenter();
                //根据手机定位地点，得到手机定位点的周围半径1000米范围内的车辆信息的方法
                getBikeInfo(mCurrentLantitude, mCurrentLongitude);
            }
            //根据手机定位的不同得到定位点信息，将这个信息传递给搜索页面
            Address address = location.getAddress();
            address1 = address.address;
        }
    }

    private void getBikeInfo(double mCurrentLantitude, double mCurrentLongitude) {
        String lat = mCurrentLantitude + "";
        String lng = mCurrentLongitude + "";
        Map<String, String> map = new HashMap<>();
        map.put("lng", lng);
        map.put("lat", lat);
        OkHttpUtils.post().url(Api.BASE_URL + Api.GINPUT).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                Log.d("978787878", response);
                if (response != null && !response.equals("")) {
                    try {
                        JSONArray jsonArray = new JSONArray(response);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Log.d("自行车", jsonObject + "");
                            BikeInfo info = new BikeInfo();
                            info.setAddress(jsonObject.getString("address"));
                            info.setBicycleId(jsonObject.getInt("bicycleId"));
                            info.setLatitude(jsonObject.getString("latitude"));
                            info.setLongitude(jsonObject.getString("longitude"));
                            info.setUnitPrice(jsonObject.getInt("unitPrice"));
                            bikeInfos.add(info);
                        }
                        Log.d("车辆信息", bikeInfos + "");
                        addOverlay(bikeInfos);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    ToastUtils.showLong(App.getContext(), "当前周围没有车辆");

                }
            }
        });
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

    /**
     * 设置中心点
     */
    private void setUserMapCenter() {
//        Log.v("pcw","setUserMapCenter : lat : "+ lat+" lon : " + lon);

        LatLng cenpt = new LatLng(mCurrentLantitude, mCurrentLongitude);
        //定义地图状态
        mMapStatus = new MapStatus.Builder()
                .target(cenpt)
                .zoom(18)
                .build();

        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        mMapStatusUpdate = MapStatusUpdateFactory
                .newMapStatus(mMapStatus);
        //改变地图状态
        mMap.animateMapStatus(mMapStatusUpdate);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //反注册EventBus
        EventBus.getDefault().unregister(this);
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        mMapView = null;
        //释放资源
        if (mSearch != null) {
            mSearch.destroy();
        }
        System.exit(0);

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

    //注册EventBus


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //注册EventBus
        EventBus.getDefault().register(this);
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
                return BitmapDescriptorFactory.fromResource(R.drawable.unchecked);
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
                return BitmapDescriptorFactory.fromResource(R.drawable.unchecked);
            }
            return null;
        }

    }

    //点击手机上的返回键退出App的方法
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //按下的如果是BACK键，同时没有重复
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                App.getInstance().exit();
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    public class SelectPicPopupWindow extends PopupWindow {


        TextView mBikeLocationInfo;

        TextView mUnitPrice;

        TextView mDistance;

        TextView mArrivalTime;

        Button mOrder;
        private View mMenuView;

        public SelectPicPopupWindow(Context context, View.OnClickListener itemsOnClick) {
            super(context);

            //创建布局反射器
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            //加载布局
            mMenuView = inflater.inflate(R.layout.item_popwindow, null);

            //初始化控件
            mBikeLocationInfo = (TextView) mMenuView.findViewById(R.id.bike_location_info);
            mUnitPrice = (TextView) mMenuView.findViewById(R.id.unitPrice);
            mDistance = (TextView) mMenuView.findViewById(R.id.distance);
            Log.d("8888", mDistance.getText().toString());
            mArrivalTime = (TextView) mMenuView.findViewById(R.id.arrivalTime);
            mOrder = (Button) mMenuView.findViewById(R.id.order);
            //为控件赋值
            mBikeLocationInfo.setText(resultAddress);
//            Log.d("456",resultAddress);
            mDistance.setText(String.valueOf(mDuration) + "米");

            // 设置按钮监听
            mOrder.setOnClickListener(itemsOnClick);
            // 设置SelectPicPopupWindow的View
            this.setContentView(mMenuView);

            // 设置SelectPicPopupWindow的View
            this.setContentView(mMenuView);
            // 设置SelectPicPopupWindow弹出窗体的宽
            this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            // 设置SelectPicPopupWindow弹出窗体的高
            this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            // 设置SelectPicPopupWindow弹出窗体可点击
//            this.setFocusable(true);
////             设置SelectPicPopupWindow弹出窗体动画效果
//            this.setAnimationStyle(R.style.PopupAnimation);
            // 实例化一个ColorDrawable颜色为半透明
//        ColorDrawable dw = new ColorDrawable(0x00000000);
            // 设置SelectPicPopupWindow弹出窗体的背景
//        this.setBackgroundDrawable(dw);
            // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
            mMenuView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                @SuppressLint("ClickableViewAccessibility")
                public boolean onTouch(View v, MotionEvent event) {

                    int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                    int y = (int) event.getY();
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (y < height) {
                            dismiss();
                        }
                    }
                    return true;
                }
            });
        }
    }

    //退出登录页面后，设置页面发来的消息，将mInstructions控件显示
    @Subscriber(tag = "visible", mode = ThreadMode.ASYNC)
    private void receiveFromSetting(MessageEvent info) {
        LogUtils.e(info.toString());
        mInstructions.setVisibility(View.VISIBLE);
    }

    //登录成功页面发来的消息，将mInstructions控件隐藏
    @Subscriber(tag = "gone", mode = ThreadMode.ASYNC)
    private void receiveFromLogin(MessageEvent info) {
        LogUtils.e(info.toString());
        mInstructions.setVisibility(View.GONE);
    }

    //登录成功后，登录页面发来的消息，将user对象暂时传递到主页面
    @Subscriber(tag = "user", mode = ThreadMode.ASYNC)
    private void receiveFromLogin(UserInfo info) {
        LogUtils.e(info.toString());
        mInstructions.setVisibility(View.GONE);
    }


}
