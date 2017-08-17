//package com.dcch.sharebike.moudle.login.activity;
//
//import android.content.Intent;
//import android.view.View;
//import android.widget.ImageView;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.signature.StringSignature;
//import com.dcch.sharebike.R;
//import com.dcch.sharebike.base.BaseActivity;
//import com.dcch.sharebike.utils.LogUtils;
//
//import butterknife.BindView;
//import butterknife.OnClick;
//
//public class PropagandaPosterActivity extends BaseActivity {
//
//    @BindView(R.id.adversimg)
//    ImageView mImg;
//    @BindView(R.id.close)
//    ImageView mClose;
//    private String mActivityUrl;
//    private String mTitle;
//
//    @Override
//    protected int getLayoutId() {
//        return R.layout.activity_propaganda_poster;
//    }
//
//    @Override
//    protected void initData() {
//        Intent intent = getIntent();
//        if (intent != null) {
//            mActivityUrl = intent.getStringExtra("activityUrl");
//            String imageUrl = intent.getStringExtra("imageUrl");
//            mTitle = intent.getStringExtra("title");
//            LogUtils.d("弹出层", imageUrl);
//            Glide.with(this)
//                    .load(imageUrl)
//                    .signature(new StringSignature("01"))//增加签名
//                    .error(R.drawable.default_image)
//                    .into(mImg);
//        }
//    }
//
//    @OnClick({R.id.adversimg, R.id.close})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.adversimg:
//                if (mActivityUrl != null && !mActivityUrl.equals("") && mTitle != null && !mTitle.equals("")) {
//                    Intent webActivity = new Intent(PropagandaPosterActivity.this, AdvertisementActivity.class);
//                    webActivity.putExtra("activityWebView", mActivityUrl);
//                    webActivity.putExtra("title", mTitle);
//                    startActivity(webActivity);
//                }
//                finish();
//                break;
//            case R.id.close:
//                finish();
//                break;
//        }
//    }
//}
