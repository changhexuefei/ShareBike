package com.dcch.sharebike.app;

import android.app.Application;
import android.content.Context;

import com.baidu.mapapi.SDKInitializer;
import com.dcch.sharebike.moudle.login.activity.PersonalCenterActivity;
import com.dcch.sharebike.service.InitializeService;

import timber.log.BuildConfig;
import timber.log.Timber;

public class App extends Application {
    //    private static List<Activity> activityList = Collections
//            .synchronizedList(new LinkedList<Activity>());
    private static App instance;
    private static Context mContext;
    //    private static LocationInfo mLocationInfo;
    public static int code = 0;
//    private static RefWatcher sRefWatcher;

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
        mContext = getApplicationContext();
        InitializeService.start(this);
        SDKInitializer.initialize(getApplicationContext());
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//        sRefWatcher = LeakCanary.install(this);
//        BlockCanary.install(this, new AppBlockCanaryContext()).start();
//        JPushInterface.setDebugMode(true);
//        JPushInterface.init(this);
//        ShareSDK.initSDK(this);
//        //初始化OkHttp
//        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LoggerInterceptor("HttpUtils"))
//                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
//                .readTimeout(10000L, TimeUnit.MILLISECONDS)
//                .build();
//        HttpUtils.init(okHttpClient);
//        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
//
//        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
//
//            @Override
//            public void onViewInitFinished(boolean arg0) {
//                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
//                Log.d("app", " onViewInitFinished is " + arg0);
//            }
//
//            @Override
//            public void onCoreInitFinished() {
//            }
//        };
//        //x5内核初始化接口
//        QbSdk.initX5Environment(getApplicationContext(), cb);
    }

//    public static RefWatcher getRefWatcher() {
//        return sRefWatcher;
//    }


    /**
     * 获取全局Context变量
     *
     * @return Context
     */
    public static Context getContext() {
        return mContext;
    }


//    // * get current Activity 获取当前Activity（栈中最后一个压入的）
//
//    public static Activity currentActivity() {
//        if (activityList == null||activityList.isEmpty()) {
//            return null;
//        }
//        Activity activity = activityList.get(activityList.size()-1);
//        return activity;
//    }
//
//    /**
//     * 结束当前Activity（栈中最后一个压入的）
//     */
//    public static void finishCurrentActivity() {
//        if (activityList == null||activityList.isEmpty()) {
//            return;
//        }
//        Activity activity = activityList.get(activityList.size()-1);
//        finishActivity(activity);
//    }
//
//    /**
//     * 结束指定的Activity
//     */
//    public static void finishActivity(Activity activity) {
//        if (activityList == null||activityList.isEmpty()) {
//            return;
//        }
//        if (activity != null) {
//            activityList.remove(activity);
//            activity.finish();
//            activity = null;
//        }
//    }
//
//    /**
//     * 结束指定类名的Activity
//     */
//    public static void finishActivity(Class<?> cls) {
//        if (activityList == null||activityList.isEmpty()) {
//            return;
//        }
//        for (Activity activity : activityList) {
//            if (activity.getClass().equals(cls)) {
//                finishActivity(activity);
//            }
//        }
//    }
//
//    /**
//     * 按照指定类名找到activity
//     *
//     * @param cls
//     * @return
//     */
//    public static Activity findActivity(Class<?> cls) {
//        Activity targetActivity = null;
//        if (activityList != null) {
//            for (Activity activity : activityList) {
//                if (activity.getClass().equals(cls)) {
//                    targetActivity = activity;
//                    break;
//                }
//            }
//        }
//        return targetActivity;
//    }
//
//    /**
//     * @return 作用说明 ：获取当前最顶部activity的实例
//     */
//    public Activity getTopActivity() {
//        Activity mBaseActivity = null;
//        synchronized (activityList) {
//            final int size = activityList.size() - 1;
//            if (size < 0) {
//                return null;
//            }
//            mBaseActivity = activityList.get(size);
//        }
//        return mBaseActivity;
//
//    }
//
//    /**
//     * @return 作用说明 ：获取当前最顶部的acitivity 名字
//     */
//    public String getTopActivityName() {
//        Activity mBaseActivity = null;
//        synchronized (activityList) {
//            final int size = activityList.size() - 1;
//            if (size < 0) {
//                return null;
//            }
//            mBaseActivity = activityList.get(size);
//        }
//        return mBaseActivity.getClass().getName();
//    }
//
//
//    //    添加Activity到容器中
//    public void addActivity(Activity activity) {
//        activityList.add(activity);
//    }
//
//    /**
//     * @param activity 作用说明 ：删除一个activity在管理里
//     */
//    public void popActivity(Activity activity) {
//        activityList.remove(activity);
//        LogUtils.d("activityList:size:" + activityList.size());
//    }
//
//
//    //遍历所有Activity并finish
//
//    public static void exit() {
//        if (activityList == null) {
//            return;
//        }
//        for (Activity activity : activityList) {
//            activity.finish();
//        }
//        activityList.clear();
//    }
//
//
//    /**
//     * 退出应用程序
//     */
//    public static void appExit() {
//        try {
//            LogUtils.e("app exit");
//            exit();
//        } catch (Exception e) {
//        }
//    }
//
//
//    private void registerActivityListener() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
//            LogUtils.d("东东",Build.VERSION.SDK_INT +"\n"+Build.VERSION_CODES.ICE_CREAM_SANDWICH);
//            registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
//                @Override
//                public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
//                    /**
//                     *  监听到 Activity创建事件 将该 Activity 加入list
//                     */
//                    addActivity(activity);
//
//                }
//
//                @Override
//                public void onActivityStarted(Activity activity) {
//
//                }
//
//                @Override
//                public void onActivityResumed(Activity activity) {
//
//                }
//
//                @Override
//                public void onActivityPaused(Activity activity) {
//
//                }
//
//                @Override
//                public void onActivityStopped(Activity activity) {
//
//                }
//
//                @Override
//                public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
//
//                }
//
//                @Override
//                public void onActivityDestroyed(Activity activity) {
//                    if (null == activityList && activityList.isEmpty()) {
//                        return;
//                    }
//                    if (activityList.contains(activity)) {
//                        /**
//                         *  监听到 Activity销毁事件 将该Activity 从list中移除
//                         */
//                        popActivity(activity);
//                    }
//                }
//            });
//        }
//    }


}
