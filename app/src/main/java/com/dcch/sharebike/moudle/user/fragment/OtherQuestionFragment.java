package com.dcch.sharebike.moudle.user.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.dcch.sharebike.R;
import com.dcch.sharebike.moudle.user.activity.CashPledgeExplainActivity;
import com.dcch.sharebike.moudle.user.activity.RechargeAgreementActivity;
import com.dcch.sharebike.moudle.user.activity.UserAgreementActivity;
import com.dcch.sharebike.utils.ClickUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class OtherQuestionFragment extends Fragment {


    @BindView(R.id.foundFaulty)
    RelativeLayout mFoundFaulty;
    @BindView(R.id.unlock)
    RelativeLayout mUnlock;
    @BindView(R.id.FuserAgreement)
    RelativeLayout mFuserAgreement;
    @BindView(R.id.FcashPledgeExplain)
    RelativeLayout mFcashPledgeExplain;
    @BindView(R.id.FrechargeAgreement)
    RelativeLayout mFrechargeAgreement;
    Unbinder unbinder;
    private View mView;

    public OtherQuestionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_other_question, container, false);
        unbinder = ButterKnife.bind(this, mView);
        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.foundFaulty, R.id.unlock, R.id.FuserAgreement, R.id.FcashPledgeExplain, R.id.FrechargeAgreement})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.foundFaulty:
                if(ClickUtils.isFastClick()){
                    return;
                }
//                ToastUtils.showShort(getContext(),"发现故障");
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.customerService, new CycleFailureFragment()).commit();
                break;
            case R.id.unlock:
                if(ClickUtils.isFastClick()){
                    return;
                }
//                ToastUtils.showShort(getContext(),"开不了锁");
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.customerService, new UnableUnlockFragment()).commit();
                break;
            case R.id.FuserAgreement:
                if(ClickUtils.isFastClick()){
                    return;
                }
//                ToastUtils.showShort(getContext(),"用户协议");
                startActivity(new Intent(getActivity(), UserAgreementActivity.class));
                break;
            case R.id.FcashPledgeExplain:
                if(ClickUtils.isFastClick()){
                    return;
                }
//                ToastUtils.showShort(getContext(),"充值协议");
                startActivity(new Intent(getActivity(), CashPledgeExplainActivity.class));
                break;
            case R.id.FrechargeAgreement:
                if(ClickUtils.isFastClick()){
                    return;
                }
//                ToastUtils.showShort(getContext(),"发现故障");
                startActivity(new Intent(getActivity(), RechargeAgreementActivity.class));
                break;
        }
    }
}
