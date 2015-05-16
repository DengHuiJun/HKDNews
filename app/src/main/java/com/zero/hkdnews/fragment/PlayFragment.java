package com.zero.hkdnews.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zero.hkdnews.R;

/**
 * Created by luowei on 15/4/11.
 */
public class PlayFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View playLayout = inflater.inflate(R.layout.fragment_play,container,false);
        return playLayout;
    }
}
