package com.pfh.mycustomview.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.pfh.mycustomview.R;

/**
 *
 */
public class DragLayout_activity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_activity_draglayout);
        setToolbarTitle("可拖动的view");

        TextView tv7 = (TextView) findViewById(R.id.view7);
        tv7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("点击了view7");
            }
        });
    }


}
