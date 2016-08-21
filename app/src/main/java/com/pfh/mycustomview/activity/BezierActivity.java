package com.pfh.mycustomview.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.pfh.mycustomview.R;
import com.pfh.mycustomview.view.MagicCircle;
import com.pfh.mycustomview.view.PathAnimBezier;
import com.pfh.mycustomview.view.PathMorphBezier;
import com.pfh.mycustomview.view.SecondOrderBezier;
import com.pfh.mycustomview.view.SketchpadBezier;
import com.pfh.mycustomview.view.SpringIndicator;
import com.pfh.mycustomview.view.ThreeOrderBezier;
import com.pfh.mycustomview.view.WaveBezier;

/**
 * Created by Administrator on 2016/8/21.
 */
public class BezierActivity extends BaseActivity {


    private LinearLayout content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bezier);
        setToolbarTitle("贝塞尔曲线");
        content = (LinearLayout) findViewById(R.id.content);
    }

    public void SecondOrderBezier(View view) {
        addToContent(new SecondOrderBezier(this));
    }

    public void ThreeOrderBezier(View view) {
        addToContent(new ThreeOrderBezier(this));
    }

    public void SketchpadBezier(View view) {
        addToContent(new SketchpadBezier(this));
    }

    public void PathMorphBezier(View view) {
        addToContent(new PathMorphBezier(this));
    }

    public void waveBezier(View view) {
        addToContent(new WaveBezier(this));
    }

    public void pathAnimBezier(View view) {
        addToContent(new PathAnimBezier(this));
    }

    public void magicCircle(View view) {
        final MagicCircle magicCircle = new MagicCircle(this);
        magicCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                magicCircle.startAnimation();
            }
        });
        addToContent(magicCircle);
    }

    public void springIndicator(View view) {
        final SpringIndicator springIndicator = new SpringIndicator(this);
        springIndicator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                springIndicator.start();
            }
        });
        addToContent(springIndicator);
    }


    private void addToContent(View view) {
        content.removeAllViews();
        content.addView(view);

    }


}
