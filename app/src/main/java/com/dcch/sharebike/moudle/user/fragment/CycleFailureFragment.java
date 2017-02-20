package com.dcch.sharebike.moudle.user.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dcch.sharebike.R;
import com.dcch.sharebike.app.App;
import com.dcch.sharebike.moudle.user.activity.PickPhotoActivity;
import com.louisgeek.multiedittextviewlib.MultiEditInputView;
import com.xys.libzxing.zxing.activity.CaptureActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class CycleFailureFragment extends Fragment {


    @BindView(R.id.bike_code)
    TextView mBikeCode;
    @BindView(R.id.tips)
    TextView mTips;
    @BindView(R.id.scan_code)
    RelativeLayout mScanCode;
    @BindView(R.id.questionOne)
    CheckBox mQuestionOne;
    @BindView(R.id.questionTwo)
    CheckBox mQuestionTwo;
    @BindView(R.id.questionthere)
    CheckBox mQuestionthere;
    @BindView(R.id.questionFour)
    CheckBox mQuestionFour;
    @BindView(R.id.questionFive)
    CheckBox mQuestionFive;
    @BindView(R.id.questionSix)
    CheckBox mQuestionSix;
    @BindView(R.id.questionSeven)
    CheckBox mQuestionSeven;
    @BindView(R.id.questionEight)
    CheckBox mQuestionEight;
    @BindView(R.id.cycle_photo)
    ImageView mCyclePhoto;
    @BindView(R.id.questionDesc)
    MultiEditInputView mQuestionDesc;
    @BindView(R.id.confirm)
    TextView mConfirm;

    public CycleFailureFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cycle_failure, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick({R.id.scan_code, R.id.questionOne, R.id.questionTwo, R.id.questionthere, R.id.questionFour, R.id.questionFive, R.id.questionSix, R.id.questionSeven, R.id.questionEight, R.id.cycle_photo, R.id.confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.scan_code:
                Intent i4 = new Intent(App.getContext(), CaptureActivity.class);
                startActivityForResult(i4, 0);
                break;
            case R.id.questionOne:
                break;
            case R.id.questionTwo:
                break;
            case R.id.questionthere:
                break;
            case R.id.questionFour:
                break;
            case R.id.questionFive:
                break;
            case R.id.questionSix:
                break;
            case R.id.questionSeven:
                break;
            case R.id.questionEight:
                break;
            case R.id.cycle_photo:
                Intent selectPhoto = new Intent(App.getContext(),PickPhotoActivity.class);
//                startActivity(selectPhoto);
                startActivityForResult(selectPhoto,1);
                break;
            case R.id.confirm:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
