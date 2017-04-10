package com.dcch.sharebike.receiver;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.dcch.sharebike.R;
import com.dcch.sharebike.activity.SplashActivity;
import com.dcch.sharebike.moudle.user.activity.SettingActivity;
import com.dcch.sharebike.service.DownService;
import com.dcch.sharebike.utils.LogUtils;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by gao on 2017/2/20.
 */

public class MyReceiver extends BroadcastReceiver {
    private String TAG = "Debug";
    private NotificationManager manager;
    private NotificationCompat.Builder notifyBuilder = null;
    private Notification notification = null;
    private Bitmap bitmap = null;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            if(bitmap!=null){
                notifyBuilder.setLargeIcon(bitmap);
            }else{
                notifyBuilder.setSmallIcon(R.drawable.ic_circle);
            }
            notification = notifyBuilder.build();
            notification.defaults |= Notification.DEFAULT_SOUND;
            notification.defaults |= Notification.DEFAULT_VIBRATE;
            manager.notify(1000, notification);
        }
    };

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d(TAG, "onReceive - " + intent.getAction());

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
        }else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "收到了自定义消息。消息内容是：" + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            openNotification(context,bundle);
            // 自定义消息不会展示在通知栏，完全要开发者写代码去处理
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "收到了通知");
            // 在这里可以做些统计，或者做些其他工作
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "用户点击打开了通知");
            // 在这里可以自己写代码去定义用户点击后的行为
            Intent i = new Intent(context, SplashActivity.class);  //自定义打开的界面
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        } else {
            Log.d(TAG, "Unhandled intent - " + intent.getAction());
        }

    }

    /***
     * 打开通知要处理的方法
     * @param context
     * @param bundle
     */
    private void openNotification(Context context, Bundle bundle){
        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notifyBuilder = new NotificationCompat.Builder(context);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        String title = bundle.getString(JPushInterface.EXTRA_TITLE);
        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        LogUtils.d(TAG,extras+"\n"+title+"\n"+message);
        String versionsUrl = "";
        try {
            JSONObject extrasJson = new JSONObject(extras);
            versionsUrl = extrasJson.optString("DownloadDate");
            Log.d(TAG,"收到key为 one的数据    "+versionsUrl);
            Intent i = new Intent(context, SplashActivity.class);
            bundle.putBoolean("push", true);
            i.putExtras(bundle);
            PendingIntent pi = PendingIntent.getActivity(context, 1000, i, PendingIntent.FLAG_UPDATE_CURRENT);

            notifyBuilder.setContentTitle(title);
            notifyBuilder.setContentText(message);
            notifyBuilder.setContentIntent(pi);
            notifyBuilder.setAutoCancel(true);
            final String finalVersionsUrl = versionsUrl;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    bitmap = returnBitMap(finalVersionsUrl);
                    handler.sendEmptyMessage(1);
                }
            }).start();

            handler.sendEmptyMessage(1);
            //这里要先发送一次，因为</span><span style="font-size:14px;">onReceive</span><span style="font-size:14px;">方法实现不可以超过10秒，获取图片是耗时的，然而Notification没有图片通知是发送不了的。

            if(!versionsUrl.equals("")){
                SettingActivity.IS = true;
                Log.d(TAG,"有更新数据");
                Intent intent = new Intent(context,DownService.class);
                intent.putExtra("DateUrl",versionsUrl);
                context.startService(intent);
            }else{
                Log.d(TAG,"没有更新数据");
            }
        } catch (Exception e) {
            Log.d(TAG, "Unexpected: extras is not a valid json", e);
            return;
        }

    }

    //以Bitmap的方式获取一张图片
    public Bitmap returnBitMap(String url){
        URL myFileUrl = null;
        Bitmap bitmap = null;
        try{
            myFileUrl = new URL(url);
        }catch(MalformedURLException e){
            e.printStackTrace();
        }
        try{
            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        return bitmap;
    }



}
