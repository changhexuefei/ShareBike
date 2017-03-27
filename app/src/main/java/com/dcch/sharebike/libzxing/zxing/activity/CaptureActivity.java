/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dcch.sharebike.libzxing.zxing.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.dcch.sharebike.R;
import com.dcch.sharebike.libzxing.zxing.camera.CameraManager;
import com.dcch.sharebike.libzxing.zxing.decode.DecodeThread;
import com.dcch.sharebike.libzxing.zxing.utils.BeepManager;
import com.dcch.sharebike.libzxing.zxing.utils.CaptureActivityHandler;
import com.dcch.sharebike.libzxing.zxing.utils.InactivityTimer;
import com.dcch.sharebike.moudle.user.activity.ManualInputActivity;
import com.dcch.sharebike.moudle.user.activity.UnlockProgressActivity;
import com.dcch.sharebike.utils.DensityUtils;
import com.dcch.sharebike.utils.LogUtils;
import com.dcch.sharebike.utils.ToastUtils;
import com.google.zxing.Result;

import java.io.IOException;
import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * This activity opens the camera and does the actual scanning on a background
 * thread. It draws a viewfinder to help the user place the barcode correctly,
 * shows feedback as the image processing is happening, and then overlays the
 * results when a scan is successful.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 * @author Sean Owen
 */
public final class CaptureActivity extends Activity implements SurfaceHolder.Callback {

    private static final String TAG = CaptureActivity.class.getSimpleName();

    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.help_tip)
    TextView mHelpTip;
    @BindView(R.id.manualInput)
    TextView mManualInput;
    @BindView(R.id.openFlashLight)
    ToggleButton mOpenFlashLight;


    private CameraManager cameraManager;
    private CaptureActivityHandler handler;
    private InactivityTimer inactivityTimer;
    private BeepManager beepManager;

    private SurfaceView scanPreview = null;
    private RelativeLayout scanContainer;
    private RelativeLayout scanCropView;
    private ImageView scanLine;
    //ToggleButton

    private Rect mCropRect = null;
    private boolean isHasSurface = false;
    private Camera camera = null;
    private Camera.Parameters parameters = null;
    public static boolean kaiguan = true; // 定义开关状态，状态为false，打开状态，状态为true，关闭状态
    private String mMsg;

    public Handler getHandler() {
        return handler;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_capture);
        ButterKnife.bind(this);
        controlIconSize();

        scanPreview = (SurfaceView) findViewById(R.id.capture_preview);
        scanContainer = (RelativeLayout) findViewById(R.id.capture_container);
        scanCropView = (RelativeLayout) findViewById(R.id.capture_crop_view);
        scanLine = (ImageView) findViewById(R.id.capture_scan_line);
        inactivityTimer = new InactivityTimer(this);
        beepManager = new BeepManager(this);

        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation
                .RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT,
                0.9f);
        animation.setDuration(4500);
        animation.setRepeatCount(-1);
        animation.setRepeatMode(Animation.RESTART);
        scanLine.startAnimation(animation);
    }

    private void controlIconSize() {
        initDrawable(mOpenFlashLight);
        initDrawable(mManualInput);
    }


    private void init() {
        Intent intent = getIntent();
        if (intent != null) {
            mMsg = intent.getStringExtra("msg");
            LogUtils.d("標記",mMsg);
        }


        mManualInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent manual = new Intent(CaptureActivity.this, ManualInputActivity.class);
                manual.putExtra("tag",mMsg);
                startActivity(manual);
            }
        });

        mOpenFlashLight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                //低于6.0系统的手电筒
                if (isChecked) {
                    camera = Camera.open();
                    parameters = camera.getParameters();
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);// 开启
                    camera.setParameters(parameters);
                    camera.startPreview();
                } else {
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);// 关闭
                    camera.setParameters(parameters);
                    camera.stopPreview();
                    camera.release();
                }
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        init();

        // CameraManager must be initialized here, not in onCreate(). This is
        // necessary because we don't
        // want to open the camera driver and measure the screen size if we're
        // going to show the help on
        // first launch. That led to bugs where the scanning rectangle was the
        // wrong size and partially
        // off screen.
        cameraManager = new CameraManager(getApplication());
        handler = null;

        if (isHasSurface) {
            // The activity was paused but not stopped, so the surface still
            // exists. Therefore
            // surfaceCreated() won't be called, so init the camera here.
            initCamera(scanPreview.getHolder());
        } else {
            // Install the callback and wait for surfaceCreated() to init the
            // camera.
            scanPreview.getHolder().addCallback(this);
        }

        inactivityTimer.onResume();
    }

    @Override
    protected void onPause() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        inactivityTimer.onPause();
        beepManager.close();
        cameraManager.closeDriver();
        if (!isHasSurface) {
            scanPreview.getHolder().removeCallback(this);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (holder == null) {
            Log.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
        }
        if (!isHasSurface) {
            isHasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isHasSurface = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }


    /**
     * A valid barcode has been found, so give an indication of success and show
     * the results.
     *
     * @param rawResult The contents of the barcode.
     * @param bundle    The extras
     */
    public void handleDecode(Result rawResult, Bundle bundle) {
        inactivityTimer.onActivity();
        beepManager.playBeepSoundAndVibrate();
        startActivity(new Intent(CaptureActivity.this, UnlockProgressActivity.class));
        Intent resultIntent = new Intent();
        bundle.putInt("width", mCropRect.width());
        bundle.putInt("height", mCropRect.height());
        bundle.putString("result", rawResult.getText());
        resultIntent.putExtras(bundle);
        this.setResult(RESULT_OK, resultIntent);
        CaptureActivity.this.finish();
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            Log.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
            return;
        }
        try {
            cameraManager.openDriver(surfaceHolder);
            // Creating the handler starts the preview, which can also throw a
            // RuntimeException.
            if (handler == null) {
                handler = new CaptureActivityHandler(this, cameraManager, DecodeThread.ALL_MODE);
            }

            initCrop();
        } catch (IOException ioe) {
            Log.w(TAG, ioe);
            displayFrameworkBugMessageAndExit();
        } catch (RuntimeException e) {
            // Barcode Scanner has seen crashes in the wild of this variety:
            // java.?lang.?RuntimeException: Fail to connect to camera service
            Log.w(TAG, "Unexpected error initializing camera", e);
            displayFrameworkBugMessageAndExit();
        }
    }

    private void displayFrameworkBugMessageAndExit() {
        // camera error
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.app_name));
        builder.setMessage("Camera error");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }

        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });
        builder.show();
    }

    public void restartPreviewAfterDelay(long delayMS) {
        if (handler != null) {
            handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
        }
    }

    public Rect getCropRect() {
        return mCropRect;
    }

    /**
     * 初始化截取的矩形区域
     */
    private void initCrop() {
        int cameraWidth = cameraManager.getCameraResolution().y;
        int cameraHeight = cameraManager.getCameraResolution().x;

        /** 获取布局中扫描框的位置信息 */
        int[] location = new int[2];
        scanCropView.getLocationInWindow(location);

        int cropLeft = location[0];
        int cropTop = location[1] - getStatusBarHeight();

        int cropWidth = scanCropView.getWidth();
        int cropHeight = scanCropView.getHeight();

        /** 获取布局容器的宽高 */
        int containerWidth = scanContainer.getWidth();
        int containerHeight = scanContainer.getHeight();

        /** 计算最终截取的矩形的左上角顶点x坐标 */
        int x = cropLeft * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的左上角顶点y坐标 */
        int y = cropTop * cameraHeight / containerHeight;

        /** 计算最终截取的矩形的宽度 */
        int width = cropWidth * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的高度 */
        int height = cropHeight * cameraHeight / containerHeight;

        /** 生成最终的截取的矩形 */
        mCropRect = new Rect(x, y, width + x, height + y);
    }

    private int getStatusBarHeight() {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    //将TextView中的图片转化为规定大小的方法
    public void initDrawable(TextView v) {
        Drawable drawable = v.getCompoundDrawables()[1];
        drawable.setBounds(0, 0, DensityUtils.dp2px(CaptureActivity.this, 50), DensityUtils.dp2px(CaptureActivity.this, 50));
        v.setCompoundDrawables(null, drawable, null, null);
    }


    @OnClick({R.id.back, R.id.help_tip})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.help_tip:
                ToastUtils.showShort(CaptureActivity.this, "我是帮助");
                break;

        }
    }

