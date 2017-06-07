package com.dcch.sharebike.moudle.user.activity;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dcch.sharebike.R;
import com.dcch.sharebike.base.BaseActivity;
import com.louisgeek.multiedittextviewlib.MultiEditInputView;

import butterknife.BindView;
import butterknife.OnClick;

public class UnlockBillPageActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.unlockDesc)
    MultiEditInputView mUnlockDesc;
    @BindView(R.id.select_photo_two)
    ImageView mSelectPhotoTwo;
    @BindView(R.id.select_photo_area)
    RelativeLayout mSelectPhotoArea;
    @BindView(R.id.tv20)
    TextView mTv20;
    @BindView(R.id.input_moneySum)
    EditText mInputMoneySum;
    @BindView(R.id.rg_rec_rg_1)
    RadioGroup mRgRecRg1;
    @BindView(R.id.rg_rec_rg_2)
    RadioGroup mRgRecRg2;
    @BindView(R.id.select_bikefare)
    RelativeLayout mSelectBikefare;
    @BindView(R.id.rb_rg_1)
    RadioButton mRbRg1;
    @BindView(R.id.rb_rg_2)
    RadioButton mRbRg2;
    @BindView(R.id.rb_rg_3)
    RadioButton mRbRg3;
    @BindView(R.id.rb_rg1_4)
    RadioButton mRbRg14;
    @BindView(R.id.rb_rg1_5)
    RadioButton mRbRg15;
    @BindView(R.id.rb_rg1_6)
    RadioButton mRbRg16;
    @BindView(R.id.mbikefareconfirm)
    TextView mMbikefareconfirm;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_unlock_bill_page;
    }

    @Override
    protected void initData() {
        mToolbar.setTitle("");
        mTitle.setText(getResources().getString(R.string.unlock_fare));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }


    @OnClick(R.id.mbikefareconfirm)
    public void onViewClicked() {


    }


}
