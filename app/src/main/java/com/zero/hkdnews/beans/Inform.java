package com.zero.hkdnews.beans;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * 通知服务的消息表
 * Created by zero on 15/6/15.
 */
public class Inform extends BmobObject{

    private String title;
    private String content;
    private String author;

    private Group group;

    private BmobRelation users;

    public Inform(){}

    public Inform(String t,String c){
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public BmobRelation getUsers() {
        return users;
    }

    public void setUsers(BmobRelation users) {
        this.users = users;
    }
}
