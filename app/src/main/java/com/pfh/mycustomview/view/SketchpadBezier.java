package com.pfh.mycustomview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * 利用贝塞尔曲线实现的画图板
 * 相比与通过Path.lineTo将各个触点连接起来，
 * 利用二阶贝塞尔曲线来将各个触点连接，就会圆滑的多
 */
public class SketchpadBezier extends View {
    private float mLastX;
    private float mLastY;
    private float offset = ViewConfiguration.get(getContext()).getScaledTouchSlop();

    private Paint mPaint;
    private Path mPath;

    public SketchpadBezier(Context context) {
        this(context,null);
    }

    public SketchpadBezier(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SketchpadBezier(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPath = new Path();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);
        mPaint.setColor(Color.RED);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mLastX = x;
                mLastY = y;
                mPath.reset();
                mPath.moveTo(mLastX,mLastY);
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = Math.abs(x - mLastX);
                float dy = Math.abs(x - mLastX);
                if (dx >= offset || dy >= offset){
                    mPath.quadTo(mLastX,mLastY,(x+mLastX)/2,(y+mLastY)/2);
                }

                mLastX = x;
                mLastY = y;
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(mPath,mPaint);
    }
}
