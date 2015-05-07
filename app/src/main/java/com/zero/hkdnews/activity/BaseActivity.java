package com.zero.hkdnews.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.zero.hkdnews.common.ActivityCollector;

import butterknife.ButterKnife;

/**
 * Created by luowei on 15/5/4.
 */
public class BaseActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("BaseActivity", getClass().getSimpleName());
        ActivityCollector.addActivity(this);
        //绑定注解
        ButterKnife.inject(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
        //销毁注解
        ButterKnife.reset(this);

    }
}
