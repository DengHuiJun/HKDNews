package com.zero.hkdnews.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zero.hkdnews.R;

/**
 * 关于我的分享
 * Created by luowei on 15/4/11.
 */
public class ShareFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View shareLayout = inflater.inflate(R.layout.fragment_share,container,false);
        return shareLayout;
    }
}
