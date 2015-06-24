package com.zero.hkdnews.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.quentindommerc.superlistview.SuperListview;
import com.zero.hkdnews.R;
import com.zero.hkdnews.adapter.GroupAdapter;
import com.zero.hkdnews.beans.Group;
import com.zero.hkdnews.common.UIHelper;
import com.zero.hkdnews.util.T;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luowei on 15/6/15.
 */
public class GroupActivity extends BaseActivity implements AdapterView.OnItemClickListener{

    private SuperListview listview;
    private List<Group> datas;
    private GroupAdapter adapter;

    //弹出菜单
    private PopupWindow popupWindow;


    //发布通知按钮
    private Button btn_inform;

    //邀请成员
    private Button btn_add_meb;

    //查看成员
    private Button btn_check_meb;

    //历史通知记录
    private Button btn_check_history;

    private ImageView back;
    private ImageView addGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        initView();

        initPopup();

        initData();

        adapter = new GroupAdapter(datas,this);

        adapter.setList(datas);

        listview.setAdapter(adapter);

        listview.setOnItemClickListener(this);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupActivity.this,AddGroupActivity.class);
                startActivity(intent);
            }
        });


    }

    private void initView() {
        listview = (SuperListview) findViewById(R.id.group_list_view);

        back = (ImageView) findViewById(R.id.group_header_home);

        addGroup = (ImageView) findViewById(R.id.group_header_add);

    }

    /**
     * 初始化弹出框
     */
    private void initPopup() {
        View popupView = getLayoutInflater().inflate(R.layout.group_popup_view, null);
        popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));

        btn_inform = (Button) popupView.findViewById(R.id.btn_group_view_inform);
        btn_add_meb = (Button) popupView.findViewById(R.id.btn_group_view_add);
        btn_check_meb = (Button) popupView.findViewById(R.id.btn_group_view_check);
        btn_check_history = (Button) popupView.findViewById(R.id.btn_group_view_history);

        btn_inform.setOnClickListener(informListener);
        btn_add_meb.setOnClickListener(addMebListener);
        btn_check_meb.setOnClickListener(checkMebListener);
        btn_check_history.setOnClickListener(historyListener);
    }

    private void initData() {
        datas = new ArrayList<>();
        Group group1 = new Group("IT2班","计算机科学与技术2班，技术之家");
        Group group2 = new Group("组织部","计算机学院组织部大家庭");
        Group group3 = new Group("ELAA社团","湖南科技大学第一大社团");
        Group group4 = new Group("计算机学院","技术强院");

        datas.add(group1);
        datas.add(group2);
        datas.add(group3);
        datas.add(group4);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        T.showShort(this,datas.get(position).getName());
        popupWindow.showAsDropDown(view);
    }

    /**
     * 发布通知监听事件
     */
    private View.OnClickListener informListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           // T.showShort(getApplicationContext(),"发布通知");
            Intent intent = new Intent(GroupActivity.this,SendInformActivity.class);
            startActivity(intent);
        }
    };

    /**
     * 添加成员监听事件
     */
    private View.OnClickListener addMebListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           // T.showShort(getApplicationContext(),"添加成员");
            UIHelper.jumbActivity(GroupActivity.this,AddMemberActivity.class);
        }
    };

    /**
     * 查看成员监听事件
     */
    private View.OnClickListener checkMebListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            T.showShort(getApplicationContext(),"查看成员");
        }
    };

    /**
     * 查看历史监听事件
     */
    private View.OnClickListener historyListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            T.showShort(getApplicationContext(),"查看历史");
        }
    };




}
