package com.dcch.sharebike.moudle.login.activity;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dcch.sharebike.MainActivity;
import com.dcch.sharebike.R;
import com.dcch.sharebike.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ClickMyHelpActivity extends BaseActivity {
    @BindView(R.id.close_view_pager)
    ImageView mCloseViewPager;
    @BindView(R.id.explainPage)
    ViewPager pager;
    //    private List<Integer> mViewList;
    private List<View> pageViews;
    private boolean misScrolled;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_click_my_help;
    }

    @Override
    protected void initData() {
        pageViews = new ArrayList<>();
        View viewOne = LayoutInflater.from(this).inflate(R.layout.item_tip_one, null);
        View viewTwo = LayoutInflater.from(this).inflate(R.layout.item_tip_two, null);
        View viewThere = LayoutInflater.from(this).inflate(R.layout.item_tip_there, null);
        View viewFour = LayoutInflater.from(this).inflate(R.layout.item_tip_four, null);

        pageViews.add(viewOne);
        pageViews.add(viewTwo);
        pageViews.add(viewThere);
        pageViews.add(viewFour);

        pager.setAdapter(new PagerAdapter() {

            //viewpager中的组件数量
            @Override
            public int getCount() {
                return pageViews.size();
            }

            //滑动切换的时候销毁当前的组件
            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                ((ViewPager) container).removeView(pageViews.get(position));
            }

            //每次滑动的时候生成的组件
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ((ViewPager) container).addView(pageViews.get(position));
                return pageViews.get(position);
            }

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public int getItemPosition(Object object) {
                return super.getItemPosition(object);
            }

        });
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        misScrolled = false;
                        break;
                    case ViewPager.SCROLL_STATE_SETTLING:
                        misScrolled = true;
                        break;
                    case ViewPager.SCROLL_STATE_IDLE:
                        if (pager.getCurrentItem() == pager.getAdapter().getCount() - 1 && !misScrolled) {
                            startActivity(new Intent(ClickMyHelpActivity.this, MainActivity.class));
                            ClickMyHelpActivity.this.finish();
                        }
                        misScrolled = true;
                        break;
                }
            }
        });
    }

    @OnClick(R.id.close_view_pager)
    public void onClick() {
        finish();

    }

}
