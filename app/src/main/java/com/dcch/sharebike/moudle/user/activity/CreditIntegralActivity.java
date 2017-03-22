package com.dcch.sharebike.moudle.user.activity;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.dcch.sharebike.R;
import com.dcch.sharebike.base.BaseActivity;

import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;
import io.netopen.hotbitmapgg.view.NewCreditSesameView;

public class CreditIntegralActivity extends BaseActivity {

    @BindView(R.id.back)
    ImageView back;

    private final int[] mColors = new int[]{
            0xFFFF80AB,
            0xFFFF4081,
            0xFFFF5177,
            0xFFFF7997
    };
    @BindView(R.id.new_credit)
    NewCreditSesameView mNewCredit;
    @BindView(R.id.activity_credit_integral)
    RelativeLayout mActivityCreditIntegral;
    private Random random = new Random();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_credit_integral;
    }

    @Override
    protected void initData() {
        mActivityCreditIntegral.setBackgroundColor(mColors[0]);
        int i = random.nextInt(950);
        mNewCredit.setSesameValues(i);
        startColorChangeAnim();

    }


    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }
    public void startColorChangeAnim() {

        ObjectAnimator animator = ObjectAnimator.ofInt(mActivityCreditIntegral, "backgroundColor", mColors);
        animator.setDuration(3000);
        animator.setEvaluator(new ArgbEvaluator());
        animator.start();
    }

}
