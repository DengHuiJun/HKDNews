package com.zero.hkdnews.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.widget.EditText;

import com.zero.hkdnews.R;
import com.zero.hkdnews.beans.Group;
import com.zero.hkdnews.beans.HnustUser;
import com.zero.hkdnews.util.T;

import android.os.Handler;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 添加群组列表
 * Created by 邓慧 on 15/6/24.
 */
public class AddGroupActivity extends BaseActivity {

    @InjectView(R.id.add_group_name)
    EditText et_name;

    @InjectView(R.id.add_group_intro)
    EditText et_intro;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 22){
                HnustUser user = BmobUser.getCurrentUser(AddGroupActivity.this,HnustUser.class);

                final Group group = new Group();
                group.setObjectId((String) msg.obj);

                BmobRelation relation = new BmobRelation();

                relation.add(user);

                group.setUsers(relation);

                group.update(AddGroupActivity.this, new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        T.showShort(getApplicationContext(), "创建成功！关联了用户");
                        Message msg = Message.obtain();
                        msg.what =23;
                        msg.obj = group;
                        mHandler.sendMessage(msg);

                    }

                    @Override
                    public void onFailure(int i, String s) {
                        T.showShort(getApplicationContext(), s);
                    }
                });

            }
            if (msg.what == 23){
                Group group = (Group) msg.obj;

                HnustUser user = BmobUser.getCurrentUser(AddGroupActivity.this,HnustUser.class);

                BmobRelation relation = new BmobRelation();

                relation.add(group);

                user.setGroups(relation);

                user.update(AddGroupActivity.this, new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        T.showShort(getApplicationContext(), "关联用户！");
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        T.showShort(getApplicationContext(), s);
                    }
                });

            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);
        ButterKnife.inject(this);

    }

    @OnClick(R.id.add_group_add_btn)
    public void uploadEvent(){

        //创建群组
        final Group group = new Group();
        group.setName(et_name.getText().toString());
        group.setIntro(et_intro.getText().toString());

        group.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                Message msg = Message.obtain();
                msg.obj =  group.getObjectId();
                msg.what = 22;
                mHandler.sendMessage(msg);
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.reset(this);
    }
}
