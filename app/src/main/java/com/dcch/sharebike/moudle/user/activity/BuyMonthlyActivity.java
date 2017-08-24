package com.dcch.sharebike.moudle.user.activity;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dcch.sharebike.R;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.utils.ClickUtils;
import com.dcch.sharebike.utils.LogUtils;
import com.dcch.sharebike.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class BuyMonthlyActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.buy_month_card)
    Button mBuyMonthCard;
    @BindView(R.id.select_one)
    TextView mSelectOne;
    @BindView(R.id.useful_life_one)
    TextView mUsefulLifeOne;
    @BindView(R.id.original_price_one)
    TextView mOriginalPriceOne;
    @BindView(R.id.oneMonth)
    RelativeLayout mOneMonth;
    @BindView(R.id.select_two)
    TextView mSelectTwo;
    @BindView(R.id.useful_life_two)
    TextView mUsefulLifeTwo;
    @BindView(R.id.original_price_two)
    TextView mOriginalPriceTwo;
    @BindView(R.id.thereMonth)
    RelativeLayout mThereMonth;
    @BindView(R.id.month_weixin)
    TextView mMonthWeixin;
    @BindView(R.id.month_weixin_checkbox)
    CheckBox mMonthWeixinCheckbox;
    @BindView(R.id.month_weixinArea)
    RelativeLayout mMonthWeixinArea;
    @BindView(R.id.month_ali_checkbox)
    CheckBox mMonthAliCheckbox;
    @BindView(R.id.month_aliArea)
    RelativeLayout mMonthAliArea;
    @BindView(R.id.month_scrollView)
    ScrollView mMonthScrollView;
    private String payNumber;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_buy_monthly;
    }

    @Override
    protected void initData() {
        mToolbar.setTitle("");
        mTitle.setText(getString(R.string.month_card));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initListener() {
        super.initListener();
        mOneMonth.setOnClickListener(this);
        mThereMonth.setOnClickListener(this);
    }

    @OnClick({R.id.buy_month_card, R.id.oneMonth, R.id.thereMonth, R.id.month_weixinArea, R.id.month_aliArea})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buy_month_card:
                if (ClickUtils.isFastClick()) {
                    return;
                }
                ToastUtils.showShort(BuyMonthlyActivity.this, "你点了购买");

                break;
            case R.id.oneMonth:
                if (ClickUtils.isFastClick()) {
                    return;
                }
                mOneMonth.setSelected(true);
                mThereMonth.setSelected(false);
                ToastUtils.showShort(BuyMonthlyActivity.this, "你点我了");
                String oneNumber = mSelectOne.getText().toString().trim();
                payNumber = oneNumber.substring(0, oneNumber.length() - 1);
                LogUtils.d("看看", payNumber + "123");
                break;
            case R.id.thereMonth:
                if (ClickUtils.isFastClick()) {
                    return;
                }
                mThereMonth.setSelected(true);
                mOneMonth.setSelected(false);
                ToastUtils.showShort(BuyMonthlyActivity.this, "你点我了");
                String twoNumber = mSelectTwo.getText().toString().trim();
                payNumber = twoNumber.substring(0, twoNumber.length() - 1);
                LogUtils.d("看看", payNumber + "456");
                break;

            case R.id.month_weixinArea:
                mMonthWeixinCheckbox.setChecked(true);
                mMonthAliCheckbox.setChecked(false);
                break;

            case R.id.month_aliArea:
                mMonthAliCheckbox.setChecked(true);
                mMonthWeixinCheckbox.setChecked(false);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMonthWeixinCheckbox.setChecked(true);
        mOneMonth.setSelected(true);
        payNumber = mSelectOne.getText().toString().trim().substring(0, mSelectOne.getText().toString().trim().length() - 1);
        LogUtils.d("看看", payNumber);
        mOriginalPriceOne.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); //中划线
        mOriginalPriceTwo.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); //中划线
        mMonthScrollView.smoothScrollTo(0, 0);

    }
}
