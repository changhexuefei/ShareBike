package com.dcch.sharebike.moudle.user.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dcch.sharebike.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class UnableUnlockFragment extends Fragment {


    public UnableUnlockFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_unable_unlock, container, false);
        return view;
    }

}
