package com.zero.hkdnews.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.melnykov.fab.FloatingActionButton;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.quentindommerc.superlistview.SuperListview;
import com.zero.hkdnews.R;
import com.zero.hkdnews.activity.ShareUploadActivity;
import com.zero.hkdnews.adapter.ShareAdapter;
import com.zero.hkdnews.beans.UploadNews;
import com.zero.hkdnews.myview.RefreshableView;
import com.zero.hkdnews.util.T;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * 校园分享Fragment
 * Created by zero on 15/4/11.
 */
public class ShareFragment extends Fragment{

    private ListView listview;

    private List<UploadNews> datalist;
    private ShareAdapter adapter;

    //浮动的按钮
    private FloatingActionButton fab;

    //加载分享数据线程
    private Thread addThread;

    private ProgressWheel pw;

    private static final int UP_DATA = 0x11;
    private static final int REFRESH_DATA = 0x22;

    private Handler mHandler =  new Handler(){
        public void handleMessage(Message msg){
            if(msg.what == UP_DATA){
                datalist = (List<UploadNews>) msg.obj;
                adapter.setDatalist(datalist);
                adapter.notifyDataSetChanged();
                pw.setVisibility(View.GONE);

            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View shareLayout = inflater.inflate(R.layout.fragment_share,container,false);
        return shareLayout;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        pw = (ProgressWheel) getActivity().findViewById(R.id.fragment_share_pb);

        listview = (ListView) getActivity().findViewById(R.id.share_list_view);

        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);

        datalist = new ArrayList<>();

        adapter = new ShareAdapter(datalist,getActivity());

        adapter.setDatalist(datalist);

        listview.setAdapter(adapter);

        fab.attachToListView(listview);

        //查询数据的线程
        addThread = new Thread(new Runnable() {
            @Override
            public void run() {
                BmobQuery<UploadNews> query = new BmobQuery<>();

                //判断是否有缓存
                boolean isCache = query.hasCachedResult(getActivity());

                if(isCache){  //此为举个例子，并不一定按这种方式来设置缓存策略
                    query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);    // 如果有缓存的话，则设置策略为CACHE_ELSE_NETWORK
                }else{
                    query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);    // 如果没有缓存的话，则设置策略为NETWORK_ELSE_CACHE
                }

                query.order("-createdAt");
                query.setLimit(10);
                query.findObjects(getActivity(),new FindListener<UploadNews>() {
                    @Override
                    public void onSuccess(List<UploadNews> list) {
                        Message msg = Message.obtain();
                        msg.obj = list;
                        msg.what = UP_DATA;
                        mHandler.sendMessage(msg);
                    }
                    @Override
                    public void onError(int i, String s) {

                    }
                });
            }

        });

        addThread.start();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ShareUploadActivity.class);
                getActivity().startActivity(intent);
            }
        });

    }

}
