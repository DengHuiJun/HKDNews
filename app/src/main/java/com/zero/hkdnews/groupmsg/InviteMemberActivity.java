package com.zero.hkdnews.groupmsg;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;

import com.zero.hkdnews.R;
import com.zero.hkdnews.activity.BaseActivity;
import com.zero.hkdnews.myview.TitleBar;
import com.zero.hkdnews.util.T;

import cn.bmob.v3.BmobPushManager;

/**
 * Created by 邓慧 on 16/4/12.
 */
public class InviteMemberActivity extends BaseActivity{

    private TitleBar mTitleBar;
    private TextInputLayout mTIL;
    private Button mBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_member);

        mTIL = (TextInputLayout) findViewById(R.id.invite_member_til);
        mTitleBar = (TitleBar) findViewById(R.id.invite_member_title_bar);
        mBtn = (Button) findViewById(R.id.invite_member_btn);

        mTitleBar.setTitleText("邀请成员");
        mTitleBar.setBackClickListener(this);
        mTitleBar.isShowRight(false);


        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = mTIL.getEditText().getText().toString();

                if (name.equals("")) {
                    return;
                }

                doInvite(name);

            }
        });

    }

    private void doInvite(String username) {

        BmobPushManager bmobPushManager = new BmobPushManager(this);
        bmobPushManager.pushMessageAll("这是给所有设备推送的一条消息。"+ username);



        T.showShort(this, "发送成功！");
        finish();
    }
}
