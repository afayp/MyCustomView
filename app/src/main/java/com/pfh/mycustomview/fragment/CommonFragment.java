package com.pfh.mycustomview.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2016/8/21.
 */
public class CommonFragment extends android.app.Fragment {

    public static final String RESID = "resId";

    private int resId;


    public static CommonFragment newInstance(int resId) {

        Bundle args = new Bundle();
        args.putInt(RESID,resId);

        CommonFragment fragment = new CommonFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null){
            resId = bundle.getInt(RESID);
        }
        View view = inflater.inflate(resId, container, false);
        return view;
    }
}
