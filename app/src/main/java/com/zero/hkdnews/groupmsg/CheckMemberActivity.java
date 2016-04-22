package com.zero.hkdnews.groupmsg;

import android.os.Bundle;

import com.zero.hkdnews.R;
import com.zero.hkdnews.activity.BaseActivity;
import com.zero.hkdnews.myview.TitleBar;

/**
 * 查看群组的成员
 * Created by 邓慧 on 15/6/24.
 */
public class CheckMemberActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_member);

        TitleBar titleBar = (TitleBar) findViewById(R.id.check_member_tb);
        titleBar.isShowRight(false);
        titleBar.setTitleText("成员管理");
        titleBar.setBackClickListener(this);


    }
}
