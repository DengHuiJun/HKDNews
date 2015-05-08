package com.zero.hkdnews.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zero.hkdnews.R;
import com.zero.hkdnews.util.T;

/**
 * 发表评论
 * Created by luowei on 15/5/8.
 */
public class CommentActivity extends BaseActivity {

    private String commentId;
    private String author;
    private String content;

    private TextView tv_author;
    private TextView tv_content;
    private Button btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_pub);

        Bundle bundle = getIntent().getBundleExtra("data");
        author = bundle.getString("author");
        commentId = bundle.getString("id");
        content = bundle.getString("content");

        tv_author = (TextView) findViewById(R.id.comment_pub_quote);
        tv_content = (TextView) findViewById(R.id.comment_pub_content);
        btn = (Button) findViewById(R.id.comment_pub_head_btn);

        btn.setOnClickListener(onClickListener);


        initdata();
    }

    private void initdata() {

        tv_author.setText("回复："+ author);
        tv_content.setText(content);

    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            T.showShort(getApplicationContext(),"huifu");
            updateReply();
            finish();

        }
    };

    /**
     * 将回复跟新至服务器的线程，
     */
    private void updateReply(){

    }
}
