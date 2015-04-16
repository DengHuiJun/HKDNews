package com.zero.hkdnews.beans;

import java.io.File;

import cn.bmob.v3.BmobObject;

/**
 * Created by luowei on 15/4/13.
 */
public class News extends BmobObject{
    private int newsID;
    private String newsTitle;
    private String newsSource;
    private String newsTime;
    private String strUrl;
    private Number commentCount;
    private String body;


    private File newsImage;


    public void setBody(String body) {
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public void setCommentCount(Number commentCount) {
        this.commentCount = commentCount;
    }

    public Number getCommentCount() {
        return commentCount;
    }

    public void setNewsImage(File newsImage) {
        this.newsImage = newsImage;
    }

    public File getNewsImage() {
        return newsImage;
    }

    public void setNewsID(int newsID) {
        this.newsID = newsID;
    }

    public int getNewsID() {
        return newsID;
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
