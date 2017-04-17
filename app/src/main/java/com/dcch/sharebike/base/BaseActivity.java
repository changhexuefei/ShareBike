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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        App.getInstance().addActivity(this);

//        UpdateManager updateManager = new UpdateManager(App.getContext());
//        updateManager.versionUpdate();

//        //1:
//        currentVersionCode = getCurrentVersionCode();
//        //2:
//        serverVersionCode = getServerVersionCode();
//        LogUtils.d("版本",currentVersionCode+"\n"+serverVersionCode);
//        versionUpdate();
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

    @Override
    protected void onResume() {
        super.onResume();
//
    }

//    public void versionUpdate() {
//        //3:
//        if (currentVersionCode < serverVersionCode) {
//            //4:
//            showUpdateVersionDialog();
//        } else {
//            Toast.makeText(this, "已经是最新版本", Toast.LENGTH_SHORT).show();
//        }
//    }

//    /**
//     * 获取软件版本号
//     *
//     * @param
//     * @return
//     */
//    private int getCurrentVersionCode() {
//        PackageManager pm = getPackageManager();
//        try {
//            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
//            return packageInfo.versionCode;
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        return 0;
//    }

//    public int getServerVersionCode() {
//        versionInfo = getServerXml();
//        LogUtils.d("怎麽不走這裏", versionInfo + "");
//        if (versionInfo != null) {
//            serverVersionCode = versionInfo.getVersionCode();
//            LogUtils.d("怎麽不走這裏", versionInfo.getVersionCode() + "");
//        }
//        return serverVersionCode;
//    }

//    public VersionInfo getServerXml() {
//        // 把version.xml放到网络上，然后获取文件信息
//        Thread thread = new Thread() {
//            public void run() {
//                InputStream inputStream = null;
//                HttpURLConnection conn = null;
//                try {
//                    URL url = new URL(xmlUrl);
//                    conn = (HttpURLConnection) url.openConnection();
//                    conn.setReadTimeout(5 * 1000);
//                    conn.setRequestMethod("GET");// 必须要大写
//                    inputStream = conn.getInputStream();
//                    ParseXmlService pareseXmlService = new ParseXmlService();
//                    versionInfo = pareseXmlService.parseXml(inputStream);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } finally {
//                    if (inputStream != null) {
//                        try {
//                            inputStream.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }
//        };
//        thread.start();
//        try {
//            thread.join();
//        } catch (Exception e) {
//        }
//        if (versionInfo != null) {
//
//            return versionInfo;
//        }
//        return null;
//    }

//    /**
//     * 更新提示框
//     */
//    private void showUpdateVersionDialog() {
//        // 构造对话框
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("软件更新");
//        builder.setMessage("检测到新版本,是否下载更新");
//        // 更新
//        builder.setPositiveButton("更新", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                // 显示下载对话框
//                //5:
//                showDownloadDialog();
//            }
//        });
//        // 稍后更新
//        builder.setNegativeButton("稍后更新", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        Dialog noticeDialog = builder.create();
//        noticeDialog.show();
//    }


//    private void showDownloadDialog() {
//        // 构造软件下载对话框
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("正在更新");
//        LogUtils.d("怎麽不走這裏","1223333");
//        // 给下载对话框增加进度条
//        final LayoutInflater inflater = LayoutInflater.from(this);
//        View v = inflater.inflate(R.layout.downloaddialog, null);
//        progressBar = (ProgressBar) v.findViewById(R.id.updateProgress);
//        builder.setView(v);
//        // 取消更新
//        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                // 设置取消状态
//                cancelUpdate = true;
//            }
//        });
//        downLoadDialog = builder.create();
//        downLoadDialog.show();
//        // 下载文件
//        //6:
//        downloadApk();
//    }

//    private void downloadApk() {
//        new downloadApkThread().start();
//    }

//    /**
//     * 下载apk的方法
//     */
//    public class downloadApkThread extends Thread {
//        InputStream is;
//        HttpURLConnection conn;
//
//        @Override
//        public void run() {
//            super.run();
//            // 判断SD卡是否存在，并且是否具有读写权限
//            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//                // 获得存储卡的路径
//                String sdpath = Environment.getExternalStorageDirectory() + "/";
//                fileSavePath = sdpath + "download";
//                try {
//                    URL url = new URL(versionInfo.getLoadUrl());
//                    // 创建连接
//                    conn = (HttpURLConnection) url.openConnection();
//                    conn.setReadTimeout(5 * 1000);// 设置超时时间
//                    conn.setRequestMethod("GET");
//                    conn.setRequestProperty("Charset", "GBK,utf-8;q=0.7,*;q=0.3");
//                    // 获取文件大小
//                    int length = conn.getContentLength();
////                    progressBar.setMax(length*100);
//                    // 创建输入流
//                    is = conn.getInputStream();
//                    File file = new File(fileSavePath);
//                    // 判断文件目录是否存在
//                    if (!file.exists()) {
//                        file.mkdir();
//                    }
//                    File apkFile = new File(fileSavePath, versionInfo.getFileName() + ".apk");
//                    LogUtils.d("怎麽不走這裏","1223333"+apkFile+"\n"+is+"\n"+length);
//                    readFile(apkFile, is, length);
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                } catch (ProtocolException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

//    private void readFile(final File apkFile, final InputStream is, final int length) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    FileOutputStream fos = null;
//                    fos = new FileOutputStream(apkFile);
//                    int count = 0;
//                    // 缓存
//                    byte buf[] = new byte[1024];
//                    // 写入到文件中
//                    do {
//                        int numread = is.read(buf);
//                        count += numread;
//                        LogUtils.d("怎麽不走這裏","1223333"+count);
//                        // 计算进度条位置
//                        progress = (int) (((float) count / length) * 100);
//                        LogUtils.d("怎麽不走這裏","1223333进度条"+progress);
//                        // 更新进度
//                        Message message = new Message();
//                        message.what = DOWN;
//                        mHandler.sendMessage(message);
//                        if (numread <= 0) {
//                            // 下载完成
//                            // 取消下载对话框显示
//                            downLoadDialog.dismiss();
//                            Message message2 = new Message();
//                            message2.obj = DOWN_FINISH;
//                            mHandler.sendMessage(message2);
//                            break;
//                        }
//                        // 写入文件
//                        fos.write(buf, 0, numread);
//                    } while (!cancelUpdate);// 点击取消就停止下载.
//                    fos.close();
//                    is.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }

//    public void checkVersion(View view) {
//        versionUpdate();
//    }
//
//    public void installAPK() {
//        File apkfile = new File(fileSavePath, versionInfo.getFileName() + ".apk");
//        if (!apkfile.exists()) {
//            return;
//        }
//        // 通过Intent安装APK文件
//        Intent i = new Intent(Intent.ACTION_VIEW);
//        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
//        startActivity(i);
//        android.os.Process.killProcess(android.os.Process.myPid());
//    }
}
