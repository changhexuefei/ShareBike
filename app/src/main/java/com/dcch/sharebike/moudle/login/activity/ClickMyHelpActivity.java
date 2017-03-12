package com.dcch.sharebike.moudle.login.activity;

import android.widget.ImageView;

import com.dcch.sharebike.R;
import com.dcch.sharebike.base.BaseActivity;
import com.recker.flybanner.FlyBanner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ClickMyHelpActivity extends BaseActivity {
    @BindView(R.id.close_view_pager)
    ImageView mCloseViewPager;
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
        mViewList.add(R.drawable.hint_card_1);
        mViewList.add(R.drawable.hint_card_2);
        mViewList.add(R.drawable.hint_card_3);
        mViewList.add(R.drawable.hint_card_4);
        mViewList.add(R.drawable.hint_card_5);
        mExplainPage.setImages(mViewList);
    }

    @OnClick(R.id.close_view_pager)
    public void onClick() {
        finish();

    }
}
