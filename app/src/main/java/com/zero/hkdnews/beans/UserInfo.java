package com.zero.hkdnews.beans;

import java.util.ArrayList;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by luowei on 15/5/7.
 */
public class UserInfo extends BmobObject {
    private ArrayList<Comment> comments;

    private BmobFile MyImage;

    private String userId;

    private String intro;

    private String name;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public BmobFile getMyImage() {
        return MyImage;
    }

    public void setMyImage(BmobFile myImage) {
        MyImage = myImage;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
