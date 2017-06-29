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
import com.hss01248.dialog.StyledDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

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
            uID = String.valueOf(SPUtils.get(App.getContext(), "userId", 0));
        }

        Intent intent = getIntent();
        if (intent != null) {
            mPhone = intent.getStringExtra("phone");
            mToken = intent.getStringExtra("token");
        }

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (NetUtils.isConnected(App.getContext())) {
            getJourneyInfo(mPhone, mToken);
            StyledDialog.buildLoading(MyJourneyActivity.this, "正在加载..", true, false).setMsgColor(R.color.color_ff).show();
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
                StyledDialog.dismissLoading();
                ToastUtils.showShort(MyJourneyActivity.this, getString(R.string.server_tip));
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d("我的行程", response);
                if (JsonUtils.isSuccess(response)) {
                    Gson gson = new Gson();
                    mJourneyInfo = gson.fromJson(response, JourneyInfo.class);
                    if (mJourneyInfo.getCarrOrders().size() > 0) {
                        LogUtils.d("记录", mJourneyInfo.getCarrOrders().size() + "");
                        StyledDialog.dismissLoading();
                        journeyList.setVisibility(View.VISIBLE);
                        mAdapter = new JourneyInfoAdapter(MyJourneyActivity.this, R.layout.item_my_journey, mJourneyInfo.getCarrOrders());
                        journeyList.setLayoutManager(new LinearLayoutManager(MyJourneyActivity.this, OrientationHelper.VERTICAL, false));
                        LRecyclerViewAdapter adapter = new LRecyclerViewAdapter(mAdapter);
//                        //添加分割线
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
                                String nickName = mJourneyInfo.getCarrOrders().get(position).getNickName();
                                double calorie = mJourneyInfo.getCarrOrders().get(position).getCalorie();
                                double tripDist = mJourneyInfo.getCarrOrders().get(position).getTripDist();
                                int tripTime = mJourneyInfo.getCarrOrders().get(position).getTripTime();
                                String image = mJourneyInfo.getCarrOrders().get(position).getUserImage();
                                if (mToken != null && bicycleNo != null && carRentalOrderId != null && nickName != null && uID != null && calorie >= 0 && tripDist >= 0) {
                                    Intent journeyDetail = new Intent(MyJourneyActivity.this, JourneyDetailActivity.class);
                                    journeyDetail.putExtra("bicycleNo", bicycleNo);
                                    journeyDetail.putExtra("carRentalOrderId", carRentalOrderId);
                                    journeyDetail.putExtra("userId", uID);
                                    journeyDetail.putExtra("token", mToken);
                                    journeyDetail.putExtra("nickname", nickName);
                                    journeyDetail.putExtra("calorie", calorie);
                                    journeyDetail.putExtra("tripDist", tripDist);
                                    journeyDetail.putExtra("tripTime", tripTime);
                                    journeyDetail.putExtra("image", image);
                                    LogUtils.d("距离和大卡", tripDist + "\n" + calorie);
                                    startActivity(journeyDetail);
                                }
                            }
                        });
                    }
                } else {
                    StyledDialog.dismissLoading();
                    mIvNoJourney.setVisibility(View.VISIBLE);
                    noJourney.setVisibility(View.VISIBLE);
                }
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        StyledDialog.dismiss();
    }
}
