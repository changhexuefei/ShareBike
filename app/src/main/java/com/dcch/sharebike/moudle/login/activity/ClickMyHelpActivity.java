package com.dcch.sharebike.moudle.login.activity;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dcch.sharebike.MainActivity;
import com.dcch.sharebike.R;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.base.MessageEvent;
import com.dcch.sharebike.utils.MapUtil;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class ClickMyHelpActivity extends BaseActivity {
    @BindView(R.id.close_view_pager)
    ImageView mCloseViewPager;
    @BindView(R.id.explainPage)
    ViewPager pager;
    @BindView(R.id.linear_dot)
    LinearLayout mLinearDot;
    @BindView(R.id.go_to_login)
    Button mGoToLogin;
    private ArrayList<View> pageViews;
    private boolean misScrolled;
    private ArrayList<ImageView> mDots;


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

        //通过循环动态的添加点。
        mDots = new ArrayList<ImageView>();
        for (int i = 0; i < pageViews.size(); i++) {
            ImageView imageView = new ImageView(this);
            int width = MapUtil.Dp2Px(this, 6);
            int heigth = MapUtil.Dp2Px(this, 6);
            int margin = MapUtil.Dp2Px(this, 5);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, heigth);
            params.setMargins(margin, margin, margin, margin);//设置margin,也就是外边距。
            imageView.setLayoutParams(params);//传入参数params设置宽和高
            imageView.setImageResource(R.drawable.dot_normal);//设置图片
            mLinearDot.addView(imageView);//将图片添加到布局中
            //将dot添加到dots集合中
            mDots.add(imageView);
        }
        mDots.get(0).setImageResource(R.drawable.dot_focus);//设置启动后显示的第一个点
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
                container.removeView(pageViews.get(position));
            }

            //每次滑动的时候生成的组件
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(pageViews.get(position));
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
                //for-each循环将所有的dot设置为dot_normal
                for (ImageView imageView : mDots) {
                    imageView.setImageResource(R.drawable.dot_normal);
                }
                //设置当前显示的页面的dot设置为dot_focused
                mDots.get(position).setImageResource(R.drawable.dot_focus);

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
                            EventBus.getDefault().post(new MessageEvent(), "allShow");
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
        EventBus.getDefault().post(new MessageEvent(), "allShow");
    }


    @OnClick(R.id.go_to_login)
    public void onViewClicked() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
        EventBus.getDefault().post(new MessageEvent(), "allShow");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        EventBus.getDefault().post(new MessageEvent(), "allShow");
    }
}
