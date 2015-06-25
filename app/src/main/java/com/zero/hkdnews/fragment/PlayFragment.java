package com.zero.hkdnews.fragment;

import android.content.Intent;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.melnykov.fab.FloatingActionButton;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.zero.hkdnews.R;
import com.zero.hkdnews.activity.GroupActivity;
import com.zero.hkdnews.adapter.PlayAdapter;
import com.zero.hkdnews.app.AppContext;
import com.zero.hkdnews.beans.Group;
import com.zero.hkdnews.beans.HnustUser;
import com.zero.hkdnews.beans.Inform;
import com.zero.hkdnews.util.T;

import java.util.ArrayList;
import java.util.List;
import android.os.Handler;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by luowei on 15/4/11.
 */
public class PlayFragment extends Fragment {

    private PlayAdapter adapter;
    private ListView listView;
    private List<Inform> datas;

    private FloatingActionButton fab;

    private ProgressWheel pw;


    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 11){
                datas = (List<Inform>) msg.obj;
                adapter.setList(datas);
                adapter.notifyDataSetChanged();
                pw.setVisibility(View.GONE);
            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View playLayout = inflater.inflate(R.layout.fragment_play,container,false);
        return playLayout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();

        initDatas();

        addData();

        adapter = new PlayAdapter(datas,getActivity());
        listView.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GroupActivity.class);
                getActivity().startActivity(intent);
            }
        });



    }

    private void initDatas() {
        datas = new ArrayList<>();
//        Inform inform1 = new Inform("来自－IT2班","请注意，体育检测推迟到6月14日！");
//        Inform inform2 = new Inform("来自－ELAA社团","本周五将在北校体育馆开展英语角！");
//
//        datas.add(inform1);
//        datas.add(inform2);

    }

    private void initView() {
        pw = (ProgressWheel) getActivity().findViewById(R.id.fragment_play_pb);

        listView = (ListView) getActivity().findViewById(R.id.play_list_view);
        fab = (FloatingActionButton) getActivity().findViewById(R.id.play_fab);

        fab.attachToListView(listView);
    }


    private void addData(){
        BmobQuery<Inform> query = new BmobQuery<>();

        HnustUser user = BmobUser.getCurrentUser(getActivity(),HnustUser.class);

        query.addWhereRelatedTo("informs",new BmobPointer(user));

        query.order("created");

        query.findObjects(getActivity(), new FindListener<Inform>() {
            @Override
            public void onSuccess(List<Inform> list) {
                Message msg = Message.obtain();
                msg.obj =list;
                msg.what =11;
                mHandler.sendMessage(msg);
            }

            @Override
            public void onError(int i, String s) {

            }
        });



    }
}