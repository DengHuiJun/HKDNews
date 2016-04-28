package com.zero.hkdnews.groupmsg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zero.hkdnews.R;
import com.zero.hkdnews.beans.HnustUser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by denghui on 16/4/27.
 */
public class InformCheckAdapter extends BaseAdapter {

    private List<HnustUser> mUsers; // 查看过的
    private List<HnustUser> mGroupUsers;
    private Context mContext;
    public static Map<Integer, Boolean> isSelected;

    public InformCheckAdapter(Context context, List<HnustUser> users, List<HnustUser> groupUsers) {
        mContext = context;
        mUsers = users;
        mGroupUsers = groupUsers;

        initSelectMap();
    }

    private void initSelectMap() {

        int length = mGroupUsers.size();

        isSelected = new HashMap<>(length);

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < mUsers.size(); j++) {
                if (mGroupUsers.get(i).getObjectId().equals(mUsers.get(j).getObjectId())) {
                    isSelected.put(i, true);
                    break;
                } else {
                    isSelected.put(i, false);
                }
            }
        }
    }


    @Override
    public int getCount() {
        return mGroupUsers.size();
    }

    @Override
    public Object getItem(int i) {
        return mGroupUsers.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        HnustUser user = mGroupUsers.get(i);

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.member_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.headIv = (ImageView) view.findViewById(R.id.member_item_img);
            viewHolder.nameTv = (TextView) view.findViewById(R.id.member_item_name);
            viewHolder.introTv = (TextView) view.findViewById(R.id.member_item_intro);
            viewHolder.checkBox = (CheckBox) view.findViewById(R.id.member_item_check);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        if (user.getHead() != null) {
            Picasso.with(mContext).load(user.getHead().getFileUrl(mContext)).into(viewHolder.headIv);
        } else {
            Picasso.with(mContext).load(R.mipmap.default_me).into(viewHolder.headIv);
        }

        viewHolder.nameTv.setText(user.getNickname());
        viewHolder.introTv.setText(user.getIntro());

        viewHolder.checkBox.setVisibility(View.VISIBLE);
        viewHolder.checkBox.setChecked(isSelected.get(i));

        return view;
    }


    class ViewHolder {
        ImageView headIv;
        TextView nameTv;
        TextView introTv;
        CheckBox checkBox;
    }
}
