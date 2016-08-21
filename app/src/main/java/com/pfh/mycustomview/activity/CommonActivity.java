package com.pfh.mycustomview.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.pfh.mycustomview.R;

/**
 * 那些不需要太多java代码的view用例统一用这个activity展示
 */
public class CommonActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int flag = getIntent().getIntExtra("flag", -1);
        switch (flag){
            case 0:
                setContentView(R.layout.common_activity_draglayout);
                setToolbarTitle("可拖动的view");
                TextView tv7 = (TextView) findViewById(R.id.view7);
                tv7.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showToast("点击了view7");
                    }
                });
                break;
            case 1:
                setContentView(R.layout.common_activity_sketchpad_surfaceview);
                setToolbarTitle("surfaceview实现的画图板");
                break;
            case 2:
                setContentView(R.layout.common_activity_analogclock);
                setToolbarTitle("一个时钟");
                break;
            case 3:
                setContentView(R.layout.common_activity_scratchcardview);
                setToolbarTitle("刮刮卡");
                break;
            case 4:
                setContentView(R.layout.common_activity_smallthings);
                setToolbarTitle("小东西");
                break;
            case 5:
                setContentView(R.layout.common_activity_3d_view);
                setToolbarTitle("3D立体旋转效果");
                break;


        }
    }
}
