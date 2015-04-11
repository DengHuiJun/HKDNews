package com.zero.hkdnews.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.quentindommerc.superlistview.OnMoreListener;
import com.quentindommerc.superlistview.SuperListview;
import com.quentindommerc.superlistview.SwipeDismissListViewTouchListener;
import com.zero.hkdnews.R;

import java.util.ArrayList;
import android.os.Handler;

/**
 * Created by luowei on 15/4/11.
 */
public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, OnMoreListener ,AdapterView.OnItemClickListener {

    private SuperListview mList;
    private ArrayAdapter<String> mAdapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayList<String> lst = new ArrayList<String>();
        mAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, lst);
        // This is what you're looking for
        mList = (SuperListview) getActivity().findViewById(R.id.list);

        Thread thread = new Thread( new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.add("More stuff");
                        mAdapter.add("More stuff");
                        mAdapter.add("More stuff");

                        mList.setAdapter(mAdapter);

                    }
                });
            }
        });
        thread.start();

        // Setting the refresh listener will enable the refresh progressbar
        mList.setRefreshListener(this);

        // Wow so beautiful
     //   mList.setRefreshingColor(android.R.color.holo_orange_light, android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_red_light);

        // I want to get loadMore triggered if I see the last item (1)
        mList.setupMoreListener(this, 1);

        mList.setupSwipeToDismiss(new SwipeDismissListViewTouchListener.DismissCallbacks() {
            @Override
            public boolean canDismiss(int position) {
                return true;
            }

            @Override
            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
            }
        }, true);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View homeLayout = inflater.inflate(R.layout.fragment_home,container,false);
        return homeLayout;
    }

    @Override
    public void onMoreAsked(int i, int i2, int i3) {
        Toast.makeText(getActivity(), "More", Toast.LENGTH_LONG).show();

        //demo purpose, adding to the bottom
        mAdapter.add("More asked, more served");

    }

    @Override
    public void onRefresh() {
        Toast.makeText(getActivity(), "Refresh", Toast.LENGTH_LONG).show();

        // enjoy the beaty of the progressbar
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {

                // demo purpose, adding to the top so you can see it
                mAdapter.insert("New stuff", 0);

            }
        }, 2000);


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getActivity(),"Click:+"+position,Toast.LENGTH_LONG).show();
    }
}
