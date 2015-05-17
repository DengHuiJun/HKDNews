package com.zero.hkdnews.beans;

import cn.bmob.v3.BmobObject;

/**
 * Created by luowei on 15/5/17.
 */
public class NewsBody extends BmobObject {
    private String newsId;
    private String body;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public String getNewsId() {
        return newsId;
    }
}
