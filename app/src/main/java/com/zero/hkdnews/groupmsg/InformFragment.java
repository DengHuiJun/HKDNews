package com.zero.hkdnews.groupmsg;

import android.content.Intent;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.zero.hkdnews.R;
import com.zero.hkdnews.adapter.PlayAdapter;
import com.zero.hkdnews.beans.Group;
import com.zero.hkdnews.beans.HnustUser;
import com.zero.hkdnews.beans.Inform;
import com.zero.hkdnews.util.T;

import java.util.ArrayList;
import java.util.List;
import android.os.Handler;
import android.widget.ProgressBar;
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
public class InformFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "InformFragment";

    private static final int QUERY_GROUP_SUCCESS = 1;
    private static final int QUERY_INFORM_SUCCESS = 3;
    private static final int QUERY_FAILED = 2;


    private PlayAdapter mAdapter;
    private ListView mInformLv;
    private SwipeRefreshLayout mSRL;
    private List<Inform> mData = new ArrayList<>();
    private List<Group> mGroups = new ArrayList<>(6);

    private FloatingActionButton mFAB;

    private ProgressBar mLoadPw;
    private TextView mEmptyTv;

    private final Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == QUERY_GROUP_SUCCESS) {

                mGroups = (List<Group>) msg.obj;

                if (mGroups.size() == 0) {
                    showEmptyText(true);
                    mSRL.setRefreshing(false);
                } else {
                    showEmptyText(false);
                    for (int i = 0; i<mGroups.size();i++) {
                        queryInformsByGroup(mGroups.get(i).getObjectId());
                    }
                }

            } else if (msg.what == QUERY_INFORM_SUCCESS) {
                if (mData.size() == 0) {
                    showEmptyText(true);
                } else {
                    showEmptyText(false);
                    mAdapter.setList(mData);
                    mAdapter.notifyDataSetChanged();
                }
                mSRL.setRefreshing(false);

            } else if (msg.what == QUERY_FAILED) {
                showEmptyText(true);
                mSRL.setRefreshing(false);
            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View playLayout = inflater.inflate(R.layout.fragment_inform,container,false);

        mSRL = (SwipeRefreshLayout) playLayout.findViewById(R.id.fragment_inform_srl);
        mSRL.setOnRefreshListener(this);

        return playLayout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();
        queryAllGroup();

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
        mLoadPw = (ProgressBar) getActivity().findViewById(R.id.fragment_play_pb);
        mInformLv = (ListView) getActivity().findViewById(R.id.play_list_view);
        mFAB = (FloatingActionButton) getActivity().findViewById(R.id.play_fab);
        mEmptyTv = (TextView) getActivity().findViewById(R.id.inform_empty_tv);
//        mFAB.attachToListView(mInformLv);
    }

    /**
     * 查询数据
     */
    private void queryAllGroup(){
        BmobQuery<Group> query = new BmobQuery<>();
        HnustUser user = BmobUser.getCurrentUser(getActivity(),HnustUser.class);
        query.addWhereRelatedTo("groups",new BmobPointer(user));
        query.order("-createdAt");
        query.findObjects(getActivity(), new FindListener<Group>() {
            @Override
            public void onSuccess(List<Group> list) {
                Message msg = Message.obtain();
                msg.obj = list;
                msg.what = QUERY_GROUP_SUCCESS;
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

    private void queryInformsByGroup(String groupId) {
        BmobQuery<Inform> query = new BmobQuery<Inform>();
        Group group = new Group();
        group.setObjectId(groupId);
        query.addWhereEqualTo("group",new BmobPointer(group));
        query.findObjects(getActivity(), new FindListener<Inform>() {

            @Override
            public void onSuccess(List<Inform> object) {
               if (object.size() > 0) {
                   for (int i = 0; i < object.size(); i++) {
                       mData.add(object.get(i));
                   }
               }

                Message msg = Message.obtain();
                msg.what = QUERY_INFORM_SUCCESS;
                mHandler.sendMessage(msg);
            }

            @Override
            public void onError(int code, String msg) {

            }
        });
    }

    private void showEmptyText(boolean isShow){
        if (isShow) {
            mLoadPw.setVisibility(View.GONE);
            mEmptyTv.setVisibility(View.VISIBLE);
        } else {
            mLoadPw.setVisibility(View.GONE);
            mEmptyTv.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRefresh() {
        mData.clear();
        queryAllGroup();
    }
}