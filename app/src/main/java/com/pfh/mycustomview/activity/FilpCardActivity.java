package com.pfh.mycustomview.activity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pfh.mycustomview.R;
import com.pfh.mycustomview.animation.FlipCardAnimation;

/**
 * Created by Administrator on 2016/8/21.
 */
public class FilpCardActivity extends BaseActivity {

    private RelativeLayout rl;
    private TextView tv;
    private ImageView iv_bg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flip_card);
        setToolbarTitle("卡片效果");
        rl = (RelativeLayout) findViewById(R.id.rl);
        tv = (TextView) findViewById(R.id.tv);
        iv_bg = (ImageView) findViewById(R.id.iv_bg);

        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAnimation(tv,0,180, tv.getWidth()/2, tv.getHeight()/2);
                startAnimation(iv_bg,0,-180, iv_bg.getWidth()/2, iv_bg.getHeight()/2);
            }
        });
    }

    private void startAnimation(View view ,int fromDegree, int toDegree, int centerX, int centerY) {
        FlipCardAnimation animation = new FlipCardAnimation(fromDegree, toDegree, centerX, centerY);
        animation.setDuration(1000);
        animation.setRepeatMode(Animation.REVERSE);
        view.startAnimation(animation);
    }


}
