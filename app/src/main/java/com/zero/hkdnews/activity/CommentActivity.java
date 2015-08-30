package com.zero.hkdnews.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zero.hkdnews.R;
import com.zero.hkdnews.beans.Comment;
import com.zero.hkdnews.beans.Reply;
import com.zero.hkdnews.util.L;
import com.zero.hkdnews.util.T;

import java.lang.ref.WeakReference;

import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 回复评论的界面
 * Created by 邓慧 on 15/5/8.
 */
public class CommentActivity extends BaseActivity {
    private static final String TAG = "CommentActivity";

    private TextView tv_author;
    private TextView tv_content;
    private EditText et_content;
    private Button mCommentPubBtn;

    private TextView mTitleBar;

    private Comment comment;
    private Reply reply;

    private static final int UPDATE_REPLY_OK_MSG = 0x01;
    private static final int ADD_REPLY_TO_COM_OK_MSG = 0x02;

    private WeakReferenceHander mHandler;

    static class WeakReferenceHander extends Handler {
        private final WeakReference<CommentActivity> mActivity;

        public WeakReferenceHander(CommentActivity activity) {
            mActivity = new WeakReference<CommentActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mActivity.get() != null) {
                mActivity.get().handleReceiveMessage(msg);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_pub);

        findView();
        initData();
        setClickListener();
    }

    private void setClickListener() {
        mCommentPubBtn.setOnClickListener(onClickListener);
    }

    private void findView() {
        tv_author = (TextView) findViewById(R.id.comment_pub_quote);
        tv_content = (TextView) findViewById(R.id.comment_pub_content);
        et_content = (EditText) findViewById(R.id.comment_pub_et_content);

        mCommentPubBtn = (Button) findViewById(R.id.comment_pub_btn);

        mTitleBar = (TextView) findViewById(R.id.news_header_title);
    }

    private void initData() {
        mTitleBar.setText("回复评论");
        mHandler = new WeakReferenceHander(this);

        comment = (Comment) getIntent().getBundleExtra("data").getSerializable("comment");
        tv_author.setText("回复："+ comment.getAuthor());
        tv_content.setText(comment.getContent());
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            updateReply();
        }
    };

    /**
     * 将回复更新至服务器，
     */
    private void updateReply(){
        reply = new Reply();
        reply.setAuthor("tt");
        reply.setContent(et_content.getText().toString());
        reply.setComment(comment);
        reply.save(getApplicationContext(), new SaveListener() {
            @Override
            public void onSuccess() {
                Message msg = Message.obtain();
                msg.what = UPDATE_REPLY_OK_MSG;
                mHandler.sendMessage(msg);
            }

            @Override
            public void onFailure(int i, String s) {
                T.showShort(getApplicationContext(), "回复失败！");
                L.d(s);
            }
        });
    }

    private void addReplyToComment(Reply reply){
        BmobRelation replies = new BmobRelation();
        replies.add(reply);
        comment.setReplies(replies);
        comment.update(getApplicationContext(), new UpdateListener() {
            @Override
            public void onSuccess() {
                Message msg = Message.obtain();
                msg.what = ADD_REPLY_TO_COM_OK_MSG;
                mHandler.sendMessage(msg);
            }

            @Override
            public void onFailure(int i, String s) {
                T.showShort(getApplicationContext(), "回复失败，网络异常！");
            }
        });
    }

    private void handleReceiveMessage(Message msg){
        if (msg.what == UPDATE_REPLY_OK_MSG) {
            addReplyToComment(reply);
        } else if (msg.what == ADD_REPLY_TO_COM_OK_MSG) {
            T.showShort(getApplicationContext(), "回复成功！");
            finish();
        }
    }
}
