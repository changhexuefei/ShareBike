package com.dcch.sharebike.moudle.user.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteLine;
import com.baidu.mapapi.search.route.BikingRoutePlanOption;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.bumptech.glide.Glide;
import com.dcch.sharebike.R;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.http.Api;
import com.dcch.sharebike.moudle.home.bean.RoutePoint;
import com.dcch.sharebike.overlayutil.BikingRouteOverlay;
import com.dcch.sharebike.overlayutil.OverlayManager;
import com.dcch.sharebike.utils.ClickUtils;
import com.dcch.sharebike.utils.JsonUtils;
import com.dcch.sharebike.utils.LogUtils;
import com.dcch.sharebike.utils.MapUtil;
import com.dcch.sharebike.utils.ToastUtils;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.bottomsheet.BottomSheetBean;
import com.hss01248.dialog.interfaces.MyItemDialogListener;
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
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;

public class JourneyDetailActivity extends BaseActivity implements OnGetRoutePlanResultListener {

    @BindView(R.id.journey_mapView)
    MapView mJourneyMapView;
    public ArrayList<RoutePoint> routePoints;
    //    @BindView(R.id.title)
//    TextView mTitle;
//    @BindView(R.id.toolbar)
//    Toolbar mToolbar;
    @BindView(R.id.icon)
    CircleImageView mIcon;
    @BindView(R.id.name)
    TextView mName;
    @BindView(R.id.bikeNO)
    TextView mBikeNO;
    @BindView(R.id.ridetime)
    TextView mRidetime;
    @BindView(R.id.distanceshow)
    TextView mDistanceshow;
    @BindView(R.id.kCal)
    TextView mKCal;
    @BindView(R.id.go_back)
    ImageView mGoBack;
    @BindView(R.id.shareJouerney)
    TextView mShareJouerney;
    private BaiduMap mRouteBaiduMap;
    private BitmapDescriptor startBmp, endBmp;
    private List<LatLng> mPoints;
    PlanNode startNodeStr, endNodeStr;
    OverlayManager routeOverlay = null;//该类提供一个能够显示和管理多个Overlay的基类
    RoutePlanSearch mRPSearch = null;    // 搜索模块，也可去掉地图模块独立使用
    private List<BottomSheetBean> shareBtn;
    private double i;
    private double mTripDist;
    private double mCalorie;
    private int mTripTime;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_journey_detail;
    }

    @Override
    protected void initData() {
//        mToolbar.setTitle("");
//        mTitle.setText(getResources().getString(R.string.journey_detail));
//        setSupportActionBar(mToolbar);
//        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
        Intent intent = getIntent();
        if (intent != null) {
            String bicycleNo = intent.getStringExtra("bicycleNo");
            String carRentalOrderId = intent.getStringExtra("carRentalOrderId");
            String userId = intent.getStringExtra("userId");
            String token = intent.getStringExtra("token");
            String nickname = intent.getStringExtra("nickname");
            String image = intent.getStringExtra("image");
            mCalorie = intent.getDoubleExtra("calorie", 0);
            mTripTime = intent.getIntExtra("tripTime", 0);
            mTripDist = intent.getDoubleExtra("tripDist", 0);
            mBikeNO.setText(bicycleNo);
            mName.setText(nickname);
            mRidetime.setText(String.valueOf(mTripTime));
            mDistanceshow.setText(String.valueOf(MapUtil.changeDouble(mTripDist)));
            mKCal.setText(String.valueOf(MapUtil.changeDouble(mCalorie)));
            Glide.with(this).load(image).error(R.drawable.sharebike).into(mIcon);
            if (bicycleNo != null && !bicycleNo.equals("") && carRentalOrderId != null
                    && !carRentalOrderId.equals("") && !userId.equals("") && userId != null
                    && !token.equals("") && token != null) {

                checkTrip(bicycleNo, carRentalOrderId, userId, token);
            }
        }

        mPoints = new ArrayList<>();
        mRouteBaiduMap = mJourneyMapView.getMap();
        mJourneyMapView.showZoomControls(false);
        mRPSearch = RoutePlanSearch.newInstance();
        mRPSearch.setOnGetRoutePlanResultListener(this);
        startBmp = BitmapDescriptorFactory.fromResource(R.mipmap.route_start);
        endBmp = BitmapDescriptorFactory.fromResource(R.mipmap.route_end);
        initMap();
        StyledDialog.buildLoading(JourneyDetailActivity.this, "正在加载..", true, false).setMsgColor(R.color.color_ff).show();
    }

    private void addOverLayout(LatLng startPosition, LatLng endPosition) {
//        先清除图层
//        mRouteBaiduMap.clear();
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
        LocationClientOption mOption = new LocationClientOption();
        //设置坐标类型
        mOption.setCoorType("bd09ll");
        //设置是否需要地址信息，默认为无地址
//        mOption.setIsNeedAddress(true);
        //设置是否打开gps进行定位
//        mOption.setOpenGps(true);
//        //设置扫描间隔，单位是毫秒 当<1000(1s)时，定时定位无效
//        int span = 10000;
//        mOption.setScanSpan(span);
        UiSettings settings = mRouteBaiduMap.getUiSettings();
        settings.setScrollGesturesEnabled(true);
    }

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

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

        if (bikingRouteResult == null || bikingRouteResult.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(JourneyDetailActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        }
        if (bikingRouteResult.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
//                    result.getSuggestAddrInfo();
            return;
        }
        if (bikingRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {
            if (bikingRouteResult.getRouteLines().size() > 0) {
                BikingRouteLine bikingRouteLine = bikingRouteResult.getRouteLines().get(0);
                BikingRouteOverlay overlay = new BikingRouteOverlay(mRouteBaiduMap);
                routeOverlay = overlay;
                if (!overlay.equals("")) {
                    overlay.setData(bikingRouteResult.getRouteLines().get(0));
                    overlay.addToMap();
                    overlay.zoomToSpan();
                }
            }
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void onDestroy() {
        super.onDestroy();
        mRouteBaiduMap.setMyLocationEnabled(false);
        mJourneyMapView.onDestroy();
        mJourneyMapView = null;
        StyledDialog.dismiss();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mJourneyMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mJourneyMapView.onPause();
        Log.d("实验", "onPause+1");
    }


    private void checkTrip(String bicycleNo, String carRentalOrderId, String userId, String token) {
        Map<String, String> map = new HashMap<>();
        map.put("bicycleNo", bicycleNo);
        map.put("carRentalOrderId", carRentalOrderId);
        map.put("userId", userId);
        map.put("token", token);
        LogUtils.d("参数", bicycleNo + "\n" + carRentalOrderId + "\n" + userId);
        OkHttpUtils.post().url(Api.BASE_URL + Api.TRIPRECORD).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.showShort(JourneyDetailActivity.this, getString(R.string.server_tip));
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d("卡卡", response);
                if (JsonUtils.isSuccess(response)) {
                    StyledDialog.dismissLoading();
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
                            //.color()
//                            BitmapDescriptor descriptor = BitmapDescriptorFactory.fromResource(R.mipmap.icon_road_blue_arrow);
//                            OverlayOptions ooPolyline = new PolylineOptions().width(10).customTexture(descriptor).points(mPoints);
//                            mRouteBaiduMap.addOverlay(ooPolyline);

                            RoutePoint startPoint = routePoints.get(0);
                            startNodeStr = PlanNode.withLocation(new LatLng(startPoint.getRouteLat(), startPoint.getRouteLng()));
                            LatLng startPosition = new LatLng(startPoint.getRouteLat(), startPoint.getRouteLng());
                            MapStatus.Builder builder = new MapStatus.Builder();
                            builder.target(startPosition).zoom(18.0f);
                            mRouteBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                            RoutePoint endPoint = routePoints.get(routePoints.size() - 1);
                            endNodeStr = PlanNode.withLocation(new LatLng(endPoint.getRouteLat(), endPoint.getRouteLng()));
                            LatLng endPosition = new LatLng(endPoint.getRouteLat(), endPoint.getRouteLng());
                            mRPSearch.bikingSearch((new BikingRoutePlanOption()).from(startNodeStr).to(endNodeStr));
//                            addOverLayout(startPosition, endPosition);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    ToastUtils.showShort(JourneyDetailActivity.this, getString(R.string.sorry));
                    StyledDialog.dismissLoading();
                }
            }
        });
    }

    @OnClick({R.id.go_back, R.id.shareJouerney})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.go_back:
                if (ClickUtils.isFastClick()) {
                    return;
                }
                finish();
                break;
            case R.id.shareJouerney:
                if (ClickUtils.isFastClick()) {
                    return;
                }
                shareBtn = new ArrayList<>();
                shareBtn.add(new BottomSheetBean(R.mipmap.umeng_socialize_wechat, "微信"));
                shareBtn.add(new BottomSheetBean(R.mipmap.umeng_socialize_wxcircle, "微信朋友圈"));
                shareBtn.add(new BottomSheetBean(R.mipmap.umeng_socialize_qq_on, "QQ好友"));
                shareBtn.add(new BottomSheetBean(R.mipmap.umeng_socialize_qzone_on, "QQ空间"));
                shareBtn.add(new BottomSheetBean(R.mipmap.umeng_socialize_sina_on, "微博"));
                StyledDialog.buildBottomSheetGv(JourneyDetailActivity.this, "", shareBtn, "", 3, new MyItemDialogListener() {
                    @Override
                    public void onItemClick(CharSequence charSequence, int i) {
                        if (i == 0) {
                            //分享到微信
                            Platform platWeiChat = ShareSDK.getPlatform(Wechat.NAME);
                            showShare(platWeiChat.getName());
                        }
                        if (i == 1) {
                            //分享到微信朋友圈
                            Platform platWeiChatCircle = ShareSDK.getPlatform(WechatMoments.NAME);
                            showShare(platWeiChatCircle.getName());
                        }
                        if (i == 2) {
                            //比如分享到QQ，其他平台则只需要更换平台类名，例如Wechat.NAME则是微信
                            Platform plat = ShareSDK.getPlatform(QQ.NAME);
                            showShare(plat.getName());
                        }
                        if (i == 3) {
                            //比如分享到QQ，其他平台则只需要更换平台类名，例如Wechat.NAME则是微信
                            Platform platQQZONE = ShareSDK.getPlatform(QZone.NAME);
                            showShare(platQQZONE.getName());
                        }
                        if (i == 4) {
                            //比如分享到QQ，其他平台则只需要更换平台类名，例如Wechat.NAME则是微信
                            Platform platSina = ShareSDK.getPlatform(SinaWeibo.NAME);
                            showShare(platSina.getName());
                        }
                    }
                }).setMsgSize(12).setCancelable(true, true).show();
        }
    }

    private void showShare(String platform) {
        final OnekeyShare oks = new OnekeyShare();
        //指定分享的平台，如果为空，还是会调用九宫格的平台列表界面
        if (platform != null) {
            oks.setPlatform(platform);
        }
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(getString(R.string.publicity));
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        oks.setTitleUrl("http://a.app.qq.com/o/simple.jsp?pkgname=com.dcch.sharebike");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我用麒麟单车骑行" + mTripTime + "分钟,消耗" + mCalorie + "千卡");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl("http://www.70bikes.com/MavenSSM/Images/qilin.jpg");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//        oks.setImagePath();//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://a.app.qq.com/o/simple.jsp?pkgname=com.dcch.sharebike");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment(getString(R.string.propagation_language_other));
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("70bikes");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://a.app.qq.com/o/simple.jsp?pkgname=com.dcch.sharebike");
        //启动分享
        oks.show(this);
    }
}
