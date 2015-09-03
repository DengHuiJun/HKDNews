package com.zero.hkdnews.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.zero.hkdnews.R;


/**
 * 首页信息Fragment
 * Created by zero on 15/4/11.
 */
public class MainFragment extends Fragment{

    private IndicatorViewPager mBannerIVP;
    private ViewPager mBannerVP;
    private Indicator mBannerI;
    private LayoutInflater mInflater;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View meLayout = inflater.inflate(R.layout.fragment_main,container,false);
        return meLayout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mInflater = LayoutInflater.from(getActivity());

        findView();
        initData();

    }
    private void findView() {
        mBannerVP = (ViewPager) getActivity().findViewById(R.id.main_banner_viewPager);
        mBannerI = (Indicator) getActivity().findViewById(R.id.main_banner_indicator);

        mBannerIVP = new IndicatorViewPager(mBannerI, mBannerVP);
    }

    private void initData() {
        mBannerIVP.setAdapter(mBannerAdapter);
    }

    private IndicatorViewPager.IndicatorPagerAdapter mBannerAdapter = new IndicatorViewPager.IndicatorViewPagerAdapter() {
        private int[] images = { R.mipmap.guide_one, R.mipmap.guide_two, R.mipmap.guide_three};
        @Override
        public View getViewForTab(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.tab_guide, container, false);
            }
            return convertView;
        }
        @Override
        public View getViewForPage(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = new View(getActivity());
                convertView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            }
            convertView.setBackgroundResource(images[position]);
            return convertView;
        }
        @Override
        public int getCount() {
            return images.length;
        }
    };
}
