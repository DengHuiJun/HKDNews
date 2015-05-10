package com.zero.hkdnews.app;

import android.app.Application;
import android.content.Context;

/**
 * Created by luowei on 15/5/4.
 */
public class AppContext extends Application {

    private static Context context;

    //当前用户登录的id
    public static String currentUserId;

    public static String userName;

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
}
