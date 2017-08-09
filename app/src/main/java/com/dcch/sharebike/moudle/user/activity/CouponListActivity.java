package com.dcch.sharebike.moudle.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

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
import com.hss01248.dialog.StyledDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

public class CouponListActivity extends BaseActivity {

    @BindView(R.id.coupon_list)
    LRecyclerView mCouponList;
    @BindView(R.id.default_show)
    RelativeLayout mDefaultShow;
    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.coupon_rule)
    ImageView mCouponRule;
    @BindView(R.id.without_network)
    RelativeLayout mWithoutNetwork;
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
        Intent intent = getIntent();
        if (intent != null) {
            mUserId = intent.getStringExtra("userId");
            mToken = intent.getStringExtra("token");
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (NetUtils.isConnected(App.getContext())) {
            getCouponListInfo(mUserId, mToken);
            StyledDialog.buildLoading(CouponListActivity.this, "正在加载..", true, false).setMsgColor(R.color.color_ff).show();
        } else {
            mWithoutNetwork.setVisibility(View.VISIBLE);
        }
    }

    private void getCouponListInfo(String userId, String token) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", userId);
        map.put("token", token);
        LogUtils.d("空空", userId + "\n" + token);
        OkHttpUtils.post().url(Api.BASE_URL + Api.GETCOUPON).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                StyledDialog.dismissLoading();
                ToastUtils.showShort(CouponListActivity.this, getString(R.string.server_tip));
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d("我的行程", response);
                if (JsonUtils.isSuccess(response)) {
                    Gson gson = new Gson();
                    mCouponInfo = gson.fromJson(response, CouponInfo.class);
                    if (mCouponInfo.getCoupons().size() > 0) {
                        StyledDialog.dismissLoading();
                        mDefaultShow.setVisibility(View.GONE);
                        mCouponList.setVisibility(View.VISIBLE);
                        mCouponInfoAdapter = new CouponInfoAdapter(CouponListActivity.this, R.layout.item_my_coupon, mCouponInfo.getCoupons());
                        mCouponList.setLayoutManager(new LinearLayoutManager(CouponListActivity.this, OrientationHelper.VERTICAL, false));
                        LRecyclerViewAdapter adapter = new LRecyclerViewAdapter(mCouponInfoAdapter);
                        mCouponList.setAdapter(adapter);
                        CommonFooter footerView = new CommonFooter(CouponListActivity.this, R.layout.footer);
                        adapter.addFooterView(footerView);
                        //禁用下拉刷新功能
                        mCouponList.setPullRefreshEnabled(false);
                        //禁用自动加载更多功能
                        mCouponList.setLoadMoreEnabled(false);
                    }
                } else {
                    StyledDialog.dismissLoading();
                    mDefaultShow.setVisibility(View.VISIBLE);
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
