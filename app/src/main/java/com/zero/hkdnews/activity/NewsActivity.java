package com.zero.hkdnews.activity;

import android.content.Context;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.quentindommerc.superlistview.SuperListview;
import com.zero.hkdnews.R;
import com.zero.hkdnews.adapter.CommentAdapter;
import com.zero.hkdnews.beans.Comment;
import com.zero.hkdnews.beans.News;
import com.zero.hkdnews.common.UIHelper;
import com.zero.hkdnews.util.L;
import com.zero.hkdnews.util.T;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;

/**
 * 新闻详情
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

    private String newsId;

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

        initDate(newsId, false);

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
                query.addWhereEqualTo("newsId",newsId);
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
                        L.d("get News"+s);
                    }
                });

            }

        }.start();
    }

    private void initView() {

        context =this;
        //获取传递过来newsId
        newsId = getIntent().getStringExtra("news_id");

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

        mCommentContent = (EditText) findViewById(R.id.news_detail_foot_editer);
        mCommentBtn = (Button) findViewById(R.id.news_detail_foot_comment_btn);

        //包裹webview的控件
        mViewSwitcher = (ViewSwitcher) findViewById(R.id.news_detail_viewswitcher);
        mScrollView = (ScrollView) findViewById(R.id.news_detail_scrollview);

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
            initDate(newsId, true);
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

    private View.OnClickListener commentListClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            T.showShort(getApplicationContext(),"commentList");
            loadCommentData();
            switchIsComment(VIEWSWITCH_TYPE_COMMENT);
        }
    };

    private View.OnClickListener loveClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            T.showShort(getApplicationContext(),"love");
        }
    };

    private View.OnClickListener shareClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            T.showShort(getApplicationContext(),"share");
        }
    };

    private View.OnClickListener pubClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            T.showShort(getApplicationContext(),"Btn");
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
        bundle.putString("author",com.getAuthor());
        bundle.putString("id",com.getObjectId());
        bundle.putString("content",com.getContent());
        UIHelper.showCommentPub(this,bundle);
    }
}
