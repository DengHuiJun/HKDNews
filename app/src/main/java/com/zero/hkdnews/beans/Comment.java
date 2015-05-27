package com.zero.hkdnews.beans;

import java.io.Serializable;
import java.util.ArrayList;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * 评论类实体
 * Created by luowei on 15/5/7.
 */
public class Comment extends BmobObject implements Serializable{

    //评论的新闻id
    private String newsId;

    //评论者昵称
    private String author;

    //评论者id
    private String authorId;

    //评论的内容
    private String content;

    //对应的新闻
    private News news;

    private BmobRelation replies;


    private BmobFile face;

    public BmobFile getFace() {
        return face;
    }

    public void setFace(BmobFile face) {
        this.face = face;
    }

    public void setReplies(BmobRelation replies) {
        this.replies = replies;
    }

    public BmobRelation getReplies() {
        return replies;
    }

    public void setNews(News news) {
        this.news = news;
    }

    public News getNews() {
        return news;
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


}
