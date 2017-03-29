package com.dcch.sharebike.base;


import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.dcch.sharebike.app.App;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/2/7 0007.
 */
public abstract class BaseActivity extends AppCompatActivity {
    public static Boolean IS = false;
    private ServiceAndroidContact serviceAndroidContact = new ServiceAndroidContact();

    ServiceConnection coon = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            serviceAndroidContact.Log();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        App.getInstance().addActivity(this);
//        Intent intent = new Intent();
//        intent.setAction("com.gao.startService");
//        intent.setPackage(getPackageName());
//        bindService(intent,coon,BIND_AUTO_CREATE);
//        if (IS == true) {
//            /*String path = Environment.getExternalStorageDirectory() + "/DateApp.apk";
//            Intent intent = new Intent(Intent.ACTION_VIEW);
//            intent.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive");
//            startActivity(intent);*/
//            IS = false;
//        }
        initData();
        initListener();
    }

    protected void initListener() {
    }

    protected abstract int getLayoutId();

    protected abstract void initData();

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        App.getInstance().exit();
    }
}
