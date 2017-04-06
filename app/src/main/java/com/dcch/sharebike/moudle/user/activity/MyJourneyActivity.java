package com.dcch.sharebike.moudle.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
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
import com.dcch.sharebike.moudle.user.adapter.JourneyInfoAdapter;
import com.dcch.sharebike.moudle.user.bean.JourneyInfo;
import com.dcch.sharebike.utils.JsonUtils;
import com.dcch.sharebike.utils.LogUtils;
import com.dcch.sharebike.utils.ToastUtils;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
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

public class MyJourneyActivity extends BaseActivity {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.help)
    TextView help;
    @BindView(R.id.journey_list)
    LRecyclerView journeyList;
    @BindView(R.id.no_journey)
    TextView noJourney;
    @BindView(R.id.default_show)
    RelativeLayout mDefaultShow;
    private String mPhone;
    private JourneyInfoAdapter mAdapter;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_journey;
    }

    @Override
    protected void initData() {
        mAdapter = new JourneyInfoAdapter(this);
        Intent intent = getIntent();
        if (intent != null) {
            mPhone = intent.getStringExtra("phone");
        }
    }

    @OnClick({R.id.back, R.id.help})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.help:
                ToastUtils.showShort(this, "我是帮助");
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        LogUtils.d("why", "为什么onCreate");
        super.onCreate(savedInstanceState);
        getJourneyInfo(mPhone);
    }

    private void getJourneyInfo(String phone) {
        Map<String, String> map = new HashMap<>();
        map.put("phone", mPhone);
        OkHttpUtils.post().url(Api.BASE_URL + Api.GETCARRENTALORDERBYPHONE).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.showShort(App.getContext(), "服务器忙，请稍后重试！");
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d("我的行程", response);
                if (JsonUtils.isSuccess(response)) {
                    mDefaultShow.setVisibility(View.GONE);
                    journeyList.setVisibility(View.VISIBLE);
                    Gson gson = new Gson();
                    JourneyInfo journeyInfo = gson.fromJson(response, JourneyInfo.class);
                    journeyList.setLayoutManager(new LinearLayoutManager(MyJourneyActivity.this, OrientationHelper.VERTICAL, false));
                    mAdapter.setInfos(journeyInfo);
                    LRecyclerViewAdapter adapter = new LRecyclerViewAdapter(mAdapter);
                    //添加分割线
                    journeyList.addItemDecoration(new DividerItemDecoration(MyJourneyActivity.this, DividerItemDecoration.VERTICAL));
                    journeyList.setAdapter(adapter);
                    CommonFooter footerView = new CommonFooter(MyJourneyActivity.this, R.layout.footer);
                    adapter.addFooterView(footerView);
                    //禁用下拉刷新功能
                    journeyList.setPullRefreshEnabled(false);
                    //禁用自动加载更多功能
                    journeyList.setLoadMoreEnabled(false);
                    adapter.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            ToastUtils.showShort(MyJourneyActivity.this, position + "");
                        }
                    });
                    journeyList.setLScrollListener(new LRecyclerView.LScrollListener() {
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
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.d("why", "为什么onResume");
    }
}
