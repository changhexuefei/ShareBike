package com.dcch.sharebike.moudle.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dcch.sharebike.R;
import com.dcch.sharebike.app.App;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.http.Api;
import com.dcch.sharebike.moudle.user.adapter.TransactionDetailInfoAdapter;
import com.dcch.sharebike.moudle.user.bean.TransactionDetailInfo;
import com.dcch.sharebike.utils.ClickUtils;
import com.dcch.sharebike.utils.JsonUtils;
import com.dcch.sharebike.utils.LogUtils;
import com.dcch.sharebike.utils.NetUtils;
import com.dcch.sharebike.utils.ToastUtils;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.google.gson.Gson;
import com.hss01248.dialog.StyledDialog;
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


    @BindView(R.id.transact_list)
    LRecyclerView mTransactList;

    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.refundExplain)
    TextView mRefundExplain;
    @BindView(R.id.iv_noPay)
    ImageView mIvNoPay;
    @BindView(R.id.iv_no_network_linking)
    ImageView mIvNoNetworkLinking;
    @BindView(R.id.no_network_linking_tip)
    TextView mNoNetworkLinkingTip;
    @BindView(R.id.no_pay_tip)
    TextView mNoPayTip;
    private String mUserId;
    private TransactionDetailInfo mTransactionDetailInfo;
    private TransactionDetailInfoAdapter mAdapter;
    private String mToken;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_transaction_detail;
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
            if (mUserId != null && !mUserId.equals("")) {
                getTransactionDetail(mUserId);
                StyledDialog.buildLoading(TransactionDetailActivity.this, "正在加载..", true, false).setMsgColor(R.color.color_ff).show();
            }
        } else {
            mNoPayTip.setVisibility(View.GONE);
            mIvNoPay.setVisibility(View.GONE);
            mIvNoNetworkLinking.setVisibility(View.VISIBLE);
            mNoNetworkLinkingTip.setVisibility(View.VISIBLE);
        }
    }


    private void getTransactionDetail(String userId) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", userId);
        map.put("token", mToken);
        OkHttpUtils.post().url(Api.BASE_URL + Api.SEARCHPAYLIST).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.showShort(TransactionDetailActivity.this, "服务器忙！");
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d("充值", response);
                if (JsonUtils.isSuccess(response)) {
                    StyledDialog.dismissLoading();
                    mTransactList.setVisibility(View.VISIBLE);
                    Gson gson = new Gson();
                    mTransactionDetailInfo = gson.fromJson(response, TransactionDetailInfo.class);
                    LogUtils.d("几条", mTransactionDetailInfo.getPayBills().size() + "");
                    if (mTransactionDetailInfo.getPayBills().size() > 0) {
                        mTransactList.setLayoutManager(new LinearLayoutManager(TransactionDetailActivity.this, OrientationHelper.VERTICAL, false));
                        mAdapter = new TransactionDetailInfoAdapter(TransactionDetailActivity.this, R.layout.item_transaction_detail, mTransactionDetailInfo.getPayBills());
                        LRecyclerViewAdapter adapter = new LRecyclerViewAdapter(mAdapter);
                        //添加分割线
                        mTransactList.addItemDecoration(new DividerItemDecoration(TransactionDetailActivity.this, DividerItemDecoration.VERTICAL));
                        mTransactList.setAdapter(adapter);
                        //禁用下拉刷新功能
                        mTransactList.setPullRefreshEnabled(false);
                        //禁用自动加载更多功能
                        mTransactList.setLoadMoreEnabled(false);
                    } else {
                        mNoPayTip.setVisibility(View.VISIBLE);
                        mIvNoPay.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    @OnClick({R.id.back, R.id.refundExplain})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                if (ClickUtils.isFastClick()) {
                    return;
                }
                finish();
                break;
            case R.id.refundExplain:
                if (ClickUtils.isFastClick()) {
                    return;
                }
                startActivity(new Intent(this, RefundExplainActivity.class));
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        StyledDialog.dismiss();
    }
}
