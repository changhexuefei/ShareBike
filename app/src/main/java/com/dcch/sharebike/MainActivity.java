package com.dcch.sharebike;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.TimeZone;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
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
import com.dcch.sharebike.moudle.home.bean.BikeRentalOrderInfo;
import com.dcch.sharebike.moudle.home.bean.BookingBikeInfo;
import com.dcch.sharebike.moudle.home.bean.UserBookingBikeInfo;
import com.dcch.sharebike.moudle.login.activity.ClickCameraPopupActivity;
import com.dcch.sharebike.moudle.login.activity.ClickMyHelpActivity;
import com.dcch.sharebike.moudle.login.activity.IdentityAuthentication;
import com.dcch.sharebike.moudle.login.activity.LoginActivity;
import com.dcch.sharebike.moudle.login.activity.PersonalCenterActivity;
import com.dcch.sharebike.moudle.login.activity.RechargeActivity;
import com.dcch.sharebike.moudle.search.activity.SeekActivity;
import com.dcch.sharebike.moudle.user.activity.CustomerServiceActivity;
import com.dcch.sharebike.overlayutil.OverlayManager;
import com.dcch.sharebike.overlayutil.WalkingRouteOverlay;
import com.dcch.sharebike.utils.LogUtils;
import com.dcch.sharebike.utils.SPUtils;
import com.dcch.sharebike.utils.ToastUtils;
import com.dcch.sharebike.view.BikeRentalOrderPopupWindow;
import com.dcch.sharebike.view.BookBikePopupWindow;
import com.dcch.sharebike.view.MyCountDownTimer;
import com.dcch.sharebike.view.UserBookingBikePopupWindow;
import com.google.gson.Gson;
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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;


@RuntimePermissions
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

    private final int SDK_PERMISSION_REQUEST = 127;

    //显示marker
    private boolean showMarker = false;
    private String permissionInfo;
    boolean useDefaultIcon = false;
    private long mExitTime; //退出时间
    long initialTime = 600;
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
    //    private Address locationDescribe;
    //POI搜索相关
//    public PoiSearch mPoiSearch = null;
    private SelectPicPopupWindow menuWindow = null; // 自定义弹出框
    private int mDuration;
    private String resultAddress;
    private String address1;
    private List<BikeInfo> bikeInfos;
    private MapStatus mMapStatus;
    private MapStatusUpdate mMapStatusUpdate;
    private BikeInfo bikeInfo;
    //    private int userid;
    private BookBikePopupWindow bookBikePopupWindow = null;
    private UserBookingBikePopupWindow userBookingBikePopupWindow = null;
    private int bikeNo;
    private int cashStatus;
    private int status;
    private String bikeID;
    private String bookingCarId;
    private String phone;
    private String result;
    private String userDetail;
    private JSONObject object;
    private String uID;
    private String currentTime;
    private String bookingCarDate;
    private UserBookingBikeInfo userBookingBikeInfo = null;
    private Double locationLongitude;
    private Double locationLatitude;
    private long diff;
    private String stringDate;
    private long time;
    private LatLng clickMarkLatlng;
    private LatLng currentLatLng;
    private boolean isBook = false;
    private boolean isShowRideOrder = false;
    private boolean isShowBookOrder = false;
    private double clickLat;
    private double clickLon;
    private String bicycleNo = "";
    private MyCountDownTimer timer;
    private String count;
    private BookingBikeInfo bookingBikeInfo = null;
    private BikeRentalOrderInfo bikeRentalOrderInfo = null;
    private BikeRentalOrderPopupWindow orderPopupWindow = null;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
