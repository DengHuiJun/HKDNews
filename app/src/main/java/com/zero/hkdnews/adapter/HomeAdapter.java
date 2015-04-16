package com.zero.hkdnews.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zero.hkdnews.R;
import com.zero.hkdnews.beans.News;

import java.util.List;

/**
 * Created by luowei on 15/4/13.
 */
public class HomeAdapter extends BaseAdapter{
    private List<News> dataList;
    private LayoutInflater layoutInflater;
    private Context context;


    public HomeAdapter(List<News> list,Context context){
        dataList = list;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);

    }

    public void setDataList(List<News> dataList) {
        this.dataList = dataList;
    }

    public List<News> getDataList() {
        return dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        News data = dataList.get(position);
        ViewHolder viewHolder = null;
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.home_list_view_item2,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.newsTitle = (TextView) convertView.findViewById(R.id.home_news_title);
            viewHolder.newsTime = (TextView) convertView.findViewById(R.id.home_news_time);
            viewHolder.newsSource = (TextView) convertView.findViewById(R.id.home_news_source);
            viewHolder.newsImage = (ImageView) convertView.findViewById(R.id.home_news_image);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.newsTitle.setText(data.getNewsTitle());
        viewHolder.newsTime.setText(data.getNewsTime());
        viewHolder.newsSource.setText(data.getNewsSource());
        viewHolder.newsImage.setBackgroundColor(context.getResources().getColor(R.color.custom_theme_primary));


        return convertView;
    }

    class ViewHolder{
        TextView newsTitle;
        TextView newsTime;
        TextView newsSource;
        ImageView newsImage;
    }
}
