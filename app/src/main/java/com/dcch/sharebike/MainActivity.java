package com.dcch.sharebike;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
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
import com.dcch.sharebike.base.CodeEvent;
import com.dcch.sharebike.base.MessageEvent;
import com.dcch.sharebike.http.Api;
import com.dcch.sharebike.libzxing.zxing.activity.CaptureActivity;
import com.dcch.sharebike.listener.MyOrientationListener;
import com.dcch.sharebike.moudle.home.bean.BikeInfo;
import com.dcch.sharebike.moudle.home.bean.BikeRentalOrderInfo;
import com.dcch.sharebike.moudle.home.bean.BookingBikeInfo;
import com.dcch.sharebike.moudle.home.bean.RidingInfo;
import com.dcch.sharebike.moudle.home.bean.ShowBikeRentalOrderInfo;
import com.dcch.sharebike.moudle.home.bean.UserBookingBikeInfo;
import com.dcch.sharebike.moudle.login.activity.ClickCameraPopupActivity;
import com.dcch.sharebike.moudle.login.activity.ClickMyHelpActivity;
import com.dcch.sharebike.moudle.login.activity.IdentityAuthentication;
import com.dcch.sharebike.moudle.login.activity.LoginActivity;
import com.dcch.sharebike.moudle.login.activity.PersonalCenterActivity;
import com.dcch.sharebike.moudle.login.activity.RechargeActivity;
import com.dcch.sharebike.moudle.search.activity.SeekActivity;
import com.dcch.sharebike.moudle.user.activity.CustomerServiceActivity;
import com.dcch.sharebike.moudle.user.activity.RechargeDepositActivity;
import com.dcch.sharebike.moudle.user.activity.RidingResultActivity;
import com.dcch.sharebike.moudle.user.activity.UnlockProgressActivity;
import com.dcch.sharebike.moudle.user.bean.UserInfo;
import com.dcch.sharebike.overlayutil.OverlayManager;
import com.dcch.sharebike.overlayutil.WalkingRouteOverlay;
import com.dcch.sharebike.service.GPSService;
import com.dcch.sharebike.utils.JsonUtils;
import com.dcch.sharebike.utils.LogUtils;
import com.dcch.sharebike.utils.MapUtil;
import com.dcch.sharebike.utils.SPUtils;
import com.dcch.sharebike.utils.ToastUtils;
import com.dcch.sharebike.view.BikeRentalOrderPopupWindow;
import com.dcch.sharebike.view.BookBikePopupWindow;
import com.dcch.sharebike.view.MyCountDownTimer;
import com.dcch.sharebike.view.SelectPicPopupWindow;
import com.dcch.sharebike.view.ShowBikeRentalOrderPopupWindow;
import com.dcch.sharebike.view.UserBookingBikePopupWindow;
import com.google.gson.Gson;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.bottomsheet.BottomSheetBean;
import com.hss01248.dialog.interfaces.MyItemDialogListener;
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
import java.util.TreeSet;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static com.dcch.sharebike.R.id.seek;
import static com.dcch.sharebike.utils.MapUtil.stringToInt;


@RuntimePermissions
public class MainActivity extends BaseActivity implements BaiduMap.OnMapStatusChangeListener, OnGetRoutePlanResultListener {
    @BindView(R.id.mapView)
    MapView mMapView;
    @BindView(R.id.MyCenter)
    ImageView mMyCenter;
    @BindView(seek)
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
    BaiduMap mMap;
    public MyLocationListener mMyLocationListener;//定位的监听器

    //显示marker
    private boolean showMarker = false;
    boolean useDefaultIcon = false;
    private long mExitTime; //退出时间
    OverlayManager routeOverlay = null;//该类提供一个能够显示和管理多个Overlay的基类

    private LocationClient mLocationClient;//定位的客户端

    private MyLocationConfiguration.LocationMode mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;//当前定位的模式

    private volatile boolean isFristLocation = true;//是否是第一次定位

    private double mCurrentLantitude;//最新一次的经纬度
    private double mCurrentLongitude;

    private double changeLatitude, changeLongitude;

    private float mCurrentAccracy;//当前的精度

    private MyOrientationListener myOrientationListener;//方向传感器的监听器

