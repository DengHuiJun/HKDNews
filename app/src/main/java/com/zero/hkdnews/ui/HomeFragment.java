package com.zero.hkdnews.ui;

import android.app.Fragment;
import android.content.Intent;
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
import com.zero.hkdnews.adapter.HomeAdapter;
import com.zero.hkdnews.beans.News;
import com.zero.hkdnews.common.UIHelper;

import java.util.ArrayList;
import java.util.List;

import android.os.Handler;

/**
 * Created by luowei on 15/4/11.
 */
public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, OnMoreListener ,AdapterView.OnItemClickListener {

    private SuperListview mList;
 //   private ArrayAdapter<String> mAdapter;

    private List<News> dataList;
    private HomeAdapter homeAdapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
     //   ArrayList<String> lst = new ArrayList<>();
        dataList = new ArrayList<>();
        homeAdapter =  new HomeAdapter(dataList,getActivity());

    //    mAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, lst);

        //绑定fragment_home里面的SuperListView
        mList = (SuperListview) getActivity().findViewById(R.id.list);

        Thread thread = new Thread( new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                   //     mAdapter.add("初始数据1");
                   //     mAdapter.add("初始数据2");
                   //     mAdapter.add("初始数据3");
                        News data = new News();
                        data.setNewsID(12);
                        data.setNewsTime("04-21 12:33");
                        data.setNewsSource("计算机学院");
                        data.setNewsTitle("湖南科技大学个性化新闻客户端正在火速研发当中，由zero主导。");
                    //    data.setImageUrl("wait");
                        for (int i=0 ;i < 6 ;i++)
                            dataList.add(data);
                        homeAdapter.setDataList(dataList);
                        mList.setAdapter(homeAdapter);

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


        mList.setOnItemClickListener(this);

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
       // mAdapter.add("More asked, more served");

    }

    @Override
    public void onRefresh() {
        Toast.makeText(getActivity(), "已经刷新！", Toast.LENGTH_LONG).show();

        // enjoy the beaty of the progressbar
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {

                // demo purpose, adding to the top so you can see it
             //   mAdapter.insert("插入新数据", 0);
                News data = new News();
                data.setNewsID(13);
                data.setNewsTime("04-22 14:33");
                data.setNewsSource("加上");
                data.setNewsTitle("湖南科技大学个性化新闻客户端正在火速研发当中，刷新测试。");
            //    data.setImageUrl("wait");
           //     dataList.add(data);
                dataList.add(0,data);
                homeAdapter.setDataList(dataList);
                homeAdapter.notifyDataSetChanged();


            }
        }, 1500);


    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getActivity(),"OK:"+position,Toast.LENGTH_LONG).show();

        UIHelper.showNewsDetail(getActivity(),dataList.get(position).getObjectId());

    }
}
