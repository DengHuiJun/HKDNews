package com.zero.hkdnews.fragment;

import android.content.Intent;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.quentindommerc.superlistview.OnMoreListener;
import com.quentindommerc.superlistview.SuperListview;
import com.quentindommerc.superlistview.SwipeDismissListViewTouchListener;
import com.zero.hkdnews.R;
import com.zero.hkdnews.activity.ShareUploadActivity;
import com.zero.hkdnews.adapter.ShareAdapter;
import com.zero.hkdnews.beans.UploadNews;

import java.util.ArrayList;
import java.util.List;
import android.os.Handler;
import android.widget.ListView;
import android.widget.Toast;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * 关于我的分享
 * Created by luowei on 15/4/11.
 */
public class ShareFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, OnMoreListener {

    private SuperListview listview;
    private Button uploadBtn;
    private List<UploadNews> datalist;
    private ShareAdapter adapter;

    private Thread addThread;

    private static final int UP_DATA = 0x11;

    private Handler mHandler =  new Handler(){
        public void handleMessage(Message msg){
            if(msg.what == UP_DATA){
                datalist = (List<UploadNews>) msg.obj;
                adapter.setDatalist(datalist);
                adapter.notifyDataSetChanged();

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

        listview = (SuperListview) getActivity().findViewById(R.id.share_list_view);

        uploadBtn = (Button) getActivity().findViewById(R.id.share_upload_btn);

        datalist = new ArrayList<>();

        adapter = new ShareAdapter(datalist,getActivity());

        adapter.setDatalist(datalist);

        listview.setAdapter(adapter);


        addThread = new Thread(new Runnable() {
            @Override
            public void run() {
                BmobQuery<UploadNews> query = new BmobQuery<>();
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

        listview.setRefreshListener(this);

        listview.setOnMoreListener(this);

        listview.setupSwipeToDismiss(new SwipeDismissListViewTouchListener.DismissCallbacks() {
            @Override
            public boolean canDismiss(int position) {
                return true;
            }

            @Override
            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
            }
        }, true);


        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ShareUploadActivity.class);
                getActivity().startActivity(intent);
            }
        });


    }

    @Override
    public void onMoreAsked(int i, int i1, int i2) {
        Toast.makeText(getActivity(), "no more", Toast.LENGTH_LONG).show();
       // adapter.notifyAll();

    }

    @Override
    public void onRefresh() {
        //Toast.makeText(getActivity(), "已经刷新！", Toast.LENGTH_LONG).show();
       // adapter.notifyAll();
        addThread.start();
    }
}
