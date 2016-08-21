package com.pfh.mycustomview.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.pfh.mycustomview.R;
import com.pfh.mycustomview.view.StickScrollView;

/**
 * Created by Administrator on 2016/8/20.
 */
public class StickScrollViewActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_activity_stickscrollview);
        setToolbarTitle("粘性ScrollView");
        StickScrollView stickScrollView = (StickScrollView) findViewById(R.id.stickScrollView);

        WindowManager wm= (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        int mScreenHeight = wm.getDefaultDisplay().getHeight();

        ViewGroup.LayoutParams layoutParams=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,mScreenHeight);
        ViewGroup.LayoutParams layoutParams2=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,mScreenHeight);
        ViewGroup.LayoutParams layoutParams3=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,mScreenHeight);

        ImageView imageView = new ImageView(this);
        ImageView imageView2 = new ImageView(this);
        ImageView imageView3 = new ImageView(this);

        imageView.setLayoutParams(layoutParams);
        imageView2.setLayoutParams(layoutParams2);
        imageView3.setLayoutParams(layoutParams3);

        imageView.setBackgroundResource(R.drawable.nice1);
        imageView2.setBackgroundResource(R.drawable.nice2);
        imageView3.setBackgroundResource(R.drawable.nice3);

        stickScrollView.addView(imageView);
        stickScrollView.addView(imageView2);
        stickScrollView.addView(imageView3);
    }
}
