package com.zero.hkdnews.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zero.hkdnews.R;


/**
 * 首页信息Fragment
 * Created by zero on 15/4/11.
 */
public class MainFragment extends Fragment{


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View meLayout = inflater.inflate(R.layout.fragment_main,container,false);

        return meLayout;
    }

}