//        classify.check(R.id.allBike);
        ButterKnife.bind(this);
        MainActivityPermissionsDispatcher.initPermissionWithCheck(this);
        showCamera();
        initPermission();
        userDetail = (String) SPUtils.get(App.getContext(), "userDetail", "");
        Log.d("用户明细", userDetail);
        object = null;
        try {
            object = new JSONObject(userDetail);
            phone = object.getString("phone");
            int id = object.getInt("id");
            uID = String.valueOf(id);
            Log.d("手机号", phone);

        } catch (JSONException e) {
            e.printStackTrace();
        }
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
        clickDismissOverlay();
    }

    //进入主页面检查客户是否有预约订单的方法
    private void checkBookingBikeInfoByUserID(String uID) {
        final Map<String, String> map = new HashMap<>();
        map.put("userId", uID);
        OkHttpUtils.post().url(Api.BASE_URL + Api.SEARCHBOOKING).params(map).build()
                .execute(new StringCallback() {
                             @Override
                             public void onError(Call call, Exception e, int id) {
                                 if (e != null && !e.equals("")) {
                                     LogUtils.e(e.getMessage());
                                 }
                             }

                             @Override
                             public void onResponse(String response, int id) {
                                 Log.d("呵呵", response);
                                 Gson gson = new Gson();
                                 userBookingBikeInfo = gson.fromJson(response, UserBookingBikeInfo.class);
                                 LogUtils.d("预约车辆信息", userBookingBikeInfo + "");
                                 LogUtils.d("用户约车信息", userBookingBikeInfo.getBicycleNo() + "");
                                 bicycleNo = userBookingBikeInfo.getBicycleNo();
                                 int bicycleId = userBookingBikeInfo.getBicycleId();
                                 resultAddress = userBookingBikeInfo.getAddress();
                                 String userBookingBikeInfoLongitude = userBookingBikeInfo.getLongitude();
                                 String userBookingBikeInfoLatitude = userBookingBikeInfo.getLatitude();
                                 bookingCarId = userBookingBikeInfo.getBookingCarId();
                                 String bookingCarDate = userBookingBikeInfo.getBookingCarDate();
                                 String stringDate = getStringDate();
                                 if (bicycleNo != null && !bicycleNo.equals("")
                                         && resultAddress != null && !resultAddress.equals("")
                                         && bookingCarId != null && !bookingCarId.equals("")) {
                                     mMap.clear();
                                     if (bookingCarDate != null && !bookingCarDate.equals("") && stringDate != null && !stringDate.equals("")) {
                                         long countTime = 600000 - countTime(stringDate, bookingCarDate);
                                         if (userBookingBikeInfoLongitude != null && !userBookingBikeInfoLongitude.equals("")
                                                 && userBookingBikeInfoLatitude != null && !userBookingBikeInfoLatitude.equals("")) {
                                             locationLongitude = Double.valueOf(userBookingBikeInfoLongitude);
                                             locationLatitude = Double.valueOf(userBookingBikeInfoLatitude);
                                             isShowBookOrder = true;
                                             forLocationAddMark(locationLongitude, locationLatitude);
                                         }
                                         userBookingBikePopupWindow = new UserBookingBikePopupWindow(MainActivity.this, userBookingBikeInfo, userBookBikeItemsOnClick);
                                         userBookingBikePopupWindow.showAsDropDown(findViewById(R.id.top));
                                         timer = (MyCountDownTimer) new MyCountDownTimer(countTime, 1000) {
                                             @Override
                                             public void onTick(long millisUntilFinished) {
                                                 userBookingBikePopupWindow.mHoldTime.setText(toClock(millisUntilFinished));
                                             }

                                             @Override
                                             public String toClock(long millis) {
                                                 return super.toClock(millis);
                                             }

                                             @Override
                                             public void onFinish() {
                                                 super.onFinish();
                                                 cancelBookingBike(bookingCarId, bicycleNo);
                                             }
                                         }.start();
                                     } else {
                                         addOverlay(bikeInfos);
                                     }
                                     if (bicycleId != 0) {
                                         bikeID = String.valueOf(bicycleId);
                                     }
                                 }
                             }
                         }
                );
    }

    //客户有预约订单时，显示单个车辆位置的覆盖物
    private void forLocationAddMark(Double locationLongitude, Double locationLatitude) {
//        mMap.clear();
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.ease_icon_marka);
        LatLng latLng = null;
        OverlayOptions options;
        latLng = new LatLng(locationLatitude, locationLongitude);
        //设置marker
        options = new MarkerOptions()
                .position(latLng)//设置位置
                .icon(bitmap)//设置图标样式
                .zIndex(9) // 设置marker所在层级
                .draggable(true); // 设置手势拖拽;
        //添加marker
        mMarker = (Marker) mMap.addOverlay(options);
    }

    //百度地图的点击方法
    private void clickDismissOverlay() {
        mMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                //当客户已经约车，这时客户点击地图，只显示预约的车辆和路线的覆盖物，其余消失。
                if (isBook) {
                    mMap.clear();
                    forLocationAddMark(clickLon, clickLat);
                    paintingLine(currentLatLng, clickMarkLatlng);
                } else if (isShowRideOrder) {
                    mMap.clear();
                } else if (isShowBookOrder) {
                    mMap.clear();
                    forLocationAddMark(clickLon, clickLat);
                } else {
                    mMap.clear();
                    addOverlay(bikeInfos);
                    setUserMapCenter();
                }
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
                if (SPUtils.isLogin()) {
                    popupDialog();
                } else {
                    Intent i3 = new Intent(this, ClickMyHelpActivity.class);
                    startActivity(i3);
                }
                break;
            case R.id.scan:
                ToastUtils.showLong(this, "我是扫描");
                // && cashStatus == 1 && status == 1
                if (SPUtils.isLogin()) {
                    MainActivityPermissionsDispatcher.showCameraWithCheck(this);
                    Intent i4 = new Intent(this, CaptureActivity.class);
                    startActivityForResult(i4, 0);
                } else if (SPUtils.isLogin() && cashStatus == 0) {
                    Intent i4 = new Intent(this, RechargeActivity.class);
                    startActivity(i4);
                } else if (SPUtils.isLogin() && cashStatus == 1 && status == 0) {
                    Intent i4 = new Intent(this, IdentityAuthentication.class);
                    startActivity(i4);
                } else {

//                    没有登录的情况设置Activity
//                    mBtnMyHelp.setVisibility(View.GONE);
//                    mBtnMyLocation.setVisibility(View.GONE);
//                    mScan.setVisibility(View.GONE);
                    startActivity(new Intent(MainActivity.this, ClickCameraPopupActivity.class));

                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission(Manifest.permission.CAMERA)
    void showCamera() {

    }

    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    void initPermission() {

    }

    //百度地图添加覆盖物的方法
    private void addOverlay(List bikeInfos) {
        if (bikeInfos.size() > 0) {
            //清空地图
            mMap.clear();
            //创建marker的显示图标
            BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.ease_icon_marka);
            LatLng latLng = null;
            OverlayOptions options;
            for (int i = 0; i < bikeInfos.size(); i++) {
                bikeInfo = (BikeInfo) bikeInfos.get(i);
                String lat = bikeInfo.getLatitude();
                String lng = bikeInfo.getLongitude();
                double lat1 = Double.parseDouble(lat);
                double lng1 = Double.parseDouble(lng);
                latLng = new LatLng(lat1, lng1);
                //设置marker
                options = new MarkerOptions()
                        .position(latLng)//设置位置
                        .icon(bitmap)//设置图标样式
                        .zIndex(9) // 设置marker所在层级
                        .draggable(true); // 设置手势拖拽;
                //添加marker
                mMarker = (Marker) mMap.addOverlay(options);
            }
        } else {
            ToastUtils.showLong(this, "当前周围没有车辆");
        }
    }

    //百度地图的覆盖物点击方法
    private void clickBaiduMapMark() {
        mMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker != null) {
                    clickMarkLatlng = marker.getPosition();

//                    Point p = mMap.getProjection().toScreenLocation(clickMarkLatlng);
//                    Log.d("zongshi",p+"");
//                    p.y -= 90;
//                    LatLng llInfo = mMap.getProjection().fromScreenLocation(p);
//                    Log.d("经纬度",clickMarkLatlng+"\n"+llInfo);
                    if (menuWindow != null && !menuWindow.equals("")) {
                        menuWindow.dismiss();
                    }
                    if (isBook) {


                    }
                    mInstructions.setVisibility(View.GONE);
                    addOverlay(bikeInfos);//
                    reverseGeoCoder(clickMarkLatlng);
                    return true;
                }

                return false;
            }
        });

    }

    //预约车辆的点击监听事件
    View.OnClickListener itemsOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.order:
                    //&& cashStatus == 1 && status == 1
                    if (SPUtils.isLogin()) {
                        ToastUtils.showShort(MainActivity.this, "我登录了");
                        if (phone != null && !phone.equals("")) {
                            queryBookingNum(phone);
                            LogUtils.d("电话", phone);
                        }
                        // ! &&
//                        if (count != null && !count.equals("") && Integer.valueOf(count) < 5) {
//                            LogUtils.d("++++++count", count);
//                        mMap.clear();
                        if (menuWindow != null && !menuWindow.equals("")) {
                            menuWindow.setOutsideTouchable(true);
                            menuWindow.dismiss();
                        }
                        if (clickMarkLatlng != null && !clickMarkLatlng.equals("")) {
                            clickLat = clickMarkLatlng.latitude;
                            clickLon = clickMarkLatlng.longitude;
                            forLocationAddMark(clickLon, clickLat);
                            paintingLine(currentLatLng, clickMarkLatlng);
                        }
                        mInstructions.setVisibility(View.GONE);
                        Log.d("ooooo", userDetail);
                        if (userDetail != null) {
                            try {
                                int bicycleId = bikeInfo.getBicycleId();
                                cashStatus = object.getInt("cashStatus");
                                status = object.getInt("status");
                                bikeNo = bikeInfo.getBicycleNo();
                                Log.d("zixinghe", bicycleNo + "");
                                bikeID = String.valueOf(bicycleId);
                                bicycleNo = String.valueOf(bikeNo);
                                bookingBike(uID, bicycleNo);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
//                        }
//                    else {
//                            ToastUtils.showLong(MainActivity.this, "抱歉，您今天已经预约5次，明天再来吧。");
//                            menuWindow.setFocusable(true);
//                            menuWindow.dismiss();
//                            mMap.clear();
//                            addOverlay(bikeInfos);
//                            setUserMapCenter();
//                            }
//                        }
                    } else if (SPUtils.isLogin() && cashStatus == 0) {
                        startActivity(new Intent(MainActivity.this, RechargeActivity.class));
                        //
                    } else if (SPUtils.isLogin() && cashStatus == 1 && status == 0) {
                        startActivity(new Intent(MainActivity.this, IdentityAuthentication.class));
                    } else {
                        ToastUtils.showShort(MainActivity.this, "我没登录");
                        mInstructions.setVisibility(View.VISIBLE);
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    }
            }

        }
