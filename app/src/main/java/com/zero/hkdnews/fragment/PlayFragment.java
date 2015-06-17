package com.zero.hkdnews.fragment;

import android.content.Intent;
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
import com.zero.hkdnews.beans.Group;
import com.zero.hkdnews.beans.Informs;
import com.zero.hkdnews.util.T;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luowei on 15/4/11.
 */
public class PlayFragment extends Fragment {

    private PlayAdapter adapter;
    private ListView listView;
    private List<Informs> datas;

    private FloatingActionButton fab;



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
        Informs inform1 = new Informs("来自－IT2班","请注意，体育检测推迟到6月14日！");
        Informs inform2 = new Informs("来自－ELAA社团","本周五将在北校体育馆开展英语角！");
        Informs inform3 = new Informs("来自－IT2班","本月6月13日晚6:30在矿院路口好实惠餐厅举行聚餐活动，请各位准时到达！");
        Informs inform4 = new Informs("来自－计算机学院","各同学请注意，最近湖南地区强降雨，出行请带好雨伞");
        Informs inform5 = new Informs("来自－IT2班","请注意，体育检测推迟到6月14日！");

        datas.add(inform1);
        datas.add(inform2);
        datas.add(inform3);
        datas.add(inform4);
        datas.add(inform5);

    }

    private void initView() {
        listView = (ListView) getActivity().findViewById(R.id.play_list_view);
        fab = (FloatingActionButton) getActivity().findViewById(R.id.play_fab);

        fab.attachToListView(listView);
    }
}