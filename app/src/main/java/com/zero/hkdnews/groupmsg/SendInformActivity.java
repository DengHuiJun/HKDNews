package com.zero.hkdnews.groupmsg;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.zero.hkdnews.R;
import com.zero.hkdnews.activity.BaseActivity;
import com.zero.hkdnews.app.AppContext;
import com.zero.hkdnews.beans.Group;
import com.zero.hkdnews.beans.HnustUser;
import com.zero.hkdnews.beans.Inform;
import com.zero.hkdnews.util.L;
import com.zero.hkdnews.util.T;

import android.os.Handler;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 发布通知
 * Created by 邓慧 on 15/6/20.
 */
public class SendInformActivity extends BaseActivity {

    private String groupId ;

    private String groupName;

    private EditText mContentEt;
    private Button   mSendBtn;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 11){
                HnustUser user = BmobUser.getCurrentUser(SendInformActivity.this,HnustUser.class);

                final Inform inform = (Inform) msg.obj;

                BmobRelation relation = new BmobRelation();
                relation.add(user);

                inform.setUsers(relation);

                inform.update(SendInformActivity.this, new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        Message msg = Message.obtain();
                        msg.what =22;
                        msg.obj = inform;
                        mHandler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(int i, String s) {

                    }
                });

            }
            if (msg.what == 22){
                Inform inform = (Inform) msg.obj;

                HnustUser user = BmobUser.getCurrentUser(SendInformActivity.this,HnustUser.class);

                BmobRelation relation = new BmobRelation();
                relation.add(inform);

                user.setInforms(relation);

                user.update(SendInformActivity.this, new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        T.showShort(SendInformActivity.this,"发布成功！");
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        T.showShort(SendInformActivity.this,s);
                    }
                });

            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_inform);

        mContentEt = (EditText) findViewById(R.id.send_inform_content);
        mSendBtn = (Button) findViewById(R.id.send_inform_btn);
        getDatas();

        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendInform();
            }
        });
    }

    /**
     * 得到传来的群组ID和名称
     */
    private void getDatas(){

        groupId = getIntent().getStringExtra("id");
        groupName = getIntent().getStringExtra("name");

        L.d(groupId + "%" + groupName);

    }

    public void sendInform(){
        Group group = new Group();
        group.setObjectId(groupId);


        final Inform inform = new Inform();
        inform.setAuthor(AppContext.getUserName());
        inform.setContent(mContentEt.getText().toString());
        inform.setTitle(groupName);
        inform.setGroup(group);

        inform.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                Message msg = Message.obtain();
                msg.what = 11;
                msg.obj = inform;
                mHandler.sendMessage(msg);
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }
}
