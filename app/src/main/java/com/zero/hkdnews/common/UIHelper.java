package com.zero.hkdnews.common;

import android.content.Context;
import android.content.Intent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zero.hkdnews.ui.MainActivity;
import com.zero.hkdnews.ui.NewsActivity;

/**
 * Created by luowei on 15/4/16.
 */
public class UIHelper {
    public static void showNewsDetail(Context context,String newsId){
        Intent intent = new Intent(context, NewsActivity.class);
        intent.putExtra("news_id",newsId);
        context.startActivity(intent);
    }


    public static void showHome(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    public static void addWebImageShow(final Context c,WebView wv){
        wv.getSettings().setJavaScriptEnabled(true);
        wv.addJavascriptInterface(new OnWebViewImageListener() {
            @Override
            public void onImageClick(String bigImageUrl) {
                if(bigImageUrl != null){

                }
            }
        },"mWebImageListener");
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
