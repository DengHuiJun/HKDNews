package com.zero.hkdnews.activity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.zero.hkdnews.R;
import com.zero.hkdnews.app.AppContext;
import com.zero.hkdnews.beans.HnustUser;
import com.zero.hkdnews.util.T;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by 邓慧 on 15/6/19.
 */
public class MeSetActivity extends BaseActivity {

    @InjectView(R.id.me_set_nickname)
    EditText nick;

    @InjectView(R.id.me_set_intro)
    EditText intro;

    @InjectView(R.id.me_set_email)
    EditText email;

    @InjectView(R.id.me_set_city)
    EditText city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_set);
        ButterKnife.inject(this);


    }

    @OnClick(R.id.me_set_ok_btn)
    public void send(){

        new Thread(){
            public void run(){
                HnustUser hnustUser = new HnustUser();
                hnustUser.setNickname(nick.getText().toString());
                hnustUser.setIntro(intro.getText().toString());
                hnustUser.setEmail(email.getText().toString());
                hnustUser.setLocation(city.getText().toString());
                hnustUser.update(getApplicationContext(), AppContext.currentUserId, new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        T.showShort(getApplicationContext(),"更新完毕！");
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        T.showShort(getApplicationContext(),"更新失败！");
                    }
                });

            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.reset(this);
    }
}
