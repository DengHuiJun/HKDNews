package com.zero.hkdnews.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.zero.hkdnews.common.ActivityCollector;

import butterknife.ButterKnife;

/**
 * Activity的基类
 * Created by 邓慧 on 15/5/4.
 */
public class BaseActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("BaseActivity", getClass().getSimpleName());
        ActivityCollector.addActivity(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);

    }
}
