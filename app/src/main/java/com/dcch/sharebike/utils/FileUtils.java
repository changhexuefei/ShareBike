package com.dcch.sharebike.utils;

import android.os.Environment;
import android.util.Log;

import com.dcch.sharebike.moudle.user.activity.SettingActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Administrator on 2017/3/28 0028.
 */

public class FileUtils {
    private String SDPATH;

    public FileUtils() {
        SDPATH = Environment.getExternalStorageDirectory() + "/";
    }

    public File createSdFile(String fileName) throws IOException {
        File file = new File(SDPATH + fileName);
        file.createNewFile();
        Log.i("Debug", file.getAbsolutePath());
        return file;
    }

    public File createSdDir(String dirName) {
        File dir = new File(SDPATH + dirName);
        dir.mkdir();
        return dir;
    }

    public boolean isFileExist(String fileName) {
        File file = new File(SDPATH + fileName);
        file.delete();
        return file.exists();
    }

    public File writeToSdFromInput(String path, String fileName, InputStream inputStream) throws IOException {
        SettingActivity.IS = true;
        File file;
        OutputStream outputStream;
        createSdDir(path);
        file = createSdFile(path + fileName);
        outputStream = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }
        Log.d("@@","@@");
        outputStream.flush();
        inputStream.close();
        outputStream.close();
        return file;
    }


}
