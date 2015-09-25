package com.zero.hkdnews.beans;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by zero on 15/5/26.
 */
public class UploadNews extends BmobObject{
    private String author;
    private String content;
    private BmobFile head;
    private BmobFile photo;
    private Number love;

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setHead(BmobFile head) {
        this.head = head;
    }

    public BmobFile getHead() {
        return head;
    }

    public void setPhoto(BmobFile photo) {
        this.photo = photo;
    }

    public BmobFile getPhoto() {
        return photo;
    }

    public Number getLove() {
        return love;
    }

    public void setLove(Number love) {
        this.love = love;
    }
}
