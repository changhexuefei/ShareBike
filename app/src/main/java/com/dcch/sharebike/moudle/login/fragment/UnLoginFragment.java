package com.dcch.sharebike.moudle.login.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dcch.sharebike.R;
import com.dcch.sharebike.moudle.login.activity.LoginActivity;
import com.dcch.sharebike.utils.ClickUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class UnLoginFragment extends Fragment {

    @BindView(R.id.login)
    TextView login;
    private View view;
//    private static UnLoginFragment uf;
//
//    public static UnLoginFragment getUf() {
//        // 提供一个全局的静态方法
//        if (uf == null) {
//            synchronized (UnLoginFragment.class) {
//                if (uf == null) {
//                    uf = new UnLoginFragment();
//                }
//            }
//        }
//        return uf;
//    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_un_login, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.login)
    public void onClick() {
        if(ClickUtils.isFastClick()){
            return;
        }
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);

    }
}
