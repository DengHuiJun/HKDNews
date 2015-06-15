package com.zero.hkdnews.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zero.hkdnews.R;
import com.zero.hkdnews.beans.Informs;

import java.util.List;

/**
 * Created by luowei on 15/6/15.
 */
public class PlayAdapter extends BaseAdapter{
    private List<Informs> list;
    private Context context;
    private LayoutInflater layoutInflater;

    public PlayAdapter(List<Informs> list,Context context){
        this.list = list;
        this.context = context;
        layoutInflater = LayoutInflater.from(this.context);

    }

    public List<Informs> getList() {
        return list;
    }

    public void setList(List<Informs> list) {
        this.list = list;
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
        Informs inform = list.get(position);
        ViewHolder viewHolder = null;
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.play_list_view_item,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.play_item_title);
            viewHolder.content = (TextView) convertView.findViewById(R.id.play_item_content);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.title.setText(inform.getTitle());
        viewHolder.content.setText(inform.getContent());

        return convertView;
    }

    static class ViewHolder{
        TextView title;
        TextView content;
    }
}
