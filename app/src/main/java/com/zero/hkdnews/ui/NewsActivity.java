package com.zero.hkdnews.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.zero.hkdnews.R;
import com.zero.hkdnews.beans.News;
import com.zero.hkdnews.common.UIHelper;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.GetListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * 新闻详情
 * Created by luowei on 15/4/11.
 */
public class NewsActivity extends Activity{

    private Context context ;

    private LinearLayout mHeader; //上面的头部导航

    private LinearLayout mFooter; //底部的导航
    private ImageView mHome;  //头部的home
    private ImageView mRefresh;
    private TextView mHeadTitle;
    private ScrollView mScrollView;
    private ViewSwitcher mViewSwitcher;

    private ImageView mComment;
    private ImageView mDetail;
    private ImageView mCommentList;
    private ImageView mShare;

    private WebView mWebView;
    private Handler mHandler;

    private News newsDetail;
    private String newsId;

    private ViewSwitcher mFootViewSwitcher;

    public static final int GET_NEWS_CODE = 2;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news2);

        initView();
        initData();


    }

    private void initData() {
        mHandler = new Handler() {
            public void handleMessage(Message msg){
                if(msg.what == GET_NEWS_CODE){
                    News body = (News) msg.obj;
                    mWebView.loadDataWithBaseURL(null,body.getBody(),"text/html","utf-8",null);
                    mWebView.setWebViewClient(new WebViewClient(){
                        @Override
                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                            view.loadUrl(url);
                            return super.shouldOverrideUrlLoading(view,url);
                        }
                    });

                }
            }
        };

        initDate(newsId,false);

    }


    private void initDate(final String newsId,final boolean isRefresh){
        new Thread(){
            public void run(){
                BmobQuery<News> query = new BmobQuery<News>();
                query.getObject(context,newsId,new GetListener<News>() {
                    @Override
                    public void onSuccess(News news) {
                        Message msg = Message.obtain();
                        msg.obj = news;
                        msg.what = GET_NEWS_CODE;
                        mHandler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(int i, String s) {

                    }
                });

            }

        }.start();
    }

    private void initView() {

        context =this;
        newsId = getIntent().getStringExtra("news_id");

        mHeader = (LinearLayout) findViewById(R.id.news_header);
        mFooter = (LinearLayout) findViewById(R.id.news_detail_footer);
        mHome = (ImageView) findViewById(R.id.news_header_home);
        mRefresh = (ImageView) findViewById(R.id.news_header_refresh);
        mHeadTitle = (TextView) findViewById(R.id.news_header_title);

        mViewSwitcher = (ViewSwitcher) findViewById(R.id.news_detail_viewswitcher);
        mScrollView = (ScrollView) findViewById(R.id.news_detail_scrollview);

        mComment = (ImageView) findViewById(R.id.news_detail_foot_com);
        mDetail = (ImageView) findViewById(R.id.news_detail_foot_detail);
        mCommentList  = (ImageView) findViewById(R.id.news_detail_foot_comlist);
        mShare = (ImageView) findViewById(R.id.news_detail_foot_share);

        mDetail.setEnabled(false);

        mWebView = (WebView) findViewById(R.id.news_detail_webview);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setDefaultFontSize(15);


   //     UIHelper.addWebImageShow(this,mWebView);

        mHome.setOnClickListener(homeClickListenter);
        mRefresh.setOnClickListener(refreshClickListener);
        mComment.setOnClickListener(comClickListener);
        mDetail.setOnClickListener(detailClickListener);
        mCommentList.setOnClickListener(comListClickListener);
        mShare.setOnClickListener(shareClickListener);



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

    private View.OnClickListener homeClickListenter = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            UIHelper.showHome(context);
            finish();
           // Toast.makeText(context,"Click Home!",Toast.LENGTH_SHORT).show();
        }
    };

    private View.OnClickListener refreshClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            initDate(newsId,true);
            Toast.makeText(context,"refresh",Toast.LENGTH_SHORT).show();
        }
    };

    private View.OnClickListener shareClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Toast.makeText(context,"share",Toast.LENGTH_SHORT).show();
            showShare();
        }
    };

    private View.OnClickListener detailClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
        Toast.makeText(context,"detail",Toast.LENGTH_SHORT).show();
        }
    };

    private View.OnClickListener comListClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        Toast.makeText(context,"comList",Toast.LENGTH_SHORT).show();
        }
    };

    private View.OnClickListener comClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
        Toast.makeText(context,"comment",Toast.LENGTH_SHORT).show();
        }
    };


}
