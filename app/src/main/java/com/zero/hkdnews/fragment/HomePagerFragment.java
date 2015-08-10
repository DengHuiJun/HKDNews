package com.zero.hkdnews.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shizhefei.view.indicator.FixedIndicatorView;
import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.slidebar.SpringBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;
import com.zero.hkdnews.R;
import com.zero.hkdnews.adapter.HomePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 新闻适配Fragment，内嵌3个Fragment
 * Created by zero on 15/5/16.
 */
public class HomePagerFragment extends Fragment {

    private IndicatorViewPager indicatorViewPager;
    private LayoutInflater inflater;

    private ViewPager viewPager;
    private Indicator indicator;


    private List<Fragment> list;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.home_pager_fragment,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initData();

        viewPager = (ViewPager) getActivity().findViewById(R.id.home_pager_view_pager);
        indicator = (Indicator) getActivity().findViewById(R.id.home_pager_indicator);

        int selectColorId = Color.parseColor("#f8f8f8");
        int unSelectColorId = Color.parseColor("#010101");

        indicator.setOnTransitionListener(new OnTransitionTextListener().setColor(selectColorId, unSelectColorId));
        indicator.setScrollBar(new SpringBar(getActivity(), Color.GRAY));
        viewPager.setOffscreenPageLimit(3);

        indicatorViewPager = new IndicatorViewPager(indicator,viewPager);

        inflater = LayoutInflater.from(getActivity());

        HomePagerAdapter adapter = new HomePagerAdapter(getFragmentManager(),list,getActivity());

        indicatorViewPager.setAdapter(adapter);

        indicatorViewPager.setCurrentItem(1,false);

    }

    private void initData() {
        list = new ArrayList<>();
        HomeFragment newst = new HomeFragment();
        list.add(newst);

        RecomFragment recom = new RecomFragment();
        list.add(recom);

        TeseFragment tese = new TeseFragment();
        list.add(tese);
    }

}
