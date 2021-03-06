package com.dcch.sharebike.base;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.dcch.sharebike.R;
import com.dcch.sharebike.http.Api;
import com.dcch.sharebike.moudle.home.bean.VersionInfo;
import com.dcch.sharebike.moudle.home.parse.ParseXmlService;
import com.dcch.sharebike.utils.LogUtils;
import com.dcch.sharebike.utils.NetUtils;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyDialogListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class UpdateManager {
    private Context mContext;
    private VersionInfo versionInfo;
    private int serverVersionCode;
    private String xmlUrl = Api.VERSION;
    private ProgressBar progressBar;
    private boolean cancelUpdate = false;
    private String fileSavePath;
    private int progress;
    private AlertDialog downLoadDialog;
    private static final int DOWN = 1;// 用于区分正在下载
    private static final int DOWN_FINISH = 0;// 用于区分下载完成


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

    private void versionUpdate() {
        if (isUpdate()) {
            //4:
            showUpdateVersionDialog();
        } else {
//            Toast.makeText(mContext, "已经是最新版本", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 检查软件是否有更新版本
     *
     * @return
     */
    private boolean isUpdate() {
        // 获取当前软件版本
        int currentVersionCode = getVersionCode(mContext);
        serverVersionCode = getServerVersionCode();
        // 版本判断
        if (serverVersionCode > currentVersionCode) {
            return true;
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
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private int getServerVersionCode() {
        versionInfo = getServerXml();
        LogUtils.d("怎麽不走這裏", versionInfo + "");
        if (versionInfo != null) {
            serverVersionCode = versionInfo.getVersionCode();
            LogUtils.d("怎麽不走這裏", versionInfo.getVersionCode() + "");
        }
        return serverVersionCode;
    }

    private VersionInfo getServerXml() {
        Thread thread = new Thread() {
            public void run() {
                // 把ve.srsion.xml放到网络上，然后获取文件信息
                InputStream inputStream = null;
                HttpURLConnection conn = null;
                try {
                    URL url = new URL(xmlUrl);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(5 * 1000);
                    conn.setRequestMethod("GET");// 必须要大写
                    inputStream = conn.getInputStream();
                    ParseXmlService pareseXmlService = new ParseXmlService();
                    versionInfo = pareseXmlService.parseXml(inputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (inputStream != null) {
                        try {
                            conn.disconnect();
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        };
        thread.start();
        try {
            thread.join();
        } catch (Exception ignored) {
        }
        if (versionInfo != null) {
            return versionInfo;
        }
        return null;
    }

    /**
     * //     * 更新提示框
     * //
     */
    private void showUpdateVersionDialog() {
        // 构造对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("软件更新");
        builder.setMessage(versionInfo.getDescription());
        builder.setCancelable(false);
        // 更新
        builder.setPositiveButton("更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // 显示下载对话框
                //5:
                if (NetUtils.isConnected(mContext)) {
                    if (NetUtils.isWifi(mContext)) {
                        showDownloadDialog();
                    } else {
                        StyledDialog.buildIosAlert(mContext, "提示", "您当前使用的是手机网络，确定更新？", new MyDialogListener() {
                            @Override
                            public void onFirst() {
                                showDownloadDialog();
                            }

                            @Override
                            public void onSecond() {
                                return;
                            }
                        }).show();
                    }
                }
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
        builder.setCancelable(false);
        LogUtils.d("怎麽不走這裏", "1223333");
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
    private class downloadApkThread extends Thread {
        InputStream is;
        HttpURLConnection conn;

        @Override
        public void run() {
            super.run();
            // 判断SD卡是否存在，并且是否具有读写权限
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                // 获得存储卡的路径
                String sdpath = Environment.getExternalStorageDirectory() + "/";
                fileSavePath = sdpath;
                try {
                    LogUtils.d("监听", "1223333++++++" + sdpath + versionInfo.getLoadUrl());
                    URL url = new URL(versionInfo.getLoadUrl());
                    // 创建连接
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(5 * 1000);// 设置超时时间
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Charset", "GBK,utf-8;q=0.7,*;q=0.3");
                    // 获取文件大小
                    int length = conn.getContentLength();
//                    progressBar.setMax(length);
                    LogUtils.d("怎麽不走這裏", "1223333++++++" + length);
                    // 创建输入流
                    is = conn.getInputStream();
                    File file = new File(fileSavePath);
                    // 判断文件目录是否存在
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    File apkFile = new File(fileSavePath, versionInfo.getFileName() + ".apk");
                    LogUtils.d("怎麽不走這裏", apkFile + "\n" + length + "\n" + is);
                    readFile(apkFile, is, length);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void readFile(final File apkFile, final InputStream is, final int length) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                LogUtils.d("怎麽不走這裏", "1223333-----");
                FileOutputStream fos;
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
                        LogUtils.d("怎麽不走這裏", "1223333-----" + progress);
                        // 更新进度
                        Message message = new Message();
                        message.what = DOWN;
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
        }).start();
    }

    public void checkVersion() {
        versionUpdate();
    }

    private void installAPK() {
        File apkFile = new File(fileSavePath, versionInfo.getFileName() + ".apk");
        if (!apkFile.exists()) {
            return;
        }
        // 通过Intent安装APK文件
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        FileProvider7.setIntentDataAndType(mContext,
                intent, "application/vnd.android.package-archive", apkFile, true);
        mContext.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    //如何监控在安装包完成安装后，将apk文件删除掉？
    public static void uninstallApk(Context context, String packageName) {
        Uri uri = Uri.parse("package:" + packageName);
        Intent intent = new Intent(Intent.ACTION_DELETE, uri);
        context.startActivity(intent);
    }
}