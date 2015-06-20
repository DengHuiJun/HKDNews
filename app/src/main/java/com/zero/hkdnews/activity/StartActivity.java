package com.zero.hkdnews.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.view.Window;

import com.zero.hkdnews.R;

import android.os.Handler;

/**
 * 启动界面
 * Created by Guzz on /17.
 */
public class StartActivity extends BaseActivity {

    //用来标志是否第一次登录，来判断进入引导，还是登录
    private boolean flag = true;

    private Handler handler = new Handler() {

        public void handleMessage(Message msg){
            if (msg.what == 11){

                Intent intent;
                if(flag){
                    intent = new Intent(StartActivity.this,GuideActivity.class);
                } else{
                    intent = new Intent(StartActivity.this,LoginActivity.class);
                }
                startActivity(intent);
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_start);

        flag = getFlag();

        setFlag();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    Message msg = Message.obtain();
                    msg.what = 11;
                    handler.sendMessage(msg);


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }

    /**
     * 通过sharePreferences存储 来判断是否第一次
     */
    private void setFlag() {
        SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
        editor.putBoolean("flag", false);
        editor.commit();
    }


    public boolean getFlag() {
        SharedPreferences pref = getSharedPreferences("data",MODE_PRIVATE);
        boolean f = pref.getBoolean("flag",true);
        return f;
    }
}
