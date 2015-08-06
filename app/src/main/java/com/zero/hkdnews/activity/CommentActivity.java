package com.zero.hkdnews.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zero.hkdnews.R;
import com.zero.hkdnews.beans.Comment;
import com.zero.hkdnews.beans.Reply;
import com.zero.hkdnews.util.L;
import com.zero.hkdnews.util.T;

import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 回复评论的界面
 * Created by 邓慧 on 15/5/8.
 */
public class CommentActivity extends BaseActivity {

    private TextView tv_author;
    private TextView tv_content;
    private EditText et_content;
    private Button btn;

    private Comment comment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_pub);

        comment = (Comment) getIntent().getBundleExtra("data").getSerializable("comment");


        tv_author = (TextView) findViewById(R.id.comment_pub_quote);
        tv_content = (TextView) findViewById(R.id.comment_pub_content);
        et_content = (EditText) findViewById(R.id.comment_pub_et_content);
        btn = (Button) findViewById(R.id.comment_pub_head_btn);

        btn.setOnClickListener(onClickListener);


        initdata();
    }

    private void initdata() {

        tv_author.setText("回复："+ comment.getAuthor());
        tv_content.setText(comment.getContent());

    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            T.showShort(getApplicationContext(), "huifu");
            updateReply();
            finish();

        }
    };

    /**
     * 将回复更新至服务器的线程，
     */
    private void updateReply(){
        new Thread(){
            public void run(){
                Reply reply = new Reply();
                reply.setAuthor("tt");
                reply.setContent(et_content.getText().toString());
                reply.setComment(comment);
                reply.save(getApplicationContext(), new SaveListener() {
                    @Override
                    public void onSuccess() {
                        T.showShort(getApplicationContext(),"回复成功！");
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        L.d(s);
                    }
                });
                addReplyToComment(reply);

            }
        }.start();
    }


    private void addReplyToComment(Reply reply){

        BmobRelation replies = new BmobRelation();
        replies.add(reply);
        comment.setReplies(replies);
        comment.update(getApplicationContext(), new UpdateListener() {
            @Override
            public void onSuccess() {
                T.showShort(getApplicationContext(),"将回复加入评论中");
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }
}
