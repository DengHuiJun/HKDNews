package com.zero.hkdnews.ui;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.zero.hkdnews.R;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import uk.co.deanwild.flowtextview.FlowTextView;

/**
 * Created by luowei on 15/4/11.
 */
public class NewsActivity extends Activity implements View.OnClickListener{

    private ImageButton share_imgBtn;
    private ImageButton comment_imgBtn;
    private ImageButton comList_imgBtn;

     private FlowTextView ftv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);


        initView();

        share_imgBtn.setOnClickListener(this);
        comList_imgBtn.setOnClickListener(this);
        comment_imgBtn.setOnClickListener(this);



        Spanned html = Html.fromHtml("<h1>welcome to mylife <h1>");

        ftv.setText(html);


    }

    private void initView() {
        ftv = (FlowTextView) findViewById(R.id.ftv);
        share_imgBtn = (ImageButton) findViewById(R.id.news_bottom_share);
        comment_imgBtn = (ImageButton) findViewById(R.id.news_bottom_comment);
        comList_imgBtn = (ImageButton) findViewById(R.id.news_bottom_comment_list);
    }

    private void showShare(){
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        // 分享时Notification的图标和文字

    //    oks.setNotification(R.drawable.ic_launcher, getResources().getString(R.string.app_name));
        oks.setText("我是分享文本");
        oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/05/21/oESpJ78_533x800.jpg");
        // 启动分享GUI
        oks.show(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //点击分享事件
            case R.id.news_bottom_share:
                showShare();
              //  Toast.makeText(this,"you click share",Toast.LENGTH_SHORT).show();
                break;
            //评论事件
            case R.id.news_bottom_comment:
                Toast.makeText(this,"you click comment",Toast.LENGTH_SHORT).show();
                break;
            //进入评论列表
            case R.id.news_bottom_comment_list:
                Toast.makeText(this,"you click list",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
