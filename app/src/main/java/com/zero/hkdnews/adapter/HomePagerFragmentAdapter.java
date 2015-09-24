package com.zero.hkdnews.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by zero on 15/5/17.
 */
public class HomePagerFragmentAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> mFragments;
    private List<String>   mTabNames;

    public HomePagerFragmentAdapter(FragmentManager fm,List<Fragment> mFragments,List<String>   mTabNames) {
        super(fm);
        this.mFragments = mFragments;
        this.mTabNames = mTabNames;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabNames.get(position);
    }
}
