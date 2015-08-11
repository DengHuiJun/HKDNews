package com.zero.hkdnews.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zero.hkdnews.R;
import com.zero.hkdnews.adapter.HomePagerFragmentAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 新闻适配Fragment，内嵌3个Fragment
 * Created by zero on 15/5/16.
 */
public class HomePagerFragment extends Fragment {

    private TabLayout mTabLayout;

    private ViewPager mViewPager;

    private List<Fragment> mFragmentsList;

    private List<String>   mTabNames;

    private HomePagerFragmentAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.home_pager_fragment,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initData();

        mViewPager = (ViewPager) getActivity().findViewById(R.id.home_pager_view_pager);

        mTabLayout = (TabLayout) getActivity().findViewById(R.id.home_pager_tabs);

     //   mViewPager.setOffscreenPageLimit(3);

        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.addTab(mTabLayout.newTab().setText(mTabNames.get(0)));
        mTabLayout.addTab(mTabLayout.newTab().setText(mTabNames.get(1)));
        mTabLayout.addTab(mTabLayout.newTab().setText(mTabNames.get(2)));

        mAdapter = new HomePagerFragmentAdapter(getFragmentManager(),mFragmentsList,mTabNames);

        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabsFromPagerAdapter(mAdapter);
    }

    /**
     * 初始化内嵌的Fragments和Tabs的文本内容
     */
    private void initData() {
        mFragmentsList = new ArrayList<>();
        HomeFragment newst = new HomeFragment();
        mFragmentsList.add(newst);
        RecomFragment recom = new RecomFragment();
        mFragmentsList.add(recom);
        TeseFragment tese = new TeseFragment();
        mFragmentsList.add(tese);

        mTabNames = new ArrayList<>();
        mTabNames.add("最新");
        mTabNames.add("推荐");
        mTabNames.add("招聘");
    }

}
