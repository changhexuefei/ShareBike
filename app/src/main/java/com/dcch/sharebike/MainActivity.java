package com.dcch.sharebike;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.mapapi.utils.DistanceUtil;
import com.dcch.sharebike.app.App;
import com.dcch.sharebike.base.AppManager;
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
import com.dcch.sharebike.moudle.home.content.MyContent;
import com.dcch.sharebike.moudle.login.activity.ClickCameraPopupActivity;
import com.dcch.sharebike.moudle.login.activity.ClickMyHelpActivity;
import com.dcch.sharebike.moudle.login.activity.IdentityAuthenticationActivity;
import com.dcch.sharebike.moudle.login.activity.LoginActivity;
import com.dcch.sharebike.moudle.login.activity.PersonalCenterActivity;
import com.dcch.sharebike.moudle.login.activity.PropagandaPosterActivity;
import com.dcch.sharebike.moudle.login.activity.RechargeActivity;
import com.dcch.sharebike.moudle.user.activity.CustomerServiceActivity;
import com.dcch.sharebike.moudle.user.activity.MyMessageActivity;
import com.dcch.sharebike.moudle.user.activity.RechargeBikeFareActivity;
import com.dcch.sharebike.moudle.user.activity.RechargeDepositActivity;
import com.dcch.sharebike.moudle.user.activity.RidingResultActivity;
import com.dcch.sharebike.moudle.user.activity.UnlockBillPageActivity;
import com.dcch.sharebike.moudle.user.activity.UnlockProgressActivity;
import com.dcch.sharebike.moudle.user.activity.UserAgreementActivity;
import com.dcch.sharebike.netty.NettyClient;
import com.dcch.sharebike.overlayutil.OverlayManager;
import com.dcch.sharebike.overlayutil.WalkingRouteOverlay;
import com.dcch.sharebike.service.GPSService;
import com.dcch.sharebike.service.NettyService;
import com.dcch.sharebike.utils.AES;
import com.dcch.sharebike.utils.ClickUtils;
import com.dcch.sharebike.utils.JsonUtils;
import com.dcch.sharebike.utils.LogUtils;
import com.dcch.sharebike.utils.MapUtil;
import com.dcch.sharebike.utils.NetUtils;
import com.dcch.sharebike.utils.SPUtils;
import com.dcch.sharebike.utils.ToastUtils;
import com.dcch.sharebike.view.BikeRentalOrderPopupWindow;
import com.dcch.sharebike.view.BookBikePopupWindow;
import com.dcch.sharebike.view.MyCountDownTimer;
import com.dcch.sharebike.view.SelectPicPopupWindow;
import com.google.gson.Gson;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.bottomsheet.BottomSheetBean;
import com.hss01248.dialog.interfaces.MyDialogListener;
import com.hss01248.dialog.interfaces.MyItemDialogListener;
import com.iflytek.autoupdate.IFlytekUpdate;
import com.iflytek.autoupdate.IFlytekUpdateListener;
import com.iflytek.autoupdate.UpdateConstants;
import com.iflytek.autoupdate.UpdateErrorCode;
import com.iflytek.autoupdate.UpdateInfo;
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
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import okhttp3.Call;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static com.dcch.sharebike.utils.MapUtil.stringToInt;


@RuntimePermissions
public class MainActivity extends BaseActivity implements BaiduMap.OnMapStatusChangeListener, OnGetRoutePlanResultListener, OnGetGeoCoderResultListener {
    @BindView(R.id.mapView)
    MapView mMapView;
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
    RelativeLayout top;
    @BindView(R.id.specialOffer)
    ImageView mSpecialOffer;
    BaiduMap mMap;
    public MyLocationListener mMyLocationListener;//定位的监听器
    boolean useDefaultIcon = false;
    @BindView(R.id.centerIcon)
    ImageView mCenterIcon;
    private long mExitTime; //退出时间
    OverlayManager routeOverlay = null;//该类提供一个能够显示和管理多个Overlay的基类
    private LocationClient mLocationClient;//定位的客户端
    private MyLocationConfiguration.LocationMode mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;//当前定位的模式
    private volatile boolean isFristLocation = true;//是否是第一次定位
    private double mCurrentLantitude;//最新一次的经纬度
    private double mCurrentLongitude;
    private float mCurrentAccracy;//当前的精度
    private MyOrientationListener myOrientationListener;//方向传感器的监听器
    private int mXDirection;//方向传感器X方向的值
    private GeoCoder mSearch = null;//地理编码
    // private Address locationDescribe;
    //POI搜索相关
    // public PoiSearch mPoiSearch = null;
    private SelectPicPopupWindow menuWindow; // 自定义弹出框
    private String address1;
    private List<BikeInfo> bikeInfos;
    private BikeInfo bikeInfo;
    private BookBikePopupWindow bookBikePopupWindow;
    private int cashStatus;
    private int status;
    private String bookingCarId;
    private String phone;
    private String result;
    private String uID;
    private Double locationLongitude;
    private Double locationLatitude;
    private boolean isFirst = true;
    private boolean isChecked = true;
    private boolean isBook = false;
    private boolean isShowMenu = false;
    private boolean isShowRideOrder = false;
    private boolean isShowBookOrder = false;
    private boolean isClick = true;
    private String bicycleNo;
    private MyCountDownTimer timer;
    private String count;
    private BookingBikeInfo bookingBikeInfo = null;
    private BikeRentalOrderInfo bikeRentalOrderInfo = null;
    private BikeRentalOrderPopupWindow orderPopupWindow = null;
    private LocationReceiver lr;
    PlanNode startNodeStr, endNodeStr;
    RoutePlanSearch mRPSearch = null;    // 搜索模块，也可去掉地图模块独立使用
    private Intent mServiceIntent;
    Marker mMarker = null;
    private String mToken;
    private boolean mHasPlanRoute;
    private String mCarRentalOrderId;
    private Intent mNettyService;
    private boolean IsConnect;
    private Double mDoulat;
    private Double mDoulon;
    private LatLng mMCenterLatLng;
    private String mReverseGeoCodeResultAddress;
    private double mChangeLongitude;
    private double mChangeLatitude;
    private LatLng mLlr;
    private LatLng mLl;

    public MainActivity() {
    }


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
        bikeInfos = new ArrayList<>();
        // 初始化GeoCoder模块，注册事件监听
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);
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
        //注册EventBus
        EventBus.getDefault().register(this);
        lr = new LocationReceiver();
//
        updataApp();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("NEW LOCATION SENT");
        registerReceiver(lr, intentFilter);
//        mResultReceiver = new ResultReceiver();
        intentFilter = new IntentFilter();
        intentFilter.addAction("RESULT SENT");
