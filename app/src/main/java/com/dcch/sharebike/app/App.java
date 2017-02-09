package com.dcch.sharebike.app;

import android.app.Application;
import android.content.Context;

import com.baidu.mapapi.SDKInitializer;
import com.dcch.sharebike.http.HttpUtils;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class App extends Application {
    private static Context mContext;
//    private static LocationInfo mLocationInfo;
    public static int code = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(getApplicationContext());
        mContext = getApplicationContext();
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

}
