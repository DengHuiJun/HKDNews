package com.zero.hkdnews.app;

import android.app.Application;
import android.content.Context;

/**
 * Created by luowei on 15/5/4.
 */
public class AppContext extends Application {

    private static Context context;

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
}
