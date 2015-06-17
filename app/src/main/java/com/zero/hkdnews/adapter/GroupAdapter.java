package com.zero.hkdnews.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zero.hkdnews.R;
import com.zero.hkdnews.beans.Group;

import java.util.List;

/**
 * Created by luowei on 15/6/15.
 */
public class GroupAdapter extends BaseAdapter {

    private List<Group> list;
    private LayoutInflater inflater;


    public GroupAdapter(List<Group> list,Context c){
        this.list = list;
        inflater = LayoutInflater.from(c);
    }


    public void setList(List<Group> list) {
        this.list = list;
    }

    public List<Group> getList() {
        return list;
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

        Group group = list.get(position);
        ViewHolder viewHolder = null;
        if(convertView == null ){

            convertView = inflater.inflate(R.layout.group_list_view_item,parent,false);

            viewHolder = new ViewHolder();

            viewHolder.name = (TextView) convertView.findViewById(R.id.group_item_name);

        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.name.setText(group.getName());


        return convertView;
    }

    static class ViewHolder{
        TextView name;
    }
}
