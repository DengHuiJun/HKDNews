package com.zero.hkdnews.beans;

import cn.bmob.v3.BmobObject;

/**
 * 回复评论实体类
 * Created by zero on 15/5/10.
 */
public class Reply extends BmobObject {
    private String content;
    private String author;

    private Comment comment;

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }
}