    private int mXDirection;//方向传感器X方向的值
    private GeoCoder mSearch = null;
    //    private Address locationDescribe;
    //POI搜索相关
    // public PoiSearch mPoiSearch = null;
    private SelectPicPopupWindow menuWindow = null; // 自定义弹出框
    //步行距离
    private int distance;
    //步行时间
    private int mWalkTime;
    private String resultAddress;
    private String address1;
    private List<BikeInfo> bikeInfos;
    private BikeInfo bikeInfo;
    private BookBikePopupWindow bookBikePopupWindow = null;
    private UserBookingBikePopupWindow userBookingBikePopupWindow = null;
    private int cashStatus;
    private int status;
    private String bookingCarId;
    private String phone;
    private String result;
    private String userDetail;
    private JSONObject object;
    private String uID;
    private String bookingCarDate;
    private UserBookingBikeInfo userBookingBikeInfo = null;
    private Double locationLongitude;
    private Double locationLatitude;
    private LatLng clickMarkLatlng;
    private LatLng currentLatLng;
    private boolean isChecked = false;
    private boolean isBook = false;
    private boolean isShowRideOrder = false;
    private boolean isShowBookOrder = false;
    private boolean isClick = true;
    private boolean hasPlanRoute = false;
    private double clickLat;
    private double clickLon;
    private String bicycleNo = "";
    private MyCountDownTimer timer;
    private String count;
    private BookingBikeInfo bookingBikeInfo = null;
    private BikeRentalOrderInfo bikeRentalOrderInfo = null;
    private static BikeRentalOrderPopupWindow orderPopupWindow = null;
    private String mCastTime;
    private String mDistance;
    private WalkingRouteLine walkingRouteLine;
    private LocationReceiver lr;
    private AlarmManager alarmManager;
    private PendingIntent pi;
    private static final String LOCSTART = "START_LOCATING";
    PlanNode startNodeStr, endNodeStr;
    WalkingRouteResult nowResultwalk = null;
    RouteLine routeLine = null;
    RoutePlanSearch mRPSearch = null;    // 搜索模块，也可去掉地图模块独立使用
    private double mLat1;
    private double mLng1;
    private Intent mServiceIntent;
    ResultReceiver mResultReceiver;
    private ShowBikeRentalOrderInfo mShowBikeRentalOrderInfo;
    private ShowBikeRentalOrderPopupWindow mShowBikeRentalOrderPopupWindow;
    Marker mMarker = null;
    private String mToken;

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
        bikeInfos = new ArrayList<BikeInfo>();
        // 初始化GeoCoder模块，注册事件监听
//        mSearch = GeoCoder.newInstance();
//        mSearch.setOnGetGeoCodeResultListener(this);
        mRPSearch = RoutePlanSearch.newInstance();
        mRPSearch.setOnGetRoutePlanResultListener(this);
        mMap = mMapView.getMap();
        mMapView.showZoomControls(false);
        //隐藏logo和缩放图标child instanceof ImageView ||
        View child = mMapView.getChildAt(1);
        if (child != null && child instanceof ZoomControls) {
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

    private void queryUserInfo(String uID, String token) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", uID);
        map.put("token", token);

        OkHttpUtils.post().url(Api.BASE_URL + Api.INFOUSER).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.showShort(MainActivity.this, "服务器忙，请重试");
            }

