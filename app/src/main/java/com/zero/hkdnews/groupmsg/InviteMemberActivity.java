package com.zero.hkdnews.groupmsg;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;

import com.zero.hkdnews.R;
import com.zero.hkdnews.activity.BaseActivity;
import com.zero.hkdnews.app.AppContext;
import com.zero.hkdnews.beans.HnustUser;
import com.zero.hkdnews.myview.TitleBar;
import com.zero.hkdnews.util.T;

import java.util.List;

import cn.bmob.v3.BmobPushManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by 邓慧 on 16/4/12.
 */
public class InviteMemberActivity extends BaseActivity {

    private TitleBar mTitleBar;
    private TextInputLayout mTIL;
    private Button mBtn;
    private String mGroupId;
    private String mGroupName;
    private String mUserId;
    private String mUserName;

    private final static int CODE_HAVE = 0x01;
    private final static int CODE_NO = 0x02;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == CODE_HAVE) {
                HnustUser  user = (HnustUser) msg.obj;
                mUserId = user.getObjectId();
                doInvite();
            } else if (msg.what == CODE_NO) {
                T.showShort(InviteMemberActivity.this, "该用户不存在！");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_member);

        if (getIntent() != null) {
            mGroupId = getIntent().getStringExtra("id");
            mGroupName = getIntent().getStringExtra("name");
        }

        mTIL = (TextInputLayout) findViewById(R.id.invite_member_til);
        mTitleBar = (TitleBar) findViewById(R.id.invite_member_title_bar);
        mBtn = (Button) findViewById(R.id.invite_member_btn);

        mTitleBar.setTitleText("邀请成员");
        mTitleBar.setBackClickListener(this);
        mTitleBar.isShowRight(false);

        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUserName = mTIL.getEditText().getText().toString();

                if (mUserName.equals("")) {
                    return;
                }

                queryUser();

            }
        });

    }

    private void doInvite() {
        BmobPushManager bmobPushManager = new BmobPushManager(this);
        bmobPushManager.pushMessageAll(mGroupId + "&&&" + mUserId + "&&&" + mGroupName  + "&&&" + AppContext.getUserName());
        T.showShort(InviteMemberActivity.this, "邀请成功！");
        finish();
    }

    private void queryUser() {
        BmobQuery<HnustUser> query = new BmobQuery<>();
        query.addWhereEqualTo("username", mUserName);
        query.findObjects(this, new FindListener<HnustUser>() {
            @Override
            public void onSuccess(List<HnustUser> list) {
                Message msg = new Message();
                if (list.size() > 0) {
                    msg.what = CODE_HAVE;
                    msg.obj = list.get(0);
                } else {
                    msg.what = CODE_NO;
                }
                mHandler.sendMessage(msg);
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }
}
