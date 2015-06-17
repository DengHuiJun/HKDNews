package com.zero.hkdnews.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.Window;

import com.zero.hkdnews.R;

import android.os.Handler;

/**
 * Created by luowei on 15/6/17.
 */
public class StartActivity extends BaseActivity {

    private Handler handler = new Handler() {

        public void handleMessage(Message msg){
            if (msg.what == 11){
                Intent intent = new Intent(StartActivity.this,LoginActivity.class);
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
}
