package com.zero.hkdnews.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.zero.hkdnews.R;
import com.zero.hkdnews.app.AppContext;
import com.zero.hkdnews.beans.HnustUser;
import com.zero.hkdnews.util.T;

import cn.bmob.v3.listener.UpdateListener;

/**
 * 修改个人信息
 * Created by 邓慧 on 15/6/19.
 */
public class MeSetActivity extends BaseActivity {
    private static final String TAG = "MeSetActivity";

    private EditText mSetNickEt;
    private EditText mSetIntroEt;
    private EditText mSetEmailEt;
    private EditText mSetCityEt;
    private Button   mSetOkBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_set);

        findView();
        mSetOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserInfo();
            }
        });
    }

    private void findView() {
        mSetNickEt = (EditText) findViewById(R.id.me_set_nickname);
        mSetIntroEt = (EditText) findViewById(R.id.me_set_intro);
        mSetCityEt = (EditText) findViewById(R.id.me_set_city);
        mSetEmailEt = (EditText) findViewById(R.id.me_set_email);
        mSetOkBtn = (Button) findViewById(R.id.me_set_ok_btn);
    }

    public void updateUserInfo(){
        HnustUser hnustUser = new HnustUser();
        hnustUser.setNickname(mSetNickEt.getText().toString());
        hnustUser.setIntro(mSetIntroEt.getText().toString());
        hnustUser.setEmail(mSetEmailEt.getText().toString());
        hnustUser.setLocation(mSetCityEt.getText().toString());
        hnustUser.update(getApplicationContext(), AppContext.currentUserId, new UpdateListener() {
            @Override
            public void onSuccess() {
                T.showShort(getApplicationContext(), "更新完毕！");
            }

            @Override
            public void onFailure(int i, String s) {
                T.showShort(getApplicationContext(), "更新失败！");
            }
        });
    }
}
