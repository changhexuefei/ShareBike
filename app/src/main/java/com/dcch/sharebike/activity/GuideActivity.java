package com.dcch.sharebike.activity;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.dcch.sharebike.MainActivity;
import com.dcch.sharebike.R;
import com.dcch.sharebike.app.App;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.utils.SPUtils;

import java.util.ArrayList;

import butterknife.BindView;

public class GuideActivity extends BaseActivity {

    @BindView(R.id.viewpager_guide)
    ViewPager viewpager_guide;
    @BindView(R.id.btn_start_main)
    Button btn_start_main;
    private boolean istete = false;
    private ArrayList imageViews = new ArrayList();


    @Override
    protected int getLayoutId() {
        return R.layout.activity_guide;
    }

    @Override
    protected void initData() {
        //设置全屏显示
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        int[] ids = {R.drawable.spring, R.drawable.summer, R.drawable.autumn,
                R.drawable.winter};
        imageViews = new ArrayList();
        for (int i = 0; i < ids.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(ids[i]);//一定要设置为背景
            //加入到集合中
            imageViews.add(imageView);
        }
    }


    @Override
    protected void initListener() {
        super.initListener();
        viewpager_guide.setAdapter(new MyPagerAdapter());
        viewpager_guide.addOnPageChangeListener(new MyOnPageChangeListener());
        btn_start_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(App.getContext(), MainActivity.class);
                startActivity(intent);
                finish();
                SPUtils.put(App.getContext(), "isStartGuide", true);
                SPUtils.put(App.getContext(), "isfirst", false);
            }
        });
    }

    class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imageViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        /**
         * @param container ViewPager
         * @param position  当前添加到哪个页面
         * @return
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = (ImageView) imageViews.get(position);
            container.addView(imageView);// 把页面添加到ViewPager中
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }


    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        /**
         * 当页面滚动了的时候回调这个方法
         *
         * @param position             当前滚动页面的位置
         * @param positionOffset       当前页面滑动的百分比
         * @param positionOffsetPixels 当前页面滑动了多少像数
         */
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (positionOffset != 0) {
                istete = true;
            } else {
                istete = false;
            }
        }

        @Override
        public void onPageSelected(int position) {
            if (position == imageViews.size() - 1 && istete) {// 最后一个页面才显示
                // 显示按钮
                btn_start_main.setVisibility(View.VISIBLE);
            } else {
                // 隐藏按钮
                btn_start_main.setVisibility(View.GONE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }


}