            @Override
            public void onResponse(String response, int id) {
                Log.d("个人信息", response);
                if (JsonUtils.isSuccess(response)) {
                    Gson gson = new Gson();
                    UserInfo userInfo = gson.fromJson(response, UserInfo.class);
                    status = userInfo.getStatus();
                    cashStatus = userInfo.getCashStatus();
                    phone = userInfo.getPhone();
                    LogUtils.d("个人信息", status + "\n" + cashStatus + "\n" + phone);
                }
            }
        });
    }

    //进入主页面检查客户是否有预约订单的方法
    private void checkBookingBikeInfoByUserID(final String uID) {
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
                                 Log.d("你好", response);
                                 if (JsonUtils.isSuccess(response)) {
                                     Gson gson = new Gson();
                                     userBookingBikeInfo = gson.fromJson(response, UserBookingBikeInfo.class);
                                     String bookingCarDate = userBookingBikeInfo.getBookingCarDate();
                                     String stringDate = MapUtil.getStringDate();
                                     long countTime = 600000 - MapUtil.countTime(stringDate, bookingCarDate);
                                     LogUtils.d("预约时间", countTime + "");
                                     if (countTime > 0) {
                                         mMap.clear();
                                         bicycleNo = userBookingBikeInfo.getBicycleNo();
                                         resultAddress = userBookingBikeInfo.getAddress();
                                         bookingCarId = userBookingBikeInfo.getBookingCarId();
                                         locationLongitude = Double.valueOf(userBookingBikeInfo.getLongitude());
                                         locationLatitude = Double.valueOf(userBookingBikeInfo.getLatitude());
                                         isShowBookOrder = true;
                                         isChecked = true;
                                         isClick = false;
                                         isBook = true;
                                         forLocationAddMark(locationLongitude, locationLatitude);
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
                                         userBookingBikePopupWindow.dismiss();
                                         timer.cancel();
                                     }

                                 } else {
                                     //根据手机定位地点，得到手机定位点的周围半径1000米范围内的车辆信息的方法
//                                     getBikeInfo(mCurrentLantitude, mCurrentLongitude);
                                     checkOrderInfoByUserID(uID);
                                 }
                             }
                         }
                );
    }

    //客户有预约订单时，显示单个车辆位置的覆盖物
    private void forLocationAddMark(Double locationLongitude, Double locationLatitude) {
//        mMap.clear();
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.bike_icon);
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
                    mMapView.setEnabled(false);
                    isChecked = true;
                } else if (isShowRideOrder) {
                    mMap.clear();
                    mMapView.setEnabled(false);

                } else if (isShowBookOrder) {
                    mMapView.setEnabled(false);
                    isChecked = true;
                    forLocationAddMark(clickLon, clickLat);
                } else {
                    isChecked = false;
                    mMap.clear();
                    addOverlay(bikeInfos);
                    setUserMapCenter(mCurrentLantitude, mCurrentLongitude);
                }
                //由于menuWindow会和地图抢夺焦点，所以在设置他的属性时设置为不能获得焦点
                //就能够满足一起消失的功能menuWindow != null &&
                if (menuWindow != null && menuWindow.isShowing() && routeOverlay != null) {
                    menuWindow.dismiss();
                    routeOverlay.removeFromMap();
                    setUserMapCenter(mCurrentLantitude, mCurrentLongitude);
                }

                if (SPUtils.isLogin()) {
                    mInstructions.setVisibility(View.GONE);
                } else {
                    mInstructions.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });
    }

    @Override
    protected void initListener() {
        //地图状态改变相关监听
        mMap.setOnMapStatusChangeListener(this);
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
        option.setScanSpan(5000);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
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


    @OnClick({R.id.MyCenter, R.id.btn_my_location, R.id.scan, seek, R.id.instructions, R.id.btn_my_help})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.MyCenter:
                Intent personal = new Intent(this, PersonalCenterActivity.class);
                startActivity(personal);
                break;
            case R.id.btn_my_help:
                if (SPUtils.isLogin()) {
                    popupDialog();
                } else {
                    startActivity(new Intent(this, ClickMyHelpActivity.class));
                }
                break;

            case R.id.instructions:
                Intent i2 = new Intent(this, PersonalCenterActivity.class);
                startActivity(i2);
                break;

            case R.id.seek:
                if (isClick) {
                    Intent seek = new Intent(this, SeekActivity.class);
                    seek.putExtra("address", address1);
                    startActivityForResult(seek, 1);
                }
                if (!isClick) {
                    ToastUtils.showShort(MainActivity.this, "在预约和骑行过程中，此功能不可用！");
                }
                break;

            case R.id.btn_my_location:
                setUserMapCenter(mCurrentLantitude, mCurrentLongitude);
                getMyLocation();
                break;

            case R.id.scan:
                if (SPUtils.isLogin()) {
                    if (cashStatus == 1 && status == 1 && mToken != null) {
                        checkAggregate(uID, mToken);
                    } else if (cashStatus == 0 && status == 0) {
                        startActivity(new Intent(this, RechargeActivity.class));
                    } else if (cashStatus == 1 && status == 0) {
                        startActivity(new Intent(this, IdentityAuthentication.class));
                    } else if (cashStatus == 0 && status == 1) {
                        startActivity(new Intent(this, RechargeDepositActivity.class));
                    }
                } else {
////                    没有登录的情况设置Activity
                    mBtnMyHelp.setVisibility(View.GONE);
                    mBtnMyLocation.setVisibility(View.GONE);
                    mScan.setVisibility(View.GONE);
                    startActivity(new Intent(MainActivity.this, ClickCameraPopupActivity.class));
                }
                break;
        }
    }

    public void getMyLocation() {
        MyLocationData data = new MyLocationData.Builder()
                .accuracy(1000)//范围半径，单位：米
                .latitude(mCurrentLantitude)//
                .longitude(mCurrentLongitude).build();
        mMap.setMyLocationData(data);
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
            BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.bike_icon);
            LatLng latLng = null;

            List<Double> doubles = new ArrayList<>();
            for (int i = 0; i < bikeInfos.size(); i++) {
                bikeInfo = (BikeInfo) bikeInfos.get(i);
                String lat = bikeInfo.getLatitude();
                String lng = bikeInfo.getLongitude();
                mLat1 = Double.parseDouble(lat);
                mLng1 = Double.parseDouble(lng);
                latLng = new LatLng(mLat1, mLng1);
//                //两点之间直线距离的算法
//                double distance1 = DistanceUtil.getDistance(latLng, currentLatLng);
//                doubles.add(distance1);
                //设置marker
                OverlayOptions options = new MarkerOptions()
                        .position(latLng)//设置位置
                        .icon(bitmap)//设置图标样式
                        .zIndex(i) // 设置marker所在层级
                        .draggable(true); // 设置手势拖拽;
                //添加marker
                mMarker = (Marker) mMap.addOverlay(options);
                //使用marker携带info信息，当点击事件的时候可以通过marker获得info信息
                Bundle bundle = new Bundle();
                // bikeInfo必须实现序列化接口
                bundle.putSerializable("bikeInfo", bikeInfo);
                mMarker.setExtraInfo(bundle);
            }

        } else {
            ToastUtils.showLong(this, "当前周围没有车辆");
        }

    }

    //百度地图的覆盖物点击方法
    private void clickBaiduMapMark() {
        final TreeSet<Integer> integers = new TreeSet<>();
        mMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                if (isChecked) {
                    return false;
                }
                if (marker.getExtraInfo() != null && marker != null) {
                    int zIndex = marker.getZIndex();
                    integers.add(Integer.valueOf(zIndex));
                    LogUtils.d("覆盖物", zIndex + "\n" + integers.size());
                    Bundle bundle = marker.getExtraInfo();
                    clickMarkLatlng = marker.getPosition();
                    bikeInfo = (BikeInfo) bundle.getSerializable("bikeInfo");
                    if (bikeInfo != null) {
                        bicycleNo = bikeInfo.getBicycleNo() + "";
                        if (menuWindow == null || !menuWindow.isShowing()) {
                            showMenuWindow(bikeInfo);
                        }
                        updateBikeInfo(bikeInfo);
                    }
//                    reverseGeoCoder(clickMarkLatlng);
                }
                if (userBookingBikePopupWindow != null && userBookingBikePopupWindow.isShowing()) {
                    mMapView.setFocusable(false);
                    mMapView.setEnabled(false);
                }
                mInstructions.setVisibility(View.GONE);
                mMap.clear();
                addOverlay(bikeInfos);//
                return true;
            }
        });
    }

    private void initNearestBike(final BikeInfo bikeInfo, LatLng ll) {
        ImageView nearestIcon = new ImageView(getApplicationContext());
        nearestIcon.setImageResource(R.mipmap.nearest_icon);
        InfoWindow.OnInfoWindowClickListener listener = null;
        listener = new InfoWindow.OnInfoWindowClickListener() {
            public void onInfoWindowClick() {
                updateBikeInfo(bikeInfo);
                mMap.hideInfoWindow();
            }
        };
        InfoWindow mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(nearestIcon), ll, -118, listener);
        mMap.showInfoWindow(mInfoWindow);
    }


    private void updateBikeInfo(BikeInfo bikeInfo) {
        if (!hasPlanRoute) {
            this.bikeInfo = bikeInfo;
            Double doulat = Double.valueOf(bikeInfo.getLatitude());
            Double doulon = Double.valueOf(bikeInfo.getLongitude());
            endNodeStr = PlanNode.withLocation(new LatLng(doulat, doulon));
            drawPlanRoute(endNodeStr);
        }
    }

    private void drawPlanRoute(PlanNode endNodeStr) {
        if (routeOverlay != null)
            routeOverlay.removeFromMap();
        if (endNodeStr != null) {
            Log.d("gao", "changeLatitude-----startNode--------" + startNodeStr.getLocation().latitude);
            Log.d("gao", "changeLongitude-----startNode--------" + startNodeStr.getLocation().longitude);
            mRPSearch.walkingSearch((new WalkingRoutePlanOption()).from(startNodeStr).to(endNodeStr));
        }
    }

    //预约车辆的点击监听事件
    View.OnClickListener itemsOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.order:
                    if (SPUtils.isLogin()) {
                        if (menuWindow != null && menuWindow.isShowing()) {
                            menuWindow.dismiss();
                        }
                        mMap.clear();
                        addOverlay(bikeInfos);

                        if (cashStatus == 1 && status == 1) {
                            mInstructions.setVisibility(View.GONE);
                            if (phone != null && !phone.equals("")) {
                                queryBookingNum(phone);
                            }
                        } else if (cashStatus == 0 && status == 0) {
                            startActivity(new Intent(MainActivity.this, RechargeActivity.class));
                        } else if (cashStatus == 1 && status == 0) {
                            startActivity(new Intent(MainActivity.this, IdentityAuthentication.class));
                        } else if (cashStatus == 0 && status == 1) {
                            startActivity(new Intent(MainActivity.this, RechargeDepositActivity.class));
                        }

                    } else {
                        mInstructions.setVisibility(View.VISIBLE);
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        menuWindow.setFocusable(true);
                        menuWindow.dismiss();
                        mMap.clear();
                        addOverlay(bikeInfos);
                        setUserMapCenter(mCurrentLantitude, mCurrentLongitude);
                    }
            }
        }
    };

    //点击取消预约的点击监听事件
    View.OnClickListener bookBikeItemsOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.cancel_book:
                    new AlertDialog.Builder(MainActivity.this).setTitle(R.string.cancel_title)
                            .setMessage(R.string.cancel_tip)
                            .setNegativeButton("取消", null)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    cancelBookingBike(bookingCarId, bicycleNo);
                                }
                            }).create().show();
                    break;

                case R.id.forBellIcon:
                    ToastUtils.showShort(MainActivity.this, "寻车铃");
                    bookBikePopupWindow.forBellIcon.setImageResource(R.drawable.frame);
                    AnimationDrawable anim = (AnimationDrawable) bookBikePopupWindow.forBellIcon.getDrawable();
                    anim.start();
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
                                    cancelBookingBike(bookingCarId, bicycleNo);
                                }
                            }).create().show();
                    break;
                case R.id.forBellIcon:
                    userBookingBikePopupWindow.forBellIcon.setImageResource(R.drawable.frame);
                    AnimationDrawable anim = (AnimationDrawable) userBookingBikePopupWindow.forBellIcon.getDrawable();
                    anim.start();
                    break;
            }
        }
    };

    //根据手机号，查询预约次数
    private void queryBookingNum(String phone) {
        Map<String, String> map = new HashMap<>();
        map.put("phone", phone);
        OkHttpUtils.post().url(Api.BASE_URL + Api.BOOKINGNUMBER).params(map).build().execute(new StringCallback() {

            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.e(e.getMessage());
                ToastUtils.showShort(MainActivity.this, "抱歉，服务器正忙！");
            }

            @Override
            public void onResponse(String response, int id) {
                Log.d("次数", response);
                // {"count":"4","resultStatus":"1"} {"count":"","resultStatus":"0"}
                if (JsonUtils.isSuccess(response)) {
                    try {
                        JSONObject object = new JSONObject(response);
                        count = object.optString("count");
                        if (Integer.valueOf(count).intValue() < 5) {
                            if (menuWindow != null && !menuWindow.equals("")) {
                                menuWindow.dismiss();
                            }
                            final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
                            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条的形式为圆形转动的进度条
                            dialog.setCancelable(false);// 设置是否可以通过点击Back键取消
                            dialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
                            dialog.setTitle(null);
                            // dismiss监听
                            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

                                @Override
                                public void onDismiss(DialogInterface dialog) {

                                }
                            });
                            // 监听Key事件被传递给dialog
                            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                                @Override
                                public boolean onKey(DialogInterface dialog, int keyCode,
                                                     KeyEvent event) {
                                    return false;
                                }
                            });
                            // 监听cancel事件
                            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    if (clickMarkLatlng != null && !clickMarkLatlng.equals("")) {
                                        clickLat = clickMarkLatlng.latitude;
                                        clickLon = clickMarkLatlng.longitude;
                                        Log.d("查看信息", clickMarkLatlng.latitude + "\n" + clickMarkLatlng.longitude);
                                        forLocationAddMark(clickLon, clickLat);
                                    }
                                    bookingBike(uID, bicycleNo, mToken);
                                }
                            });
                            dialog.setMessage("预约申请中....");
                            dialog.show();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Thread.sleep(3000);
                                        // cancel和dismiss方法本质都是一样的，都是从屏幕中删除Dialog,唯一的区别是
                                        // 调用cancel方法会回调DialogInterface.OnCancelListener如果注册的话,dismiss方法不会回调
                                        dialog.cancel();
                                        // dialog.dismiss();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();

                        } else {
                            ToastUtils.showLong(MainActivity.this, "抱歉，您今天已经预约5次，明天再来吧。");
                            menuWindow.setFocusable(true);
                            menuWindow.dismiss();
                            mMap.clear();
                            addOverlay(bikeInfos);
                            setUserMapCenter(mCurrentLantitude, mCurrentLongitude);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    ToastUtils.showShort(MainActivity.this, "抱歉，服务器正忙！");
                    menuWindow.dismiss();
                    mMap.clear();
                    addOverlay(bikeInfos);
                    setUserMapCenter(mCurrentLantitude, mCurrentLongitude);
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
                    Log.e("错误", e.getMessage());
                }

                @Override
                public void onResponse(String response, int id) {
                    try {
                        JSONObject object = new JSONObject(response);
                        String resultStatus = object.getString("resultStatus");
                        int bookingCarStatus = object.getInt("bookingCarStatus");
                        if (resultStatus.equals("1") && bookingCarStatus == 1) {
                            ToastUtils.showLong(MainActivity.this, "取消成功！");
                            isBook = false;
                            isChecked = false;
                            isClick = true;
                            if (bookBikePopupWindow != null && !bookBikePopupWindow.equals("")) {
                                bookBikePopupWindow.dismiss();
                                bookBikePopupWindow.setFocusable(true);
                                timer.cancel();
                            } else if (!userBookingBikePopupWindow.equals("") && userBookingBikePopupWindow != null) {
                                userBookingBikePopupWindow.setFocusable(true);
                                userBookingBikePopupWindow.dismiss();
                                timer.cancel();
                            }
                            isShowBookOrder = false;
                            mMap.clear();
                            //根据手机定位地点，得到手机定位点的周围半径1000米范围内的车辆信息的方法
                            getBikeInfo(mCurrentLantitude, mCurrentLongitude);
                            setUserMapCenter(mCurrentLantitude, mCurrentLongitude);
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
    private void bookingBike(String uID, String bikeNo, String mToken) {
        if (uID != null && bikeNo != null) {
            Map<String, String> map = new HashMap<>();
            map.put("userId", uID);
            map.put("bicycleNo", bikeNo);
            map.put("token", mToken);
            OkHttpUtils.post().url(Api.BASE_URL + Api.BOOKBIKE).params(map).build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    ToastUtils.showShort(MainActivity.this, "服务器正忙，请稍后再试！");
                }

                @Override
                public void onResponse(String response, int id) {
                    if (JsonUtils.isSuccess(response)) {
                        LogUtils.d("查看信息", response);
                        Gson gson = new Gson();
                        bookingBikeInfo = gson.fromJson(response, BookingBikeInfo.class);
                        mMap.clear();
                        mMapView.setFocusable(false);
                        mMapView.setEnabled(false);
                        forLocationAddMark(clickLon, clickLat);
                        endNodeStr = PlanNode.withLocation(new LatLng(clickLat, clickLon));
                        drawPlanRoute(endNodeStr);
                        bookingCarId = bookingBikeInfo.getBookingCarId();
                        bookingCarDate = bookingBikeInfo.getBookingCarDate();
                        final String bicycleNo = bookingBikeInfo.getBicycleNo();
                        isChecked = true;
                        isBook = true;
                        isClick = false;
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

                    } else {
                        ToastUtils.showShort(MainActivity.this, "预约失败，请重新预约！");
                    }

                }
            });
        }
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
                    reportIllegalParking.putExtra("name", "2");
                    startActivity(reportIllegalParking);
                }
                if (position == 3) {
                    Intent otherProblem = new Intent(MainActivity.this, CustomerServiceActivity.class);
                    otherProblem.putExtra("name", "3");
                    startActivity(otherProblem);
                }
            }
        }).show();
    }

    //扫一扫二维码时的回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Bundle bundle = data.getExtras();
            double lat = data.getDoubleExtra("lat", 0);
            double lon = data.getDoubleExtra("lon", 0);
            switch (requestCode) {
                case 0:
                    if (bundle != null) {
                        result = bundle.getString("result");
                        openScan(uID, phone, result, mToken);
                        Intent intent = new Intent(MainActivity.this, UnlockProgressActivity.class);
                        startActivity(intent);
                        ToastUtils.showLong(this, result);
                    }
                    break;

                case 1:
                    setUserMapCenter(lat, lon);
                    break;
            }

        }
    }

    //扫码开锁的方法
    private void openScan(final String uID, String phone, final String result, final String mToken) {
        if (phone != null && !phone.equals("") && result != null && !result.equals("")) {
            Map<String, String> map = new HashMap<>();
            map.clear();
            map.put("userId", uID);
            map.put("phone", phone);
            map.put("bicycleNo", result);
            map.put("token", mToken);
            LogUtils.d("开锁", phone);
            LogUtils.d("开锁", result);
            LogUtils.d("开锁", uID);
            OkHttpUtils.post().url(Api.BASE_URL + Api.OPENSCAN).params(map).build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    if (!e.equals("") && e != null) {
                        LogUtils.e(e.getMessage());
                    }
                    ToastUtils.showShort(MainActivity.this, "服务器忙，请稍后重试");
                }

                @Override
                public void onResponse(String response, int id) {
                    Log.d("开锁", response);
                    ToastUtils.showShort(MainActivity.this, response);
                    if (JsonUtils.isSuccess(response)) {
                        LogUtils.d("开锁", "什么情况！");
                        EventBus.getDefault().post(new MessageEvent(), "on");
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("userId", uID);
                        map.put("bicycleNo", result);
                        map.put("token", mToken);
                        OkHttpUtils.post().url(Api.BASE_URL + Api.RENTALORDER).params(map).build().execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                LogUtils.e(e.getMessage());
                                ToastUtils.showShort(MainActivity.this, "服务器忙！！！");
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Log.d("骑行订单", response);
                                if (JsonUtils.isSuccess(response)) {
                                    isShowRideOrder = true;
                                    isClick = false;
                                    Gson gson = new Gson();
                                    bikeRentalOrderInfo = gson.fromJson(response, BikeRentalOrderInfo.class);
                                    mServiceIntent = new Intent(MainActivity.this, GPSService.class);
                                    mServiceIntent.putExtra("token", mToken);
                                    mServiceIntent.putExtra("userId", uID);
                                    mServiceIntent.putExtra("bicycleNo", bikeRentalOrderInfo.getBicycleNo());
                                    mServiceIntent.putExtra("carRentalOrderDate", bikeRentalOrderInfo.getCarRentalOrderDate());
                                    mServiceIntent.putExtra("carRentalOrderId", bikeRentalOrderInfo.getCarRentalOrderId());
                                    LogUtils.d("神圣", uID + "\n" + bikeRentalOrderInfo.getBicycleNo() + "\n" + bikeRentalOrderInfo.getCarRentalOrderDate() + "\n" + bikeRentalOrderInfo.getCarRentalOrderId());
                                    startService(mServiceIntent);
                                    mScan.setVisibility(View.INVISIBLE);
                                    String bicycleNo = bikeRentalOrderInfo.getBicycleNo();
                                    ToastUtils.showShort(MainActivity.this, bicycleNo);
                                    if (menuWindow != null) {
                                        menuWindow.dismiss();
                                    }
                                    if (bookBikePopupWindow != null) {
                                        bookBikePopupWindow.dismiss();
                                    }
                                    if (userBookingBikePopupWindow != null) {
                                        userBookingBikePopupWindow.dismiss();
                                    }
                                    mMap.clear();
                                    orderPopupWindow = new BikeRentalOrderPopupWindow(MainActivity.this, bikeRentalOrderInfo);
                                    orderPopupWindow.showAsDropDown(findViewById(R.id.top));
                                    orderPopupWindow.setOutsideTouchable(false);
                                    orderPopupWindow.setFocusable(false);
                                } else {
                                    ToastUtils.showShort(MainActivity.this, "服务器忙！！！");
                                }
                            }
                        });
                    } else {
                        EventBus.getDefault().post(new MessageEvent(), "off");
                    }
                }
            });
        }
    }

    private void showMenuWindow(BikeInfo bikeInfo) {
        if (menuWindow == null) {
            menuWindow = new SelectPicPopupWindow(MainActivity.this, bikeInfo, itemsOnClick);
        }
        menuWindow.setFocusable(false);
        menuWindow.setOutsideTouchable(false);
        menuWindow.showAsDropDown(findViewById(R.id.top));
        if (SPUtils.isLogin()) {
            if (cashStatus == 1 && status == 1) {
                menuWindow.mOrder.setText("预约用车");
            } else if (cashStatus == 0 && status == 0) {
                menuWindow.mOrder.setText("完成注册即可骑单车");
            } else if (cashStatus == 1 && status == 0) {
                menuWindow.mOrder.setText("完成注册即可骑单车");
            } else if (cashStatus == 0 && status == 1) {
                menuWindow.mOrder.setText("预约用车");
            }
        } else {
            menuWindow.mOrder.setText("立即登录即可骑单车");
        }

    }

    @Override
    public void onMapStatusChangeStart(MapStatus mapStatus) {

    }

    @Override
    public void onMapStatusChange(MapStatus mapStatus) {

    }

    @Override
    public void onMapStatusChangeFinish(MapStatus mapStatus) {
//        LatLng target = mapStatus.target;
//        LogUtils.d("移动", target.latitude + "\n" + target.longitude);
//        mMap.clear();
//        getBikeInfo(target.latitude, target.longitude);
//        setUserMapCenter(target.latitude,target.longitude);
    }

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
            walkingRouteLine = result.getRouteLines().get(0);
            distance = walkingRouteLine.getDistance();
            mWalkTime = walkingRouteLine.getDuration() / 60;
