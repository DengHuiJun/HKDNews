package com.zero.hkdnews.groupmsg;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zero.hkdnews.R;
import com.zero.hkdnews.activity.BaseActivity;
import com.zero.hkdnews.beans.Group;
import com.zero.hkdnews.beans.HnustUser;
import com.zero.hkdnews.myview.TitleBar;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;

/**
 * 查看群组的成员
 * Created by 邓慧 on 15/6/24.
 */
public class CheckMemberActivity extends BaseActivity {

    private ListView mListView;
    private List<HnustUser> mUsers = new ArrayList<>(8);
    private String mId;
    private String mName;
    private Context mContext;
    private MemberAdapter mAdapter;

    private final static int CODE_OK = 0x01;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == CODE_OK) {
                mUsers = (List<HnustUser>) msg.obj;
                mAdapter.setList(mUsers);
                mAdapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_member);

        mContext = this;

        if (getIntent() != null) {
            mId = getIntent().getStringExtra("id");
            mName = getIntent().getStringExtra("name");
        }

        initTitleBar();

        mListView = (ListView) findViewById(R.id.member_list_view);
        mAdapter = new MemberAdapter(mUsers);
        mListView.setAdapter(mAdapter);

        queryMember();

    }

    private void initTitleBar() {
        TitleBar titleBar = (TitleBar) findViewById(R.id.check_member_tb);
        titleBar.isShowRight(false);
        titleBar.setTitleText(mName);
        titleBar.setBackClickListener(this);
    }


    private void queryMember() {
        BmobQuery<HnustUser> query = new BmobQuery<HnustUser>();
        Group group = new Group();
        group.setObjectId(mId);
        query.addWhereRelatedTo("users", new BmobPointer(group));
        query.findObjects(this, new FindListener<HnustUser>() {

            @Override
            public void onSuccess(List<HnustUser> object) {
                Message msg = new Message();
                msg.what = CODE_OK;
                msg.obj = object;
                mHandler.sendMessage(msg);
            }

            @Override
            public void onError(int code, String msg) {
                // TODO Auto-generated method stub
            }
        });


    }

    class MemberAdapter extends BaseAdapter {
        private List<HnustUser> mList;

        public MemberAdapter(List<HnustUser> list) {
            mList = list;
        }

        public void setList(List<HnustUser> list) {
            mList = list;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int i) {
            return mList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder = null;

            if (view == null) {
                view = LayoutInflater.from(mContext).inflate(R.layout.member_list_item, null);
                viewHolder = new ViewHolder();
                viewHolder.logoIv = (ImageView) view.findViewById(R.id.member_item_img);
                viewHolder.name = (TextView) view.findViewById(R.id.member_item_name);
                viewHolder.intro = (TextView) view.findViewById(R.id.member_item_intro);

                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            HnustUser user = mList.get(i);
            //如果不存在，则加载本地头像
            if (user.getHead() !=null ) {
                Picasso.with(mContext).load(user.getHead().getFileUrl(mContext)).into(viewHolder.logoIv);
            } else {
                Picasso.with(mContext).load(R.mipmap.default_me).into(viewHolder.logoIv);
            }

            viewHolder.name.setText(user.getNickname());
            viewHolder.intro.setText(user.getIntro());

            return view;
        }

        class ViewHolder {
            ImageView logoIv;
            TextView name;
            TextView intro;
        }
    }
}
