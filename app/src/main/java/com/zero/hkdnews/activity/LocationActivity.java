package com.zero.hkdnews.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SectionIndexer;
import android.widget.Toast;

import com.zero.hkdnews.R;
import com.zero.hkdnews.common.UIHelper;
import com.zero.hkdnews.myview.IndexableListView;
import com.zero.hkdnews.myview.StringMatcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by luowei on 15/5/29.
 */
public class LocationActivity extends Activity implements AdapterView.OnItemClickListener{
    private ArrayList<String> mItems;
    private IndexableListView indexableListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        ButterKnife.inject(this);

        mItems = new ArrayList<>();
        mItems.add("北京");
        mItems.add("天津");
        mItems.add("上海");
        mItems.add("重庆");
        mItems.add("哈尔滨");
        mItems.add("大庆市");
        mItems.add("牡丹江市");
        mItems.add("长春");
        mItems.add("吉林");
        mItems.add("沈阳");
        mItems.add("大连");
        mItems.add("呼和浩特");
        mItems.add("包头");
        mItems.add("乌鲁木齐");
        mItems.add("拉萨");
        mItems.add("西宁");
        mItems.add("兰州");
        mItems.add("西安");
        mItems.add("太原");
        mItems.add("石家庄");
        mItems.add("济南");
        mItems.add("南京");
        mItems.add("合肥");
        mItems.add("郑州");
        mItems.add("武汉");
        mItems.add("成都");
        mItems.add("昆明");
        mItems.add("贵阳");
        mItems.add("长沙");
        mItems.add("衡阳");
        mItems.add("杭州");
        mItems.add("福州");
        mItems.add("郴州");

        Collections.sort(mItems);

        ContentAdapter adapter = new ContentAdapter(this,
                android.R.layout.simple_list_item_1, mItems);

        indexableListView = (IndexableListView) findViewById(R.id.position_list_view);
        indexableListView.setAdapter(adapter);
        indexableListView.setFastScrollEnabled(true);
        indexableListView.setOnItemClickListener(this);


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this,"切换到"+ mItems.get(position), Toast.LENGTH_SHORT).show();
        toBack();
    }


    private class ContentAdapter extends ArrayAdapter<String> implements SectionIndexer {

        private String mSections = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        public ContentAdapter(Context context, int textViewResourceId,
                              List<String> objects) {
            super(context, textViewResourceId, objects);
        }

        @Override
        public int getPositionForSection(int section) {
            // If there is no item for current section, previous section will be selected
            for (int i = section; i >= 0; i--) {
                for (int j = 0; j < getCount(); j++) {
                    if (i == 0) {
                        // For numeric section
                        for (int k = 0; k <= 9; k++) {
                            if (StringMatcher.match(String.valueOf(getItem(j).charAt(0)), String.valueOf(k)))
                                return j;
                        }
                    } else {
                        if (StringMatcher.match(String.valueOf(getItem(j).charAt(0)), String.valueOf(mSections.charAt(i))))
                            return j;
                    }
                }
            }
            return 0;
        }

        @Override
        public int getSectionForPosition(int position) {
            return 0;
        }

        @Override
        public Object[] getSections() {
            String[] sections = new String[mSections.length()];
            for (int i = 0; i < mSections.length(); i++)
                sections[i] = String.valueOf(mSections.charAt(i));
            return sections;
        }
    }

    @OnClick(R.id.position_btn)
    public void toBack(){
        UIHelper.showHome(this);
        finish();
    }
}
