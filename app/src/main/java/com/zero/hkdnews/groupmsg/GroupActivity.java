package com.zero.hkdnews.groupmsg;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.BottomSheetDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.zero.hkdnews.R;
import com.zero.hkdnews.activity.BaseActivity;
import com.zero.hkdnews.adapter.GroupAdapter;
import com.zero.hkdnews.beans.Group;
import com.zero.hkdnews.beans.HnustUser;
import com.zero.hkdnews.common.UIHelper;
import com.zero.hkdnews.myview.TitleBar;
import com.zero.hkdnews.util.L;
import com.zero.hkdnews.util.T;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;

/**
 * 群组列表
 * Created by zero on 15/6/15.
 */
public class GroupActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private static final String TAG = "GroupActivity";
    private ListView listview;
    private List<Group> datas;
    private GroupAdapter adapter;

    private TitleBar mTitleBar;
    private TextView mOneTv;
    private TextView mTwoTv;
    private TextView mThreeTv;
    private TextView mFourTv;

    private static final int REQ_CODE_ADD_GROUP = 0x01;

    //存储群组的id,name
    private String groupId = "";
    private String groupName = "";

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == 1) {
                datas = (List<Group>) msg.obj;
                adapter.setList(datas);
                adapter.notifyDataSetChanged();
//                T.showShort(GroupActivity.this, "加载完成！");

            } else if (msg.what == 2) {
                adapter.setList(datas);
                adapter.notifyDataSetChanged();
                T.showShort(GroupActivity.this, "暂未加入任何群组！");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        L.d(TAG,"onCreate()");
        initView();

        initData();

        addGroup();

        adapter = new GroupAdapter(datas, this);

        adapter.setList(datas);

        listview.setAdapter(adapter);

        listview.setOnItemClickListener(this);

    }

    private void initView() {

        mTitleBar = (TitleBar) findViewById(R.id.group_title_bar);

        mTitleBar.setBackClickListener(this);
        mTitleBar.setTitleText("群组列表");
        mTitleBar.setRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupActivity.this, AddGroupActivity.class);
                startActivityForResult(intent, REQ_CODE_ADD_GROUP);
            }
        });

        listview = (ListView) findViewById(R.id.group_list_view);
    }

    private void initData() {
        datas = new ArrayList<>();
    }

    //加载该用户的群组
    private void addGroup(){
        BmobQuery<Group> query = new BmobQuery<>();
        HnustUser user = BmobUser.getCurrentUser(GroupActivity.this, HnustUser.class);
        query.addWhereRelatedTo("groups",new BmobPointer(user));
        query.findObjects(GroupActivity.this, new FindListener<Group>() {
            @Override
            public void onSuccess(List<Group> list) {
                    Message msg =Message.obtain();

                    if (list.size() == 0) {
                        msg.what = 2;
                    } else {
                        msg.obj =list;
                        msg.what = 1 ;
                    }
                    mHandler.sendMessage(msg);
            }

            @Override
            public void onError(int i, String s) {
                    T.showShort(getApplicationContext(),"ERROR"+s);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_CODE_ADD_GROUP && resultCode == RESULT_OK) {
            addGroup();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        groupId = datas.get(position).getObjectId();
        groupName = datas.get(position).getName();

        showBottomSheetView();
    }

    private void showBottomSheetView() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);

        View view = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_list, null);
        mOneTv = (TextView) view.findViewById(R.id.group_bottom_one);
        mTwoTv = (TextView) view.findViewById(R.id.group_bottom_two);
        mThreeTv = (TextView) view.findViewById(R.id.group_bottom_three);
        mFourTv = (TextView) view.findViewById(R.id.group_bottom_four);

        mOneTv.setOnClickListener(oneClickListener);
        mTwoTv.setOnClickListener(twoClickListener);
        mThreeTv.setOnClickListener(threeClickListener);
        mFourTv.setOnClickListener(fourClickListener);

        dialog.setContentView(view);
        dialog.setTitle("功能列表");
        dialog.show();
    }

    // 发布通知
    private View.OnClickListener oneClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Intent toSendInform = new Intent(GroupActivity.this, SendInformActivity.class);
            toSendInform.putExtra("id", groupId);
            toSendInform.putExtra("name", groupName);
            startActivity(toSendInform);
        }
    };

    // 邀请成员
    private View.OnClickListener twoClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent toInvite = new Intent(GroupActivity.this, InviteMemberActivity.class);
            toInvite.putExtra("id", groupId);
            toInvite.putExtra("name", groupName);
            startActivity(toInvite);
        }
    };

    // 查看成员
    private View.OnClickListener threeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent toCheck = new Intent(GroupActivity.this, CheckMemberActivity.class);
            toCheck.putExtra("id", groupId);
            toCheck.putExtra("name", groupName);
            startActivity(toCheck);
        }
    };

    // 历史通知
    private View.OnClickListener fourClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            UIHelper.toAnActivity(GroupActivity.this, HistoryInformActivity.class);
        }
    };
}
