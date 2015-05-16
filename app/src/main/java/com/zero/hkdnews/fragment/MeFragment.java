package com.zero.hkdnews.fragment;

import android.support.v4.app.Fragment;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.zero.hkdnews.beans.News;
import com.zero.hkdnews.beans.UserInfo;
import com.zero.hkdnews.util.L;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by luowei on 15/4/11.
 */
public class MeFragment extends Fragment{
    public static final int READ_INFO_OK = 12;
    public static final int READ_INFO_FAILED = 11;

    private UserInfo me;

    private List<MeItem> list;

    private MeAdapter meAdapter;

    private Handler mHandler =  new Handler(){
        public void handleMessage(Message msg){
            if(msg.what == READ_INFO_OK){
                me = (UserInfo) msg.obj;
                my_name.setText(me.getName());
                my_intro.setText(me.getIntro());

                Picasso.with(getActivity()).load(me.getMyImage()
                        .getFileUrl(getActivity()))
                        .into(my_img);
            }
        }
    };

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

        Thread addInfo = new Thread(){
            @Override
            public void run() {
                BmobQuery<UserInfo> query = new BmobQuery<>();
                query.addWhereEqualTo("userId", AppContext.getCurrentUserId());
                query.findObjects(getActivity(), new FindListener<UserInfo>() {
                    @Override
                    public void onSuccess(List<UserInfo> list) {
                        Message msg = Message.obtain();
                        if(list.size() == 0 ){
                            msg.what = READ_INFO_FAILED;
                        }else{
                            msg.obj = list.get(0);
                            msg.what = READ_INFO_OK;
                        }
                        mHandler.sendMessage(msg);
                    }

                    @Override
                    public void onError(int i, String s) {
                        L.d("onError"+s);
                    }
                });
            }
        };
        addInfo.start();
    }

    /**
     * 加载列表数据
     */
    private void initData() {
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
