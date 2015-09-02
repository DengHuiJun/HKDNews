package com.zero.hkdnews.beans;

import cn.bmob.v3.BmobObject;

/**
 * 收藏新闻的表
 * Created by zero on 15-9-2.
 */
public class CollectNews extends BmobObject {

    private String userId;
    private String newsId;
    private String newsSource;
    private String newsTitle;
    private Number code;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getNewsSource() {
        return newsSource;
    }

    public void setNewsSource(String newsSource) {
        this.newsSource = newsSource;
    }

    public Number getCode() {
        return code;
    }

    public void setCode(Number code) {
        this.code = code;
    }
}
