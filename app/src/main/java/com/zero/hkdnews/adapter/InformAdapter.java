package com.zero.hkdnews.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zero.hkdnews.R;
import com.zero.hkdnews.beans.HnustUser;
import com.zero.hkdnews.beans.Inform;
import com.zero.hkdnews.groupmsg.CheckReceiveActivity;
import com.zero.hkdnews.util.T;

import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by denghui on 15/6/15.
 */
public class InformAdapter extends BaseAdapter{
    private List<Inform> list;
    private Context context;
    private LayoutInflater layoutInflater;

    public InformAdapter(List<Inform> list, Context context){
        this.list = list;
        this.context = context;
        layoutInflater = LayoutInflater.from(this.context);

    }

    public List<Inform> getList() {
        return list;
    }

    public void setList(List<Inform> list) {
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
        final Inform inform = list.get(position);
        ViewHolder viewHolder = null;
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.inform_list_view_item,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.play_item_title);
            viewHolder.content = (TextView) convertView.findViewById(R.id.play_item_content);
            viewHolder.author = (TextView) convertView.findViewById(R.id.play_item_author);
            viewHolder.time = (TextView) convertView.findViewById(R.id.play_item_time);

            viewHolder.receive = convertView.findViewById(R.id.inform_list_view_item_bottom_receive);
            viewHolder.look = convertView.findViewById(R.id.inform_list_view_item_bottom_look);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.title.setText(inform.getTitle());
        viewHolder.content.setText(inform.getContent());
        viewHolder.author.setText("发布者："+inform.getAuthor());
        viewHolder.time.setText(inform.getCreatedAt());

        viewHolder.receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doReceiveWork(inform);
            }
        });

        viewHolder.look.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CheckReceiveActivity.class);
                intent.putExtra("groupId", inform.getGroup().getObjectId());
                intent.putExtra("informId", inform.getObjectId());
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    class ViewHolder {
        TextView title;
        TextView content;
        TextView author;
        TextView time;

        View receive;
        View look;
    }


    private void doReceiveWork(final Inform inform) {
        HnustUser user = BmobUser.getCurrentUser(context, HnustUser.class);
        BmobRelation relation = new BmobRelation();
        relation.add(user);
        inform.setUsers(relation);
        inform.update(context, new UpdateListener() {

            @Override
            public void onSuccess() {
                T.showShort(context, "确认收到！");
            }

            @Override
            public void onFailure(int arg0, String arg1) {
                T.showShort(context, arg1);
            }
        });
    }


}
