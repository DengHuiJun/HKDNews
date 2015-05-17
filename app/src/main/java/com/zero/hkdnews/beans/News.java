package com.zero.hkdnews.beans;

import java.io.File;
import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * 新闻消息实体类
 * Created by luowei on 15/4/13.
 */
public class News extends BmobObject implements Serializable{


    private String newsTitle;
    private String newsSource;
    private String newsTime;
    private String strUrl;
    private Number commentCount;
    private BmobFile newsImage;

    private BmobRelation comments;

    //新闻类型
    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public BmobRelation getComments() {
        return comments;
    }

    public void setComments(BmobRelation comments) {
        this.comments = comments;
    }

    public void setNewsImage(BmobFile newsImage) {
        this.newsImage = newsImage;
    }

    public BmobFile getNewsImage() {
        return newsImage;
    }

    public void setCommentCount(Number commentCount) {
        this.commentCount = commentCount;
    }

    public Number getCommentCount() {
        return commentCount;
    }


    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTime(String newsTime) {
        this.newsTime = newsTime;
    }

    public String getNewsTime() {
        return newsTime;
    }

    public void setNewsSource(String newsSource) {
        this.newsSource = newsSource;
    }

    public String getNewsSource() {
        return newsSource;
    }

    public void setStrUrl(String strUrl) {
        this.strUrl = strUrl;
    }

    public String getStrUrl() {
        return strUrl;
    }
}
