package com.dcch.sharebike.base;

import android.util.Log;

import com.dcch.sharebike.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2017/3/28 0028.
 */

public class DownloadFile {
    private URL url = null;

    public int downloadFile(String url, String path, String fileName) throws IOException {
        InputStream inputStream;
        FileUtils fileUtils = new FileUtils();
        if (fileUtils.isFileExist(path + fileName)) {//如果存在
            Log.i("Debug", "  " + "1");
            return 1;
        } else {
            inputStream = getInputStreamFromUrl(url);
            File requltFile = fileUtils.writeToSdFromInput(path, fileName, inputStream);
            if (requltFile == null) { //下载失败
                Log.i("Debug", "  " + "-1");
                return -1;
            }
        }
        if (inputStream != null) inputStream.close();
        Log.i("Debug", "  " + "0");
        return 0;//成功
    }

    private InputStream getInputStreamFromUrl(String urlPath) throws IOException {
        url = new URL(urlPath);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        return urlConnection.getInputStream();
    }

}
