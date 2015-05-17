package com.zero.hkdnews.fragment;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by luowei on 15/5/17.
 */
public class TeseFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, OnMoreListener,AdapterView.OnItemClickListener{

    private SuperListview mList;

    private List<News> dataList;
    private HomeAdapter homeAdapter;

    public static final int ADD_DATA = 1;

    private Handler mHandler =  new Handler(){
        public void handleMessage(Message msg){
            if(msg.what == ADD_DATA){
                dataList = (List<News>) msg.obj;
                homeAdapter.setDataList(dataList);
                homeAdapter.notifyDataSetChanged();

            }
        }
    };



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tese,container,false);
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        dataList = new ArrayList<>();
        homeAdapter =  new HomeAdapter(dataList,getActivity());

        //绑定fragment_home里面的SuperListView
        mList = (SuperListview) getActivity().findViewById(R.id.tese_list);

        //初始化
        homeAdapter.setDataList(dataList);
        mList.setAdapter(homeAdapter);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                BmobQuery<News> query = new BmobQuery<>();
                query.addWhereEqualTo("code",2);
                query.findObjects(getActivity(),new FindListener<News>() {
                    @Override
                    public void onSuccess(List<News> list) {
                        Message msg = Message.obtain();
                        msg.obj = list;
                        msg.what = ADD_DATA;
                        mHandler.sendMessage(msg);
                    }

                    @Override
                    public void onError(int i, String s) {

                    }
                });
            }

        });

        thread.start();

        // Setting the refresh listener will enable the refresh progressbar
        mList.setRefreshListener(this);

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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Bundle bundle = new Bundle();

        bundle.putSerializable("news",dataList.get(position));

        UIHelper.showNewsDetail(getActivity(), bundle);
    }

    @Override
    public void onMoreAsked(int i, int i1, int i2) {
        Toast.makeText(getActivity(), "no more!", Toast.LENGTH_LONG).show();

        homeAdapter.notifyDataSetChanged();

    }

    @Override
    public void onRefresh() {
        Toast.makeText(getActivity(), "新！", Toast.LENGTH_LONG).show();
        homeAdapter.notifyDataSetChanged();
    }
}