//        registerReceiver(mResultReceiver, intentFilter);
        Log.d("实验", "onCreate");
    }

    //自动更新的方法
    private void updataApp() {
        //初始化自动更新对象
        final IFlytekUpdate updManager = IFlytekUpdate.getInstance(App.getContext());
        //开启调试模式，默认不开启
        updManager.setDebugMode(false);
        //开启wifi环境下检测更新，仅对自动更新有效，强制更新则生效
        updManager.setParameter(UpdateConstants.EXTRA_WIFIONLY, "true");
        //设置通知栏使用应用icon，详情请见示例
        updManager.setParameter(UpdateConstants.EXTRA_NOTI_ICON, "true");
        //设置更新提示类型，默认为通知栏提示
        updManager.setParameter(UpdateConstants.EXTRA_STYLE, UpdateConstants.UPDATE_UI_DIALOG);
        //自动更新回调方法，详情参考demo
        IFlytekUpdateListener updateListener = new IFlytekUpdateListener() {
            @Override
            public void onResult(int errorcode, UpdateInfo result) {
                if (errorcode == UpdateErrorCode.OK && result != null) {
                    updManager.showUpdateInfo(MainActivity.this, result);
                }
            }
        };
        // 启动自动更新
//        updManager.autoUpdate(this, updateListener);
        updManager.forceUpdate(this, updateListener);
    }

    //进入主页面检查客户是否有预约订单的方法
    private void checkBookingBikeInfoByUserID(final String uID) {
        final Map<String, String> map = new HashMap<>();
        map.put("userId", uID);
        OkHttpUtils.post().url(Api.BASE_URL + Api.SEARCHBOOKING).params(map).build()
                .execute(new StringCallback() {
                             @Override
                             public void onError(Call call, Exception e, int id) {
                                 Log.d("你好", "454545454545");
                                 ToastUtils.showShort(MainActivity.this, getString(R.string.server_tip));
                             }

                             @Override
                             public void onResponse(String response, int id) {

                                 if (JsonUtils.isSuccess(response)) {
                                     Gson gson = new Gson();
                                     bookingBikeInfo = gson.fromJson(response, BookingBikeInfo.class);
                                     String bookingCarDate = bookingBikeInfo.getBookingCarDate();
                                     String stringDate = MapUtil.getStringDate();
                                     long countTime = 600000 - MapUtil.countTime(stringDate, bookingCarDate);
                                     LogUtils.d("预约时间", countTime + "");
                                     if (countTime > 0) {
                                         mMap.clear();
                                         mCenterIcon.setVisibility(View.INVISIBLE);
                                         bicycleNo = bookingBikeInfo.getBicycleNo();
                                         bookingCarId = bookingBikeInfo.getBookingCarId();
                                         locationLongitude = bookingBikeInfo.getLongitude();
                                         locationLatitude = bookingBikeInfo.getLatitude();
                                         isShowBookOrder = true;
                                         isChecked = true;
                                         isClick = false;
                                         isBook = true;
                                         if (locationLongitude != null && locationLatitude != null) {
                                             Log.d("你好", "123");
                                             forLocationAddMark(locationLatitude, locationLongitude);
                                             reverseGeoCoder(transform(locationLatitude, locationLongitude));
                                             endNodeStr = PlanNode.withLocation(transform(locationLatitude, locationLongitude));
                                             drawPlanRoute(endNodeStr);
                                         }
                                         bookBikePopupWindow = new BookBikePopupWindow(MainActivity.this, bookingBikeInfo, bookBikeItemsOnClick);
                                         bookBikePopupWindow.showAsDropDown(findViewById(R.id.top));
                                         timer = (MyCountDownTimer) new MyCountDownTimer(countTime, 1000) {
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
                                                 cancelBookingBike(bookingCarId, bicycleNo, uID, mToken);
                                             }
                                         }.start();
                                     } else {
                                         bookBikePopupWindow.dismiss();
                                         timer.cancel();
                                     }
                                 } else {
                                     checkOrderInfoByUserID(uID);
                                 }
                             }
                         }
                );
    }

    private LatLng transform(double lat, double lng) {
        LatLng sourceLatLng = new LatLng(lat, lng);
        CoordinateConverter converter = new CoordinateConverter();
        converter.from(CoordinateConverter.CoordType.COMMON);
        // sourceLatLng待转换坐标
        converter.coord(sourceLatLng);
        return converter.convert();
    }


    //添加覆盖物的方法
    private void forLocationAddMark(double lat, double lng) {
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.bike_icon);
        OverlayOptions options;
        LatLng latLng = transform(lat, lng);
        //设置marker
        options = new MarkerOptions()
                .position(latLng)//设置位置
                .icon(bitmap)//设置图标样式
                .zIndex(9) // 设置marker所在层级
                .draggable(true) // 设置手势拖拽;
                .animateType(MarkerOptions.MarkerAnimateType.grow);//设置增长动画
        //添加marker
        mMarker = (Marker) mMap.addOverlay(options);
        Bundle bundle = new Bundle();
//                // bikeInfo必须实现序列化接口
        bundle.putSerializable("bikeInfo", bikeInfo);
        mMarker.setExtraInfo(bundle);
    }

    //百度地图的点击方法
    private void clickDismissOverlay() {
        mMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                //当客户已经约车，这时客户点击地图，只显示预约的车辆和路线的覆盖物，其余消失。
                if (isBook) {
                    mMapView.setEnabled(false);
                } else if (isShowRideOrder) {
                    mMapView.setEnabled(false);
                } else if (isShowBookOrder) {
                    mMapView.setEnabled(false);
                }
                //由于menuWindow会和地图抢夺焦点，所以在设置他的属性时设置为不能获得焦点
                //就能够满足一起消失的功能menuWindow != null &&
                if (menuWindow != null && menuWindow.isShowing()) {
                    menuWindow.dismiss();
                    isShowMenu = false;
                    mCenterIcon.setVisibility(View.VISIBLE);
                    if (routeOverlay != null) {
                        routeOverlay.removeFromMap();
                    }
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
        option.setScanSpan(60000);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setIsNeedLocationDescribe(true);//可选，设置是否需要地址描述
        option.setNeedDeviceDirect(false);//可选，设置是否需要设备方向结果
        option.setLocationNotify(false);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        mLocationClient.setLocOption(option);
        clickBaiduMapMark();
        clickDismissOverlay();
    }

    @OnClick({R.id.MyCenter, R.id.btn_my_location, R.id.scan, R.id.seek, R.id.instructions, R.id.btn_my_help, R.id.specialOffer})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.MyCenter:
                if (ClickUtils.isFastClick()) {
                    return;
                }
                goToPersonCenter();
                break;
            case R.id.btn_my_help:
                if (ClickUtils.isFastClick()) {
                    return;
                }
                if (SPUtils.isLogin()) {
                    popupDialog();
                } else {
                    //没有登录的情况设置Activity
                    mBtnMyHelp.setVisibility(View.GONE);
                    mBtnMyLocation.setVisibility(View.GONE);
                    mScan.setVisibility(View.GONE);
                    startActivity(new Intent(MainActivity.this, ClickMyHelpActivity.class));
                }
                break;

            case R.id.instructions:
                if (ClickUtils.isFastClick()) {
                    return;
                }
                goToPersonCenter();
                break;

            case R.id.seek:
                if (ClickUtils.isFastClick()) {
                    return;
                }
                StyledDialog.buildIosAlert(MainActivity.this, "提示", "拨打麒麟单车客服电话 400-660-6215", new MyDialogListener() {
                    @Override
                    public void onFirst() {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "400-660-6215"));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }

                    @Override
                    public void onSecond() {
                        return;
                    }
                }).setMsgColor(R.color.colorHeading).setMsgSize(16).show();
