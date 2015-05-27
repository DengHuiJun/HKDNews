package com.zero.hkdnews.app;

import android.app.Application;
import android.content.Context;

import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by luowei on 15/5/4.
 */
public class AppContext extends Application {

    private static Context context;

    //当前用户登录的id
    public static String currentUserId;

    public static String userName;


    public static String intro;


    public static BmobFile myHead;

    @Override
    public void onCreate() {
        context = getApplicationContext();
    }

    /**
     * 获取全局context
     * @return
     */
    public static Context getContext(){
        return context;
    }

    public static String getCurrentUserId() {
        return currentUserId;
    }

    public static void setCurrentUserId(String currentUserId) {
        AppContext.currentUserId = currentUserId;
    }

    public static void setUserName(String userName) {
        AppContext.userName = userName;
    }

    public static String getUserName() {
        return userName;
    }

    public static String getIntro() {
        return intro;
    }

    public static void setIntro(String intro) {
        AppContext.intro = intro;
    }

    public static BmobFile getMyHead() {
        return myHead;
    }

    public static void setMyHead(BmobFile myHead) {
        AppContext.myHead = myHead;
    }
}
