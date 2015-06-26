package com.zero.hkdnews.beans;

/**
 * Created by 邓慧 on 15/6/26.
 */
public class LeftMenu {
    private String name;
    private int resourceId;

    public LeftMenu(String name,int resourceId){
        this.name =name;
        this.resourceId =resourceId;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public int getResourceId() {
        return resourceId;
    }
}
