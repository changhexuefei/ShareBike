package com.dcch.sharebike.moudle.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.Toolbar;
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
import com.dcch.sharebike.utils.NetUtils;
import com.dcch.sharebike.utils.SPUtils;
import com.dcch.sharebike.utils.ToastUtils;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.view.CommonFooter;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import okhttp3.Call;

public class MyJourneyActivity extends BaseActivity {

    @BindView(R.id.journey_list)
    LRecyclerView journeyList;
    @BindView(R.id.no_journey)
    TextView noJourney;
    @BindView(R.id.default_show)
    RelativeLayout mDefaultShow;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.iv_no_journey)
    ImageView mIvNoJourney;
    @BindView(R.id.iv_no_network_linking)
    ImageView mIvNoNetworkLinking;
    @BindView(R.id.no_network_linking_tip)
    TextView mNoNetworkLinkingTip;
    private String mPhone;
    private JourneyInfoAdapter mAdapter;
    private String userDetail;
    private JSONObject object;
    private String uID;
    private JourneyInfo mJourneyInfo;
    private String mToken;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_journey;
    }

    @Override
    protected void initData() {
        mToolbar.setTitle("");
        mTitle.setText(getResources().getString(R.string.my_journey));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (SPUtils.isLogin()) {
            userDetail = (String) SPUtils.get(App.getContext(), "userDetail", "");
            object = null;
            try {
                object = new JSONObject(userDetail);
                int id = object.getInt("id");
                uID = String.valueOf(id);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Intent intent = getIntent();
        if (intent != null) {
            mPhone = intent.getStringExtra("phone");
            mToken = intent.getStringExtra("token");
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        LogUtils.d("why", "为什么onCreate");
        super.onCreate(savedInstanceState);
        if (NetUtils.isConnected(App.getContext())) {
            getJourneyInfo(mPhone, mToken);
        } else {
            mIvNoJourney.setVisibility(View.GONE);
            noJourney.setVisibility(View.GONE);
            mIvNoNetworkLinking.setVisibility(View.VISIBLE);
            mNoNetworkLinkingTip.setVisibility(View.VISIBLE);
        }
    }

    private void getJourneyInfo(String phone, final String mToken) {
        Map<String, String> map = new HashMap<>();
        map.put("phone", phone);
        map.put("token", mToken);
        LogUtils.d("空空", phone + "\n" + mToken);
        OkHttpUtils.post().url(Api.BASE_URL + Api.GETCARRENTALORDERBYPHONE).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.showShort(App.getContext(), "服务器忙，请稍后重试！");
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d("我的行程", response);
                if (JsonUtils.isSuccess(response)) {
                    Gson gson = new Gson();
                    mJourneyInfo = gson.fromJson(response, JourneyInfo.class);
                    if (mJourneyInfo.getCarrOrders().size() > 0) {
                        mDefaultShow.setVisibility(View.GONE);
                        journeyList.setVisibility(View.VISIBLE);
                        mAdapter = new JourneyInfoAdapter(MyJourneyActivity.this, R.layout.item_my_journey, mJourneyInfo.getCarrOrders());
                        journeyList.setLayoutManager(new LinearLayoutManager(MyJourneyActivity.this, OrientationHelper.VERTICAL, false));
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
                                String bicycleNo = mJourneyInfo.getCarrOrders().get(position).getBicycleNo();
                                String carRentalOrderId = mJourneyInfo.getCarrOrders().get(position).getCarRentalOrderId();
                                if (bicycleNo != null && !bicycleNo.equals("") && !carRentalOrderId.equals("") && carRentalOrderId != null && uID != null && !uID.equals("")) {
                                    Intent journeyDetail = new Intent(MyJourneyActivity.this, JourneyDetailActivity.class);
                                    journeyDetail.putExtra("bicycleNo", bicycleNo);
                                    journeyDetail.putExtra("carRentalOrderId", carRentalOrderId);
                                    journeyDetail.putExtra("userId", uID);
                                    journeyDetail.putExtra("token", mToken);
                                    startActivity(journeyDetail);
                                }
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
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.d("why", "为什么onResume");
    }
}
