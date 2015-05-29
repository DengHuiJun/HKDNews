package com.zero.hkdnews.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zero.hkdnews.activity.CommentActivity;
import com.zero.hkdnews.activity.LocationActivity;
import com.zero.hkdnews.activity.LoginActivity;
import com.zero.hkdnews.activity.MainActivity;
import com.zero.hkdnews.activity.NewsActivity;
import com.zero.hkdnews.activity.RegisterActivity;

/**
 * Created by luowei on 15/4/16.
 */
public class UIHelper {

    /**
     * 跳转到新闻详情界面
     * @param context
     * @param bundle
     */
    public static void showNewsDetail(Context context,Bundle bundle){
        Intent intent = new Intent(context, NewsActivity.class);
        intent.putExtra("data",bundle);
        context.startActivity(intent);
    }

    /**
     * 跳转到主界面
     * @param context
     */
    public static void showHome(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }


    /**
     * 跳转至登录界面
     * @param context
     */
    public static void showLogin(Context context){
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    /**
     * 跳转至注册界面
     * @param context
     */
    public static void showRegister(Context context){
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }

    /**
     * 跳转至定位界面
     * @param context
     */
    public static void showLocation(Context context){
        Intent intent = new Intent(context, LocationActivity.class);
        context.startActivity(intent);
    }

    /**
     * 跳转至评论回复
     * @param context
     */
    public static void showCommentPub(Context context,Bundle bundle){
        Intent intent = new Intent(context, CommentActivity.class);
        intent.putExtra("data",bundle);
        context.startActivity(intent);
    }

    public static WebViewClient getWebViewClient(){
        return new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return  true;
            }
        };
    }



}
