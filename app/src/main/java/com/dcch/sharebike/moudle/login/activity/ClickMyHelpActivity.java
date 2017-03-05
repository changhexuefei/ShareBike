package com.dcch.sharebike.moudle.login.activity;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dcch.sharebike.R;
import com.dcch.sharebike.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ClickMyHelpActivity extends BaseActivity {



    private List<View> mViewList;

    @BindView(R.id.explainPage)
    ViewPager mExplainPage;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_click_my_help;
    }


    @Override
    protected void initData() {

        mViewList = new ArrayList<>();
        View viewOne = LayoutInflater.from(this).inflate(R.layout.item_tip_one,null);
        View viewTwo = LayoutInflater.from(this).inflate(R.layout.item_tip_two,null);
        View viewThere = LayoutInflater.from(this).inflate(R.layout.item_tip_there,null);
        View viewFour = LayoutInflater.from(this).inflate(R.layout.item_tip_four,null);

        mViewList.add(viewOne);
        mViewList.add(viewTwo);
        mViewList.add(viewThere);
        mViewList.add(viewFour);

        mExplainPage.setAdapter(new PagerAdapter() {
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(mViewList.get(position));
                return mViewList.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(mViewList.get(position));
            }

            @Override
            public int getCount() {
                return mViewList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
        });

        mExplainPage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


}
