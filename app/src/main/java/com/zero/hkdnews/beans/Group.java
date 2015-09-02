package com.zero.hkdnews.beans;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * 通知服务的分组信息表
 * Created by zero on 15/6/15.
 */
public class Group extends BmobObject{
    private String name;

    private BmobFile img;

    private String intro;

    private BmobRelation users;


    public Group(){}
    public Group(String name,String intro){
        this.name = name;
        this.intro = intro;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BmobFile getImg() {
        return img;
    }

    public void setImg(BmobFile img) {
        this.img = img;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public BmobRelation getUsers() {
        return users;
    }

    public void setUsers(BmobRelation users) {
        this.users = users;
    }
}
