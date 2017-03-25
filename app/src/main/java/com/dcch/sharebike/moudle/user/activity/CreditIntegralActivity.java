package com.dcch.sharebike.moudle.user.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.dcch.sharebike.R;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.view.CreditSesameView;

import butterknife.BindView;
import butterknife.OnClick;

public class CreditIntegralActivity extends BaseActivity {

    @BindView(R.id.back)
    ImageView back;
//    private final int[] mColors = new int[]{
//            0xFFFF80AB,
//            0xFFFF4081,
//            0xFFFF5177,
//            0xFFFF7997
//    };
    @BindView(R.id.new_credit)
    CreditSesameView mNewCredit;
    @BindView(R.id.activity_credit_integral)
    RelativeLayout mCreditArea;
    //    private Random random = new Random();
//    int i = random.nextInt(Integer.valueOf(mScore).intValue());
    private String mScore;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_credit_integral;
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            mScore = intent.getStringExtra("score");
        }
        mCreditArea.setBackgroundColor(getResources().getColor(R.color.colorHeading));
//        getWindow().setStatusBarColor(Color.parseColor("#FF7997"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mNewCredit.setSesameValues(Integer.valueOf(mScore).intValue());
//        startColorChangeAnim();
    }

    @OnClick(R.id.back)
    public void onClick(View view) {
        finish();
    }

//    public void startColorChangeAnim() {
//        ObjectAnimator animator = ObjectAnimator.ofInt(mCreditArea, "backgroundColor", mColors);
//        animator.setDuration(3000);
//        animator.setEvaluator(new ArgbEvaluator());
//        animator.start();
//    }
}
