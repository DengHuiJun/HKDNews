package com.zero.hkdnews.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.quentindommerc.superlistview.SuperListview;
import com.zero.hkdnews.R;
import com.zero.hkdnews.adapter.CommentAdapter;
import com.zero.hkdnews.app.AppContext;
import com.zero.hkdnews.beans.Comment;
import com.zero.hkdnews.beans.News;
import com.zero.hkdnews.beans.NewsBody;
import com.zero.hkdnews.common.UIHelper;
import com.zero.hkdnews.util.L;
import com.zero.hkdnews.util.T;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.social.share.core.BMShareListener;
import cn.bmob.social.share.core.ErrorInfo;
import cn.bmob.social.share.core.data.BMPlatform;
import cn.bmob.social.share.core.data.ShareData;
import cn.bmob.social.share.view.BMShare;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 新闻详情页
 * Created by luowei on 15/4/11.
 */
public class NewsActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener,AdapterView.OnItemClickListener{

    private Context context ;

    private ImageView mHome;  //头部的home
    private ImageView mRefresh;
    private TextView mHeadTitle;

    //包裹webView
    private ScrollView mScrollView;
    private ViewSwitcher mViewSwitcher;
    private ProgressBar loadPB;

    //评论
    private View mComment;

    //回到新闻界面
    private View mDetail;

    //查看评论列表
    private View mCommentList;

    //点赞
    private View mLove;

    //一键分享
    private View mShare;

    private ViewSwitcher mFootViewSwitcher;
    private EditText mCommentContent;
    private Button mCommentBtn;

    private WebView mWebView;
    private Handler mHandler;

    private News  news;

    private static final int GET_NEWS_COMMENT = 1;
    private static final int GET_NEWS_CODE = 2;

    private static final int VIEWSWITCH_TYPE_DETAIL = 0x001;
    private static final int VIEWSWITCH_TYPE_COMMENT = 0x002;

    //是否评论模式
    private boolean isCommented = false;


