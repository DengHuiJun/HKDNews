package com.zero.hkdnews.beans;

/**
 * Created by luowei on 15/5/11.
 */
public class MeItem {
    private int resId;
    private String text;

    public MeItem(int id,String text){
        resId = id;
        this.text = text;
    }

    public int getResId() {
        return resId;
    }

    public String getText() {
        return text;
    }
}
