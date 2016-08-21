package com.pfh.mycustomview.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.pfh.mycustomview.R;
import com.pfh.mycustomview.view.PathMeasureAnim_1;
import com.pfh.mycustomview.view.PathMeasureAnim_2;
import com.pfh.mycustomview.view.SearchView;

/**
 * Created by Administrator on 2016/8/21.
 */
public class PathMeasureActivit extends BaseActivity {

    private LinearLayout content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pathmeasure);
        setToolbarTitle("PathMeasure使用");
        content = (LinearLayout) findViewById(R.id.content);
    }

    public void pathMeasureAnim_1(View view){
        addToContent(new PathMeasureAnim_1(this));
    }

    public void pathMeasureAnim_2(View view){
        addToContent(new PathMeasureAnim_2(this));
    }

    public void searchView(View view){
        addToContent(new SearchView(this));
    }

    private void addToContent(View view){
        content.removeAllViews();
        content.addView(view);
    }
}
