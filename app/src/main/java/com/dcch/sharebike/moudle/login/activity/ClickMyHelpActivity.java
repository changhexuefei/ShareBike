package com.dcch.sharebike.moudle.login.activity;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.dcch.sharebike.R;
import com.dcch.sharebike.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ClickMyHelpActivity extends BaseActivity {
    private int[] mImgIds = new int[]{R.drawable.hint_card_1,
            R.drawable.hint_card_2, R.drawable.hint_card_3,
            R.drawable.hint_card_4, R.drawable.hint_card_5};
    private List<ImageView> mImageViews = new ArrayList<ImageView>();

    @BindView(R.id.explainPage)
    ViewPager mExplainPage;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_click_my_help;
    }


    @Override
    protected void initData() {

        for (int imgId : mImgIds) {
            ImageView imageView = new ImageView(getApplicationContext());
            imageView.setScaleType(ScaleType.CENTER_CROP);
            imageView.setImageResource(imgId);
            mImageViews.add(imageView);
        }
//        mExplainPage.setPageTransformer(true, new DepthPageTransformer());
//        mExplainPage.setPageTransformer(true,new DepthPageTransformer());
        mExplainPage.setAdapter(new PagerAdapter() {


            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(mImageViews.get(position));
                return mImageViews.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                container.removeView(mImageViews.get(position));
            }


            @Override
            public int getCount() {
                return mImgIds.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
        });
    }


}
