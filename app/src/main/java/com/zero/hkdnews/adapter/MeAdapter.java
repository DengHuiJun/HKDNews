package com.zero.hkdnews.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zero.hkdnews.R;
import com.zero.hkdnews.beans.MeItem;

import java.util.List;

/**
 * Created by luowei on 15/5/11.
 */
public class MeAdapter extends BaseAdapter {
    private Context context;
    private List<MeItem> list;
    private LayoutInflater layoutInflater;


    public MeAdapter(Context context,List<MeItem> list){
        this.context = context;
        this.list = list;
        layoutInflater = LayoutInflater.from(context);
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
        MeItem meItem = list.get(position);
        ViewHolder viewHolder = null;

        if(convertView == null){
           convertView =  layoutInflater.inflate(R.layout.me_list_view_item,parent,false);

            viewHolder = new ViewHolder();

            viewHolder.img = (ImageView) convertView.findViewById(R.id.me_list_view_img);
            viewHolder.text = (TextView) convertView.findViewById(R.id.me_list_view_text);
            convertView.setTag(viewHolder);


        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.img.setBackgroundResource(meItem.getResId());
        viewHolder.text.setText(meItem.getText());

        return convertView;
    }

    static class ViewHolder{
        ImageView img;
        TextView text;
    }
}
