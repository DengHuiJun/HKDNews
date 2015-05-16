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

/**
 * Created by luowei on 15/5/16.
 */
public class HomePagerFragment extends Fragment {
    private IndicatorViewPager indicatorViewPager;
    private LayoutInflater inflater;

    private ViewPager viewPager;
    private Indicator indicator;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.home_pager_fragment,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewPager = (ViewPager) getActivity().findViewById(R.id.home_pager_view_pager);
        indicator = (Indicator) getActivity().findViewById(R.id.home_pager_indicator);

        int selectColorId = Color.parseColor("#f8f8f8");
        int unSelectColorId = Color.parseColor("#010101");

        indicator.setOnTransitionListener(new OnTransitionTextListener().setColor(selectColorId, unSelectColorId));
        indicator.setScrollBar(new SpringBar(getActivity(), Color.GRAY));
        viewPager.setOffscreenPageLimit(3);

        indicatorViewPager = new IndicatorViewPager(indicator,viewPager);

        inflater = LayoutInflater.from(getActivity());

        MyAdapter adapter = new MyAdapter(getFragmentManager());

        indicatorViewPager.setAdapter(adapter);

        indicatorViewPager.setCurrentItem(1,false);

    }
    
    private  class MyAdapter extends IndicatorViewPager.IndicatorFragmentPagerAdapter{


            private String[] tabNames = {"最新","推荐","特色"};
            private int[] status = {11,22,33};

            public  MyAdapter(FragmentManager fragmentManager){
                super(fragmentManager);
               // inflater = LayoutInflater.from(getActivity());
            }

            @Override
            public int getCount() {
                return 3;
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
            public android.support.v4.app.Fragment getFragmentForPage(int i) {
                HomeFragment homeFragment = new HomeFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("code",status[i]);
                homeFragment.setArguments(bundle);
                return homeFragment;
            }
        }

}