//                if (isClick) {
//                    Intent seek = new Intent(MainActivity.this, SeekActivity.class);
//                    seek.putExtra("address", address1);
//                    startActivityForResult(seek, 1);
//                }
//                if (!isClick) {
//                    ToastUtils.showShort(MainActivity.this, getString(R.string.booking_riding_tip));
//                }
                break;

            case R.id.btn_my_location:
                if (routeOverlay != null && menuWindow != null && menuWindow.isShowing()) {
                    menuWindow.dismiss();
                    routeOverlay.removeFromMap();
                    mMap.clear();
                    mCenterIcon.setVisibility(View.VISIBLE);
                    addOverlays(bikeInfos);
                }
                setUserMapCenter(mCurrentLantitude, mCurrentLongitude);
//                getMyLocation();
                if (SPUtils.isLogin()) {
                    mInstructions.setVisibility(View.GONE);
                } else {
                    mInstructions.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.scan:
                if (ClickUtils.isFastClick()) {
                    return;
                }
                if (SPUtils.isLogin()) {
                    LogUtils.d("余额情况", "123456789" + cashStatus + "\n" + status + "\n" + mToken);
                    if (mToken != null) {
                        if (cashStatus == 1 && status == 1) {
                            if (NetUtils.isConnected(App.getContext())) {
                                checkAggregate(uID, mToken);
                            } else {
                                ToastUtils.showShort(MainActivity.this, getString(R.string.no_network_tip));
                            }
                        } else if (cashStatus == 0 && status == 0) {
                            startActivity(new Intent(this, RechargeActivity.class));
                        } else if (cashStatus == 1 && status == 0) {
                            startActivity(new Intent(this, IdentityAuthenticationActivity.class));
                        } else if (cashStatus == 0 && status == 1) {
                            startActivity(new Intent(this, RechargeDepositActivity.class));
                        }
                    }
                } else {
//                没有登录的情况设置Activity
                    mBtnMyHelp.setVisibility(View.GONE);
                    mBtnMyLocation.setVisibility(View.GONE);
                    mScan.setVisibility(View.GONE);
                    startActivity(new Intent(MainActivity.this, ClickCameraPopupActivity.class));
                }
                break;
            case R.id.specialOffer:
                if (ClickUtils.isFastClick()) {
                    return;
                }
                if (SPUtils.isLogin()) {
                    goToMessageList(uID, mToken);
                } else {
                    startActivity(new Intent(MainActivity.this, PropagandaPosterActivity.class));
                }
                break;
        }
    }

    //去消息列表的方法
    private void goToMessageList(String uID, String token) {
        Intent myMessage = new Intent(MainActivity.this, MyMessageActivity.class);
        myMessage.putExtra("userId", uID);
        myMessage.putExtra("token", token);
        startActivity(myMessage);
    }

    //跳转到个人中心
    private void goToPersonCenter() {
        Intent personal = new Intent(MainActivity.this, PersonalCenterActivity.class);
        startActivity(personal);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission(Manifest.permission.CAMERA)
    void showCamera() {
    }

    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
    void initPermission() {

    }

    //百度地图添加覆盖物的方法
    private void addOverlays(List bikeInfos) {
        if (bikeInfos.size() > 0) {
            //清空地图
            mMap.clear();
            for (int i = 0; i < bikeInfos.size(); i++) {
                bikeInfo = (BikeInfo) bikeInfos.get(i);
                double lat = bikeInfo.getLatitude();
                double lng = bikeInfo.getLongitude();
                LatLng latLng = transform(lat, lng);
                double distance = DistanceUtil.getDistance(latLng, mMCenterLatLng);
//                if (distance < 4000) {
                Log.d("你好", "456");
                forLocationAddMark(lat, lng);
//                }

            }
        }
    }

    //百度地图的覆盖物点击方法
    private void clickBaiduMapMark() {
        final TreeSet<Integer> integers = new TreeSet<>();
        mMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                LogUtils.d("点击", isChecked + "\n" + marker.getExtraInfo());
//                if (!isChecked) {
//                    return false;
//                }
                if (marker.getExtraInfo() != null && isChecked && isFirst) {
                    int zIndex = marker.getZIndex();
                    integers.add(zIndex);
                    LogUtils.d("覆盖物", zIndex + "\n" + integers.size());
                    Bundle bundle = marker.getExtraInfo();
                    bikeInfo = (BikeInfo) bundle.getSerializable("bikeInfo");
                    if (bikeInfo != null) {
                        bicycleNo = bikeInfo.getBicycleNo();
                        if (NetUtils.isConnected(App.getContext())) {
                            updateBikeInfo(bikeInfo);
                            StyledDialog.buildMdLoading(MainActivity.this, getString(R.string.route_planning), true, false).show();
                        } else {
                            ToastUtils.showShort(MainActivity.this, getString(R.string.no_network_tip));
                        }
                        if (menuWindow == null || !menuWindow.isShowing()) {
                            showMenuWindow(bikeInfo);
                            isShowMenu = true;
                        }
                    }
                }
                if (bookBikePopupWindow != null && bookBikePopupWindow.isShowing()) {
                    mMapView.setFocusable(false);
                    mMapView.setEnabled(false);
                }
                mInstructions.setVisibility(View.GONE);
                return true;
            }
        });
    }

    private void updateBikeInfo(BikeInfo bikeInfo) {
        mHasPlanRoute = false;
        if (!mHasPlanRoute) {
            mDoulat = bikeInfo.getLatitude();
            mDoulon = bikeInfo.getLongitude();
            mCenterIcon.setVisibility(View.INVISIBLE);
            LogUtils.d("谁", transform(mDoulat, mDoulon).latitude + "\n22222222\n" + transform(mDoulat, mDoulon).longitude);
            reverseGeoCoder(transform(mDoulat, mDoulon));
            endNodeStr = PlanNode.withLocation(transform(mDoulat, mDoulon));
            drawPlanRoute(endNodeStr);
        }
    }

    //画规划路线的方法
    private void drawPlanRoute(PlanNode endNodeStr) {
        if (routeOverlay != null) {
            routeOverlay.removeFromMap();
        }
        if (endNodeStr != null && startNodeStr != null) {
//            LogUtils.d("划线", endNodeStr.getLocation().latitude + "\n" + endNodeStr.getLocation().longitude);
            mRPSearch.walkingSearch((new WalkingRoutePlanOption()).from(startNodeStr).to(endNodeStr));
        }
    }

    //预约车辆的点击监听事件
    View.OnClickListener itemsOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.order:
                    mMapView.setFocusable(false);
                    mMapView.setEnabled(false);
                    if (NetUtils.isConnected(App.getContext())) {
                        if (SPUtils.isLogin()) {
                            if (menuWindow != null && menuWindow.isShowing()) {
                                menuWindow.dismiss();
                            }
                            if (cashStatus == 1 && status == 1) {
                                mInstructions.setVisibility(View.GONE);
                                if (phone != null && !phone.equals("")) {
                                    queryBookingNum(phone);
                                }
                            } else if (cashStatus == 0 && status == 0) {
                                startActivity(new Intent(MainActivity.this, RechargeActivity.class));
                                Log.d("你好", "1");
                                clearDrawingOverlay();
                            } else if (cashStatus == 1 && status == 0) {
                                startActivity(new Intent(MainActivity.this, IdentityAuthenticationActivity.class));
                                Log.d("你好", "2");
                                clearDrawingOverlay();
                            } else if (cashStatus == 0 && status == 1) {
                                startActivity(new Intent(MainActivity.this, RechargeDepositActivity.class));
                                Log.d("你好", "3");
                                clearDrawingOverlay();
                            }

                        } else {
                            mInstructions.setVisibility(View.VISIBLE);
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            menuWindow.setFocusable(true);
                            menuWindow.dismiss();
                            Log.d("你好", "4");
                            if (routeOverlay != null) {
                                routeOverlay.removeFromMap();
                            }
                            setUserMapCenter(mCurrentLantitude, mCurrentLongitude);
                        }
                    } else {
                        ToastUtils.showShort(MainActivity.this, getString(R.string.no_network_tip));
                    }
            }
        }
    };

    //清除覆盖物的方法
    private void clearDrawingOverlay() {
        mMap.clear();
        addOverlays(bikeInfos);
        setUserMapCenter(mCurrentLantitude, mCurrentLongitude);
    }

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
                                    cancelBookingBike(bookingCarId, bicycleNo, uID, mToken);
                                }
                            }).create().show();
                    break;

                case R.id.forBellIcon:
                    if (ClickUtils.isFastClick()) {
                        return;
                    }
                    bookBikePopupWindow.forBellIcon.setImageResource(R.drawable.frame);
                    AnimationDrawable anim = (AnimationDrawable) bookBikePopupWindow.forBellIcon.getDrawable();
                    anim.start();
                    String time = "2";
                    ringDown(bicycleNo, time);
                    break;
            }
        }
    };

    //响铃的方法
    private void ringDown(String bicycleNo, String time) {
        Map<String, String> map = new HashMap<>();
        map.put("bicycleNo", bicycleNo);
        map.put("Count", time);
        LogUtils.d("错误", bicycleNo + "\n" + time);
        OkHttpUtils.post().url(Api.BASE_URL + Api.FINDBIKERING).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.showShort(MainActivity.this, getString(R.string.server_tip));
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d("响铃", response);
            }
        });
    }

    //点击关锁未结费的监听事件
    View.OnClickListener userRidingBikeItemsOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.close_lock:
                    if (result != null) {
                        if (mCarRentalOrderId != null && uID != null) {
                            Intent billPage = new Intent(MainActivity.this, UnlockBillPageActivity.class);
                            billPage.putExtra("carRentalOrderId", mCarRentalOrderId);
                            billPage.putExtra("userId", uID);
                            startActivity(billPage);
                        }
                    }
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
                ToastUtils.showShort(MainActivity.this, getString(R.string.server_tip));
            }

            @Override
            public void onResponse(String response, int id) {
                Log.d("次数", response);
                // {"count":"4","resultStatus":"1"} {"count":"","resultStatus":"0"}
                if (JsonUtils.isSuccess(response)) {
                    try {
                        JSONObject object = new JSONObject(response);
                        count = object.optString("count");
                        if (Integer.valueOf(count) < 5) {
                            if (menuWindow != null && menuWindow.isShowing()) {
                                menuWindow.dismiss();
                            }
                            StyledDialog.buildMdLoading(MainActivity.this, getString(R.string.booking), true, false).setMsgColor(R.color.color_ff).show();
                            bookingBike(uID, bicycleNo, mToken);

                        } else {
                            ToastUtils.showLong(MainActivity.this, getString(R.string.booking_overfulfil));
                            menuWindow.setFocusable(true);
                            isShowMenu = false;
                            mCenterIcon.setVisibility(View.VISIBLE);
                            menuWindow.dismiss();
                            if (routeOverlay != null) {
                                routeOverlay.removeFromMap();
                            }
                            setUserMapCenter(mCurrentLantitude, mCurrentLongitude);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    ToastUtils.showShort(MainActivity.this, getString(R.string.server_tip));
                    menuWindow.dismiss();
                    clearDrawingOverlay();
                }
            }
        });

    }

    //取消预约的方法
    private void cancelBookingBike(String bookingCarId, String bicycleNo, String uID, String token) {
        if (bookingCarId != null && !bookingCarId.equals("") && bicycleNo != null && !bicycleNo.equals("")) {
            Map<String, String> map = new HashMap<>();
            map.put("bookingCarId", bookingCarId);
            map.put("bicycleNo", bicycleNo);
            map.put("userId", uID);
            map.put("token", token);
            OkHttpUtils.post().url(Api.BASE_URL + Api.CANCELBOOK).params(map).build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    ToastUtils.showShort(MainActivity.this, getString(R.string.server_tip));
                }

                @Override
                public void onResponse(String response, int id) {
                    LogUtils.d("取消", response);
                    if (JsonUtils.isSuccess(response)) {
                        isBook = false;
                        isChecked = true;
                        isClick = true;
                        if (bookBikePopupWindow != null && !bookBikePopupWindow.equals("")) {
                            bookBikePopupWindow.dismiss();
                            bookBikePopupWindow.setFocusable(true);
                        }
                        if (timer != null) {
                            timer.cancel();
                        }

                        isShowBookOrder = false;
                        LogUtils.d("进度条页面的数据", isShowRideOrder + "");
                        if (!isShowRideOrder) {
                            LogUtils.d("进度条页面的数据", !isShowRideOrder + "22222222222222");
                            mCenterIcon.setVisibility(View.VISIBLE);
                            getBikeInfo(mCurrentLantitude, mCurrentLongitude);
                            setUserMapCenter(mCurrentLantitude, mCurrentLongitude);
                        }
                    } else {
                        ToastUtils.showLong(MainActivity.this, getString(R.string.cancel_fail));
                    }
                }
            });
        }
    }

    //预约车辆的方法
    private void bookingBike(final String uID, String bikeNo, final String mToken) {
        if (uID != null && bikeNo != null) {
            Map<String, String> map = new HashMap<>();
            map.put("userId", uID);
            map.put("bicycleNo", bikeNo);
            map.put("token", mToken);
            LogUtils.d("错误", mToken + "\n" + bikeNo + "\n" + uID);
            OkHttpUtils.post().url(Api.BASE_URL + Api.BOOKBIKE).params(map).build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    ToastUtils.showShort(MainActivity.this, getString(R.string.server_tip));
                }

                @Override
                public void onResponse(String response, int id) {

                    try {
                        JSONObject bookObject = new JSONObject(response);
                        String resultStatus = bookObject.optString("resultStatus");
                        switch (resultStatus) {
                            case "0":
                                ToastUtils.showShort(MainActivity.this, getString(R.string.book_again));
                                break;
                            case "1":
                                Gson gson = new Gson();
                                bookingBikeInfo = gson.fromJson(response, BookingBikeInfo.class);
                                mMap.clear();
//                        mMarker.remove();
                                StyledDialog.dismissLoading();
                                mMapView.setFocusable(false);
                                mMapView.setEnabled(false);
                                Log.d("你好", "789");
                                if (mDoulat != null && mDoulon != null) {
                                    forLocationAddMark(mDoulat, mDoulon);
                                }
                                drawPlanRoute(endNodeStr);
                                bookingCarId = bookingBikeInfo.getBookingCarId();
//                        bookingCarDate = bookingBikeInfo.getBookingCarDate();
                                final String bicycleNo = bookingBikeInfo.getBicycleNo();
                                isChecked = false;
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
                                        bookBikePopupWindow.mBookBikeLocationInfo.setText(mReverseGeoCodeResultAddress);
                                        bookBikePopupWindow.mHoldTime.setText(toClock(millisUntilFinished));
                                    }

                                    @Override
                                    public String toClock(long millis) {
                                        return super.toClock(millis);
                                    }

                                    @Override
                                    public void onFinish() {
                                        super.onFinish();
                                        cancelBookingBike(bookingCarId, bicycleNo, uID, mToken);
                                    }
                                }.start();
                                break;
                            case "2":
                                break;
                            case "3":
                                ToastUtils.showShort(MainActivity.this, getString(R.string.biking));
                                break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//
//
//                    if (JsonUtils.isSuccess(response)) {
//                        LogUtils.d("查看信息", response);
//                        Gson gson = new Gson();
//                        bookingBikeInfo = gson.fromJson(response, BookingBikeInfo.class);
//                        mMap.clear();
////                        mMarker.remove();
//                        StyledDialog.dismissLoading();
//                        mMapView.setFocusable(false);
//                        mMapView.setEnabled(false);
//                        Log.d("你好", "789");
//                        if (mDoulat != null && mDoulon != null) {
//                            forLocationAddMark(mDoulat, mDoulon);
//                        }
//                        drawPlanRoute(endNodeStr);
//                        bookingCarId = bookingBikeInfo.getBookingCarId();
////                        bookingCarDate = bookingBikeInfo.getBookingCarDate();
//                        final String bicycleNo = bookingBikeInfo.getBicycleNo();
//                        isChecked = false;
//                        isBook = true;
//                        isClick = false;
//                        bookBikePopupWindow = new BookBikePopupWindow(MainActivity.this, bookingBikeInfo, bookBikeItemsOnClick);
//                        //指定父视图，显示在父控件的某个位置（Gravity.TOP,Gravity.RIGHT等）
//                        //  menuWindow.showAtLocation(findViewById(R.id.mapView), Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, -48);
//                        //设置显示在某个指定控件的下方
//                        bookBikePopupWindow.showAsDropDown(findViewById(R.id.top));
//                        timer = (MyCountDownTimer) new MyCountDownTimer(600000, 1000) {
//                            @Override
//                            public void onTick(long millisUntilFinished) {
//                                bookBikePopupWindow.mBookBikeLocationInfo.setText(mReverseGeoCodeResultAddress);
//                                bookBikePopupWindow.mHoldTime.setText(toClock(millisUntilFinished));
//                            }
//
//                            @Override
//                            public String toClock(long millis) {
//                                return super.toClock(millis);
//                            }
//
//                            @Override
//                            public void onFinish() {
//                                super.onFinish();
//                                cancelBookingBike(bookingCarId, bicycleNo, uID, mToken);
//                            }
//                        }.start();
//
//                    } else {
//                        ToastUtils.showShort(MainActivity.this, getString(R.string.book_again));
//                    }

                }
            });
        }
    }

    private void popupDialog() {
        final List<BottomSheetBean> strings = new ArrayList<>();
        strings.add(new BottomSheetBean(R.mipmap.locking, getString(R.string.unlock)));
        strings.add(new BottomSheetBean(R.mipmap.trouble, getString(R.string.findTrouble)));
        strings.add(new BottomSheetBean(R.mipmap.agreement, getString(R.string.user_agreement)));
        strings.add(new BottomSheetBean(R.mipmap.other, getString(R.string.other_question)));

        StyledDialog.buildBottomSheetGv(this, "客户服务", strings, "", 2, new MyItemDialogListener() {
            @Override
            public void onItemClick(CharSequence text, int position) {
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
                    Intent reportIllegalParking = new Intent(MainActivity.this, UserAgreementActivity.class);
//                    reportIllegalParking.putExtra("name", "2");
                    startActivity(reportIllegalParking);
                }
                if (position == 3) {
//                    ToastUtils.showShort(MainActivity.this, "敬请期待");
                    Intent otherProblem = new Intent(MainActivity.this, CustomerServiceActivity.class);
                    otherProblem.putExtra("name", "3");
                    startActivity(otherProblem);
                }
            }
        })
                .setCancelable(true, true)
                .show();
    }

    //检查车辆编号的是否可用, String token)
    private void checkBicycleNo(String userId, final String result) {
        Map<String, String> map = new HashMap<>();
        map.put("lockremark", result);
        map.put("userId", userId);
        LogUtils.d("檢查", result + "\n" + userId);
        OkHttpUtils.post().url(Api.BASE_URL + Api.CHECKBICYCLENO).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

                ToastUtils.showShort(MainActivity.this, getString(R.string.server_tip));
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d("锁号", response);
                //{"resultStatus":"0"}
                try {
                    JSONObject object = new JSONObject(response);
                    String resultStatus = object.optString("resultStatus");
                    switch (resultStatus) {
                        case "0":
                            ToastUtils.showLong(MainActivity.this, getString(R.string.bike_no_exist));
                            break;
                        case "1":
                            Intent intent = new Intent(MainActivity.this, UnlockProgressActivity.class);
                            startActivity(intent);
                            openScan(uID, result);
                            break;
                        case "3":
                            ToastUtils.showLong(MainActivity.this, getString(R.string.using));
                            break;
                        case "4":
                            ToastUtils.showLong(MainActivity.this, getString(R.string.breakdown));
                            break;
                        case "5":
                            ToastUtils.showLong(MainActivity.this, getString(R.string.biking));
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //扫码开锁的方法String phone,final String mToken
    private void openScan(final String uID, final String result) {
        if (uID != null && !uID.equals("") && result != null && !result.equals("")) {
            StringBuilder sb = new StringBuilder();
            sb.append("userId:").append(uID).append(";bicycleNo:").append(result);
            byte[] encryptBytes = sb.toString().getBytes();
            sendMessageUseNetty(encryptBytes);
        }
    }

    private void sendMessageUseNetty(byte[] encryptBytes) {
        NettyClient.getInstance().sendMsgToServer(encryptBytes, new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    LogUtils.d("netty", "Write successful");
                } else {
                    LogUtils.d("netty", "Write failed");
                }
            }
        });

    }

    //显示车辆预约窗口
    private void showMenuWindow(BikeInfo bikeInfo) {
        if (menuWindow == null) {
            menuWindow = new SelectPicPopupWindow(MainActivity.this, bikeInfo, itemsOnClick);

            LogUtils.d("移动", isShowMenu + "");
        }
        menuWindow.setFocusable(false);
        menuWindow.setOutsideTouchable(false);
        menuWindow.showAsDropDown(findViewById(R.id.top));
        if (SPUtils.isLogin()) {
            if (cashStatus == 1 && status == 1) {
                menuWindow.mOrder.setText(R.string.book_bike);
            } else if (cashStatus == 0 && status == 0) {
                menuWindow.mOrder.setText(R.string.complete_register);
            } else if (cashStatus == 1 && status == 0) {
                menuWindow.mOrder.setText(R.string.complete_register);
            } else if (cashStatus == 0 && status == 1) {
                menuWindow.mOrder.setText(R.string.book_bike);
            }
        } else {
            menuWindow.mOrder.setText(R.string.login_biking);
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
        LogUtils.d("移动", isShowMenu + "\n" + isShowBookOrder + "\n" + isShowRideOrder);
        if (!isShowMenu && !isShowBookOrder && !isShowRideOrder) {
            LogUtils.d("移动", "进来了");
            updateMapStatus(mapStatus);
        }

    }

    private void updateMapStatus(MapStatus mapStatus) {
//        mMap.clear();
        mMCenterLatLng = mapStatus.target;
        mChangeLatitude = mMCenterLatLng.latitude;
        mChangeLongitude = mMCenterLatLng.longitude;
        Log.i("中心点坐标", mChangeLatitude + "," + mChangeLongitude);
        WindowManager wm = this.getWindowManager();
        startNodeStr = PlanNode.withLocation(new LatLng(mChangeLatitude, mChangeLongitude));
//        getBikeInfo(mChangeLatitude, mChangeLongitude);
//        addOverlays(bikeInfos);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        int height = outMetrics.heightPixels;

        Log.i("屏幕宽度和高度", width + "," + height);
        Point pt = new Point();
        pt.x = 0;
        pt.y = 0;
        mLl = mMap.getProjection().fromScreenLocation(pt);
        Log.i("左上角经纬度", mLl.latitude + "," + mLl.longitude);

        Point ptr = new Point();
        ptr.x = width;
        ptr.y = height;
        mLlr = mMap.getProjection().fromScreenLocation(ptr);
        Log.i("右下角经纬度", mLlr.latitude + "," + mLlr.longitude);

    }

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(MainActivity.this, R.string.sorry_no_result, Toast.LENGTH_SHORT).show();
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
//                    result.getSuggestAddrInfo();
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            if (result.getRouteLines().size() > 0) {
                WalkingRouteLine walkingRouteLine = result.getRouteLines().get(0);
                int distance = walkingRouteLine.getDistance();
                int walkTime = walkingRouteLine.getDuration() / 60;
                String distance1 = MapUtil.distanceFormatter(distance);
                String castTime = String.valueOf(walkTime);
                if (!distance1.equals("")) {
                    if (menuWindow != null && menuWindow.isShowing()) {
                        menuWindow.mDistance.setText(distance1);
                    }

                }
                if (!castTime.equals("")) {
                    if (menuWindow != null && menuWindow.isShowing()) {
                        menuWindow.mArrivalTime.setText(castTime + "分钟");
                    }
                }
            }
            WalkingRouteOverlay overlay = new MyWalkingRouteOverlay(mMap);
            mMap.setOnMarkerClickListener(overlay);
            routeOverlay = overlay;
            if (!overlay.equals("") && overlay != null) {
                StyledDialog.dismissLoading();
                overlay.setData(result.getRouteLines().get(0));
                overlay.addToMap();
                overlay.zoomToSpan();
            }
            return;
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
    private class MyLocationListener implements BDLocationListener {
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
            LatLng currentLatLng = new LatLng(mCurrentLantitude, mCurrentLongitude);
            Log.d("中心点坐标", location.getAddrStr() + "\n" + mCurrentLantitude + "\n" + mCurrentLongitude);
//            BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
//                    .fromResource(R.mipmap.map_pin);
            //不设置bitmapDescriptor时代表默认使用百度地图图标
            MyLocationConfiguration config = new MyLocationConfiguration(
                    mCurrentMode, true, null);
            mMap.setMyLocationConfigeration(config);
            // 第一次定位时，将地图位置移动到当前位置
            if (isFristLocation) {
                isFristLocation = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                //地图缩放比设置为18
                builder.target(ll).zoom(18.0f);
                mMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                mChangeLatitude = location.getLatitude();
                mChangeLongitude = location.getLongitude();
                startNodeStr = PlanNode.withLocation(new LatLng(mChangeLatitude, mChangeLongitude));
                Log.d("中心点坐标", location.getAddrStr() + "\n" + mChangeLatitude + "\n" + mChangeLongitude);
                setUserMapCenter(mCurrentLantitude, mCurrentLongitude);
                if (SPUtils.isLogin()) {
                    if (cashStatus == 1 && status == 1 && uID != null) {
                        checkBookingBikeInfoByUserID(uID);
                    }
                } else {
                    //根据手机定位地点，得到车辆信息的方法
                    LogUtils.d("这里", "2");
                    if (mCurrentLantitude > 0 && mCurrentLongitude > 0) {
                        getBikeInfo(mCurrentLantitude, mCurrentLongitude);
                    }
                }
            }
        }
    }

    //检查用户订单信息
    private void checkOrderInfoByUserID(final String uID) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", uID);
        Log.d("用户的ID", uID);
        OkHttpUtils.post().url(Api.BASE_URL + Api.SEARCHORDERING).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.showShort(MainActivity.this, getString(R.string.server_tip));
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d("你好", response);
                if (JsonUtils.isSuccess(response)) {
                    isChecked = false;
                    isClick = false;
                    isShowRideOrder = true;
                    mMap.clear();
                    Gson gson = new Gson();
                    bikeRentalOrderInfo = gson.fromJson(response, BikeRentalOrderInfo.class);
                    result = bikeRentalOrderInfo.getBicycleNo();
                    String carRentalOrderDate = bikeRentalOrderInfo.getCarRentalOrderDate();
                    mCarRentalOrderId = bikeRentalOrderInfo.getCarRentalOrderId();
                    String s = "order:" + uID + result;
                    byte[] encryptBytes = s.getBytes();
                    sendMessageUseNetty(encryptBytes);
                    withServicesMutually(mToken, uID, result, carRentalOrderDate, mCarRentalOrderId);
                    mScan.setVisibility(View.INVISIBLE);
                    mCenterIcon.setVisibility(View.INVISIBLE);
                    orderPopupWindow = new BikeRentalOrderPopupWindow(MainActivity.this, bikeRentalOrderInfo, userRidingBikeItemsOnClick);
                    orderPopupWindow.showAsDropDown(findViewById(R.id.top));
                    orderPopupWindow.setOutsideTouchable(false);
                    mMapView.setEnabled(false);
                    mMapView.setClickable(false);
                } else {
                    getBikeInfo(mCurrentLantitude, mCurrentLongitude);
                }
            }
        });
    }

    //与GPSservices交互的方法
    private void withServicesMutually(String token, String uID, String result, String carRentalOrderDate, String carRentalOrderId) {
        mServiceIntent = new Intent(MainActivity.this, GPSService.class);
        mServiceIntent.putExtra("token", token);
        mServiceIntent.putExtra("userId", uID);
        mServiceIntent.putExtra("bicycleNo", result);
        mServiceIntent.putExtra("carRentalOrderDate", carRentalOrderDate);
        mServiceIntent.putExtra("carRentalOrderId", carRentalOrderId);
        startService(mServiceIntent);
    }

    //获得自行车信息的方法
    private void getBikeInfo(double Lantitude, double Longitude) {
        String lat = Lantitude + "";
        String lng = Longitude + "";
        if (lat != null && !lat.equals("") && lng != null && !lng.equals("")) {
            Map<String, String> map = new HashMap<>();
            map.put("lng", lng);
            map.put("lat", lat);
            OkHttpUtils.post().url(Api.BASE_URL + Api.FINDBICYCLE).params(map).build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    ToastUtils.showShort(MainActivity.this, getString(R.string.server_tip));
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
                                bikeInfo.setBicycleNo(jsonObject.getString("bicycleNo"));
                                bikeInfo.setLatitude(jsonObject.getDouble("latitude"));
                                bikeInfo.setLongitude(jsonObject.getDouble("longitude"));
                                bikeInfo.setUnitPrice(Float.valueOf(jsonObject.getString("unitPrice")));
                                bikeInfos.add(bikeInfo);
                            }
                            addOverlays(bikeInfos);
                        } else {
                            ToastUtils.showShort(MainActivity.this, getString(R.string.no_bikes));
                        }
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

    //检查用户账户余额
    private void checkAggregate(String uID, final String mToken) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", uID);
        map.put("token", mToken);
        LogUtils.d("余额情况", uID + "\n" + mToken);
        OkHttpUtils.post().url(Api.BASE_URL + Api.CHECKAGGREGATE).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.showShort(MainActivity.this, getString(R.string.server_tip));
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
                    startActivity(i4);
                } else {
                    ToastUtils.showShort(MainActivity.this, getString(R.string.not_sufficient_funds));
                    startActivity(new Intent(MainActivity.this, RechargeBikeFareActivity.class));
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("实验", "onResume+1");
        mMapView.onResume();
        if (SPUtils.isLogin()) {
            mInstructions.setVisibility(View.GONE);
            mToken = (String) SPUtils.get(App.getContext(), "token", "");
            uID = String.valueOf(SPUtils.get(App.getContext(), "userId", 0));
            if (uID != null && mToken != null) {
                phone = AES.decrypt((String) SPUtils.get(App.getContext(), "phone", ""), MyContent.key);
                cashStatus = (Integer) SPUtils.get(App.getContext(), "cashStatus", 0);
                status = (Integer) SPUtils.get(App.getContext(), "status", 0);
                LogUtils.d("netty", "用户信息" + phone + cashStatus + status + (String) SPUtils.get(App.getContext(), "phone", ""));
                LogUtils.d("netty", "我将要执行建立长连接" + !IsConnect + mNettyService);
                if (!IsConnect && mNettyService == null) {
                    IsConnect = true;
                    mNettyService = new Intent(MainActivity.this, NettyService.class);
                    mNettyService.putExtra("userId", uID);
                    mNettyService.putExtra("phone", phone);
                    startService(mNettyService);
                }
            }
        } else {
            mInstructions.setVisibility(View.VISIBLE);
            LogUtils.d("这里", "5");
//            addOverlays(bikeInfos);
//                getBikeInfo(mCurrentLantitude, mCurrentLongitude);
//                setUserMapCenter(mCurrentLantitude, mCurrentLongitude);
        }
//        if (menuWindow == null && bookBikePopupWindow == null && orderPopupWindow == null) {
//            LogUtils.d("这里", "6");
//            addOverlays(bikeInfos);
////          setUserMapCenter(mCurrentLantitude, mCurrentLongitude);
//        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (NetUtils.isConnected(App.getContext())) {
            if (SPUtils.isLogin()) {
                mInstructions.setVisibility(View.GONE);

            } else {
                mInstructions.setVisibility(View.VISIBLE);
                addOverlays(bikeInfos);
            }
        } else {
            ToastUtils.showShort(MainActivity.this, getString(R.string.no_network_tip));
        }
    }

    @Override
    protected void onStop() {
        // 关闭图层定位
        mMap.setMyLocationEnabled(false);
        mLocationClient.stop();
        // 关闭方向传感器
        myOrientationListener.stop();
        super.onStop();
        Log.d("实验", "onStop+1");
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
        Log.d("实验", "onPause+1");
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
    protected void onDestroy() {
        //反注册EventBus
        EventBus.getDefault().unregister(this);
        unregisterReceiver(lr);
//        stopService(new Intent(this, NettyService.class));
        Log.d("实验", "onDestroy");
        StyledDialog.dismiss();
        mMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        if (mLocationClient != null) {
            mLocationClient.unRegisterLocationListener(mMyLocationListener);
            mLocationClient.stop();
        }
        //释放资源
        if (mSearch != null) {
            mSearch.destroy();
        }
        if (mRPSearch != null) {
            mRPSearch.destroy();
        }
        if (timer != null) {
            timer = null;
        }
        super.onDestroy();
    }

    //WalkingRouteOverlay已经实现了BaiduMap.OnMarkerClickListener接口
    private class MyWalkingRouteOverlay extends WalkingRouteOverlay {

        MyWalkingRouteOverlay(BaiduMap baiduMap) {
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
                AppManager.AppExit(this);
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
        if (IsConnect = mNettyService != null) {
            IsConnect = false;
            stopService(mNettyService);
            mNettyService = null;
        }
    }

    //登录成功页面发来的消息，将mInstructions控件隐藏
    @Subscriber(tag = "gone", mode = ThreadMode.MAIN)
    private void receiveFromLogin(MessageEvent info) {
        LogUtils.e(info.toString());
        mInstructions.setVisibility(View.GONE);
    }

    //点击扫码后页面发来的消息，将底部三个控件显示
    @Subscriber(tag = "allShow", mode = ThreadMode.MAIN)
    private void receiveFromClickCamera(MessageEvent info) {
        LogUtils.e(info.toString());
        mBtnMyHelp.setVisibility(View.VISIBLE);
        mBtnMyLocation.setVisibility(View.VISIBLE);
        mScan.setVisibility(View.VISIBLE);
    }

    //开锁进度条页面发来的消息
    @Subscriber(tag = "order_show", mode = ThreadMode.MAIN)
    private void receiveFromUnlockProgress(MessageEvent info) {
        LogUtils.d("进度条页面的数据", "成功" + info.toString() + isBook);
        checkOrderInfoByUserID(uID);
        if (isBook) {
            LogUtils.d("进度条页面的数据", "12333333");
            cancelBookingBike(bookingCarId, bicycleNo, uID, mToken);
        }

        if (menuWindow != null) {
            menuWindow.dismiss();
        }
        if (bookBikePopupWindow != null) {
            bookBikePopupWindow.dismiss();
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    //开锁长时间没有回应的消息
    @Subscriber(tag = "stopNetty", mode = ThreadMode.MAIN)
    private void receiveStopNettyFromUnlockProgress(MessageEvent info) {
        LogUtils.d("进度条页面的数据", "成功" + info.toString());
        if (IsConnect = mNettyService != null) {
            IsConnect = false;
            stopService(mNettyService);
            mNettyService = null;
        }
        timeOut();
    }

    //手工输入页面发来的消息
    @Subscriber(tag = "bikeNo", mode = ThreadMode.MAIN)
    private void receiveFromManual(CodeEvent info) {
        LogUtils.d("输入", info.getBikeNo());
        if (info != null) {
            result = info.getBikeNo();
            if (uID != null && result != null) {
                openScan(uID, result);
            }
        }
    }

    //接收到开锁失败命令UnlockProgress发来的消息
    @Subscriber(tag = "lose", mode = ThreadMode.MAIN)
    private void receivefailFromUnlockProgress(MessageEvent info) {
        LogUtils.d("输入", info.toString());
        timeOut();

    }

    private void timeOut() {
        StyledDialog.buildIosAlert(MainActivity.this, "提示", "开锁超时，请找其他麒麟单车使用！", new MyDialogListener() {
            @Override
            public void onFirst() {
                StyledDialog.dismiss();
            }

            @Override
            public void onSecond() {
                StyledDialog.dismiss();
            }
        }).show();

    }

    //接收到关锁成功命令Nettyservice发来的消息
    @Subscriber(tag = "close", mode = ThreadMode.MAIN)
    private void receiveFromNettyService(MessageEvent info) {
        if (IsConnect = mNettyService != null) {
            IsConnect = false;
            stopService(mNettyService);
            mNettyService = null;
        }
        Intent ridingResult = new Intent(MainActivity.this, RidingResultActivity.class);
        ridingResult.putExtra("IMEI", result);
        ridingResult.putExtra("userId", uID);
        startActivity(ridingResult);

        mMap.clear();
        if (orderPopupWindow != null) {
            orderPopupWindow.dismiss();
        }
        isShowRideOrder = false;
        isChecked = true;
        mCenterIcon.setVisibility(View.VISIBLE);
        getBikeInfo(mCurrentLantitude, mCurrentLongitude);
        mScan.setVisibility(View.VISIBLE);
    }

    //接收到UnlockBillPage强制结费的消息
    @Subscriber(tag = "forced close", mode = ThreadMode.MAIN)
    private void receiveFromUnlockBillPage(MessageEvent info) {
        mMap.clear();
        if (mServiceIntent != null) {
            stopService(mServiceIntent);
        }
        if (IsConnect = mNettyService != null) {
            IsConnect = false;
            stopService(mNettyService);
            mNettyService = null;
        }
        if (orderPopupWindow != null) {
            orderPopupWindow.dismiss();
        }
        isChecked = true;
        mMapView.setEnabled(true);
        mMapView.setFocusable(true);
        mCenterIcon.setVisibility(View.VISIBLE);
        getBikeInfo(mCurrentLantitude, mCurrentLongitude);
        mScan.setVisibility(View.VISIBLE);
    }

    //手机的扫码消息
    @Subscriber(tag = "samsung", mode = ThreadMode.MAIN)
    private void receiveFromUnlockProgressforPhone(CodeEvent info) {
        if (info != null) {
            result = info.getBikeNo();
            if (result != null) {
                result = result.substring(result.length() - 9, result.length());
                LogUtils.d("锁号", result);
                if (NetUtils.isConnected(App.getContext())) {
                    checkBicycleNo(uID, result);
                }
            }

        }
    }

    class LocationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            RidingInfo ridingInfo = (RidingInfo) intent.getSerializableExtra("ridingInfo");
            if (ridingInfo != null) {
                double tripDist = MapUtil.changeDouble(ridingInfo.getTripDist());
                double calorie = MapUtil.changeDouble(ridingInfo.getCalorie());
                String dist = String.valueOf(tripDist * 1000);
                int i = stringToInt(dist);
                String s = MapUtil.distanceFormatter(i);
                if (orderPopupWindow != null) {
                    mMap.clear();
                    orderPopupWindow.rideDistance.setText(s);
                    orderPopupWindow.rideTime.setText(String.valueOf(ridingInfo.getTripTime()) + "分钟");
                    orderPopupWindow.consumeEnergy.setText(MapUtil.changeOneDouble(calorie) + "大卡");
                    orderPopupWindow.costCycling.setText(String.valueOf(ridingInfo.getRideCost()) + "元");
                }
            }
        }
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
        if (reverseGeoCodeResult == null
                || reverseGeoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
            // 没有检测到结果
            Toast.makeText(MainActivity.this, "抱歉，未能找到结果",
                    Toast.LENGTH_LONG).show();
        }

        mReverseGeoCodeResultAddress = reverseGeoCodeResult.getAddress();
        if (mReverseGeoCodeResultAddress != null && !mReverseGeoCodeResultAddress.equals("")) {
            if (menuWindow != null && menuWindow.isShowing()) {
                menuWindow.mBikeLocationInfo.setText(mReverseGeoCodeResultAddress);
            }
            if (bookBikePopupWindow != null && !bookBikePopupWindow.equals("")) {
                bookBikePopupWindow.mBookBikeLocationInfo.setText(mReverseGeoCodeResultAddress);
            }

        } else {
            if (menuWindow != null && menuWindow.isShowing()) {
                menuWindow.mBikeLocationInfo.setText("未知地址");
            }
            if (bookBikePopupWindow != null && !bookBikePopupWindow.equals("")) {
                bookBikePopupWindow.mBookBikeLocationInfo.setText("未知地址");
            }
        }
    }

    private void reverseGeoCoder(LatLng latlng) {
        mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(latlng));
    }

}
