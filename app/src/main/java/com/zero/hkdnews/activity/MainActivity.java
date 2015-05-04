package com.zero.hkdnews.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.zero.hkdnews.R;
import com.zero.hkdnews.beans.HnustUser;
import com.zero.hkdnews.common.ActivityCollector;
import com.zero.hkdnews.common.UIHelper;
import com.zero.hkdnews.fragment.HomeFragment;
import com.zero.hkdnews.fragment.MeFragment;
import com.zero.hkdnews.fragment.PlayFragment;
import com.zero.hkdnews.fragment.ShareFragment;

import cn.bmob.v3.BmobUser;


public class MainActivity extends ActionBarActivity implements View.OnClickListener{

    //首页新闻的fragment
    private HomeFragment homeFragment;

    //分享界面的fragment
    private ShareFragment shareFragment;

    //休闲吧界面的Fragment
    private PlayFragment playFragment;

    //我的资料界面的Fragment
    private MeFragment meFragment;

    //新闻布局
    private View homeLayout;

    //分享布局
    private View shareLayout;

    //休闲吧布局
    private View playLayout;

    //我的资料布局
    private View meLayout;

    private FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("BaseActivity", getClass().getSimpleName());
        ActivityCollector.addActivity(this);

        setContentView(R.layout.activity_main);



        initView();

        fragmentManager = getFragmentManager();
        //第一次选中首页
        setBottomSelection(0);

    }

    private void initView() {

        homeLayout = findViewById(R.id.bottom_home);
        shareLayout = findViewById(R.id.bottom_share);
        playLayout = findViewById(R.id.bottom_play);
        meLayout = findViewById(R.id.bottom_me);

        homeLayout.setOnClickListener(this);
        shareLayout.setOnClickListener(this);
        playLayout.setOnClickListener(this);
        meLayout.setOnClickListener(this);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.action_login:

                if(LoginActivity.infoUser == null) {
                    UIHelper.showLogin(this);
                    finish();
                }else{
                    Toast.makeText(this,"你已经登录！",Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.action_out:
                if(LoginActivity.infoUser != null){
                    HnustUser.logOut(this);
                    HnustUser temp = BmobUser.getCurrentUser(this,HnustUser.class);
                    if(temp == null){
                        UIHelper.showLogin(this);
                        finish();
                    }else{
                        Toast.makeText(this,"退出失败！",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this,"您未登录！",Toast.LENGTH_SHORT).show();
                }

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.bottom_home:
                setBottomSelection(0);
                break;
            case R.id.bottom_share:
                setBottomSelection(1);
                break;
            case R.id.bottom_play:
                setBottomSelection(2);
                break;
            case R.id.bottom_me:
                setBottomSelection(3);
                break;
            default:
                break;
        }

    }


    private void setBottomSelection(int index){
        clearSelection();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideAllFragment(transaction);

        switch (index){
            case 0:
                homeLayout.setBackgroundColor(getResources().getColor(R.color.custom_theme_dark));
                if(homeFragment == null){
                    homeFragment = new HomeFragment();
                    transaction.add(R.id.main_content,homeFragment);
                }else{
                    transaction.show(homeFragment);
                }
                break;
            case 1:
                shareLayout.setBackgroundColor(getResources().getColor(R.color.custom_theme_dark));

                if(shareFragment == null){
                    shareFragment = new ShareFragment();
                    transaction.add(R.id.main_content,shareFragment);
                }else{
                transaction.show(shareFragment);
            }
                break;
            case 2:
                playLayout.setBackgroundColor(getResources().getColor(R.color.custom_theme_dark));
                if(playFragment ==null ){
                    playFragment = new PlayFragment();
                    transaction.add(R.id.main_content,playFragment);

                }else{
                    transaction.show(playFragment);
                }
                break;


            case 3:
            default:
                meLayout.setBackgroundColor(getResources().getColor(R.color.custom_theme_dark));
                if(meFragment == null){
                    meFragment = new MeFragment();
                    transaction.add(R.id.main_content,meFragment);
                }else{
                    transaction.show(meFragment);
                }
                break;
        }
        transaction.commit();

    }

    /**
     * 清除当前选中状态，将颜色设置为浅色
     */
    private void clearSelection(){
        homeLayout.setBackgroundColor(getResources().getColor(R.color.custom_theme_darker));
        shareLayout.setBackgroundColor(getResources().getColor(R.color.custom_theme_darker));
        meLayout.setBackgroundColor(getResources().getColor(R.color.custom_theme_darker));
        playLayout.setBackgroundColor(getResources().getColor(R.color.custom_theme_darker));

    }

    /**
     * 隐藏所有的Fragment
     */
    private void hideAllFragment(FragmentTransaction transaction){
        if(homeFragment != null){
            transaction.hide(homeFragment);
        }
        if(meFragment != null){
            transaction.hide(meFragment);
        }
        if(playFragment != null){
            transaction.hide(playFragment);
        }
        if(shareFragment != null){
            transaction.hide(shareFragment);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
