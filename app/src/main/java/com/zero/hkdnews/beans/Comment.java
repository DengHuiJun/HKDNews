package com.zero.hkdnews.beans;

import java.util.ArrayList;

import cn.bmob.v3.BmobObject;

/**
 * 评论类实体
 * Created by luowei on 15/5/7.
 */
public class Comment extends BmobObject{

    //评论的新闻id
    private String newsId;

    //评论者昵称
    private String author;

    //评论者id
    private String authorId;

    //评论的内容
    private String content;

    //回复
    private ArrayList<Reply> replies;

    class Reply{
        String id;
        String name;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public String getNewsId() {
        return newsId;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public ArrayList<Reply> getReplies() {
        return replies;
    }

    public void setReplies(ArrayList<Reply> replies) {
        this.replies = replies;
    }
}
