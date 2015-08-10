package com.zero.hkdnews.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
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
import com.zero.hkdnews.util.T;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * 新闻适配Fragment的子Fragment之一，推荐的新闻，处于中间
 * Created by zero on 15/5/17.
 */
public class RecomFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,AdapterView.OnItemClickListener{
    private SuperListview mList;

    private List<News> dataList;
    private HomeAdapter homeAdapter;

    private static final int ADD_DATA = 1;
    private static final int REFRESH_DATA = 2;

    private Handler mHandler =  new Handler(){
        public void handleMessage(Message msg){
            if(msg.what == ADD_DATA){
                dataList = (List<News>) msg.obj;
                homeAdapter.setDataList(dataList);
                homeAdapter.notifyDataSetChanged();
            }
            if(msg.what == REFRESH_DATA){
                dataList = (List<News>) msg.obj;
                homeAdapter.setDataList(dataList);
                homeAdapter.notifyDataSetChanged();
                T.showShort(getActivity(), "刷新完成！");

            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_newst,container,false);
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dataList = new ArrayList<>();
        homeAdapter =  new HomeAdapter(dataList,getActivity());

        //绑定fragment_home里面的SuperListView
        mList = (SuperListview) getActivity().findViewById(R.id.recom_list);

        //初始化
        homeAdapter.setDataList(dataList);
        mList.setAdapter(homeAdapter);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                BmobQuery<News> query = new BmobQuery<>();

                //判断是否有缓存
                boolean isCache = query.hasCachedResult(getActivity());

                if(isCache){  //此为举个例子，并不一定按这种方式来设置缓存策略
                    query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);    // 如果有缓存的话，则设置策略为CACHE_ELSE_NETWORK
                }else{
                    query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);    // 如果没有缓存的话，则设置策略为NETWORK_ELSE_CACHE
                }

                query.order("-createdAt");
                query.addWhereEqualTo("code",1);
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

        mList.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Toast.makeText(getActivity(),"OK:"+position,Toast.LENGTH_LONG).show();

        Bundle bundle = new Bundle();

        bundle.putSerializable("news",dataList.get(position));

        UIHelper.showNewsDetail(getActivity(), bundle);
    }

    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            public void run() {
                BmobQuery<News> query = new BmobQuery<>();

                query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);    // 设置策略为NETWORK_ELSE_CACHE
                query.order("-createdAt");
                query.setLimit(15);
                query.addWhereEqualTo("code",1);
                query.findObjects(getActivity(), new FindListener<News>() {
                    @Override
                    public void onSuccess(List<News> list) {
                        Message msg = Message.obtain();
                        msg.obj = list;
                        msg.what = REFRESH_DATA;
                        mHandler.sendMessage(msg);
                    }

                    @Override
                    public void onError(int i, String s) {

                    }
                });
            }
        }, 500);

    }
}
