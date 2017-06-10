package com.dcch.sharebike.moudle.user.activity;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dcch.sharebike.R;
import com.dcch.sharebike.app.App;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.base.MessageEvent;
import com.dcch.sharebike.http.Api;
import com.dcch.sharebike.utils.ClickUtils;
import com.dcch.sharebike.utils.JsonUtils;
import com.dcch.sharebike.utils.LogUtils;
import com.dcch.sharebike.utils.NetUtils;
import com.dcch.sharebike.utils.ToastUtils;
import com.louisgeek.multiedittextviewlib.MultiEditInputView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.simple.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

public class UnlockBillPageActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

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
    private String rechargeNumber;
    private String mUserId;
    private String mCarRentalOrderId;

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
        Intent intent = getIntent();
        if (intent != null) {
            mCarRentalOrderId = intent.getStringExtra("carRentalOrderId");
            mUserId = intent.getStringExtra("userId");
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
        mRgRecRg1.setOnCheckedChangeListener(this);
        mRgRecRg2.setOnCheckedChangeListener(this);
    }

    @OnClick(R.id.mbikefareconfirm)
    public void onViewClicked() {
        if (ClickUtils.isFastClick()) {
            return;
        }
        String contentText = mUnlockDesc.getContentText();
        if (NetUtils.isConnected(App.getContext())) {
            if (rechargeNumber != null && !rechargeNumber.equals("") && mCarRentalOrderId != null
                    && !mCarRentalOrderId.equals("") && !mUserId.equals("") && mUserId != null) {
                LogUtils.d("谁是空的", rechargeNumber + "\n" + mCarRentalOrderId + "\n" + mUserId);
                knotfee(mCarRentalOrderId, rechargeNumber, mUserId);
                Intent commit = new Intent(UnlockBillPageActivity.this, DealFeedbackActivity.class);
                commit.putExtra("rechargeNumber", rechargeNumber);
                commit.putExtra("contentText", contentText);
                commit.putExtra("mCarRentalOrderId", mCarRentalOrderId);
                commit.putExtra("mUserId", mUserId);
                startActivity(commit);
                this.finish();
            } else {
                ToastUtils.showShort(UnlockBillPageActivity.this, "请选择实际用车金额");
            }
        } else {
            ToastUtils.showShort(UnlockBillPageActivity.this, getString(R.string.no_network_tip));
        }

    }

    private void knotfee(String carRentalOrderId, String rechargeNumber, String userId) {
        rechargeNumber = rechargeNumber.substring(0, rechargeNumber.length() - 1);
        Map<String, String> map = new HashMap<>();
        map.put("carRentalOrderId", carRentalOrderId);
        map.put("userId", userId);
        map.put("rideCost", rechargeNumber);

        OkHttpUtils.post().url(Api.BASE_URL + Api.FORCECLOSELOCK).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d("强制", response);
                if (JsonUtils.isSuccess(response)) {
                    ToastUtils.showShort(UnlockBillPageActivity.this, "提交成功");
                    EventBus.getDefault().post(new MessageEvent(), "forced close");
                } else {
                    ToastUtils.showShort(UnlockBillPageActivity.this, "提交失败，请重试！");
                }
            }
        });
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.rb_rg_1:
                mRgRecRg2.clearCheck();
                mRgRecRg1.check(R.id.rb_rg_1);
                rechargeNumber = "";
                rechargeNumber = mRbRg1.getText().toString().trim();
//              rechargeNumber = s1.substring(0, s1.length() - 1);
                break;
            case R.id.rb_rg_2:
                mRgRecRg2.clearCheck();
                mRgRecRg1.check(R.id.rb_rg_2);
                rechargeNumber = "";
                rechargeNumber = mRbRg2.getText().toString().trim();
//              rechargeNumber = s2.substring(0, s2.length() - 1);
                break;

            case R.id.rb_rg_3:
                mRgRecRg2.clearCheck();
                mRgRecRg1.check(R.id.rb_rg_3);
                rechargeNumber = "";
                rechargeNumber = mRbRg3.getText().toString().trim();
//              rechargeNumber = s3.substring(0, s3.length() - 1);
                break;
            case R.id.rb_rg1_4:
                mRgRecRg1.clearCheck();
                mRgRecRg2.check(R.id.rb_rg1_4);
                rechargeNumber = "";
                rechargeNumber = mRbRg14.getText().toString().trim();
//              rechargeNumber = s4.substring(0, s4.length() - 1);
                break;

            case R.id.rb_rg1_5:
                mRgRecRg1.clearCheck();
                mRgRecRg2.check(R.id.rb_rg1_5);
                rechargeNumber = "";
                rechargeNumber = mRbRg15.getText().toString().trim();
//              rechargeNumber = s5.substring(0, s5.length() - 1);
                break;
            case R.id.rb_rg1_6:
                mRgRecRg1.clearCheck();
                mRgRecRg2.check(R.id.rb_rg1_6);
                rechargeNumber = "";
                rechargeNumber = mRbRg16.getText().toString().trim();
//              rechargeNumber = s6.substring(0, s6.length() - 1);
                break;
        }
    }
}
