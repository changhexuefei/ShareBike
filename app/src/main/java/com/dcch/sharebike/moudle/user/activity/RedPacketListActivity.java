package com.dcch.sharebike.moudle.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dcch.sharebike.R;
import com.dcch.sharebike.app.App;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.http.Api;
import com.dcch.sharebike.moudle.user.adapter.RedPacketInfoAdapter;
import com.dcch.sharebike.moudle.user.bean.RedPacketInfo;
import com.dcch.sharebike.utils.JsonUtils;
import com.dcch.sharebike.utils.LogUtils;
import com.dcch.sharebike.utils.NetUtils;
import com.dcch.sharebike.utils.ToastUtils;
import com.dcch.sharebike.view.MyDividerItemDecoration;
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
import okhttp3.Call;


public class RedPacketListActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.red_packet_list)
    LRecyclerView mRedPacketList;
    @BindView(R.id.no_red_info)
    RelativeLayout mDefaultShow;
    @BindView(R.id.without_network)
    RelativeLayout mWithoutNetwork;
    private String mUserId;
    private String mToken;
    private RedPacketInfoAdapter mPacketInfoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (NetUtils.isConnected(App.getContext())) {
            if (mUserId != null && mToken != null) {
                getRedPacketList(mUserId, mToken);
                StyledDialog.buildLoading(RedPacketListActivity.this, "正在加载..", true, false).setMsgColor(R.color.color_ff).show();
            }
        } else {
            mWithoutNetwork.setVisibility(View.VISIBLE);
        }
    }

    private void getRedPacketList(String userId, String token) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", userId);
        map.put("token", token);
        OkHttpUtils.post().url(Api.BASE_URL + Api.MERCHANGIFTRECORD).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                StyledDialog.dismissLoading();
                ToastUtils.showShort(RedPacketListActivity.this, getString(R.string.server_tip));
            }

            @Override
            public void onResponse(String response, int id) {
                if (JsonUtils.isSuccess(response)) {
                    LogUtils.d("反应", response);
                    Gson gson = new Gson();
                    RedPacketInfo redPacketInfo = gson.fromJson(response, RedPacketInfo.class);
                    if (redPacketInfo.getMerchanBill().size() > 0) {
                        StyledDialog.dismissLoading();
                        mDefaultShow.setVisibility(View.GONE);
                        mRedPacketList.setVisibility(View.VISIBLE);
                        mPacketInfoAdapter = new RedPacketInfoAdapter(RedPacketListActivity.this, R.layout.item_red_packet, redPacketInfo.getMerchanBill());
                        mRedPacketList.setLayoutManager(new LinearLayoutManager(RedPacketListActivity.this, OrientationHelper.VERTICAL, false));
                        LRecyclerViewAdapter adapter = new LRecyclerViewAdapter(mPacketInfoAdapter);
                        mRedPacketList.addItemDecoration(new MyDividerItemDecoration(RedPacketListActivity.this, DividerItemDecoration.VERTICAL));
                        mRedPacketList.setAdapter(adapter);
                        CommonFooter footerView = new CommonFooter(RedPacketListActivity.this, R.layout.footer);
                        adapter.addFooterView(footerView);
                        //禁用下拉刷新功能
                        mRedPacketList.setPullRefreshEnabled(false);
                        //禁用自动加载更多功能
                        mRedPacketList.setLoadMoreEnabled(false);
                    }
                } else {
                    StyledDialog.dismissLoading();
                    mDefaultShow.setVisibility(View.VISIBLE);
                    mRedPacketList.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_red_list;
    }

    @Override
    protected void initData() {
        mToolbar.setTitle("");
        mTitle.setText(getResources().getString(R.string.my_red_packet));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Intent intent = getIntent();
        if (intent != null) {
            mUserId = intent.getStringExtra("userId");
            mToken = intent.getStringExtra("token");
        }
    }
}
