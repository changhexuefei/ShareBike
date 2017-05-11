package com.dcch.sharebike.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.baidu.mapapi.SDKInitializer;
import com.dcch.sharebike.http.HttpUtils;
import com.dcch.sharebike.moudle.login.activity.PersonalCenterActivity;
import com.github.moduth.blockcanary.BlockCanary;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.smtt.sdk.QbSdk;
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
        // 提供一个全局的静态方法
        if (instance == null) {
            synchronized (PersonalCenterActivity.class) {
                if (instance == null) {
                    instance = new App();
                }
            }
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(getApplicationContext());
        mContext = getApplicationContext();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        BlockCanary.install(this, new AppBlockCanaryContext()).start();
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
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。

        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("app", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(),cb);
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