//            mWalkTime = distance / 60;
            mDistance = MapUtil.distanceFormatter(distance);
            mCastTime = String.valueOf(mWalkTime);
            if (!mDistance.equals("") && mDistance != null) {
                menuWindow.mDistance.setText(mDistance);
            }
            if (!mCastTime.equals("") && mCastTime != null) {
                menuWindow.mArrivalTime.setText(mCastTime + "分钟");
            }

        }
        WalkingRouteOverlay overlay = new MyWalkingRouteOverlay(mMap);
        /**
         * 设置地图 Marker 覆盖物点击事件监听者
         * 需要实现的方法：     onMarkerClick(Marker marker)
         * */
        mMap.setOnMarkerClickListener(overlay);
        routeOverlay = overlay;

        if (!overlay.equals("") && overlay != null) {
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

    //实现定位回调监听
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
//            Log.d("定位点的信息", location.getAddrStr() + "\n" + mCurrentLantitude + "\n" + mCurrentLongitude);
            currentLatLng = new LatLng(mCurrentLantitude, mCurrentLongitude);
//            BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
//                    .fromResource(R.mipmap.search_center_ic);
            //不设置bitmapDescriptor时代表默认使用百度地图图标
            MyLocationConfiguration config = new MyLocationConfiguration(
                    mCurrentMode, true, null);
            mMap.setMyLocationConfigeration(config);
            startNodeStr = PlanNode.withLocation(new LatLng(mCurrentLantitude, mCurrentLongitude));
            //根据手机定位的不同得到定位点信息，将这个信息传递给搜索页面
            String locationDescribe = location.getLocationDescribe();
            String addrStr = location.getAddrStr();
            if (locationDescribe != null && locationDescribe.length() > 0 && addrStr != null && addrStr.length() > 0) {
                String substring1 = addrStr.substring(2, addrStr.length());
                String substring = locationDescribe.substring(1, locationDescribe.length());
                address1 = substring1 + substring;
            }
            // 第一次定位时，将地图位置移动到当前位置
            if (isFristLocation) {
                isFristLocation = false;
                mMap.clear();
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                //地图缩放比设置为18
                builder.target(ll).zoom(18.0f);
                mMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                changeLatitude = location.getLatitude();
                changeLongitude = location.getLongitude();

                Log.d("定位点的信息", location.getAddrStr() + "\n" + changeLatitude + "\n" + changeLongitude);
//                setBaiduMapMark();
                setUserMapCenter(mCurrentLantitude, mCurrentLongitude);
                if (uID != null && !uID.equals("")) {
                    checkBookingBikeInfoByUserID(uID);

                } else {
                    //根据手机定位地点，得到车辆信息的方法
                    getBikeInfo(mCurrentLantitude, mCurrentLongitude);
                }
            }

        }
    }

    private void checkOrderInfoByUserID(final String uID) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", uID);
        Log.d("用户的ID", uID);
        OkHttpUtils.post().url(Api.BASE_URL + Api.SEARCHORDERING).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.e(e.getMessage());
                ToastUtils.showShort(MainActivity.this, getString(R.string.error_info));
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d("你好", response);
                if (JsonUtils.isSuccess(response)) {
                    isChecked = true;
                    isClick = false;
                    isShowRideOrder = true;
                    mMap.clear();
                    Gson gson = new Gson();
                    mShowBikeRentalOrderInfo = gson.fromJson(response, ShowBikeRentalOrderInfo.class);
                    mServiceIntent = new Intent(MainActivity.this, GPSService.class);
                    mServiceIntent.putExtra("userId", uID);
                    mServiceIntent.putExtra("bicycleNo", mShowBikeRentalOrderInfo.getBicycleNo());
                    mServiceIntent.putExtra("carRentalOrderDate", mShowBikeRentalOrderInfo.getCarRentalOrderDate());
                    mServiceIntent.putExtra("carRentalOrderId", mShowBikeRentalOrderInfo.getCarRentalOrderId());
                    LogUtils.d("神圣", uID + "\n" + mShowBikeRentalOrderInfo.getBicycleNo() + "\n" + mShowBikeRentalOrderInfo.getCarRentalOrderDate() + "\n" + mShowBikeRentalOrderInfo.getCarRentalOrderId());
                    startService(mServiceIntent);
                    mScan.setVisibility(View.INVISIBLE);
                    String bicycleNo = mShowBikeRentalOrderInfo.getBicycleNo();
                    ToastUtils.showShort(MainActivity.this, bicycleNo);
                    mShowBikeRentalOrderPopupWindow = new ShowBikeRentalOrderPopupWindow(MainActivity.this, mShowBikeRentalOrderInfo);
                    mShowBikeRentalOrderPopupWindow.showAsDropDown(findViewById(R.id.top));
                    mShowBikeRentalOrderPopupWindow.setOutsideTouchable(false);
                    mShowBikeRentalOrderPopupWindow.setFocusable(false);
                } else {
                    //根据手机定位地点，得到车辆信息的方法
                    LogUtils.d("你怎么了", isBook + "");
                    getBikeInfo(mCurrentLantitude, mCurrentLongitude);
                }
            }
        });
    }

    //获得自行车信息的方法
    private void getBikeInfo(double Lantitude, double Longitude) {
        String lat = Lantitude + "";
        String lng = Longitude + "";
        if (lat != null && !lat.equals("") && lng != null && !lng.equals("")) {
            Map<String, String> map = new HashMap<>();
            map.put("lng", lng);
            map.put("lat", lat);
            OkHttpUtils.post().url(Api.BASE_URL + Api.GINPUT).params(map).build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    LogUtils.e(e.getMessage());
                    ToastUtils.showShort(MainActivity.this, "抱歉，服务器正忙！");
                }

                @Override
                public void onResponse(String response, int id) {
                    Log.d("所有的数据", response);
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Log.d("自行车", jsonObject + "");
                                bikeInfo = new BikeInfo();
                                bikeInfo.setAddress(jsonObject.getString("address"));
                                bikeInfo.setBicycleId(jsonObject.getInt("bicycleId"));
                                bikeInfo.setBicycleNo(jsonObject.getInt("bicycleNo"));
                                bikeInfo.setLatitude(jsonObject.getString("latitude"));
                                bikeInfo.setLongitude(jsonObject.getString("longitude"));
                                bikeInfo.setUnitPrice(jsonObject.getInt("unitPrice"));
                                bikeInfo.setBicycleNo(jsonObject.getInt("bicycleNo"));
                                bikeInfos.add(bikeInfo);
                            }
//                        if (bikeInfos.size() > 0) {
                            addOverlay(bikeInfos);
//                        initNearestBike(bikeInfo, new LatLng(mCurrentLantitude - 0.0005, mCurrentLongitude - 0.0005));
//                        } else {
//                            ToastUtils.showShort(MainActivity.this, "当前周围没有车辆！！");
//                        }

                        }
