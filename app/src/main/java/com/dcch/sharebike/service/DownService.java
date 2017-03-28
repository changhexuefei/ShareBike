package com.dcch.sharebike.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.dcch.sharebike.base.DownloadFile;
import com.dcch.sharebike.base.ServiceAndroidContact;

import java.io.IOException;

/**
 * Created by Administrator on 2017/3/28 0028.
 */

public class DownService extends Service {

    private String TAG = "Debug";
    private ServiceAndroidContact serviceAndroidContact = new ServiceAndroidContact();
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG,"运行到了服务阶段");
        final String downUrl = intent.getExtras().getString("DateUrl");
        Log.i(TAG,downUrl);
        new Thread(new Runnable() {
            @Override
            public void run() {
                DownloadFile downloadFile = new DownloadFile();
                try {
                    downloadFile.downloadFile(downUrl,"","DateApp.apk");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(!Thread.currentThread().isAlive()){
                    Log.i(TAG,"Thread Over");
                }
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return serviceAndroidContact;
    }
}
