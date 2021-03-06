package com.zero.hkdnews.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zero.hkdnews.R;
import com.zero.hkdnews.beans.HnustUser;
import com.zero.hkdnews.common.ActivityCollector;
import com.zero.hkdnews.common.UIHelper;
import com.zero.hkdnews.fragment.LeftMenuFragment;
import com.zero.hkdnews.news.HomePagerFragment;
import com.zero.hkdnews.groupmsg.InformFragment;
import com.zero.hkdnews.fragment.MainFragment;
import com.zero.hkdnews.share.ShareFragment;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;

/**
 * 主界面
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    private HomePagerFragment homePagerFragment;    //首页新闻的fragment,嵌入了一个viewpager
    private ShareFragment mShareFragment;     //分享界面的fragment
    private InformFragment mInformFragment; //通知服务界面的Fragment
    private MainFragment mMainFragment;  //我的资料界面的Fragment

    //新闻布局
    private View mBottomHomeRl;
    private ImageView homeImg;
    private TextView mHomeTv;

    //分享布局
    private View mBottomShareRL;
    private ImageView shareImg;
    private TextView mShareTv;

    //通知服务布局
    private View mBottomInformRl;
    private ImageView playImg;
    private TextView mPlayTv;

    //我的资料布局->改为main
    private View mBottomInMeRl;
    private ImageView meImg;
    private TextView mMeTv;

    private FragmentManager fragmentManager;
    private String mCurrentFragmentTag = "";
    private static final String TAG_SHARE_TAG = "mShareFragment";
    private static final String TAG_HOME_TAG = "HomePagerFragment";
    private static final String TAG_PLAY_TAG = "mInformFragment";
    private static final String TAG_ME_TAG = "mMainFragment";

    private int mCurrentFragmentId; // 用来判断动画效果

    private LeftMenuFragment mLeftMenuFragment;
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolBar;
    private ActionBarDrawerToggle mActionBarDrawerToggle;

    //标题
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityCollector.addActivity(this);
        setContentView(R.layout.activity_main);

        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        mToolBar.setTitle("c测试");
        mToolBar.setNavigationIcon(R.drawable.ic_drawer);

        fragmentManager = getSupportFragmentManager();

        initNav();
        initView();

        //第一次选中首页
        setBottomSelection(3);
        mCurrentFragmentTag = TAG_HOME_TAG;
    }

    /**
     * 初始化左侧滑块菜单
     */
    private void initNav() {

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolBar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        mActionBarDrawerToggle.syncState();

        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);


        mLeftMenuFragment = (LeftMenuFragment) fragmentManager.findFragmentById(R.id.left_navigation_drawer);

        mLeftMenuFragment.setOnMenuItemSelectedListener(new LeftMenuFragment.OnMenuItemSelectedListener() {
            @Override
            public void menuItemSelected(int position) {
                itemSelected(position);
            }
        });
    }

    public void itemSelected(int position) {
        switch (position){
            //home
            case 0:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                setBottomSelection(3);
                break;
            //登录功能
            case 1:
                if(LoginActivity.infoUser == null) {
                    UIHelper.showLogin(this);
                    finish();
                } else {
                    Toast.makeText(this, "你已经登录！", Toast.LENGTH_SHORT).show();
                }
                break;
            //注销功能
            case 2:
                if(LoginActivity.infoUser != null) {
                    HnustUser.logOut(this);
                    HnustUser temp = BmobUser.getCurrentUser(this, HnustUser.class);
                    if(temp == null) {
                        UIHelper.showLogin(this);
                        finish();
                    } else {
                        Toast.makeText(this,"退出失败！",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this,"您未登录！",Toast.LENGTH_SHORT).show();
                }
                break;
            //定位
            case 3:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                UIHelper.showLocation(this);
                break;

            //一键清除缓存
            case 4:
                BmobQuery.clearAllCachedResults(this);
                break;

            case 5:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                UIHelper.toAnActivity(this, SettingActivity.class);
                break;

            default:
                if (mDrawerLayout.isDrawerOpen(GravityCompat.START))
                    mDrawerLayout.closeDrawer(GravityCompat.START);
        }
    }

//    /**
//     * 点击左上角恢复原来的ActionBar
//     */
//    public void restoreActionBar() {
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
//        actionBar.setDisplayShowTitleEnabled(true);
//        actionBar.setTitle(mTitle);
//    }

    private void initView() {
        mBottomHomeRl = findViewById(R.id.bottom_home_rl);
        mBottomShareRL = findViewById(R.id.bottom_share_rl);
        mBottomInformRl = findViewById(R.id.bottom_inform_rl);
        mBottomInMeRl = findViewById(R.id.bottom_me_rl);

        homeImg = (ImageView) findViewById(R.id.home_image);
        shareImg = (ImageView) findViewById(R.id.share_image);
        playImg = (ImageView) findViewById(R.id.play_image);
        meImg = (ImageView) findViewById(R.id.me_image);

        mHomeTv = (TextView) findViewById(R.id.home_text);
        mShareTv = (TextView) findViewById(R.id.share_text);
        mPlayTv = (TextView) findViewById(R.id.play_text);
        mMeTv = (TextView) findViewById(R.id.me_text);

        mBottomHomeRl.setOnClickListener(this);
        mBottomShareRL.setOnClickListener(this);
        mBottomInformRl.setOnClickListener(this);
        mBottomInMeRl.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

            getMenuInflater().inflate(R.menu.main_activity2, menu);
//            restoreActionBar();
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

        switch (v.getId()) {
            case R.id.bottom_home_rl:
                setBottomSelection(0);
                mCurrentFragmentTag = TAG_HOME_TAG;
                break;
            case R.id.bottom_share_rl:
                setBottomSelection(1);
                mCurrentFragmentTag = TAG_SHARE_TAG;
                break;
            case R.id.bottom_inform_rl:
                setBottomSelection(2);
                mCurrentFragmentTag = TAG_PLAY_TAG;
                break;
            case R.id.bottom_me_rl:
                setBottomSelection(3);
                mCurrentFragmentTag = TAG_ME_TAG;
                break;
            default:
                break;
        }
    }

    /**
     *为了调用子Fragment中的onActivityResult
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (mCurrentFragmentTag) {
            case TAG_HOME_TAG:
                homePagerFragment.onActivityResult(requestCode,resultCode,data);
                break;
            case TAG_SHARE_TAG:
                mShareFragment.onActivityResult(requestCode,resultCode,data);
                break;
            case TAG_PLAY_TAG:
                mInformFragment.onActivityResult(requestCode,resultCode,data);
                break;
            case TAG_ME_TAG:
                mMainFragment.onActivityResult(requestCode,resultCode,data);
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
//        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        switch (index){
            case 0:
                setTransactionAnimation(transaction, 0);
                homeImg.setImageResource(R.mipmap.main_bottom_home_select);
                mHomeTv.setTextColor(getResources().getColor(R.color.select_font));
                if(homePagerFragment == null) {
                    homePagerFragment = new HomePagerFragment();
                    transaction.add(R.id.main_content,homePagerFragment);
                } else {
                    transaction.show(homePagerFragment);
                }
                mCurrentFragmentId = 0;
                break;
            case 1:
                setTransactionAnimation(transaction, 1);
                shareImg.setImageResource(R.mipmap.main_bottom_share_select);
                mShareTv.setTextColor(getResources().getColor(R.color.select_font));
                if(mShareFragment == null) {
                    mShareFragment = new ShareFragment();
                    transaction.add(R.id.main_content,mShareFragment);
                } else {
                    transaction.show(mShareFragment);
                }
                mCurrentFragmentId = 1;
                break;
            case 2:
                setTransactionAnimation(transaction, 2);
                playImg.setImageResource(R.mipmap.main_bottom_inform_select);
                mPlayTv.setTextColor(getResources().getColor(R.color.select_font));
                if(mInformFragment == null) {
                    mInformFragment = new InformFragment();
                    transaction.add(R.id.main_content,mInformFragment);

                } else {
                    transaction.show(mInformFragment);
                }
                mCurrentFragmentId = 2;
                break;
            case 3:
            default:
                setTransactionAnimation(transaction, 3);
                meImg.setImageResource(R.mipmap.main_bottom_me_select);
                mMeTv.setTextColor(getResources().getColor(R.color.select_font));
                if(mMainFragment == null) {
                    mMainFragment = new MainFragment();
                    transaction.add(R.id.main_content,mMainFragment);
                } else {
                    transaction.show(mMainFragment);
                }
                mCurrentFragmentId = 3;
                break;
        }
        transaction.commit();
    }

    /**
     * 设置转场动画
     * @param transaction
     * @param nextId
     */
    private void setTransactionAnimation(FragmentTransaction transaction,int nextId) {
//        if (nextId > mCurrentFragmentId) {
//            transaction.setCustomAnimations(
//                    R.anim.fragment_slide_left_enter,
//                    R.anim.fragment_slide_right_exit);
//        } else {
//            transaction.setCustomAnimations(
//                    R.anim.fragment_slide_right_enter,
//                    R.anim.fragment_slide_left_exit);
//        }
    }

    /**
     * 清除底部当前选中状态，将颜色设置为浅色
     */
    private void clearSelection(){

        homeImg.setImageResource(R.mipmap.main_bottom_home);
        shareImg.setImageResource(R.mipmap.main_bottom_share);
        playImg.setImageResource(R.mipmap.main_bottom_inform);
        meImg.setImageResource(R.mipmap.main_bottom_me);
        mHomeTv.setTextColor(getResources().getColor(R.color.font_gray));
        mShareTv.setTextColor(getResources().getColor(R.color.font_gray));
        mPlayTv.setTextColor(getResources().getColor(R.color.font_gray));
        mMeTv.setTextColor(getResources().getColor(R.color.font_gray));
    }

    /**
     * 隐藏主页所有的Fragment
     */
    private void hideAllFragment(FragmentTransaction transaction) {
        if(homePagerFragment != null) {
            transaction.hide(homePagerFragment);
        }
        if(mMainFragment != null) {
            transaction.hide(mMainFragment);
        }
        if(mInformFragment != null) {
            transaction.hide(mInformFragment);
        }
        if(mShareFragment != null) {
            transaction.hide(mShareFragment);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}