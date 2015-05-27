package com.zero.hkdnews.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zero.hkdnews.R;
import com.zero.hkdnews.adapter.MeAdapter;
import com.zero.hkdnews.app.AppContext;
import com.zero.hkdnews.beans.MeItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by luowei on 15/4/11.
 */
public class MeFragment extends Fragment{
    public static final int READ_INFO_OK = 12;
    public static final int READ_INFO_FAILED = 11;

    private List<MeItem> list;

    private MeAdapter meAdapter;

    @InjectView(R.id.me_info_image)
    ImageView my_img;

    @InjectView(R.id.me_info_name)
    TextView my_name;

    @InjectView(R.id.me_info_intro)
    TextView my_intro;

    @InjectView(R.id.me_list_view)
    ListView meListView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View meLayout = inflater.inflate(R.layout.fragment_me,container,false);
        ButterKnife.inject(this, meLayout);
        return meLayout;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initData();


    }

    /**
     * 加载列表数据
     */
    private void initData() {
        //初始化个人信息
        Picasso.with(getActivity()).load(AppContext.getMyHead().getFileUrl(getActivity())).into(my_img);
        my_name.setText(AppContext.getUserName());
        my_intro.setText(AppContext.getIntro());


        list = new ArrayList(){};
        MeItem  item1 = new MeItem(R.mipmap.widget_bar_share_over,"我的消息");
        MeItem  item2 = new MeItem(R.mipmap.widget_bar_share_over,"推送资讯");
        MeItem  item3 = new MeItem(R.mipmap.widget_bar_share_over,"我的收藏");
        MeItem  item4 = new MeItem(R.mipmap.widget_bar_share_over,"用户反馈");
        MeItem  item5 = new MeItem(R.mipmap.widget_bar_share_over,"检查更新");
        MeItem  item6 = new MeItem(R.mipmap.widget_bar_share_over,"关于我们");
        list.add(item1);
        list.add(item2);
        list.add(item3);
        list.add(item4);
        list.add(item5);
        list.add(item6);

        meAdapter = new MeAdapter(getActivity(),list);
        meListView.setAdapter(meAdapter);

        //添加各种响应事件
        meListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