//                        else {
//                            ToastUtils.showShort(MainActivity.this, "当前周围没有车辆！！");
//                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    //设置中心点
    private void setUserMapCenter(Double Lantitude, Double Longitude) {
        LatLng ll = new LatLng(Lantitude, Longitude);
        MapStatus.Builder builder = new MapStatus.Builder();
        //地图缩放比设置为18
        builder.target(ll).zoom(18.0f);
        mMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }

    private void checkAggregate(String uID, final String mToken) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", uID);
        map.put("token", mToken);
        OkHttpUtils.post().url(Api.BASE_URL + Api.CHECKAGGREGATE).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.showShort(MainActivity.this, "抱歉，服务器正忙！");
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d("余额情况", response);
                //{"resultStatus":"1"}
                if (JsonUtils.isSuccess(response)) {
                    MainActivityPermissionsDispatcher.showCameraWithCheck(MainActivity.this);
                    Intent i4 = new Intent(MainActivity.this, CaptureActivity.class);
                    i4.putExtra("token", mToken);
                    i4.putExtra("msg", "main");
                    startActivityForResult(i4, 0);

                } else {
                    ToastUtils.showShort(MainActivity.this, "余额不足，请充值后骑行！");
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //反注册EventBus
        EventBus.getDefault().unregister(this);
        unregisterReceiver(lr);
        unregisterReceiver(mResultReceiver);
        Log.d("实验", "onDestroy");
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
        Log.d("实验", "onResume");
        mMapView.onResume();
        if (SPUtils.isLogin()) {
            mInstructions.setVisibility(View.GONE);
            userDetail = (String) SPUtils.get(App.getContext(), "userDetail", "");
            object = null;
            try {
                object = new JSONObject(userDetail);
                int id = object.getInt("id");
                mToken = object.optString("token");
                uID = String.valueOf(id);
                if (uID != null && mToken != null) {
                    queryUserInfo(uID, mToken);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            mInstructions.setVisibility(View.VISIBLE);
        }
        getBikeInfo(mCurrentLantitude, mCurrentLongitude);
        setUserMapCenter(mCurrentLantitude, mCurrentLongitude);
        clickBaiduMapMark();
        clickDismissOverlay();
    }

    @Override
    protected void onStop() {
        // 关闭图层定位
        mMap.setMyLocationEnabled(false);
        mLocationClient.stop();
        // 关闭方向传感器
        myOrientationListener.stop();
        super.onStop();
        Log.d("实验", "onStop");
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
        Log.d("实验", "onPause");
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
        Log.d("实验", "onStart");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //注册EventBus
        EventBus.getDefault().register(this);
        alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        lr = new LocationReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("NEW LOCATION SENT");
        registerReceiver(lr, intentFilter);
        mResultReceiver = new ResultReceiver();
        intentFilter = new IntentFilter();
        intentFilter.addAction("RESULT SENT");
        registerReceiver(mResultReceiver, intentFilter);


        Log.d("实验", "onCreate");
    }

    //WalkingRouteOverlay已经实现了BaiduMap.OnMarkerClickListener接口
    private class MyWalkingRouteOverlay extends WalkingRouteOverlay {

        public MyWalkingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        //返回:起点图标
        @Override
        public BitmapDescriptor getStartMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.unchecked);
            }
            return null;
        }

        //返回:终点图标
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

    //退出登录页面后，设置页面发来的消息，将mInstructions控件显示
    @Subscriber(tag = "visible", mode = ThreadMode.MAIN)
    private void receiveFromSetting(MessageEvent info) {
        LogUtils.e(info.toString());
        mInstructions.setVisibility(View.VISIBLE);
    }

    //登录成功页面发来的消息，将mInstructions控件隐藏
    @Subscriber(tag = "gone", mode = ThreadMode.MAIN)
    private void receiveFromLogin(MessageEvent info) {
        LogUtils.e(info.toString());
        mInstructions.setVisibility(View.GONE);
    }

    //登录成功页面发来的消息，将mInstructions控件隐藏
    @Subscriber(tag = "allShow", mode = ThreadMode.MAIN)
    private void receiveFromClickCamera(MessageEvent info) {
        LogUtils.e(info.toString());
        mBtnMyHelp.setVisibility(View.VISIBLE);
        mBtnMyLocation.setVisibility(View.VISIBLE);
        mScan.setVisibility(View.VISIBLE);
    }

    //手工输入页面发来的消息
    @Subscriber(tag = "bikeNo", mode = ThreadMode.ASYNC)
    private void receiveFromManual(CodeEvent info) {
        LogUtils.d("自行车", info.getBikeNo());
        result = info.getBikeNo();
        openScan(uID, phone, result, mToken);
    }

    class LocationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            RidingInfo ridingInfo = (RidingInfo) intent.getSerializableExtra("ridingInfo");
            if (ridingInfo != null) {
                double tripDist = MapUtil.changeDouble(ridingInfo.getTripDist());
                LogUtils.d("距离", tripDist + "");
                double calorie = MapUtil.changeDouble(ridingInfo.getCalorie());
                String dist = String.valueOf(tripDist * 1000);
                LogUtils.d("距离", dist + "");
                int i = stringToInt(dist);
                String s = MapUtil.distanceFormatter(i);
                LogUtils.d("距离", s + "");
                if (orderPopupWindow != null) {
                    mMap.clear();
                    orderPopupWindow.rideDistance.setText(s);
                    orderPopupWindow.rideTime.setText(String.valueOf(ridingInfo.getTripTime()) + "分钟");
                    orderPopupWindow.consumeEnergy.setText(String.valueOf(calorie) + "大卡");
                    orderPopupWindow.costCycling.setText(String.valueOf(ridingInfo.getRideCost()));
                } else if (mShowBikeRentalOrderPopupWindow != null) {
                    mMap.clear();
                    mShowBikeRentalOrderPopupWindow.rideDistance.setText(s);
                    mShowBikeRentalOrderPopupWindow.rideTime.setText(String.valueOf(ridingInfo.getTripTime()) + "分钟");
                    mShowBikeRentalOrderPopupWindow.consumeEnergy.setText(String.valueOf(calorie) + "大卡");
                    mShowBikeRentalOrderPopupWindow.costCycling.setText(String.valueOf(ridingInfo.getRideCost()));
                }

            }
        }
    }

    class ResultReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            RidingInfo ridingInfo = (RidingInfo) intent.getSerializableExtra("ridingInfo");
            Log.d("bbbb", ridingInfo + "");
            if (ridingInfo != null) {
                if (orderPopupWindow != null) {
                    orderPopupWindow.dismiss();
                }
                if (mShowBikeRentalOrderPopupWindow != null) {
                    mShowBikeRentalOrderPopupWindow.dismiss();
                }

                if (mServiceIntent != null) {
                    stopService(mServiceIntent);
                    Intent ridingResult = new Intent(MainActivity.this, RidingResultActivity.class);
                    ridingResult.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("result", ridingInfo);
                    ridingResult.putExtras(bundle);
                    startActivity(ridingResult);
                    mMap.clear();
                    addOverlay(bikeInfos);
                    mScan.setVisibility(View.VISIBLE);
                    isClick = true;
                }
            }

        }
    }
}
