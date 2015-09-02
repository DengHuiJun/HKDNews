package com.zero.hkdnews.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.quentindommerc.superlistview.SuperListview;
import com.zero.hkdnews.R;
import com.zero.hkdnews.adapter.CommentAdapter;
import com.zero.hkdnews.app.AppContext;
import com.zero.hkdnews.beans.CollectNews;
import com.zero.hkdnews.beans.Comment;
import com.zero.hkdnews.beans.News;
import com.zero.hkdnews.beans.NewsBody;
import com.zero.hkdnews.common.UIHelper;
import com.zero.hkdnews.util.L;
import com.zero.hkdnews.util.T;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.social.share.core.BMShareListener;
import cn.bmob.social.share.core.ErrorInfo;
import cn.bmob.social.share.core.data.BMPlatform;
import cn.bmob.social.share.core.data.ShareData;
import cn.bmob.social.share.view.BMShare;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 新闻详情页
 * Created by zero on 15/4/11.
 */
public class NewsActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener,AdapterView.OnItemClickListener{
    private static final String TAG = "NewsActivity";

    private Context context ;

    private ImageView mHome;  // 头部的home
    private ImageView mRefresh;
    private TextView mHeadTitle;

    //包裹webView
    private ScrollView mScrollView;
    private ViewSwitcher mViewSwitcher;
    private ProgressBar loadPB;

    private View mCommentV; // 评论
    private View mDetailV; // 回到新闻界面
    private View mCommentListV; // 查看评论列表
    private View mLoveV; // 收藏
    private View mShareV; // 一键分享

    private ViewSwitcher mFootViewSwitcher;
    private EditText mCommentContent;
    private Button mCommentBtn;

    private WebView mWebView;

    private News  news;
    private Comment mComment;

    private static final int GET_NEWS_COMMENT = 1;
    private static final int GET_NEWS_CODE = 2;
    private static final int UPLOAD_NEWS_COMMENT_MSG = 3;
    private static final int ADD_COM_TO_NEWS_MSG = 4;
    private static final int REFRENSH_COMMENTS_MSG = 5;
    private static final int NO_COLLECTED = 6;  // 未收藏新闻
    private static final int IS_COLLECTED = 7;  // 已经收藏过
    private static final int COLLECTED_OK = 8;

    private static final int VIEWSWITCH_TYPE_DETAIL = 0x001;
    private static final int VIEWSWITCH_TYPE_COMMENT = 0x002;

    //是否评论模式
    private boolean isCommented = false;

    private CommentAdapter commentAdapter;
    private SuperListview commentListView;
    private List<Comment> comments = new ArrayList<>();

    private List<CollectNews> mCollectedNewsList = new ArrayList<>();

    private WeakReferenceHander mWeakHandler;

    static class WeakReferenceHander extends Handler {
        private final WeakReference<NewsActivity> mActivity;

