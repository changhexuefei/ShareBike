package com.dcch.sharebike.moudle.user.activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dcch.sharebike.R;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class UsualAddressActivity extends BaseActivity {

    @BindView(R.id.starTargetOne)
    ImageView mStarTargetOne;
    @BindView(R.id.placeNameOne)
    TextView mPlaceNameOne;
    @BindView(R.id.addressNameOne)
    TextView mAddressNameOne;
    @BindView(R.id.addressOne)
    RelativeLayout mAddressOne;
    @BindView(R.id.starTargetTwo)
    ImageView mStarTargetTwo;
    @BindView(R.id.placeNameTwo)
    TextView mPlaceNameTwo;
    @BindView(R.id.addressNameTwo)
    TextView mAddressNameTwo;
    @BindView(R.id.addressTwo)
    RelativeLayout mAddressTwo;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_usual_address;
    }

    @Override
    protected void initData() {
        //用户改变后的结果，应该存储到服务器端。
        mToolbar.setTitle("");
        mTitle.setText(getResources().getString(R.string.usual_address));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @OnClick({ R.id.addressOne, R.id.addressTwo})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addressOne:
                ToastUtils.showLong(this, "地址1");
                Intent intent = new Intent(UsualAddressActivity.this, SetAddressActivity.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.addressTwo:
                ToastUtils.showLong(this, "地址2");
                Intent intent1 = new Intent(UsualAddressActivity.this, SetAddressActivity.class);
                startActivityForResult(intent1, 1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            String item01 = data.getStringExtra("item01");
            String item02 = data.getStringExtra("item02");
            switch (requestCode) {
                case 0:
                    mStarTargetOne.setImageResource(R.mipmap.ease_icon_star_select);
                    mPlaceNameOne.setText(item01);
                    mAddressNameOne.setText(item02);
                    break;

                case 1:
                    mStarTargetTwo.setImageResource(R.mipmap.ease_icon_star_select);
                    mPlaceNameTwo.setText(item01);
                    mAddressNameTwo.setText(item02);
                    break;
            }
        }
    }

}