//
    };
    //点击取消预约的点击监听事件
    View.OnClickListener bookBikeItemsOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.cancel_book:
                    new AlertDialog.Builder(MainActivity.this).setTitle("取消预约")
                            .setMessage("每天可预约5次，确认要取消吗?")
                            .setNegativeButton("取消", null)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Log.d("7777777", "rererer");
                                    cancelBookingBike(bookingCarId, bicycleNo);
                                    Log.d("eeeeeee", bookingCarId);
                                    Log.d("wwwwwww", bicycleNo);
                                    Log.d("7777777", "qqqqqqqqq");
                                }
                            }).create().show();

                    break;
            }
        }
    };

    //点击取消预约的点击监听事件
    View.OnClickListener userBookBikeItemsOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.cancel_book:
                    new AlertDialog.Builder(MainActivity.this).setTitle("取消预约")
                            .setMessage("每天可预约5次，确认要取消吗?")
                            .setNegativeButton("取消", null)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Log.d("7777777", "rererer");
                                    cancelBookingBike(bookingCarId, bicycleNo);
                                    Log.d("eeeeeee", bookingCarId);
                                    Log.d("wwwwwww", bicycleNo);
                                    Log.d("7777777", "qqqqqqqqq");
                                }
                            }).create().show();
                    break;
            }
        }
    };


    //根据手机号，查询预约次数
    private void queryBookingNum(String phone) {
        Map<String, String> map = new HashMap<>();
        map.put("phone", phone);
        Log.d("手机号", phone);
        OkHttpUtils.post().url(Api.BASE_URL + Api.BOOKINGNUMBER).params(map).build().execute(new StringCallback() {

            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.e(e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
//                LogUtils.d("次数", response);
                try {
                    JSONObject object = new JSONObject(response);
                    count = object.getString("count");
                    Log.d("次数", count);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //取消预约的方法
    private void cancelBookingBike(String bookingCarId, String bicycleNo) {
        if (bookingCarId != null && !bookingCarId.equals("") && bicycleNo != null && !bicycleNo.equals("")) {
            Map<String, String> map = new HashMap<>();
            map.put("bookingCarId", bookingCarId);
            map.put("bicycleNo", bicycleNo);

            OkHttpUtils.post().url(Api.BASE_URL + Api.CANCELBOOK).params(map).build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    LogUtils.e(e.getMessage());
                }

                @Override
                public void onResponse(String response, int id) {
                    Log.d("7777777", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        String resultStatus = object.getString("resultStatus");
                        int bookingCarStatus = object.getInt("bookingCarStatus");
                        if (resultStatus.equals("1") && bookingCarStatus == 1) {
                            ToastUtils.showLong(MainActivity.this, "取消成功！");
                            isBook = false;
                            if (bookBikePopupWindow != null && !bookBikePopupWindow.equals("")) {
                                bookBikePopupWindow.dismiss();
                                bookBikePopupWindow.setFocusable(true);
                            } else if (!userBookingBikePopupWindow.equals("") && userBookingBikePopupWindow != null) {
                                userBookingBikePopupWindow.setFocusable(true);
                                userBookingBikePopupWindow.dismiss();
                            }
                            LogUtils.d("uuu", bikeInfos + "");
                            setUserMapCenter();
                            mMap.clear();
                            addOverlay(bikeInfos);

                        } else {
                            ToastUtils.showLong(MainActivity.this, "取消失败！");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    //预约车辆的方法
    private void bookingBike(String uID, final String bikeNo) {
        ToastUtils.showShort(this, uID);
        Map<String, String> map = new HashMap<>();
        map.put("userId", uID);
        map.put("bicycleNo", bikeNo);
        OkHttpUtils.post().url(Api.BASE_URL + Api.BOOKBIKE).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.e(e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                isBook = true;
                Log.d("wwwwww", response);
                Gson gson = new Gson();
                bookingBikeInfo = gson.fromJson(response, BookingBikeInfo.class);
                Log.d("111111", bookingBikeInfo + "");
                bookingCarId = bookingBikeInfo.getBookingCarId();
                LogUtils.d("bookingCarId", bookingCarId);
                bookingCarDate = bookingBikeInfo.getBookingCarDate();
                Object address = bookingBikeInfo.getAddress();
                Log.d("34343434", bookingCarDate + "\n" + bookingCarId);
                final String bicycleNo = bookingBikeInfo.getBicycleNo();
                Log.d("预约成功", response);
                bookBikePopupWindow = new BookBikePopupWindow(MainActivity.this, bookingBikeInfo, bookBikeItemsOnClick);
                //指定父视图，显示在父控件的某个位置（Gravity.TOP,Gravity.RIGHT等）
                //  menuWindow.showAtLocation(findViewById(R.id.mapView), Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, -48);
                //设置显示在某个指定控件的下方
                bookBikePopupWindow.showAsDropDown(findViewById(R.id.top));
                timer = (MyCountDownTimer) new MyCountDownTimer(600000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        bookBikePopupWindow.mHoldTime.setText(toClock(millisUntilFinished));
                    }

                    @Override
                    public String toClock(long millis) {
                        return super.toClock(millis);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        cancelBookingBike(bookingCarId, bicycleNo);
                    }
                }.start();
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
                result = bundle.getString("result");
                openScan(phone, result);
                Log.d("aaaaa", phone + "\n" + result);
                ToastUtils.showLong(this, result);
            }
        }
    }

    //扫码开锁的方法
    private void openScan(String phone, final String result) {
        if (phone != null && !phone.equals("") && result != null && !result.equals("")) {
            Log.d("电话", phone + "///////" + result);
            Map<String, String> map = new HashMap<>();
            map.put("phone", phone);
            map.put("bicycleid", result);
            OkHttpUtils.post().url(Api.BASE_URL + Api.OPENSCAN).params(map).build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    LogUtils.e(e.getMessage());
                }

                @Override
                public void onResponse(String response, int id) {
                    Log.d("开锁", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        String message = object.getString("message");
                        ToastUtils.showLong(MainActivity.this, message);
                        if (message.equals("开锁成功")) {
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("userId", uID);
                            map.put("bicycleNo", result);
                            OkHttpUtils.post().url(Api.BASE_URL + Api.RENTALORDER).params(map).build().execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    LogUtils.e(e.getMessage());
                                }

                                @Override
                                public void onResponse(String response, int id) {
                                    LogUtils.d("rrrrrr", response);
                                    isShowRideOrder = true;
                                    Gson gson = new Gson();
                                    bikeRentalOrderInfo = gson.fromJson(response, BikeRentalOrderInfo.class);
                                    String bicycleNo = bikeRentalOrderInfo.getBicycleNo();
                                    ToastUtils.showShort(MainActivity.this, bicycleNo);
                                    orderPopupWindow = new BikeRentalOrderPopupWindow(MainActivity.this, bikeRentalOrderInfo);
                                    mMap.clear();
                                    orderPopupWindow.showAsDropDown(findViewById(R.id.top));
                                    orderPopupWindow.setOutsideTouchable(false);
                                    orderPopupWindow.setFocusable(false);
                                }
                            });
                        }

                        ToastUtils.showLong(MainActivity.this, message);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
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
        countDistance(currentLatLng, latLng);
        paintingLine(currentLatLng, latLng);

    }

    //根据两点之间的坐标，重新画路线的方法
    private void paintingLine(LatLng currentLatLng, LatLng latLng) {
        RoutePlanSearch search = RoutePlanSearch.newInstance();        //百度的搜索路线的类
        //步行路线参数类
        WalkingRoutePlanOption walkingRoutePlanOption = new WalkingRoutePlanOption();
        //起始坐标和终点坐标
        PlanNode startPlanNode = PlanNode.withLocation(currentLatLng);  // lat  long
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
                    mDuration = walkingRouteLine.getDuration();
                }
                //获取所有步行规划路线
                //返回:所有步行规划路线
                WalkingRouteOverlay overlay = new MyWalkingRouteOverlay(mMap);
                /**
                 * 设置地图 Marker 覆盖物点击事件监听者
                 * 需要实现的方法：     onMarkerClick(Marker marker)
                 * */
                if (!overlay.equals("") && overlay != null) {

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

    //第一次点击图标时，画路线的方法
    private void countDistance(LatLng currentLatLng, final LatLng latLng) {
        RoutePlanSearch search = RoutePlanSearch.newInstance();        //百度的搜索路线的类
        //步行路线参数类
        WalkingRoutePlanOption walkingRoutePlanOption = new WalkingRoutePlanOption();

        //起始坐标和终点坐标
        PlanNode startPlanNode = PlanNode.withLocation(currentLatLng);  // lat  long
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
                    mDuration = walkingRouteLine.getDuration();
                    Log.d("距离", mDuration + "米");
                    menuWindow = new SelectPicPopupWindow(MainActivity.this, itemsOnClick);
                    //指定父视图，显示在父控件的某个位置（Gravity.TOP,Gravity.RIGHT等）
                    //  menuWindow.showAtLocation(findViewById(R.id.mapView), Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, -48);
                    //设置显示在某个指定控件的下方
                    menuWindow.showAsDropDown(findViewById(R.id.top));
                }

                //获取所有步行规划路线
                //返回:所有步行规划路线
                WalkingRouteOverlay overlay = new MyWalkingRouteOverlay(mMap);
                /**
                 * 设置地图 Marker 覆盖物点击事件监听者
                 * 需要实现的方法：     onMarkerClick(Marker marker)
                 * */
                if (!overlay.equals("") && overlay != null) {

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
            currentLatLng = new LatLng(mCurrentLantitude, mCurrentLongitude);

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
                if (uID != null && !uID.equals("")) {
                    checkBookingBikeInfoByUserID(uID);
                }
                //根据手机定位地点，得到手机定位点的周围半径1000米范围内的车辆信息的方法
                getBikeInfo(mCurrentLantitude, mCurrentLongitude);
            }
            //根据手机定位的不同得到定位点信息，将这个信息传递给搜索页面
            Address address = location.getAddress();
            address1 = address.address;
        }
    }

    //获得自行车信息的方法
    private void getBikeInfo(double mCurrentLantitude, double mCurrentLongitude) {
        String lat = mCurrentLantitude + "";
        String lng = mCurrentLongitude + "";
        Map<String, String> map = new HashMap<>();
        map.put("lng", lng);
        map.put("lat", lat);
        OkHttpUtils.post().url(Api.BASE_URL + Api.GINPUT).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.e(e.getMessage());
                ToastUtils.showShort(MainActivity.this, "连接服务器失败");
            }

            @Override
            public void onResponse(String response, int id) {
                if (response != null && !response.equals("")) {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Log.d("自行车", jsonObject + "");
                            bikeInfo = new BikeInfo();
                            bikeInfo.setAddress(jsonObject.getString("address"));
                            bikeInfo.setBicycleId(jsonObject.getInt("bicycleId"));
                            bikeInfo.setLatitude(jsonObject.getString("latitude"));
                            bikeInfo.setLongitude(jsonObject.getString("longitude"));
                            bikeInfo.setUnitPrice(jsonObject.getInt("unitPrice"));
                            bikeInfo.setBicycleNo(jsonObject.getInt("bicycleNo"));
                            bikeInfos.add(bikeInfo);
                        }
                        Log.d("车辆信息", bikeInfos + "");
                        Log.d("车辆信息", bikeInfos.size() + "");
                        addOverlay(bikeInfos);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    //    private void setBaiduMapMark() {
//        LatLng point = new LatLng(mCurrentLantitude, mCurrentLongitude);
//        //设置Mark(覆盖物)图标
//        BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
//                .fromResource(R.mipmap.search_center_ic);
//        OverlayOptions options = new MarkerOptions()
//                .position(point)
//                .icon(mCurrentMarker)
//                .draggable(true);
//        mMarker = (Marker) mMap.addOverlay(options);
//    }
    //设置中心点
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

            if (SPUtils.isLogin() && cashStatus == 1 && status == 1) {
                mOrder.setText("预约用车");
            } else if (SPUtils.isLogin() && cashStatus == 0) {
                mOrder.setText("完成注册即可骑单车");
            } else if (SPUtils.isLogin() && cashStatus == 1 && status == 0) {
                mOrder.setText("完成注册即可骑单车");
            } else {
                mOrder.setText("立即登录即可骑单车");
            }
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
//            ColorDrawable dw = new ColorDrawable(0x00000000);
            // 设置SelectPicPopupWindow弹出窗体的背景
//            this.setBackgroundDrawable(dw);
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

//    public class BookBikePopupWindow extends PopupWindow {
//        TextView mBookBikeLocationInfo;
//        TextView mBikeNumber;
//        TextView mHoldTime;
//        Button mCancel;
//        private View mCancelBookBikeWindow;
//
//
//        public BookBikePopupWindow(Context context, View.OnClickListener bookBikeItemsOnClick) {
//            super(context);
//            //创建布局反射器
//            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            //加载布局
//            mCancelBookBikeWindow = inflater.inflate(R.layout.item_book_bike, null);
//            //初始化控件
//            mBookBikeLocationInfo = (TextView) mCancelBookBikeWindow.findViewById(R.id.book_bike_location_info);
//            mBikeNumber = (TextView) mCancelBookBikeWindow.findViewById(R.id.bikeNumber);
//            mHoldTime = (TextView) mCancelBookBikeWindow.findViewById(R.id.hold_time);
//            mCancel = (Button) mCancelBookBikeWindow.findViewById(R.id.cancel_book);
//            //为控件赋值
//            mBookBikeLocationInfo.setText(resultAddress);
//            Log.d("自行车标号", bicycleNo + "");
//            mBikeNumber.setText(String.valueOf(bicycleNo));

//            // 设置按钮监听
//            mCancel.setOnClickListener(bookBikeItemsOnClick);
//            // 设置SelectPicPopupWindow的View
//            this.setContentView(mCancelBookBikeWindow);
//            // 设置SelectPicPopupWindow的View
//            this.setContentView(mCancelBookBikeWindow);
//            // 设置SelectPicPopupWindow弹出窗体的宽
//            this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
//            // 设置SelectPicPopupWindow弹出窗体的高
//            this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
//            // 设置SelectPicPopupWindow弹出窗体可点击
////            this.setFocusable(false);
//////             设置SelectPicPopupWindow弹出窗体动画效果
////            this.setAnimationStyle(R.style.PopupAnimation);
//            // 实例化一个ColorDrawable颜色为半透明
////        ColorDrawable dw = new ColorDrawable(0x00000000);
//            // 设置SelectPicPopupWindow弹出窗体的背景
////        this.setBackgroundDrawable(dw);
//            // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
//            mCancelBookBikeWindow.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                @SuppressLint("ClickableViewAccessibility")
//                public boolean onTouch(View v, MotionEvent event) {
//
//                    int height = mCancelBookBikeWindow.findViewById(R.id.book_bike_pop_layout).getTop();
//                    int y = (int) event.getY();
//                    if (event.getAction() == MotionEvent.ACTION_UP) {
//                        if (y < height) {
//                            dismiss();
//                        }
//                    }
//                    return true;
//                }
//            });
//        }
//
//
//    }


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

    /*
    * 获取系统的北京时间
    */
    private String getStringDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    //计算系统时间和车辆预定时间的时间差
    public long countTime(String stringDate, String bookingCarDate) {
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date1 = df.parse(stringDate);
            Date date2 = df.parse(bookingCarDate);
            //这样得到的差值是微秒级别
            diff = date1.getTime() - date2.getTime();
            LogUtils.d("时间差", diff + "");
//            long days = diff / (1000 * 60 * 60 * 24);
//            long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
//            long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return diff;
    }
}
