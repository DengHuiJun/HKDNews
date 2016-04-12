package com.zero.hkdnews.myview;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zero.hkdnews.R;


/**
 * Created by 邓慧 on 16/4/12.
 */
public class TitleBar extends LinearLayout {

    private ImageView mRightView;
    private ImageView mBackView;
    private TextView mTitleView;

    public TitleBar(Context context){
        this(context, null);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        View view = LayoutInflater.from(context).inflate(R.layout.top_bar, this, true);

        mBackView = (ImageView) view.findViewById(R.id.tb_nav_back);
        mTitleView = (TextView) view.findViewById(R.id.tb_nav_title);
        mRightView = (ImageView) view.findViewById(R.id.tb_nav_right);

    }

    public void setBackClickListener(final Activity activity) {
        mBackView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
    }

    public void setTitleText(String text) {
        mTitleView.setText(text);
    }

    public void setRightClickListener(OnClickListener onClickListener) {
        mRightView.setOnClickListener(onClickListener);
    }

    public void isShowRight(boolean isShow) {
        if (isShow) {
            mRightView.setVisibility(VISIBLE);
        } else {
            mRightView.setVisibility(GONE);
        }
    }

    public void setRightImage(int resId) {
        mRightView.setImageResource(resId);
    }
}
