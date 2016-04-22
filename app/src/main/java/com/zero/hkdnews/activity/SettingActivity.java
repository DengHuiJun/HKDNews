package com.zero.hkdnews.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zero.hkdnews.R;
import com.zero.hkdnews.adapter.MeAdapter;
import com.zero.hkdnews.app.AppContext;
import com.zero.hkdnews.beans.MeItem;
import com.zero.hkdnews.myview.TitleBar;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的设置
 * Created by zero on 15-9-2.
 */
public class SettingActivity extends Activity {
    private static final String TAG = "SettingActivity";

    private ImageView mHeadIv;
    private TextView  mNickTv;
    private TextView  mIntroTv;

    private ListView  mSettingLv;

    private List<MeItem> mSettingItems;
    private MeAdapter meAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        TitleBar titleBar = (TitleBar) findViewById(R.id.setting_title_bar);
        titleBar.setTitleText("个人中心");
        titleBar.setBackClickListener(this);
        titleBar.isShowRight(false);

        findView();
        initData();
    }

    private void findView() {
        mHeadIv = (ImageView) findViewById(R.id.me_info_image);
        mNickTv = (TextView) findViewById(R.id.me_info_name);
        mIntroTv = (TextView) findViewById(R.id.me_info_intro);
        mSettingLv = (ListView) findViewById(R.id.me_list_view);
    }

    /**
     * 加载列表数据
     */
    private void initData() {
        //初始化个人信息
        if (AppContext.getMyHead()!=null){
            Picasso.with(this).load(AppContext.getMyHead().getFileUrl(this)).into(mHeadIv);
        }else {
            mHeadIv.setImageResource(R.mipmap.default_me);
        }
        mNickTv.setText(AppContext.getUserName());
        mIntroTv.setText(AppContext.getIntro());

        mSettingItems = new ArrayList(){};
        MeItem  item1 = new MeItem(R.mipmap.news_share_comment,"我的消息");
        MeItem  item2 = new MeItem(R.mipmap.me_push_news,"推送资讯");
        MeItem  item3 = new MeItem(R.mipmap.news_detail_bottom_love,"我的收藏");
        MeItem  item4 = new MeItem(R.mipmap.me_user_reply,"用户反馈");
        MeItem  item5 = new MeItem(R.mipmap.news_detail_top_refresh,"检查更新");
        MeItem  item6 = new MeItem(R.mipmap.me_about_us,"关于我们");
        MeItem  item7 = new MeItem(R.mipmap.me_set,"资料设置");
        mSettingItems.add(item1);
        mSettingItems.add(item2);
        mSettingItems.add(item3);
        mSettingItems.add(item4);
        mSettingItems.add(item5);
        mSettingItems.add(item6);
        mSettingItems.add(item7);

        meAdapter = new MeAdapter(getApplicationContext(), mSettingItems);
        mSettingLv.setAdapter(meAdapter);
        //添加各种响应事件
        mSettingLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position){
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    case 6:
                        Intent intent = new Intent(SettingActivity.this, MeSetActivity.class);
                        SettingActivity.this.startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });
    }
}
