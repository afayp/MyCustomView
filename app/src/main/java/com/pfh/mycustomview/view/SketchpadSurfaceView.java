package com.pfh.mycustomview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Administrator on 2016/8/14.
 */
public class SketchpadSurfaceView extends SurfaceView implements SurfaceHolder.Callback,Runnable{
    //SurfaceHolder
    private SurfaceHolder mHolder;
    //用于绘制的Canvas
    private Canvas mCanvas;
    //子线程标志位
    private boolean mIsDrawing;
    private int x, y = 0;
    private Path mPath;
    private Paint mPaint;

    public SketchpadSurfaceView(Context context) {
        super(context);
        initView();
    }

    public SketchpadSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SketchpadSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setStrokeWidth(10);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mHolder = getHolder();
        mHolder.addCallback(this);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setKeepScreenOn(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        mIsDrawing = true;
        new Thread(this).start();

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        mIsDrawing = false;

    }

    @Override
    public void run() {
        long start = System.currentTimeMillis();
        while (mIsDrawing){
            draw();
        }
        long end = System.currentTimeMillis();
        //有时候绘制的不用这么频繁，一般100ms绘制一次即可，如果draw方法里面的耗时小于100ms，就sleep一下
        if (end - start < 100){
            try {
                Thread.sleep(100-(end-start));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private void draw() {
        try {
            mCanvas = mHolder.lockCanvas();
            mCanvas.drawColor(Color.WHITE);
            mCanvas.drawPath(mPath, mPaint);
        } catch (Exception e) {

        } finally {
            if (mCanvas != null) {
                //提交
                mHolder.unlockCanvasAndPost(mCanvas);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mPath.moveTo(x,y);
                break;
            case MotionEvent.ACTION_MOVE:
                mPath.lineTo(x,y);
                break;
        }
        return true;
    }
}

