package com.dcch.sharebike.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static java.net.HttpURLConnection.HTTP_OK;

/**
 * Created by Administrator on 2017/6/27 0027.
 */

public class BitmapUtil {

    public static Bitmap getBitmapFromNetWork(String imageUrl) {
        URL url = null;
        Bitmap bitmap = null;
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            url = new URL(imageUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(5 * 1000);
            httpURLConnection.setReadTimeout(10 * 1000);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            if (httpURLConnection.getResponseCode() == HTTP_OK) {
                inputStream = httpURLConnection.getInputStream();
                byteArrayOutputStream = new ByteArrayOutputStream();
                int len = 0;
                byte[] buffer = new byte[1024];
                while ((len = inputStream.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, len);
                    byteArrayOutputStream.flush();
                }
                byte[] imageData = byteArrayOutputStream.toByteArray();
                bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
            } else {
                LogUtils.d("图片请求失败");
            }
        } catch (Exception e) {
            LogUtils.e("错误", e.toString());
        } finally {
            try {
                if (byteArrayOutputStream != null) {
                    byteArrayOutputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            } catch (Exception e) {
                LogUtils.e("错误", e.toString());
            }
        }
        return bitmap;
    }

    public static void getBitmapFromNetWorkAndSaveToSDCard(String imageUrl, String filePath) {
        URL url = null;
        File imageFile = null;
        HttpURLConnection httpURLConnection = null;
        FileOutputStream fileOutputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        InputStream inputStream = null;
        BufferedInputStream bufferedInputStream = null;
        try {
            url = new URL(imageUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(5 * 1000);
            httpURLConnection.setReadTimeout(10 * 1000);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            if (httpURLConnection.getResponseCode() == HTTP_OK) {
                imageFile = new File(filePath);
                if (!imageFile.getParentFile().exists()) {
                    imageFile.getParentFile().mkdirs();
                }
                if (!imageFile.exists()) {
                    imageFile.createNewFile();
                }
                fileOutputStream = new FileOutputStream(imageFile);
                bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
                inputStream = httpURLConnection.getInputStream();
                bufferedInputStream = new BufferedInputStream(inputStream);
                int len = 0;
                byte[] buffer = new byte[1024];
                while ((len = bufferedInputStream.read(buffer)) != -1) {
                    bufferedOutputStream.write(buffer, 0, len);
                    bufferedOutputStream.flush();
                }
            } else {
                LogUtils.d("图片请求失败");
            }
        } catch (Exception e) {
            LogUtils.d("e=" + e.toString());
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
                if (bufferedOutputStream != null) {
                    bufferedOutputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            } catch (Exception e) {
                LogUtils.e("e=" + e.toString());
            }
        }
    }
}
