package com.pfh.mycustomview.activity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.pfh.mycustomview.R;
import com.pfh.mycustomview.view.SlideCutListView;

import java.util.ArrayList;
import java.util.List;

public class SlideCutListViewActivity extends BaseActivity {

    private List<String> dataList;
    private SlideCutListView listview;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_cut_view);
        init();
    }

    private void init() {
        setToolbarTitle("滑动删除item的ListView");
        listview = (SlideCutListView) findViewById(R.id.listview);
        listview.setRemoveListener(new SlideCutListView.RemoveListener() {
            @Override
            public void removeItem(int direction, int position) {
                adapter.remove(adapter.getItem(position));
                Toast.makeText(SlideCutListViewActivity.this,"删除了"+position+"item",Toast.LENGTH_SHORT).show();

            }
        });
        dataList = new ArrayList<>();
        for(int i=0;i<20;i++){
            dataList.add("滑动删除哦" + i);
        }
        adapter = new ArrayAdapter(this, R.layout.listview_item, R.id.item_tv,dataList);
        listview.setAdapter(adapter);

    }
}
