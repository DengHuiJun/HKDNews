package com.zero.hkdnews.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.zero.hkdnews.R;
import com.zero.hkdnews.beans.HnustUser;
import com.zero.hkdnews.common.UIHelper;
import com.zero.hkdnews.util.T;

import android.os.Handler;

import java.io.File;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by zero on 15/5/6.
 */
public class RegisterActivity extends BaseActivity{

    private EditText mAccout;
    private EditText mPwd;
    private EditText mRePwd;
    private EditText mEmail;
    private Button   btn;
    private View mProgressView;
    private View mRegisterView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toRegister();
            }
        });
    }

    private void toRegister() {
      final  String name = mAccout.getText().toString();
      final  String pwd = mPwd.getText().toString();
      final  String repwd = mRePwd.getText().toString();
      final  String email = mEmail.getText().toString();

        boolean cancel = false;
        if( TextUtils.isEmpty(name) || TextUtils.isEmpty(pwd) || TextUtils.isEmpty(repwd)){
            T.showShort(this,"有一项为空！");
            cancel = true;
        }else if(!pwd.equals(repwd)){
            T.showShort(this,"密码输入不一致！");
            cancel = true;
        }else if(!email.contains("@")){
            T.showShort(this,"邮箱填写不正确！");
        }

        if(cancel){

        }else{
            showProgress(true);
            HnustUser user = new HnustUser();
            user.setUsername(name);
            user.setPassword(pwd);
            user.setEmail(email);

            user.setNickname(name);
            user.setIntro("什么也没有");
            user.signUp(RegisterActivity.this, new SaveListener() {
                @Override
                public void onSuccess() {
                    T.showShort(RegisterActivity.this,"注册成功！");
                    UIHelper.showLogin(RegisterActivity.this);
                    finish();
                }

                @Override
                public void onFailure(int i, String s) {
                    showProgress(false);
                    T.showShort(RegisterActivity.this,s);
                }
            });

        }

    }

    private void initView() {
        mAccout = (EditText) findViewById(R.id.register_username);
        mPwd = (EditText) findViewById(R.id.register_password);
        mRePwd = (EditText) findViewById(R.id.register_re_password);
        mEmail = (EditText) findViewById(R.id.register_email);
        btn = (Button) findViewById(R.id.register_ok_btn);

        mProgressView = findViewById(R.id.register_progress);
        mRegisterView = findViewById(R.id.register_form);
    }

    /**
     * 显示进度条
     * @param show
     */
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mRegisterView.setVisibility(show ? View.GONE : View.VISIBLE);
            mRegisterView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mRegisterView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mRegisterView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

}
