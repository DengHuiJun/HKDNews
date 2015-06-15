package com.zero.hkdnews.beans;

/**
 * Created by luowei on 15/6/15.
 */
public class Informs {

    private String title;
    private String content;

    public Informs(){}

    public Informs(String t,String c){
        title =t;
        content =c;

    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
