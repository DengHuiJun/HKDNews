package com.zero.hkdnews.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.zero.hkdnews.R;
import com.zero.hkdnews.beans.HnustUser;
import com.zero.hkdnews.common.ActivityCollector;
import com.zero.hkdnews.common.UIHelper;
import com.zero.hkdnews.fragment.HomePagerFragment;
import com.zero.hkdnews.fragment.MeFragment;
import com.zero.hkdnews.fragment.PlayFragment;
import com.zero.hkdnews.fragment.ShareFragment;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;

/**
 * 主界面
 */

public class MainActivity extends ActionBarActivity implements View.OnClickListener,NavigationDrawerFragment.NavigationDrawerCallbacks {

    //首页新闻的fragment,嵌入了一个viewpager
    private HomePagerFragment homePagerFragment;

    //分享界面的fragment
    private ShareFragment shareFragment;

    //通知服务界面的Fragment
    private PlayFragment playFragment;

    //我的资料界面的Fragment
    private MeFragment meFragment;

    //新闻布局
    private View homeLayout;
    private ImageView homeImg;

    //分享布局
    private View shareLayout;
    private ImageView shareImg;

    //通知服务布局
    private View playLayout;
    private ImageView playImg;

    //我的资料布局
    private View meLayout;
    private ImageView meImg;

    private FragmentManager fragmentManager;


    /**
     * 左侧滑块
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    //标题
    private CharSequence mTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("BaseActivity", getClass().getSimpleName());
        ActivityCollector.addActivity(this);

        setContentView(R.layout.activity_main);

        initNav();

        initView();

        fragmentManager = getSupportFragmentManager();
        //第一次选中首页
        setBottomSelection(0);

    }

    /**
     * 初始化左侧滑块菜单
     */
    private void initNav() {
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    /**
     * 处理右侧滑菜单的选择
     * @param position
     */
    @Override
    public void onNavigationDrawerItemSelected(int position) {

        switch (position){

            //定位功能
            case 0:

                break;

            //登录功能
            case 1:
                if(LoginActivity.infoUser == null) {
                    UIHelper.showLogin(this);
                    finish();
                }else{
                    Toast.makeText(this, "你已经登录！", Toast.LENGTH_SHORT).show();
                }
                break;

            //注销功能
            case 2:
                if(LoginActivity.infoUser != null){
                    HnustUser.logOut(this);
                    HnustUser temp = BmobUser.getCurrentUser(this, HnustUser.class);
                    if(temp == null){
                        UIHelper.showLogin(this);
                        finish();
                    }else{
                        Toast.makeText(this,"退出失败！",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this,"您未登录！",Toast.LENGTH_SHORT).show();
                }
                break;

            //定位
            case 3:
                UIHelper.showLocation(this);
                break;

            //一键清除缓存
            case 4:
                BmobQuery.clearAllCachedResults(this);
                break;
        }
    }

    /**
     * 根据选项，改变ActionBar的标题
     * @param number
     */
    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    /**
     * 点击左上角恢复原来的ActionBar
     */
    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    private void initView() {

        homeLayout = findViewById(R.id.bottom_home);
        shareLayout = findViewById(R.id.bottom_share);
        playLayout = findViewById(R.id.bottom_play);
        meLayout = findViewById(R.id.bottom_me);

        homeImg = (ImageView) findViewById(R.id.home_image);
        shareImg = (ImageView) findViewById(R.id.share_image);
        playImg = (ImageView) findViewById(R.id.play_image);
        meImg = (ImageView) findViewById(R.id.me_image);


        homeLayout.setOnClickListener(this);
        shareLayout.setOnClickListener(this);
        playLayout.setOnClickListener(this);
        meLayout.setOnClickListener(this);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       // getMenuInflater().inflate(R.menu.menu_main, menu);
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main_activity2, menu);
            restoreActionBar();
            return true;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
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


    /**
     * 选择底部功能
     * @param index
     */
    private void setBottomSelection(int index){
        clearSelection();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideAllFragment(transaction);

        switch (index){
            case 0:
             //   homeLayout.setBackgroundColor(getResources().getColor(R.color.custom_theme_dark));
                homeImg.setImageResource(R.mipmap.main_bottom_home_select);
                if(homePagerFragment == null){
                    homePagerFragment = new HomePagerFragment();
                    transaction.add(R.id.main_content,homePagerFragment);
                }else{
                    transaction.show(homePagerFragment);
                }
                break;
            case 1:
          //      shareLayout.setBackgroundColor(getResources().getColor(R.color.custom_theme_dark));
                shareImg.setImageResource(R.mipmap.main_bottom_share_select);
                if(shareFragment == null){
                    shareFragment = new ShareFragment();
                    transaction.add(R.id.main_content,shareFragment);
                }else{
                    transaction.show(shareFragment);
                }
                break;
            case 2:
            //    playLayout.setBackgroundColor(getResources().getColor(R.color.custom_theme_dark));
                playImg.setImageResource(R.mipmap.main_bottom_inform_select);
                if(playFragment ==null ){
                    playFragment = new PlayFragment();
                    transaction.add(R.id.main_content,playFragment);

                }else{
                    transaction.show(playFragment);
                }
                break;


            case 3:
            default:
                //meLayout.setBackgroundColor(getResources().getColor(R.color.custom_theme_dark));
               meImg.setImageResource(R.mipmap.main_bottom_me_select);
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
     * 清除底部当前选中状态，将颜色设置为浅色
     */
    private void clearSelection(){
        /*
        homeLayout.setBackgroundColor(getResources().getColor(R.color.custom_theme_darker));
        shareLayout.setBackgroundColor(getResources().getColor(R.color.custom_theme_darker));
        meLayout.setBackgroundColor(getResources().getColor(R.color.custom_theme_darker));
        playLayout.setBackgroundColor(getResources().getColor(R.color.custom_theme_darker));
        */

        homeImg.setImageResource(R.mipmap.main_bottom_home);
        shareImg.setImageResource(R.mipmap.main_bottom_share);
        playImg.setImageResource(R.mipmap.main_bottom_inform);
        meImg.setImageResource(R.mipmap.main_bottom_me);
    }

    /**
     * 隐藏主页所有的Fragment
     */
    private void hideAllFragment(FragmentTransaction transaction){
        if(homePagerFragment != null){
            transaction.hide(homePagerFragment);
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