package com.zero.hkdnews.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zero.hkdnews.R;

/**
 * 关于我的分享
 * Created by luowei on 15/4/11.
 */
public class ShareFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View shareLayout = inflater.inflate(R.layout.fragment_share,container,false);
        return shareLayout;
    }
}
