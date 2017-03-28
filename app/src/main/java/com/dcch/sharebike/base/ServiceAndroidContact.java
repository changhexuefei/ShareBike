package com.dcch.sharebike.base;

import android.os.Binder;
import android.util.Log;

/**
 * Created by Administrator on 2017/3/28 0028.
 */

public class ServiceAndroidContact extends Binder {

    public void Log(){
        Log.i("Debug","我是Activity和Service交互相应结果");
    }
}