    private CommentAdapter commentAdapter;
    private SuperListview commentListView;
    private List<Comment> comments = new ArrayList<>();
    private Handler mCommentHandler;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news2);

        initView();
        initData();
        initCommentData();


    }

    //初始化文章内容
    private void initData() {
        mHandler = new Handler() {
            public void handleMessage(Message msg){
                if(msg.what == GET_NEWS_CODE){
                    NewsBody body = (NewsBody) msg.obj;
                    mWebView.loadDataWithBaseURL(null,body.getBody(),"text/html","utf-8",null);
                    mWebView.setWebViewClient(new WebViewClient(){
                        @Override
                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                            view.loadUrl(url);
                            return super.shouldOverrideUrlLoading(view,url);
                        }

                        @Override
                        public void onPageFinished(WebView view, String url) {
                            super.onPageFinished(view, url);
                            loadPB.setVisibility(View.GONE);
                            mWebView.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onPageStarted(WebView view, String url, Bitmap favicon) {
                            super.onPageStarted(view, url, favicon);
                        }
                    });

                }
            }
        };

        initDate(news.getObjectId(), false);

    }

    /**
     * 初始化评论列表数据
     */
    private void initCommentData(){
        commentAdapter = new CommentAdapter(context,comments);
        commentListView.setAdapter(commentAdapter);
        commentListView.setRefreshListener(this);
        commentListView.setOnItemClickListener(this);

        mCommentHandler = new Handler(){
          public void handleMessage(Message msg){
              if(msg.what == GET_NEWS_COMMENT){
                        comments = (List<Comment>) msg.obj;
                        commentAdapter.setListItems(comments);
                        commentAdapter.notifyDataSetChanged();
              }
          }
        };
    }


    /**
     * 加载评论数据的线程
     */
    private void loadCommentData(){
        new Thread(){
            public void run(){
                BmobQuery<Comment> query = new BmobQuery<Comment>();
                query.order("-createdAt");
                query.setLimit(20);
                query.addWhereEqualTo("newsId",news.getObjectId());
                query.findObjects(context, new FindListener<Comment>() {
                    @Override
                    public void onSuccess(List<Comment> list) {
                        Message msg = Message.obtain();
                        msg.what = GET_NEWS_COMMENT;
                        msg.obj = list;
                        mCommentHandler.sendMessage(msg);
                    }

                    @Override
                    public void onError(int i, String s) {
                        L.d("get Comments:"+s);
                    }
                });
            }
        }.start();
    }

    /**
     * 加载新闻内容
     * @param newsId
     * @param isRefresh
     */

    private void initDate(final String newsId,final boolean isRefresh){
        new Thread(){
            public void run(){
                BmobQuery<NewsBody> query = new BmobQuery<>();
                query.addWhereEqualTo("newsId",newsId);
                query.findObjects(getApplicationContext(), new FindListener<NewsBody>() {
                    @Override
                    public void onSuccess(List<NewsBody> list) {
                        if (list.get(0) != null){
                            Message msg = Message.obtain();
                            msg.obj = list.get(0);
                            msg.what = GET_NEWS_CODE;
                            mHandler.sendMessage(msg);
                        }
                    }
                    @Override
                    public void onError(int i, String s) {
                        T.showShort(getApplicationContext(),"ERROR!");
                    }
                });
            }

        }.start();
    }

    private void initView() {

        context =this;
        //获取传递过来news
       news = (News) getIntent().getBundleExtra("data").getSerializable("news");

        //顶部的控件绑定
        mHome = (ImageView) findViewById(R.id.news_header_home);
        mRefresh = (ImageView) findViewById(R.id.news_header_refresh);
        mHeadTitle = (TextView) findViewById(R.id.news_header_title);

        //底部的控件绑定
        mFootViewSwitcher= (ViewSwitcher) findViewById(R.id.news_detail_foot_view_switcher);

        mComment = findViewById(R.id.news_detail_comment);
        mDetail = findViewById(R.id.news_detail_comment_detail);
        mCommentList = findViewById(R.id.news_detail_comment_list);
        mLove=findViewById(R.id.news_detail_love);
        mShare=findViewById(R.id.news_detail_share);

        //评论模块2个控件
        mCommentContent = (EditText) findViewById(R.id.news_detail_foot_editer);
        mCommentBtn = (Button) findViewById(R.id.news_detail_foot_comment_btn);

        //包裹webview的控件
        mViewSwitcher = (ViewSwitcher) findViewById(R.id.news_detail_viewswitcher);
        mScrollView = (ScrollView) findViewById(R.id.news_detail_scrollview);
        loadPB = (ProgressBar) findViewById(R.id.news_detail_pb);

        mWebView = (WebView) findViewById(R.id.news_detail_webview);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setDefaultFontSize(15);

        //最顶部设置事件
        mHome.setOnClickListener(homeClickListenter);
        mRefresh.setOnClickListener(refreshClickListener);

        //最底部设置监听事件
        mComment.setOnClickListener(commentClickListener);
        mCommentList.setOnClickListener(commentListClickListener);
        mLove.setOnClickListener(loveClickListener);
        mShare.setOnClickListener(shareClickListener);
        mCommentBtn.setOnClickListener(pubClickListener);
        mDetail.setOnClickListener(toNewsClickListener);

        //评论列表
        commentListView = (SuperListview) findViewById(R.id.comment_list_listview);
    }
    private View.OnClickListener homeClickListenter = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            UIHelper.showHome(context);
            finish();
        }
    };

    private View.OnClickListener refreshClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            initDate(news.getObjectId(), true);
            Toast.makeText(context,"refresh",Toast.LENGTH_SHORT).show();
        }
    };

    //进入评论模式
    private View.OnClickListener commentClickListener =new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            isCommented =true;
            mFootViewSwitcher.setDisplayedChild(1);
        }
    };


    private View.OnClickListener toNewsClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            T.showShort(context,"ToNews");
            switchIsComment(VIEWSWITCH_TYPE_DETAIL);
        }
    };

    /**
     * 切换到评论列表事件
     */
    private View.OnClickListener commentListClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            T.showShort(getApplicationContext(),"commentList");
            loadCommentData();
            switchIsComment(VIEWSWITCH_TYPE_COMMENT);
        }
    };

    /**
     * 点赞事件
     */
    private View.OnClickListener loveClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            T.showShort(getApplicationContext(), "love");
        }
    };

    /**
     *社会化分享
     */
    private View.OnClickListener shareClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          //  T.showShort(getApplicationContext(), "share");
            ShareData shareData = new ShareData();
            shareData.setTitle(news.getNewsTitle());
            shareData.setDescription(news.getNewsTitle());
            shareData.setText("分享");
            shareData.setTarget_url(news.getStrUrl());
            shareData.setImageUrl(news.getNewsImage().getFileUrl(context));

            BMShareListener whiteViewListener = new BMShareListener() {
                @Override
                public void onPreShare() {
                    T.showShort(context,"开始分享");
                }
                @Override
                public void onSuccess() {
                    T.showShort(context,"分享成功！");
                }
                @Override
                public void onError(ErrorInfo error) {
                    T.showShort(context,"分享失败"+ error);
                }
                @Override
                public void onCancel() {
                    T.showShort(context,"取消分享");
                }
            };
            BMShare share = new BMShare(NewsActivity.this);
            share.setShareData(shareData);
            share.addListener(BMPlatform.PLATFORM_WECHAT, whiteViewListener);
            share.addListener(BMPlatform.PLATFORM_WECHATMOMENTS, whiteViewListener);
            share.addListener(BMPlatform.PLATFORM_SINAWEIBO, whiteViewListener);
            share.addListener(BMPlatform.PLATFORM_RENN, whiteViewListener);
            share.addListener(BMPlatform.PLATFORM_TENCENTWEIBO, whiteViewListener);
            share.addListener(BMPlatform.PLATFORM_QQ, whiteViewListener);
            share.addListener(BMPlatform.PLATFORM_QZONE, whiteViewListener);
            share.show();
           // Log.d("bmob", "分享end");
        }
    };

    /**
     * 点击评论监听事件
     */
    private View.OnClickListener pubClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            T.showShort(getApplicationContext(),"Btn");
            commitComment();
        }
    };
    /**
     * 切换显示新闻还是评论列表
     * @param type
     */
    private void switchIsComment(int type){
        switch (type){
            case VIEWSWITCH_TYPE_DETAIL:
                mDetail.setEnabled(false);
                mCommentList.setEnabled(true);
                mHeadTitle.setText("新闻正文");
                mViewSwitcher.setDisplayedChild(0);
                break;

            case VIEWSWITCH_TYPE_COMMENT:
                mDetail.setEnabled(true);
                mCommentList.setEnabled(false);
                mHeadTitle.setText("网友评论");
                mViewSwitcher.setDisplayedChild(1);
                break;
        }
    }
    /**
     * 上传评论至服务器线程
     */
    private void commitComment(){
        new Thread(){
            public void run(){
                if (!mCommentContent.getText().toString().equals("")) {
                    Comment temp = new Comment();
                    temp.setNews(news);
                    temp.setAuthor(AppContext.getUserName());
                    temp.setAuthorId(AppContext.currentUserId);
                    temp.setNewsId(news.getObjectId());
                    temp.setFace(AppContext.getMyHead());
                    temp.setContent(mCommentContent.getText().toString());
                    temp.save(context, new SaveListener() {
                        @Override
                        public void onSuccess() {
                            T.showShort(context, "评论成功！");
                            mFootViewSwitcher.setDisplayedChild(0);
                            mCommentContent.setText("");
                        }
                        @Override
                        public void onFailure(int i, String s) {
                            L.d(s);
                            T.showShort(context, "评论失败！");
                        }
                    });
                    addCommentToNews(temp);

                }else{
                    T.showShort(context,"请输入评论内容！");
                }
            }
        }.start();
    }


    /**
     * 将评论绑定至新闻
     * @param comment
     */
    private void addCommentToNews(Comment comment){
        BmobRelation comments = new BmobRelation();
        comments.add(comment);
        news.setComments(comments);
        news.update(context, new UpdateListener() {
            @Override
            public void onSuccess() {
                T.showShort(context,"绑定到news");
            }

            @Override
            public void onFailure(int i, String s) {
                L.d(s);
            }
        });

    }

    /**
     * 底部的评论模式下，不能退出
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(isCommented){
                mFootViewSwitcher.setDisplayedChild(0);
                isCommented = false;
                return false;
            }else{
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onRefresh() {

        T.showShort(context,"刷新数据");
    }

    //网友评论的点击事件
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Comment com = comments.get(position);
        Bundle bundle = new Bundle();
        bundle.putSerializable("comment",com);
        UIHelper.showCommentPub(this,bundle);
    }
}
