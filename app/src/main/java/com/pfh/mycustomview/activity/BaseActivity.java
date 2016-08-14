package com.pfh.mycustomview.activity;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.jaeger.library.StatusBarUtil;
import com.pfh.mycustomview.R;

/**
 * Created by Administrator on 2016/8/14.
 */
public  class BaseActivity extends AppCompatActivity {
    protected Toast mToast;
    protected Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        initToolbar();
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(" ");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
        int mColor = getResources().getColor(R.color.colorPrimary);
        StatusBarUtil.setColor(this, mColor,0);
    }

    public void setToolbarTitle(String title){
        mToolbar.setTitle(title);
    }


    public void showToast(String text) {
        if (!TextUtils.isEmpty(text)) {
            if(mToast == null){
                mToast = Toast.makeText(getApplicationContext(),text,Toast.LENGTH_SHORT);
            }else{
                mToast.setText(text);
            }
            mToast.show();
        }
    }

}
