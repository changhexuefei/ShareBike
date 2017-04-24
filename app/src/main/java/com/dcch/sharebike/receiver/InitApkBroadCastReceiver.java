package com.dcch.sharebike.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.dcch.sharebike.utils.LogUtils;

/**
 * Created by Administrator on 2017/4/24 0024.
 */

public class InitApkBroadCastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_PACKAGE_ADDED.equals(intent.getAction())) {
            LogUtils.d("监听","监听到系统广播添加");

        }

        if (Intent.ACTION_PACKAGE_REMOVED.equals(intent.getAction())) {
            LogUtils.d("监听","监听到系统广播移除");

        }

        if (Intent.ACTION_PACKAGE_REPLACED.equals(intent.getAction())) {
            LogUtils.d("监听","监听到系统广播替换");

        }
    }
}
