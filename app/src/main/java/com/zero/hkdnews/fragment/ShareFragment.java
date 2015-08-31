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
public class ShareFragment extends Fragment {

    private ListView mListView;

    private List<UploadNews> mDataList;
    private ShareAdapter mAdapter;

    //浮动的按钮
    private FloatingActionButton mUploadPhotoFAB;

    private ProgressWheel mLoadPw;

    private static final int GET_DATA_OK = 0x01;
    private static final int GET_DATA_FAIL = 0x02;

    private static final int REQUEST_CODE_OK = 0x03;
    private static final int REFRESH_CODE = 0x04;

    private Handler mHandler =  new Handler(){
        public void handleMessage(Message msg){
            if(msg.what == GET_DATA_OK) {
                mDataList = (List<UploadNews>) msg.obj;
                mAdapter.setDatalist(mDataList);
                mAdapter.notifyDataSetChanged();
                mLoadPw.setVisibility(View.GONE);
            }
            if (msg.what == GET_DATA_FAIL) {
                mLoadPw.setVisibility(View.GONE);
                T.showShort(getActivity(),"更新失败！");
            }

            if (msg.what == REFRESH_CODE) {
                mDataList = (List<UploadNews>) msg.obj;
                mAdapter.setDatalist(mDataList);
                mAdapter.notifyDataSetChanged();
                T.showShort(getActivity(),"刷新至最新！");
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

        findView();

        mDataList = new ArrayList<>();
        mAdapter = new ShareAdapter(mDataList, getActivity());

        mAdapter.setDatalist(mDataList);
        mListView.setAdapter(mAdapter);

//        mUploadPhotoFAB.attachToListView(mListView);
        //查询数据
        queryData(false);

        mUploadPhotoFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ShareUploadActivity.class);
                getActivity().startActivityForResult(intent, REQUEST_CODE_OK);
            }
        });
    }

    private void findView() {
        mLoadPw = (ProgressWheel) getActivity().findViewById(R.id.fragment_share_pb);
        mListView = (ListView) getActivity().findViewById(R.id.share_list_view);
        mUploadPhotoFAB = (FloatingActionButton) getActivity().findViewById(R.id.fab);
    }

    private void queryData(final boolean isReferensh) {
        BmobQuery<UploadNews> query = new BmobQuery<>();

        //判断是否有缓存
//        boolean isCache = query.hasCachedResult(getActivity());
//
//        if(isCache){  //此为举个例子，并不一定按这种方式来设置缓存策略
//            query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);    // 如果有缓存的话，则设置策略为CACHE_ELSE_NETWORK
//        }else{
//            query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);    // 如果没有缓存的话，则设置策略为NETWORK_ELSE_CACHE
//        }

        query.order("-createdAt");
        query.setLimit(10);
        query.findObjects(getActivity(),new FindListener<UploadNews>() {
            @Override
            public void onSuccess(List<UploadNews> list) {
                Message msg = Message.obtain();
                msg.obj = list;
                if (isReferensh) {
                    msg.what = REFRESH_CODE;
                } else {
                    msg.what = GET_DATA_OK;
                }
                mHandler.sendMessage(msg);
            }
            @Override
            public void onError(int i, String s) {
                Message msg =Message.obtain();
                msg.what = GET_DATA_FAIL;
                mHandler.sendMessage(msg);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_OK && resultCode == getActivity().RESULT_OK) {
            mLoadPw.setVisibility(View.VISIBLE);
            queryData(true);
        }
    }
}
