package com.zero.hkdnews.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zero.hkdnews.R;
import com.zero.hkdnews.beans.UploadNews;

import java.util.List;

/**
 * Created by luowei on 15/5/26.
 */
public class ShareAdapter extends BaseAdapter {
    private List<UploadNews> datalist;
    private LayoutInflater inflater;
    private Context context;

    public ShareAdapter(List<UploadNews> list,Context context){
        datalist = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setDatalist(List<UploadNews> datalist) {
        this.datalist = datalist;
    }

    public List<UploadNews> getDatalist() {
        return datalist;
    }

    @Override
    public int getCount() {
        return datalist.size();
    }

    @Override
    public Object getItem(int position) {
        return datalist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UploadNews data = datalist.get(position);
        ViewHolder viewHolder = null;
        if (viewHolder == null){
            convertView = inflater.inflate(R.layout.share_list_view_item,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.author = (TextView) convertView.findViewById(R.id.share_list_view_item_name);
            viewHolder.head = (ImageView) convertView.findViewById(R.id.share_list_view_item_me);
            viewHolder.time = (TextView) convertView.findViewById(R.id.share_list_view_item_time);
            viewHolder.content = (TextView) convertView.findViewById(R.id.share_list_view_item_content);
            viewHolder.photo = (ImageView) convertView.findViewById(R.id.share_list_view_item_img);

            convertView.setTag(viewHolder);

        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.author.setText(data.getAuthor());
        viewHolder.time.setText(data.getUpdatedAt());
        viewHolder.content.setText(data.getContent());

        if(data.getHead().getUrl()== null){
            viewHolder.head.setImageResource(R.mipmap.ic_launcher);
        }else {
            Picasso.with(context).load(data.getHead().getFileUrl(context)).into(viewHolder.head);
        }

        if(data.getPhoto().getUrl()== null){
            viewHolder.photo.setImageResource(R.mipmap.ic_launcher);
        }else {
            Picasso.with(context).load(data.getPhoto().getFileUrl(context)).into(viewHolder.photo);
        }

        return convertView;
    }

    static class ViewHolder{
        ImageView head;
        TextView author;
        TextView time;
        TextView content;
        ImageView photo;
    }
}