        public WeakReferenceHander(NewsActivity activity) {
            mActivity = new WeakReference<NewsActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mActivity.get() != null) {
                mActivity.get().handleReceiveMessage(msg);
            }
        }
    }

    /**
     * UI线程处理消息
     * @param msg
     */
    private void handleReceiveMessage(Message msg) {
        switch (msg.what) {

            //webView显示新闻内容
            case GET_NEWS_CODE:
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
                break;
            //第一次加载评论列表
            case GET_NEWS_COMMENT:
                comments = (List<Comment>) msg.obj;
                commentAdapter.setListItems(comments);
                commentAdapter.notifyDataSetChanged();
                break;

            //刷新评论列表
            case REFRENSH_COMMENTS_MSG:
                comments = (List<Comment>) msg.obj;
                commentAdapter.setListItems(comments);
                commentAdapter.notifyDataSetChanged();
                T.showShort(context, "刷新成功!");
                break;

            case UPLOAD_NEWS_COMMENT_MSG:
                addCommentToNews(mComment);
                break;

            case ADD_COM_TO_NEWS_MSG:
                T.showShort(context, "评论成功！");
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mCommentV.getWindowToken(), 0);
                switchIsComment(VIEWSWITCH_TYPE_DETAIL);
                break;

            case NO_COLLECTED:
                collectThisNews();
                break;

            case IS_COLLECTED:
                T.showShort(context, "亲，您已经收藏过了哦！");
                break;

            case COLLECTED_OK:
                T.showShort(context, "收藏成功！");
                break;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        mWeakHandler = new WeakReferenceHander(this);
        findView();
        initView();
        initData(news.getObjectId());

    }

    private void findView() {
        //顶部的控件绑定
        mHome = (ImageView) findViewById(R.id.news_header_home);
        mRefresh = (ImageView) findViewById(R.id.news_header_refresh);
        mHeadTitle = (TextView) findViewById(R.id.news_header_title);

        //底部的控件绑定
        mFootViewSwitcher = (ViewSwitcher) findViewById(R.id.news_detail_foot_view_switcher);

        mCommentV = findViewById(R.id.news_detail_comment);
        mDetailV = findViewById(R.id.news_detail_comment_detail);
        mCommentListV = findViewById(R.id.news_detail_comment_list);
        mLoveV = findViewById(R.id.news_detail_love);
        mShareV = findViewById(R.id.news_detail_share);

        //评论模块2个控件
        mCommentContent = (EditText) findViewById(R.id.news_detail_foot_editer);
        mCommentBtn = (Button) findViewById(R.id.news_detail_foot_comment_btn);

        //包裹webview的控件
        mViewSwitcher = (ViewSwitcher) findViewById(R.id.news_detail_viewswitcher);
        mScrollView = (ScrollView) findViewById(R.id.news_detail_scrollview);
        loadPB = (ProgressBar) findViewById(R.id.news_detail_pb);

        mWebView = (WebView) findViewById(R.id.news_detail_webview);
    }


    private void initView() {

        context = this;
        //获取传递过来news
        news = (News) getIntent().getBundleExtra("data").getSerializable("news");

        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setDefaultFontSize(15);

        //最顶部设置事件
        mHome.setOnClickListener(homeClickListenter);
        mRefresh.setOnClickListener(refreshClickListener);

        //最底部设置监听事件
        mCommentV.setOnClickListener(commentClickListener);
        mCommentListV.setOnClickListener(commentListClickListener);
        mLoveV.setOnClickListener(loveClickListener);
        mShareV.setOnClickListener(shareClickListener);
        mCommentBtn.setOnClickListener(pubClickListener);
        mDetailV.setOnClickListener(toNewsClickListener);

        //评论列表
        commentListView = (SuperListview) findViewById(R.id.comment_list_listview);

        commentAdapter = new CommentAdapter(context,comments);
        commentListView.setAdapter(commentAdapter);
        commentListView.setRefreshListener(this);
//        commentListView.setOnItemClickListener(this);   // 暂时关闭回复评论功能，未想好显示的方式
    }

    /**
     * 加载评论的数据
     */
    private void loadCommentData(final boolean isFirst){
        BmobQuery<Comment> query = new BmobQuery<Comment>();
        query.order("-createdAt");
        query.setLimit(20);
        query.addWhereEqualTo("newsId",news.getObjectId());
        query.findObjects(context, new FindListener<Comment>() {
            @Override
            public void onSuccess(List<Comment> list) {
                Message msg = Message.obtain();
                if (isFirst) {
                    msg.what = GET_NEWS_COMMENT;
                } else {
                    msg.what = REFRENSH_COMMENTS_MSG;
                }
                msg.obj = list;
                mWeakHandler.sendMessage(msg);
            }

            @Override
            public void onError(int i, String s) {
                L.d("get Comments:" + s);
            }
        });
    }

    /**
     * 加载新闻内容
     * @param newsId
     */
    private void initData(final String newsId){
        BmobQuery<NewsBody> query = new BmobQuery<>();
        query.addWhereEqualTo("newsId",newsId);
        query.findObjects(getApplicationContext(), new FindListener<NewsBody>() {
            @Override
            public void onSuccess(List<NewsBody> list) {
                if (list.get(0) != null){
                    Message msg = Message.obtain();
                    msg.obj = list.get(0);
                    msg.what = GET_NEWS_CODE;
                    mWeakHandler.sendMessage(msg);
                }
            }
            @Override
            public void onError(int i, String s) {
                T.showShort(getApplicationContext(), "ERROR!" + s);
            }
        });
    }


    /**
     * 查询新闻是否已收藏
     */
    private void queryCollectedNews() {
        BmobQuery<CollectNews> query = new BmobQuery<>();
        query.addWhereEqualTo("userId",AppContext.currentUserId);
        query.addWhereEqualTo("newsId",news.getObjectId());
        query.findObjects(getApplicationContext(), new FindListener<CollectNews>() {
            @Override
            public void onSuccess(List<CollectNews> list) {
                Message msg = Message.obtain();
                if (list.size() > 0) {
                    msg.what = IS_COLLECTED;
                } else {
                    msg.what = NO_COLLECTED;
                }
                mWeakHandler.sendMessage(msg);
            }
            @Override
            public void onError(int i, String s) {
                T.showShort(getApplicationContext(), "ERROR!"+s);
            }
        });
    }

    /**
     * 上传至服务器，收藏列表
     */
    private void collectThisNews() {
        CollectNews cn = new CollectNews();
        cn.setUserId(AppContext.currentUserId);
        cn.setNewsId(news.getObjectId());
        cn.setNewsTitle(news.getNewsTitle());
        cn.setNewsSource(news.getNewsSource());
        cn.setCode(news.getCode());
        cn.save(getApplicationContext(), new SaveListener() {
            @Override
            public void onSuccess() {
                Message msg = Message.obtain();
                msg.what = COLLECTED_OK;
                mWeakHandler.sendMessage(msg);
            }

            @Override
            public void onFailure(int i, String s) {
                T.showShort(getApplicationContext(), "ERROR!"+s);
            }
        });
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
            initData(news.getObjectId());
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
            switchIsComment(VIEWSWITCH_TYPE_DETAIL);
        }
    };

    /**
     * 切换到评论列表事件
     */
    private View.OnClickListener commentListClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            loadCommentData(true);
            switchIsComment(VIEWSWITCH_TYPE_COMMENT);
        }
    };

    /**
     * 收藏新闻
     */
    private View.OnClickListener loveClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            queryCollectedNews();
        }
    };

    /**
     *社会化分享
     */
    private View.OnClickListener shareClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
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
            share.show();
           // Log.d("bmob", "分享end");
        }
    };

    /**
     * 点击上传评论，监听事件
     */
    private View.OnClickListener pubClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
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
                mDetailV.setEnabled(false);
                mCommentListV.setEnabled(true);
                mHeadTitle.setText("新闻正文");
                mViewSwitcher.setDisplayedChild(0);
                break;

            case VIEWSWITCH_TYPE_COMMENT:
                mDetailV.setEnabled(true);
                mCommentListV.setEnabled(false);
                mHeadTitle.setText("网友评论");
                mViewSwitcher.setDisplayedChild(1);
                break;
        }
    }
    /**
     * 上传评论至Bmob服务器
     */
    private void commitComment(){
        if (!mCommentContent.getText().toString().equals("")) {
            mComment = new Comment();
            mComment.setNews(news);
            mComment.setAuthor(AppContext.getUserName());
            mComment.setAuthorId(AppContext.currentUserId);
            mComment.setNewsId(news.getObjectId());
            mComment.setFace(AppContext.getMyHead());
            mComment.setContent(mCommentContent.getText().toString());
            mComment.save(context, new SaveListener() {
                @Override
                public void onSuccess() {
                    mFootViewSwitcher.setDisplayedChild(0);
                    mCommentContent.setText("");
                    Message msg = Message.obtain();
                    msg.what = UPLOAD_NEWS_COMMENT_MSG;
                    mWeakHandler.sendMessage(msg);
                }

                @Override
                public void onFailure(int i, String s) {
                    L.d(s);
                    T.showShort(context, "评论失败！");
                }
            });
        } else {
            T.showShort(context,"请输入评论内容！");
        }
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
                Message msg = Message.obtain();
                msg.what = ADD_COM_TO_NEWS_MSG;
                mWeakHandler.sendMessage(msg);
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
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            if(isCommented) {
                mFootViewSwitcher.setDisplayedChild(0);
                isCommented = false;
                return false;
            } else {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onRefresh() {
        loadCommentData(false);
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
