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

import com.dcch.sharebike.R;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.http.Api;
import com.dcch.sharebike.moudle.user.adapter.TransactionDetailInfoAdapter;
import com.dcch.sharebike.moudle.user.bean.TransactionDetailInfo;
import com.dcch.sharebike.utils.JsonUtils;
import com.dcch.sharebike.utils.LogUtils;
import com.dcch.sharebike.utils.ToastUtils;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 钱包中交易明细页面
 */

public class TransactionDetailActivity extends BaseActivity {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.transact_list)
    LRecyclerView mTransactList;
    @BindView(R.id.default_transaction_show)
    RelativeLayout mDefaultTransactionShow;
    private String mUserId;
    private TransactionDetailInfo mTransactionDetailInfo;
    private TransactionDetailInfoAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_transaction_detail;
    }

    @Override
    protected void initData() {

        Intent intent = getIntent();
        if (intent != null) {
            mUserId = intent.getStringExtra("userId");
        }

    }

    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTransactionDetail(mUserId);
    }


    private void getTransactionDetail(String userId) {
        Map<String,String> map = new HashMap<>();
        map.put("userId",userId);
        OkHttpUtils.post().url(Api.BASE_URL + Api.SEARCHPAYLIST).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.showShort(TransactionDetailActivity.this,"服务器忙！");
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d("充值",response);
                if(JsonUtils.isSuccess(response)){
                    mDefaultTransactionShow.setVisibility(View.GONE);
                    mTransactList.setVisibility(View.VISIBLE);
                    Gson gson = new Gson();
                    mTransactionDetailInfo = gson.fromJson(response, TransactionDetailInfo.class);
                    LogUtils.d("几条",mTransactionDetailInfo.getPayBills().size()+"");
                    mTransactList.setLayoutManager(new LinearLayoutManager(TransactionDetailActivity.this, OrientationHelper.VERTICAL, false));
                    mAdapter=new TransactionDetailInfoAdapter(TransactionDetailActivity.this,R.layout.item_transaction_detail,mTransactionDetailInfo.getPayBills());
                    LRecyclerViewAdapter adapter = new LRecyclerViewAdapter(mAdapter);
                    //添加分割线
                    mTransactList.addItemDecoration(new DividerItemDecoration(TransactionDetailActivity.this, DividerItemDecoration.VERTICAL));
                    mTransactList.setAdapter(adapter);
                    //禁用下拉刷新功能
                    mTransactList.setPullRefreshEnabled(false);
                    //禁用自动加载更多功能
                    mTransactList.setLoadMoreEnabled(false);

                }else{
                    mDefaultTransactionShow.setVisibility(View.VISIBLE);
                }
            }
        });
    }

}
