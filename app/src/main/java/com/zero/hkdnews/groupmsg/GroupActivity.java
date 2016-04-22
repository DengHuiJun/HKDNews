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
 * 通知组实体类
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

    //存储群组的id,name
    private String groupId;
    private String groupName;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == 1){
                datas = (List<Group>) msg.obj;
                adapter.setList(datas);
                adapter.notifyDataSetChanged();
                T.showShort(GroupActivity.this,"跟新完毕！");
            } else if (msg.what == 2) {
                adapter.setList(null);
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

        adapter = new GroupAdapter(datas,this);

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
                UIHelper.toAnActivity(GroupActivity.this, AddGroupActivity.class);
            }
        });

        listview = (ListView) findViewById(R.id.group_list_view);
    }

    private void initData() {
        datas = new ArrayList<>();
        groupId = "";

    }

//    public void showListPopup(View view) {
//        String items[] = {"发布通知", "邀请成员", "查看成员", "历史通知"};
//        final ListPopupWindow listPopupWindow = new ListPopupWindow(this);
//
//        //设置ListView类型的适配器
//        listPopupWindow.setAdapter(new ArrayAdapter<>(GroupActivity.this, android.R.layout.simple_list_item_1, items));
//
//        //给每个item设置监听事件
//        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
////                listPopupWindow.dismiss();

////                });
//                listPopupListener(position);
//            }
//        });
//
//        //设置ListPopupWindow的锚点,也就是弹出框的位置是相对当前参数View的位置来显示，
//        listPopupWindow.setAnchorView(view);
//
//        //ListPopupWindow 距锚点的距离，也就是相对锚点View的位置
//        listPopupWindow.setHorizontalOffset(100);
//        listPopupWindow.setVerticalOffset(100);
//
//        //设置对话框的宽高
//        listPopupWindow.setWidth(350);
//        listPopupWindow.setHeight(500);
//        listPopupWindow.setModal(false);
//
//        listPopupWindow.show();
//    }


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

        mOneTv.setOnClickListener(oneClickListener);
        mTwoTv.setOnClickListener(twoClickListener);
        mThreeTv.setOnClickListener(threeClickListener);

        dialog.setContentView(view);
        dialog.setTitle("功能列表");
        dialog.show();
    }

    // 发布通知
    private View.OnClickListener oneClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };

    // 邀请成员
    private View.OnClickListener twoClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent toInvite = new Intent(GroupActivity.this, InviteMemberActivity.class);
            startActivity(toInvite);
        }
    };

    // 查看成员
    private View.OnClickListener threeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };
}