//    /**
//     * 是否开启了闪光灯
//     *
//     * @return
//     */
//    public boolean isFlashlightOn() {
//        if (camera == null) {
//            camera = Camera.open();
//        }
//
//        Camera.Parameters parameters = camera.getParameters();
//        String flashMode = parameters.getFlashMode();
//
//        if (flashMode.equals(Camera.Parameters.FLASH_MODE_TORCH)) {
//
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    /**
//     * 闪光灯开关
//     */
//    public void flashlightUtils() {
//        if (camera == null) {
//            camera = Camera.open();
//        }
//
//        Camera.Parameters parameters = camera.getParameters();
//        // String flashMode = parameters.getFlashMode();
//
//        if (isFlashlightOn()) {
//
//            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);// 关闭
//            camera.setParameters(parameters);
//            camera.release();
//            camera = null;
//            Toast.makeText(context, "关闭手电筒", Toast.LENGTH_SHORT).show();
//        } else {
//            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);// 开启
//            camera.setParameters(parameters);
//            Toast.makeText(context, "开启手电筒", Toast.LENGTH_SHORT).show();
//        }
//
//    }
//
//    /**
//     * 闪光灯开关2
//     */
//    public void flashUtils() {
//
//        Camera camera = Camera.open();
//
//        Camera.Parameters parameters = camera.getParameters();
//        String flashMode = parameters.getFlashMode();
//        if (flashMode.equals(Camera.Parameters.FLASH_MODE_TORCH)) {
//            camera.stopPreview();
//            camera.release();
//            camera = null;
//
//        } else {
//
//            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
//            camera.setParameters(parameters);
//            camera.autoFocus(new Camera.AutoFocusCallback() {
//                public void onAutoFocus(boolean success, Camera camera) {
//                }
//            });
//            camera.startPreview();
//        }
//    }


}