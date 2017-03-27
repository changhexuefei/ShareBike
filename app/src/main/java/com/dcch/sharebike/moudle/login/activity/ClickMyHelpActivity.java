package com.dcch.sharebike.moudle.login.activity;

import com.dcch.sharebike.R;
import com.dcch.sharebike.base.BaseActivity;
import com.recker.flybanner.FlyBanner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ClickMyHelpActivity extends BaseActivity {
//    @BindView(R.id.close_view_pager)
//    ImageView mCloseViewPager;
    @BindView(R.id.explainPage)
    FlyBanner mExplainPage;
    private List<Integer> mViewList;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_click_my_help;
    }

    @Override
    protected void initData() {

        mViewList = new ArrayList<>();
        mViewList.add(R.layout.item_tip_one);
        mViewList.add(R.layout.item_tip_two);
        mViewList.add(R.layout.item_tip_there);
        mViewList.add(R.layout.item_tip_four);
        mExplainPage.setImages(mViewList);
    }

//    @OnClick(R.id.close_view_pager)
//    public void onClick() {
//        finish();
//
//    }
}
