package com.dcch.sharebike.moudle.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.dcch.sharebike.moudle.user.adapter.MessageInfoAdapter;
import com.dcch.sharebike.moudle.user.bean.MessageInfo;
import com.dcch.sharebike.utils.JsonUtils;
import com.dcch.sharebike.utils.NetUtils;
import com.dcch.sharebike.utils.ToastUtils;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
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

public class MyMessageActivity extends BaseActivity {
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.my_message_list)
    LRecyclerView mMyMessageList;
    @BindView(R.id.no_message)
    RelativeLayout mNoMessage;
    private String mUserId;
    private String mToken;
    private MessageInfo mMessageInfo;
    private MessageInfoAdapter mMessageInfoAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_message;
    }

    @Override
    protected void initData() {
        mToolbar.setTitle("");
        mTitle.setText(getResources().getString(R.string.myMessage));
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (NetUtils.isConnected(App.getContext())) {
            if (mUserId != null && mToken != null) {
                getMessageInfo(mUserId, mToken);
                StyledDialog.buildLoading(MyMessageActivity.this, "正在加载..", false, false).setMsgColor(R.color.color_ff).show();
            } else {
                ToastUtils.showShort(this, "网络无法访问，请检查网络连接！");

            }
        }
    }

    private void getMessageInfo(String userId, String token) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", userId);
        map.put("token", token);
        OkHttpUtils.post().url(Api.BASE_URL + Api.GETACTIVITYS).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.showShort(MyMessageActivity.this, "当前服务器忙，请稍后！");
            }

            @Override
            public void onResponse(String response, int id) {
                if (JsonUtils.isSuccess(response)) {
                    StyledDialog.dismissLoading();
                    Gson gson = new Gson();
                    mMessageInfo = gson.fromJson(response, MessageInfo.class);
                    mNoMessage.setVisibility(View.GONE);
                    mMyMessageList.setVisibility(View.VISIBLE);
                    mMessageInfoAdapter = new MessageInfoAdapter(MyMessageActivity.this, R.layout.item_my_message, mMessageInfo.getActivitys());
                    mMyMessageList.setLayoutManager(new LinearLayoutManager(MyMessageActivity.this, OrientationHelper.VERTICAL, false));
                    LRecyclerViewAdapter adapter = new LRecyclerViewAdapter(mMessageInfoAdapter);
                    //添加分割线
                    mMyMessageList.addItemDecoration(new DividerItemDecoration(MyMessageActivity.this, DividerItemDecoration.VERTICAL));
                    mMyMessageList.setAdapter(adapter);
                    CommonFooter footerView = new CommonFooter(MyMessageActivity.this, R.layout.footer);
                    adapter.addFooterView(footerView);
                    //禁用下拉刷新功能
                    mMyMessageList.setPullRefreshEnabled(false);
                    //禁用自动加载更多功能
                    mMyMessageList.setLoadMoreEnabled(false);
                    adapter.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            String activityurl = mMessageInfo.getActivitys().get(position).getActivityurl();
                            String activityname = mMessageInfo.getActivitys().get(position).getActivityname();
                            if (activityurl != null && activityname != null) {
                                Intent messageDetail = new Intent(MyMessageActivity.this, MessageDetailActivity.class);
                                messageDetail.putExtra("activityUrl", activityurl);
                                messageDetail.putExtra("theme", activityname);
                                startActivity(messageDetail);
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
