package com.dcch.sharebike.moudle.user.fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.dcch.sharebike.R;
import com.dcch.sharebike.app.App;
import com.dcch.sharebike.http.Api;
import com.dcch.sharebike.utils.LogUtils;
import com.dcch.sharebike.utils.SPUtils;
import com.louisgeek.multiedittextviewlib.MultiEditInputView;
import com.xys.libzxing.zxing.activity.CaptureActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.rxgalleryfinal.RxGalleryFinal;
import cn.finalteam.rxgalleryfinal.imageloader.ImageLoaderType;
import cn.finalteam.rxgalleryfinal.rxbus.RxBusResultSubscriber;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageRadioResultEvent;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;


/**
 * A simple {@link Fragment} subclass.
 */
public class CycleFailureFragment extends Fragment {

    @BindView(R.id.bike_code)
    TextView mBikeCode;
    @BindView(R.id.tips)
    TextView mTips;
    @BindView(R.id.scan_code)
    RelativeLayout mScanCode;
    @BindView(R.id.cycle_photo)
    ImageView mCyclePhoto;
    @BindView(R.id.questionDesc)
    MultiEditInputView mQuestionDesc;
    @BindView(R.id.upload)
    TextView upload;
    private OkHttpClient mOkHttpClient;
    private String uID;
    private String result;
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    private static final String TAG = RequestManager.class.getSimpleName();

    public CycleFailureFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(SPUtils.isLogin()){
           String userDetail = (String) SPUtils.get(App.getContext(), "userDetail", "");
            Log.d("用户明细", userDetail);
            try {
                JSONObject object = new JSONObject(userDetail);

                int id = object.getInt("id");
                uID = String.valueOf(id);

                Log.d("用户ID", uID);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cycle_failure, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick({R.id.scan_code, R.id.cycle_photo, R.id.upload})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.scan_code:
                Intent i4 = new Intent(App.getContext(), CaptureActivity.class);
                startActivityForResult(i4, 0);
                break;

            case R.id.cycle_photo:
                RxGalleryFinal.with(getActivity())
                        .image()
                        .radio()
                        .crop()
                        .imageLoader(ImageLoaderType.GLIDE)
                        .subscribe(new RxBusResultSubscriber<ImageRadioResultEvent>() {
                            @Override
                            protected void onEvent(ImageRadioResultEvent imageRadioResultEvent) throws Exception {
                                //得到图片的路径
                                result = imageRadioResultEvent.getResult().getOriginalPath();

                                if (result != null && !result.equals("")) {
                                    //将图片赋值给图片控件
                                    Glide.with(App.getContext()).load(result).into(mCyclePhoto);
                                }
                            }
                        }).openGallery();
                break;
            case R.id.upload:
                String bikeNo = mBikeCode.getText().toString().trim();
                String contentText = mQuestionDesc.getContentText().trim();

                Bitmap bitmap = getimage(result);
                String imageResult = bitmapToBase64(bitmap);
//                File imageResult = new File(result);
                Log.d("图片路劲",imageResult+"");
                String CONTENT_TYPE = "multipart/form-data"; //内容类型
                Map<String,String> map = new HashMap<>();
                map.put("userId",uID);
                map.put("bicycleNo","");
                map.put("faultDescription","");
                map.put("selectFaultDescription","");
//                Request fileRequest = getFileRequest(Api.BASE_URL + Api.ADDTROUBLEORDER, imageResult, map);
//                Log.d("相应",fileRequest+"");


                map.put("imageFile",imageResult);

                OkHttpUtils.post().url(Api.BASE_URL+Api.ADDTROUBLEORDER).params(map)
                        .build()
                    .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.e(e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                            Log.d("上传",response);
                    }
                });
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 将bitmap转换成base64字符串
     *
     * @param bitmap
     * @return base64 字符串
     */
    public static String bitmapToBase64(Bitmap bitmap) {
        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                baos.flush();
                baos.close();
                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 压缩
     *
     * @param image
     * @return
     */
    private Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    private Bitmap getimage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);//此时返回bm为空
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
    }

//    public static Request  getFileRequest(String url, File file, Map<String, String> maps){
//        MultipartBody.Builder builder=  new MultipartBody.Builder().setType(MultipartBody.FORM);
//        if(maps==null){
//            builder.addPart( Headers.of("Content-Disposition", "form-data; name=\"file\";filename=\"file.jpg\""), RequestBody.create(MediaType.parse("image/png"),file)
//            ).build();
//
//        }else{
//            for (String key : maps.keySet()) {
//                builder.addFormDataPart(key, maps.get(key));
//            }
//
//            builder.addPart( Headers.of("Content-Disposition", "form-data; name=\"file\";filename=\"file.jpg\""), RequestBody.create(MediaType.parse("image/png"),file)
//            );
//
//        }
//        RequestBody body=builder.build();
//        return new Request.Builder().url(url).post(body).build();
//
//    }

//    /**
//     *上传文件
//     * @param actionUrl 接口地址
//     * @param paramsMap 参数
//     * @param callBack 回调
//     * @param <T>
//     */
//    public <T>void upLoadFile(String actionUrl, HashMap<String, Object> paramsMap, final ReqCallBack<T> callBack) {
//        try {
//            //补全请求地址
//            String requestUrl = String.format("%s/%s", upload_head, actionUrl);
//            MultipartBody.Builder builder = new MultipartBody.Builder();
//            //设置类型
//            builder.setType(MultipartBody.FORM);
//            //追加参数
//            for (String key : paramsMap.keySet()) {
//                Object object = paramsMap.get(key);
//                if (!(object instanceof File)) {
//                    builder.addFormDataPart(key, object.toString());
//                } else {
//                    File file = (File) object;
//                    builder.addFormDataPart(key, file.getName(), RequestBody.create(null, file));
//                }
//            }
//            //创建RequestBody
//            RequestBody body = builder.build();
//            //创建Request
//            final Request request = new Request.Builder().url(requestUrl).post(body).build();
//            //单独设置参数 比如读取超时时间
//            final Call call = mOkHttpClient.newBuilder().writeTimeout(50, TimeUnit.SECONDS).build().newCall(request);
//            call.enqueue(new Callback() {
//                @Override
//                public void onFailure(Call call, IOException e) {
//                    Log.e(TAG, e.toString());
//                    failedCallBack("上传失败", callBack);
//                }
//
//                @Override
//                public void onResponse(Call call, Response response) throws IOException {
//                    if (response.isSuccessful()) {
//                        String string = response.body().string();
//                        Log.e(TAG, "response ----->" + string);
//                        successCallBack((T) string, callBack);
//                    } else {
//                        failedCallBack("上传失败", callBack);
//                    }
//                }
//            });
//        } catch (Exception e) {
//            Log.e(TAG, e.toString());
//        }
//    }



}
