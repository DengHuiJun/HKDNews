package com.zero.hkdnews.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.zero.hkdnews.R;
import com.zero.hkdnews.app.AppContext;
import com.zero.hkdnews.beans.HnustUser;
import com.zero.hkdnews.common.UIHelper;
import com.zero.hkdnews.util.T;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

/**
 * IDE自带生成的登录界面
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity implements LoaderCallbacks<Cursor>,View.OnClickListener {
    private static final String TAG = "LoginActivity";

    private static final String APP_KEY = "a1a712de02b00df3a5e8d9553d33f431";
    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    private ImageView mQQLoginIv;
    private ImageView mSinaLoginIv;

    private Button mEmailSignInButton;
    private Button mRegisterBtn;

    public static HnustUser infoUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bmob.initialize(this, APP_KEY); // 使用推送服务时的初始化操作
        BmobInstallation.getCurrentInstallation(this).save();
        BmobPush.startWork(this); // 启动推送服务

        setContentView(R.layout.activity_login);

        initData();
        findView();
        populateAutoComplete();
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mEmailSignInButton.setOnClickListener(this);
        mRegisterBtn.setOnClickListener(this);
        mQQLoginIv.setOnClickListener(this);
        mSinaLoginIv.setOnClickListener(this);
    }

    private void findView() {
        mPasswordView = (EditText) findViewById(R.id.password);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        mQQLoginIv = (ImageView) findViewById(R.id.login_qq_iv);
        mSinaLoginIv = (ImageView) findViewById(R.id.login_sina_iv);
        mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mRegisterBtn = (Button) findViewById(R.id.email_sign_up_button);
    }

    /**
     * 判断是否登录成功过，自动登录，进入主界面
     */
    public void initData() {
        infoUser = BmobUser.getCurrentUser(this,HnustUser.class);
        if(infoUser !=null){
            //将用户信息全局保留下来
            AppContext.setCurrentUserId(infoUser.getObjectId());
            AppContext.setUserName(infoUser.getNickname());
            AppContext.setIntro(infoUser.getIntro());
            if(infoUser.getHead() != null) {
                AppContext.setMyHead(infoUser.getHead());
            }

            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }

    private void populateAutoComplete() {
        getLoaderManager().initLoader(0, null, this);
    }

    /**
     *登录，带错误提示
     */
    public void attemptLogin() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString().trim();
        String password = mPasswordView.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
        //    mAuthTask = new UserLoginTask(email, password);
        //    mAuthTask.execute((Void) null);
            HnustUser user = new HnustUser();
            user.setUsername(email);
            user.setPassword(password);
            user.login(LoginActivity.this,new SaveListener() {
                @Override
                public void onSuccess() {
                    initData();
                }
                @Override
                public void onFailure(int i, String s) {
                    showProgress(false);
                    T.showShort(getApplicationContext(),s+"登录失败！");
                }
            });
        }

    }

    //验证用户名是否大于6个字符
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        if (email.length() >= 6  )
            return true;
        return false;
    }

    //验证密码是否大于4个字符
    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * 显示登录进度条，隐藏登录界面
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<String>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 登录
            case R.id.email_sign_in_button:
                attemptLogin();
                break;
            // 注册
            case R.id.email_sign_up_button:
                UIHelper.showRegister(LoginActivity.this);
                break;
            case R.id.login_qq_iv:
                T.showShortBar(mQQLoginIv, "暂未开通，敬请期待~");
                break;
            case R.id.login_sina_iv:
                T.showShortBar(mQQLoginIv, "暂未开通，敬请期待~");
                break;
            default:
                break;
        }
    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };
        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);
        mEmailView.setAdapter(adapter);
    }
}



