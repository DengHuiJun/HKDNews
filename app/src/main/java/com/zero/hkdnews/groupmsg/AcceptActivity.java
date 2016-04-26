package com.zero.hkdnews.groupmsg;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zero.hkdnews.R;
import com.zero.hkdnews.beans.Group;
import com.zero.hkdnews.beans.HnustUser;
import com.zero.hkdnews.myview.TitleBar;
import com.zero.hkdnews.util.T;

import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.UpdateListener;

public class AcceptActivity extends AppCompatActivity {
    private String msg = "null";

    private TextView mContentTv;
    private Button mYesBtn;
    private Button mNoBtn;

    private String[] mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept);

        if (getIntent() != null)
            msg = getIntent().getStringExtra("msg");

        mData = msg.split("&&&");
        // 0 群组id；1 该用户id；2 群组名称；3 邀请的用户名

        mData[0] = mData[0].substring(10);
        mData[3] = mData[3].substring(0, mData[3].length()-2);

        TitleBar titleBar = (TitleBar) findViewById(R.id.accept_tb);
        titleBar.setTitleText("邀请处理");
        titleBar.isShowRight(false);
        titleBar.setBackClickListener(this);

        mYesBtn = (Button) findViewById(R.id.accept_yes_btn);
        mNoBtn = (Button) findViewById(R.id.accept_no_btn);
        mContentTv = (TextView) findViewById(R.id.accept_content_tv);
//        mContentTv.setText(" $"+ mData[3] +"$" +" 邀请您加入"+"群组 #" +mData[2]+"#.");

        mContentTv.setText( mData[0] + "%" + mData[1] + "%"+ mData[2] + "%" + mData[3]);


        mYesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doJoin();
            }
        });

        mNoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }


    private void doJoin() {
        HnustUser user = new HnustUser();
        user.setObjectId(mData[1]);
        Group group = new Group();
        group.setObjectId(mData[0]);
        BmobRelation relation = new BmobRelation();
        relation.add(user);
        group.setUsers(relation);
        group.update(this, new UpdateListener() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                T.showShort(AcceptActivity.this, "加入成功！");
                finish();
            }

            @Override
            public void onFailure(int arg0, String arg1) {
                // TODO Auto-generated method stub
            }
        });
    }
}
