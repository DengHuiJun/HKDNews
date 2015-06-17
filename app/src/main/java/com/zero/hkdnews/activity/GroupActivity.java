package com.zero.hkdnews.activity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.quentindommerc.superlistview.SuperListview;
import com.zero.hkdnews.R;
import com.zero.hkdnews.adapter.GroupAdapter;
import com.zero.hkdnews.beans.Group;
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


    private Button btn_inform;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        initPopup();

        listview = (SuperListview) findViewById(R.id.group_list_view);

        initData();

        adapter = new GroupAdapter(datas,this);

        adapter.setList(datas);

        listview.setAdapter(adapter);

        listview.setOnItemClickListener(this);


    }

    private void initPopup() {
        View popupView = getLayoutInflater().inflate(R.layout.group_popup_view, null);
        popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));

        btn_inform = (Button) popupView.findViewById(R.id.btn_group_view_inform);
        btn_inform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                T.showShort(getApplicationContext(),"Inform");
            }
        });



    }

    private void initData() {
        datas = new ArrayList<>();
        Group group1 = new Group("IT2班");
        Group group2 = new Group("组织部");
        Group group3 = new Group("ELAA社团");
        Group group4 = new Group("计算机学院");

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
}
