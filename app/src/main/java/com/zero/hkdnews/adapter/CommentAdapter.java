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
import com.zero.hkdnews.app.AppContext;
import com.zero.hkdnews.beans.Comment;
import com.zero.hkdnews.beans.Reply;

import java.util.List;

/**
 * Created by luowei on 15/5/8.
 */
public class CommentAdapter extends BaseAdapter {

    private Context             context;
    private List<Comment>       listItems;
    private LayoutInflater      listContainer;


    class ViewHoler{
        ImageView face;
        TextView name;
        TextView content;
        TextView date;
        TextView reply;
    }

    public void setListItems(List<Comment> listItems){
        this.listItems = listItems;
    }

    public CommentAdapter(Context context,List<Comment> data){
        this.context =context;
        this.listItems = data;
        listContainer = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int position) {
        return listItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHoler viewHoler = null;

        if(convertView==null){
            convertView = listContainer.inflate(R.layout.comment_list_item,null);
            viewHoler = new ViewHoler();

            viewHoler.face = (ImageView) convertView.findViewById(R.id.comment_listitem_userface);
            viewHoler.name = (TextView) convertView.findViewById(R.id.comment_listitem_name);
            viewHoler.content = (TextView) convertView.findViewById(R.id.comment_listitem_content);
            viewHoler.date = (TextView) convertView.findViewById(R.id.comment_listitem_date);
            viewHoler.reply = (TextView) convertView.findViewById(R.id.comment_listitem_replies);

            convertView.setTag(viewHoler);

        }else{
            viewHoler = (ViewHoler) convertView.getTag();
        }

        //设置数据
       Comment comment = listItems.get(position);

        viewHoler.name.setText(comment.getAuthor());
        viewHoler.date.setText(comment.getUpdatedAt());
        viewHoler.content.setText(comment.getContent());

        Picasso.with(context).load(comment.getFace().getFileUrl(context)).into(viewHoler.face);

        return convertView;
    }
}
