package com.pfh.mycustomview.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.jaeger.library.StatusBarUtil;
import com.pfh.mycustomview.R;

public class MainActivity extends AppCompatActivity {

    private Button btn_slideCutListView;
    private Button btn_draglayout;
    private Button btn_pieView;
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
        mIntent = new Intent(MainActivity.this, SlideCutListView_activity.class);
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
}
