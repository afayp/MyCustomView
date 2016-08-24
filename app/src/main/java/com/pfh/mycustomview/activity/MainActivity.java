package com.pfh.mycustomview.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.jaeger.library.StatusBarUtil;
import com.pfh.mycustomview.R;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("自定义view汇总");
        setSupportActionBar(mToolbar);
        int mColor = getResources().getColor(R.color.colorPrimary);
        StatusBarUtil.setColor(this, mColor, 0);
    }

    public void btn_slideCutListView(View view) {
        mIntent = new Intent(MainActivity.this, SlideCutListViewActivity.class);
        startActivity(mIntent);
    }

    public void btn_draglayout(View view) {
        mIntent = new Intent(MainActivity.this, CommonActivity.class);
        mIntent.putExtra("flag", 0);
        startActivity(mIntent);
    }

    public void btn_pieView(View view) {
        mIntent = new Intent(MainActivity.this, PieView_activity.class);
        startActivity(mIntent);
    }

    public void btn_sketchpad(View view) {
        mIntent = new Intent(MainActivity.this, CommonActivity.class);
        mIntent.putExtra("flag", 1);
        startActivity(mIntent);
    }

    public void btn_analogClock(View view) {
        mIntent = new Intent(MainActivity.this, CommonActivity.class);
        mIntent.putExtra("flag", 2);
        startActivity(mIntent);
    }

    public void btn_scratchCardView(View view) {
        mIntent = new Intent(MainActivity.this, CommonActivity.class);
        mIntent.putExtra("flag", 3);
        startActivity(mIntent);
    }

    public void btn_smallthings(View view) {
        mIntent = new Intent(MainActivity.this, CommonActivity.class);
        mIntent.putExtra("flag", 4);
        startActivity(mIntent);
    }

    public void btn_stickScrollView(View view) {
        mIntent = new Intent(MainActivity.this, StickScrollViewActivity.class);
        startActivity(mIntent);
    }

    public void btn_bezier(View view) {
        mIntent = new Intent(MainActivity.this, BezierActivity.class);
        startActivity(mIntent);
    }

    public void btn_pathMeasure(View view) {
        mIntent = new Intent(MainActivity.this, PathMeasureActivit.class);
        startActivity(mIntent);
    }

    public void btn_3D_view(View view) {
        mIntent = new Intent(MainActivity.this, CommonActivity.class);
        mIntent.putExtra("flag", 5);
        startActivity(mIntent);
    }

    public void btn_flip_card(View view) {
        mIntent = new Intent(MainActivity.this, FilpCardActivity.class);
        startActivity(mIntent);
    }

    public void btn_slack_loading(View view) {
        mIntent = new Intent(MainActivity.this, SlackLoadingActivity.class);
        startActivity(mIntent);
    }
}
