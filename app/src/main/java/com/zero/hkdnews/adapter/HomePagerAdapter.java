package com.zero.hkdnews.adapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shizhefei.view.indicator.IndicatorViewPager;
import com.zero.hkdnews.R;

import java.util.List;

/**
 * Created by luowei on 15/5/17.
 */
public class HomePagerAdapter extends IndicatorViewPager.IndicatorFragmentPagerAdapter {

    private String[] tabNames = {"最新","推荐","趣闻"};
    private List<Fragment> data;
    private LayoutInflater inflater;



    public HomePagerAdapter(FragmentManager fragmentManager,List<Fragment> data,Context context){
        super(fragmentManager);
        this.data =data;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public View getViewForTab(int i, View view, ViewGroup viewGroup) {
        if(view == null){
            view = inflater.inflate(R.layout.home_pager_tab,viewGroup,false);
        }
        TextView textView = (TextView) view.findViewById(R.id.home_pager_tab_text);

        textView.setText(tabNames[i]);

        return view;
    }

    @Override
    public Fragment getFragmentForPage(int i) {
        return data.get(i);
    }
}
