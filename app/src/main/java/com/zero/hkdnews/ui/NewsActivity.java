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
                if(msg.what == 1){
                    String body = (String) msg.obj;
                    mWebView.loadDataWithBaseURL(null,body,"text/html","utf-8",null);
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
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //要显示的页面
                String html = "<ul>\n" +
                        "  <li><em>学历</em> 由于拿到offer后，办理工作签证时出示本科成绩单。所以本科学历是最低要求的。</li>\n" +
                        "  <li><em>英语</em> 英语没有硬性要求，不需要雅思托福成绩。个人觉得能无障碍的听懂youtube上的技术分享，会一些基本日常语法加上相关专业词汇，就能比较顺利的完成电面和人肉面。</li>\n" +
                        "  <li><em>专业经验</em> 没有硬性的相关领域证书要求，当然如果你没有内推渠道，有个把证可以增加通过简历过滤器脱引而出的机会。</li>\n" +
                        "  <li><em>技术经验</em> 是否有能力维护设计Facebook服务器量级的系统是一个重要考察点。当然不要求你一定要经历过这么大的量级经验（毕竟这样的公司不多）。</li>\n" +
                        "  <li><em>家庭</em> “一人Offer，全家受益”是我对Facebook Relocation的总结。拿到Offer后的所有环节，Facebook都会把你的家庭（配偶和子女）作为一个整体考虑进去。所以只要家人支持，家庭不会成为入职的羁绊。</li>\n" +
                        "  <li><em>国外生活经历</em> 博主在去Facebook前，除了一次自助蜜月游，从来没有出过国。也证明这方面没有硬性要求。个人觉得生活就像学游泳，扔进水里了，扑腾几下怎么样都会了。</li>\n" +
                        "  <li><em>会翻墙</em> 呵呵。。。</li>\n" +
                        "</ul>";

                Message msg = new Message();
                msg.what = 1;
                msg.obj = html;
                mHandler.sendMessage(msg);
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
