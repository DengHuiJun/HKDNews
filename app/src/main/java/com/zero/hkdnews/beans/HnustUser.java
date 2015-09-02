package com.zero.hkdnews.beans;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * 用户表
 * Created by zero on 15/4/16.
 */
public class HnustUser extends BmobUser {

    private String nickname;

    private BmobFile head;

    private String intro;

    private String location;

    private BmobRelation groups;

    private BmobRelation informs;

    public BmobFile getHead() {
        return head;
    }

    public void setHead(BmobFile head) {
        this.head = head;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public BmobRelation getGroups() {
        return groups;
    }

    public void setGroups(BmobRelation groups) {
        this.groups = groups;
    }

    public BmobRelation getInforms() {
        return informs;
    }

    public void setInforms(BmobRelation informs) {
        this.informs = informs;
    }
}
