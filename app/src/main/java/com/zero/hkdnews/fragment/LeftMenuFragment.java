package com.zero.hkdnews.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zero.hkdnews.R;
import com.zero.hkdnews.beans.LeftMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by denghui on 16/4/22.
 */
public class LeftMenuFragment extends ListFragment {

    private static final int SIZE_MENU_ITEM = 6;

    private List<LeftMenu> mItems = new ArrayList<>(SIZE_MENU_ITEM);

    private NavigationAdapter mAdapter;

    private LayoutInflater mInflater;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mInflater = LayoutInflater.from(getActivity());
        initData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setListAdapter(mAdapter = new NavigationAdapter(mItems, getActivity()));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (mMenuItemSelectedListener != null) {
            mMenuItemSelectedListener.menuItemSelected(position);
        }
//        mAdapter.setSelected(position);
    }


    //选择回调的接口
    public interface OnMenuItemSelectedListener {
        void menuItemSelected(int position);
    }

    private OnMenuItemSelectedListener mMenuItemSelectedListener;

    public void setOnMenuItemSelectedListener(OnMenuItemSelectedListener menuItemSelectedListener) {
        this.mMenuItemSelectedListener = menuItemSelectedListener;
    }

    public class NavigationAdapter extends BaseAdapter {

        private List<LeftMenu> list;

        private Context context;
        private LayoutInflater inflater;

        public NavigationAdapter(List<LeftMenu> list, Context context) {

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

    /**
     * 初始化菜单列表
     */
    private void initData() {
        mItems = new ArrayList<>();
        LeftMenu item1 = new LeftMenu("首页", R.mipmap.main_bottom_home);
        LeftMenu item2 = new LeftMenu("登录",R.mipmap.main_bottom_me);
        LeftMenu item3 = new LeftMenu("注销",R.mipmap.main_bottom_me);
        LeftMenu item4 = new LeftMenu("定位",R.mipmap.main_left_bar_position);
        LeftMenu item5 = new LeftMenu("清除缓存",R.mipmap.main_bottom_inform);
        LeftMenu item6 = new LeftMenu("个人设置",R.mipmap.main_bottom_inform);

        mItems.add(item1);
        mItems.add(item2);
        mItems.add(item3);
        mItems.add(item4);
        mItems.add(item5);
        mItems.add(item6);
    }

}
