package com.zero.hkdnews.fragment;

import android.content.Intent;
import android.os.AsyncTask;
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
import com.zero.hkdnews.beans.HnustUser;
import com.zero.hkdnews.beans.Inform;
import com.zero.hkdnews.util.T;

import java.util.ArrayList;
import java.util.List;
import android.os.Handler;
import android.widget.TextView;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;

/**
 * 通知消息Fragment
 * 改过名字，由Play -> Inform
 * Created by zero on 15/4/11.
 */
public class InformFragment extends Fragment {
    private static final String TAG = "InformFragment";

    private static final int QUERY_SUCCESS = 1;
    private static final int QUERY_FAILED = 2;


    private PlayAdapter mAdapter;
    private ListView mInformLv;
    private List<Inform> mData = new ArrayList<>();

    private FloatingActionButton mFAB;

    private ProgressWheel mLoadPw;
    private TextView mEmptyTv;

    private final Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == QUERY_SUCCESS) {
                mData = (List<Inform>) msg.obj;
                mAdapter.setList(mData);
                mAdapter.notifyDataSetChanged();
                showEmptyText(false);

            } else if (msg.what == QUERY_FAILED) {
                String log = (String) msg.obj;
                T.showShortBar(mFAB, log);
                showEmptyText(true);
            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View playLayout = inflater.inflate(R.layout.fragment_inform,container,false);
        return playLayout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();
        queryData();

        mAdapter = new PlayAdapter(mData, getActivity());
        mInformLv.setAdapter(mAdapter);

        mFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GroupActivity.class);
                getActivity().startActivity(intent);
            }
        });
    }

    private void initView() {
        mLoadPw = (ProgressWheel) getActivity().findViewById(R.id.fragment_play_pb);
        mInformLv = (ListView) getActivity().findViewById(R.id.play_list_view);
        mFAB = (FloatingActionButton) getActivity().findViewById(R.id.play_fab);
        mEmptyTv = (TextView) getActivity().findViewById(R.id.inform_empty_tv);
//        mFAB.attachToListView(mInformLv);
    }

    /**
     * 第一次查询数据
     */
    private void queryData(){
        BmobQuery<Inform> query = new BmobQuery<>();
        HnustUser user = BmobUser.getCurrentUser(getActivity(),HnustUser.class);
        query.addWhereRelatedTo("informs",new BmobPointer(user));
        query.order("created");
        query.findObjects(getActivity(), new FindListener<Inform>() {
            @Override
            public void onSuccess(List<Inform> list) {
                Message msg = Message.obtain();
                msg.obj =list;
                msg.what = QUERY_SUCCESS;
                mHandler.sendMessage(msg);
            }
            @Override
            public void onError(int i, String s) {
                Message msg = Message.obtain();
                msg.obj = s;
                msg.what = QUERY_FAILED;
                mHandler.sendMessage(msg);
            }
        });
    }

    private void showEmptyText(boolean isShow){
        if (isShow) {
            mLoadPw.setVisibility(View.GONE);
            mFAB.setVisibility(View.GONE);
            mEmptyTv.setVisibility(View.VISIBLE);
        } else {
            mLoadPw.setVisibility(View.GONE);
            mFAB.setVisibility(View.VISIBLE);
            mEmptyTv.setVisibility(View.GONE);
        }
    }
}