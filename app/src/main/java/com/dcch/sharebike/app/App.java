package com.dcch.sharebike.app;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.baidu.mapapi.SDKInitializer;
import com.dcch.sharebike.service.InitializeService;
import com.dcch.sharebike.utils.NetUtils;
import com.squareup.leakcanary.LeakCanary;

import timber.log.BuildConfig;
import timber.log.Timber;


public class App extends MultiDexApplication {

    private static App instance;
    private static Context mContext;
    public static int mNetWorkState;

    public App() {
    }

    //单例模式中获取唯一的Application实例
    public static synchronized App getInstance() {
        // 提供一个全局的静态方法
        if (instance == null) {
            instance = new App();
        }
        return instance;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        LeakCanary.install(this);
        InitializeService.start(this);
        SDKInitializer.initialize(getApplicationContext());
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
        mNetWorkState = NetUtils.getNetworkState(this);
    }


    /**
     * 获取全局Context变量
     *
     * @return Context
     */
    public static Context getContext() {
        return mContext;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
