package com.zero.hkdnews.groupmsg;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.zero.hkdnews.R;
import com.zero.hkdnews.beans.Group;
import com.zero.hkdnews.beans.HnustUser;
import com.zero.hkdnews.beans.Inform;
import com.zero.hkdnews.myview.TitleBar;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;

public class CheckReceiveActivity extends AppCompatActivity {

    private ListView mListView;
    private ProgressBar mPb;
    private InformCheckAdapter mAdapter;
    private List<HnustUser> mUsers = new ArrayList<>();
    private List<HnustUser> mGroupUsers = new ArrayList<>();

    private String mGroupId;
    private String mInformId;

    private static final int CODE_QUERY_GROUP = 0x01;
    private static final int CODE_OK = 0x02;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (CODE_QUERY_GROUP == msg.what) {
                queryCheckUsers();
            } else if (msg.what == CODE_OK) {
                mAdapter = new InformCheckAdapter(CheckReceiveActivity.this, mUsers, mGroupUsers);
                mListView.setAdapter(mAdapter);

                mPb.setVisibility(View.GONE);
                mListView.setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_receive);

        if (getIntent() != null) {
            mGroupId = getIntent().getStringExtra("groupId");
            mInformId = getIntent().getStringExtra("informId");
        }

        TitleBar titleBar = (TitleBar) findViewById(R.id.check_receive_tb);
        titleBar.setTitleText("查收一览");
        titleBar.setBackClickListener(this);
        titleBar.isShowRight(false);

        mListView = (ListView) findViewById(R.id.check_receive_lv);
        mPb = (ProgressBar) findViewById(R.id.check_receive_pb);

        queryGroupUsers();
    }

    private void queryGroupUsers() {
        BmobQuery<HnustUser> query = new BmobQuery<>();
        Group group = new Group();
        group.setObjectId(mGroupId);

        query.addWhereRelatedTo("users", new BmobPointer(group));
        query.findObjects(this, new FindListener<HnustUser>() {
            @Override
            public void onSuccess(List<HnustUser> list) {
                mGroupUsers = list;

                Message msg = Message.obtain();
                msg.what = CODE_QUERY_GROUP;
                mHandler.sendMessage(msg);

            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    private void queryCheckUsers() {
        BmobQuery<HnustUser> query = new BmobQuery<>();

        Inform inform = new Inform();
        inform.setObjectId(mInformId);

        query.addWhereRelatedTo("users", new BmobPointer(inform));
        query.findObjects(this, new FindListener<HnustUser>() {
            @Override
            public void onSuccess(List<HnustUser> list) {
                mUsers = list;
                Message msg = Message.obtain();
                msg.what = CODE_OK;
                mHandler.sendMessage(msg);
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }
}
