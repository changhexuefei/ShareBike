package com.dcch.sharebike.moudle.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dcch.sharebike.R;
import com.dcch.sharebike.app.App;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.http.Api;
import com.dcch.sharebike.moudle.user.adapter.CouponInfoAdapter;
import com.dcch.sharebike.moudle.user.bean.CouponInfo;
import com.dcch.sharebike.utils.JsonUtils;
import com.dcch.sharebike.utils.LogUtils;
import com.dcch.sharebike.utils.NetUtils;
import com.dcch.sharebike.utils.ToastUtils;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.view.CommonFooter;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

public class CouponListActivity extends BaseActivity {

    //    @BindView(R.id.title)
//    TextView mTitle;
//    @BindView(R.id.toolbar)
//    Toolbar mToolbar;
    @BindView(R.id.coupon_list)
    LRecyclerView mCouponList;
    @BindView(R.id.iv_no_coupon)
    ImageView mIvNoJourney;
    @BindView(R.id.default_show)
    RelativeLayout mDefaultShow;
    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.coupon_rule)
    ImageView mCouponRule;
    @BindView(R.id.iv_no_network_linking)
    ImageView mIvNoNetworkLinking;
    @BindView(R.id.no_network_linking_tip)
    TextView mNoNetworkLinkingTip;
    @BindView(R.id.no_coupon)
    TextView mNoJourney;
    private String mToken;
    private String mUserId;
    private CouponInfoAdapter mCouponInfoAdapter;
    private CouponInfo mCouponInfo;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_coupon_list;
    }

    @Override
    protected void initData() {
//        mToolbar.setTitle("");
//        mTitle.setText(getResources().getString(R.string.my_coupon));
//        setSupportActionBar(mToolbar);
//        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });

        Intent intent = getIntent();
        if (intent != null) {
            mUserId = intent.getStringExtra("userId");
            mToken = intent.getStringExtra("token");
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        LogUtils.d("why", "为什么onCreate");
        super.onCreate(savedInstanceState);
        if (NetUtils.isConnected(App.getContext())) {
            getCouponListInfo(mUserId, mToken);
        } else {
            mIvNoJourney.setVisibility(View.GONE);
            mNoJourney.setVisibility(View.GONE);
            mIvNoNetworkLinking.setVisibility(View.VISIBLE);
            mNoNetworkLinkingTip.setVisibility(View.VISIBLE);
        }
    }

    private void getCouponListInfo(String userId, String token) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", userId);
        map.put("token", mToken);
        LogUtils.d("空空", userId + "\n" + mToken);
        OkHttpUtils.post().url(Api.BASE_URL + Api.GETCOUPON).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.showShort(App.getContext(), "服务器忙，请稍后重试！");
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d("我的行程", response);
                if (JsonUtils.isSuccess(response)) {
                    Gson gson = new Gson();
                    mCouponInfo = gson.fromJson(response, CouponInfo.class);
                    if (mCouponInfo.getCoupons().size() > 0) {
                        mDefaultShow.setVisibility(View.GONE);
                        mCouponList.setVisibility(View.VISIBLE);
                        mCouponInfoAdapter = new CouponInfoAdapter(CouponListActivity.this, R.layout.item_my_coupon, mCouponInfo.getCoupons());
                        mCouponList.setLayoutManager(new LinearLayoutManager(CouponListActivity.this, OrientationHelper.VERTICAL, false));
                        LRecyclerViewAdapter adapter = new LRecyclerViewAdapter(mCouponInfoAdapter);
                        //添加分割线
//                        mCouponList.addItemDecoration(new DividerItemDecoration(CouponListActivity.this, DividerItemDecoration.VERTICAL));
                        mCouponList.setAdapter(adapter);
                        CommonFooter footerView = new CommonFooter(CouponListActivity.this, R.layout.footer);
                        adapter.addFooterView(footerView);
                        //禁用下拉刷新功能
                        mCouponList.setPullRefreshEnabled(false);
                        //禁用自动加载更多功能
                        mCouponList.setLoadMoreEnabled(false);

                        mCouponList.setLScrollListener(new LRecyclerView.LScrollListener() {
                            @Override
                            public void onScrollUp() {
                            }

                            @Override
                            public void onScrollDown() {

                            }

                            @Override
                            public void onScrolled(int distanceX, int distanceY) {

                            }

                            @Override
                            public void onScrollStateChanged(int state) {

                            }
                        });

                    } else {
                        mDefaultShow.setVisibility(View.VISIBLE);
                    }
                }
            }
        });


    }


    @OnClick({R.id.back, R.id.coupon_rule})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.coupon_rule:
                startActivity(new Intent(CouponListActivity.this, CouponRuleActivity.class));
                break;
        }
    }
}
