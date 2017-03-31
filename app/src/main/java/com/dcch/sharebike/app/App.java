package com.dcch.sharebike.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.baidu.mapapi.SDKInitializer;
import com.dcch.sharebike.http.HttpUtils;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.ShareSDK;
import okhttp3.OkHttpClient;

public class App extends Application {
    private List<Activity> activityList = new LinkedList();
    private static App instance;
    private static Context mContext;
    //    private static LocationInfo mLocationInfo;
    public static int code = 0;

    public App() {
    }

    //单例模式中获取唯一的Application实例
    public static App getInstance() {
        if (null == instance) {
            instance = new App();
        }
        return instance;

    }

    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(getApplicationContext());
        mContext = getApplicationContext();
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        ShareSDK.initSDK(this);
//        LeakCanary.install(this);
        //初始化OkHttp
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new LoggerInterceptor("HttpUtils"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .build();
        HttpUtils.init(okHttpClient);
    }

    /**
     * 获取全局Context变量
     *
     * @return Context
     */
    public static Context getContext() {
        return mContext;
    }

//    /**
//     * 获取全局Location对象
//     *
//     * @return Location
//     */
//    public static LocationInfo getLocation() {
//        return mLocationInfo;
//    }
//
//    /**
//     * 设置Location
//     *
//     * @param locationInfo
//     */
//    public static void setLocation(LocationInfo locationInfo) {
//        mLocationInfo = locationInfo;
//    }

    //添加Activity到容器中
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }
    //遍历所有Activity并finish

    public void exit() {

        for (Activity activity : activityList) {
            activity.finish();
        }
        System.exit(0);
    }


}
