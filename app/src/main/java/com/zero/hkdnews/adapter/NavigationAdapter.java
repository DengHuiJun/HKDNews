package com.zero.hkdnews.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zero.hkdnews.R;
import com.zero.hkdnews.beans.LeftMenu;

import java.util.List;

/**
 * 左侧滑块
 * Created by 邓慧 on 15/6/26.
 */
public class NavigationAdapter extends BaseAdapter {

    private List<LeftMenu> list;

    private Context context;
    private LayoutInflater inflater;

    public NavigationAdapter(List<LeftMenu> list,Context context){

        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);

    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LeftMenu item = list.get(position);
        ViewHolder viewHolder = null;

        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.left_menu_item,parent,false);

            viewHolder.name = (TextView) convertView.findViewById(R.id.left_menu_item_name);
            viewHolder.img = (ImageView) convertView.findViewById(R.id.left_menu_item_img);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.name.setText(item.getName());
        viewHolder.img.setImageResource(item.getResourceId());

        return convertView;
    }


    class ViewHolder{
        TextView name;
        ImageView img;
    }
}
