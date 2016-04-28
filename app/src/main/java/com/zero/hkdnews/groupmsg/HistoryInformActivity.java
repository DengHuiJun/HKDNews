package com.zero.hkdnews.groupmsg;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zero.hkdnews.R;
import com.zero.hkdnews.myview.TitleBar;

public class HistoryInformActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_inform);

        TitleBar titleBar = (TitleBar) findViewById(R.id.history_inform_tb);
        titleBar.setBackClickListener(this);
        titleBar.setTitleText("所有通知");
        titleBar.isShowRight(false);

    }
}
