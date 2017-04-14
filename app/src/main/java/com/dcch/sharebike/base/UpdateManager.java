package com.dcch.sharebike.base;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dcch.sharebike.R;
import com.dcch.sharebike.moudle.home.bean.VersionInfo;
import com.dcch.sharebike.moudle.home.parse.ParseXmlService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by Administrator on 2017/4/14 0014.
 */

public class UpdateManager {
    private Context mContext;
    private VersionInfo versionInfo;
    private int serverVersionCode;
    private String jsonUrl = "http://192.168.1.108:8080/version.json";
    private ProgressBar progressBar;
    private boolean cancelUpdate = false;
    private String fileSavePath;
    private int progress;
    private AlertDialog downLoadDialog;
    private static final int DOWN = 1;// 用于区分正在下载
    private static final int DOWN_FINISH = 0;// 用于区分下载完成
    private int mCurrentVersionCode;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // 正在下载
                case DOWN:
                    // 设置进度条位置
                    progressBar.setProgress(progress);
                    break;
                case DOWN_FINISH:
                    // 安装文件
                    installAPK();
                    break;
                default:
                    break;
            }
        }
    };

    public UpdateManager(Context context) {
        this.mContext = context;
    }

    public void versionUpdate() {

//        LogUtils.d("版本", mCurrentVersionCode + "\n" + serverVersionCode);
        if (isUpdate()) {
            //4:
            showUpdateVersionDialog();
        } else {
            Toast.makeText(mContext, "已经是最新版本", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 检查软件是否有更新版本
     *
     * @return
     */
    private boolean isUpdate() {
        // 获取当前软件版本
        int versionCode = getVersionCode(mContext);
        // 把version.xml放到网络上，然后获取文件信息
        InputStream inStream = ParseXmlService.class.getClassLoader().getResourceAsStream("version.xml");
        // 解析XML文件。 由于XML文件比较小，因此使用DOM方式进行解析
        ParseXmlService service = new ParseXmlService();
        try {
            versionInfo = service.parseXml(inStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null != versionInfo) {
            int serviceCode = versionInfo.getVersioncode();
            // 版本判断
            if (serviceCode > versionCode) {
                return true;
            }
        }
        return false;
    }


    /**
     * 获取软件版本号
     *
     * @param context
     * @return
     */
    private int getVersionCode(Context context) {
        int versionCode = 0;
        try {
            // 获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = context.getPackageManager().getPackageInfo("com.dcch.sharebike", 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

//    public int getServerVersionCode() {
////        getServerJson();
//        if (versionInfo != null) {
//            serverVersionCode = versionInfo.getVersioncode();
//        }
//        LogUtils.d("版本", serverVersionCode + "");
//        return serverVersionCode;
//    }

//    public VersionInfo getServerJson() {
//        OkHttpUtils.post().url(jsonUrl).build().execute(new StringCallback() {
//            @Override
//            public void onError(Call call, Exception e, int id) {
//
//            }
//
//            @Override
//            public void onResponse(String response, int id) {
//                LogUtils.d("版本", response);
//                if (JsonUtils.isSuccess(response)) {
//                    Gson gson = new Gson();
//                    versionInfo = gson.fromJson(response, VersionInfo.class);
//                    LogUtils.d("版本", versionInfo.getVersioncode() + "");
//                }
//            }
//        });
////        // 把version.xml放到网络上，然后获取文件信息
////        InputStream inputStream = null;
////        HttpURLConnection conn = null;
////        try {
////            URL url = new URL(xmlUrl);
////            conn = (HttpURLConnection) url.openConnection();
////            conn.setReadTimeout(5 * 1000);
////            conn.setRequestMethod("GET");// 必须要大写
////            inputStream = conn.getInputStream();
////            ParseXmlService pareseXmlService = new ParseXmlService();
////            versionInfo = pareseXmlService.parseXml(inputStream);
////            LogUtils.d("版本", versionInfo.getVersionCode() + "");
////        } catch (IOException e) {
////            e.printStackTrace();
////        } finally {
////            if (inputStream != null) {
////                try {
////                    conn.disconnect();
////                    inputStream.close();
////                } catch (IOException e) {
////                    e.printStackTrace();
////                }
////
////            }
////        }
//        return versionInfo;
//
//    }

    /**
     * //     * 更新提示框
     * //
     */
    private void showUpdateVersionDialog() {
        // 构造对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("软件更新");
        builder.setMessage("检测到新版本,是否下载更新");
        // 更新
        builder.setPositiveButton("更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // 显示下载对话框
                //5:
                showDownloadDialog();
            }
        });
        // 稍后更新
        builder.setNegativeButton("稍后更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        Dialog noticeDialog = builder.create();
        noticeDialog.show();
    }

    private void showDownloadDialog() {
        // 构造软件下载对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("正在更新");
        // 给下载对话框增加进度条
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.downloaddialog, null);
        progressBar = (ProgressBar) v.findViewById(R.id.updateProgress);
        builder.setView(v);
        // 取消更新
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // 设置取消状态
                cancelUpdate = true;
            }
        });
        downLoadDialog = builder.create();
        downLoadDialog.show();
        // 下载文件
        //6:
        downloadApk();
    }

    private void downloadApk() {
        new downloadApkThread().start();
    }

    /**
     * 下载apk的方法
     */
    public class downloadApkThread extends Thread {
        InputStream is;
        HttpURLConnection conn;

        @Override
        public void run() {
            super.run();
            // 判断SD卡是否存在，并且是否具有读写权限
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                // 获得存储卡的路径
                String sdpath = Environment.getExternalStorageDirectory() + "/";
                fileSavePath = sdpath + "download";
                try {
                    URL url = new URL(versionInfo.getLoadurl());
                    // 创建连接
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(5 * 1000);// 设置超时时间
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Charset", "GBK,utf-8;q=0.7,*;q=0.3");
                    // 获取文件大小
                    int length = conn.getContentLength();
                    progressBar.setMax(length);
                    // 创建输入流
                    is = conn.getInputStream();
                    File file = new File(fileSavePath);
                    // 判断文件目录是否存在
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    File apkFile = new File(fileSavePath, versionInfo.getFilename() + ".apk");
                    readFile(apkFile, is, length);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (is != null) {
                            conn.disconnect();
                            is.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void readFile(File apkFile, InputStream is, int length) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(apkFile);
            int count = 0;
            // 缓存
            byte buf[] = new byte[1024];
            // 写入到文件中
            do {
                int numread = is.read(buf);
                count += numread;
                // 计算进度条位置
                progress = (int) (((float) count / length) * 100);
                // 更新进度
                Message message = new Message();
                message.obj = DOWN;
                mHandler.sendMessage(message);
                if (numread <= 0) {
                    // 下载完成
                    // 取消下载对话框显示
                    downLoadDialog.dismiss();
                    Message message2 = new Message();
                    message2.obj = DOWN_FINISH;
                    mHandler.sendMessage(message2);
                    break;
                }
                // 写入文件
                fos.write(buf, 0, numread);
            } while (!cancelUpdate);// 点击取消就停止下载.
            fos.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void checkVersion(View view) {
        versionUpdate();
    }

    public void installAPK() {
        File apkfile = new File(fileSavePath, versionInfo.getFilename() + ".apk");
        if (!apkfile.exists()) {
            return;
        }
        // 通过Intent安装APK文件
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        mContext.startActivity(i);
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
