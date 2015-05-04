package com.zero.hkdnews.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zero.hkdnews.R;

/**
 * Created by luowei on 15/4/11.
 */
public class MeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View meLayout = inflater.inflate(R.layout.fragment_me,container,false);
        return meLayout;
    }
}
