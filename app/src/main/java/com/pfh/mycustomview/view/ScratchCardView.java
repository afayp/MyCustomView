package com.pfh.mycustomview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.pfh.mycustomview.R;

/**
 * Created by Administrator on 2016/8/14.
 */
public class ScratchCardView extends View {

    private Bitmap mBgBitmap, mFgBitmap;
    private Paint mPaint;
    private Canvas mCanvas;
    private Path mPath;
    private int mBgBitmapWidth;
    private int mBgBitmapHeight;
    private float scale;
    private Matrix matrix;

    public ScratchCardView(Context context) {
        this(context,null);
    }

    public ScratchCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        //准备刮的画笔
        mPaint = new Paint();
        mPaint.setAlpha(0);//将透明度设置为0，混合时会去计算透明通道的值，就能擦除下层图像。
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));//取交集，显示下层
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(80);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        mPath = new Path();
        mBgBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.nice1);//底图
        mBgBitmapWidth = mBgBitmap.getWidth();
        mBgBitmapHeight = mBgBitmap.getHeight();

        mFgBitmap = Bitmap.createBitmap(mBgBitmap.getWidth(), mBgBitmap.getHeight(), Bitmap.Config.ARGB_8888);//在底图上面绘制一层灰色遮住底图
        mCanvas = new Canvas(mFgBitmap);
        mCanvas.drawColor(Color.GRAY);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mPath.reset();
                mPath.moveTo(event.getX(),event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                mPath.lineTo(event.getX(),event.getY());
                break;
        }
        mCanvas.drawPath(mPath,mPaint);
        invalidate();
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpceSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode=MeasureSpec.getMode(heightMeasureSpec);
        int heightSpceSize=MeasureSpec.getSize(heightMeasureSpec);

        float wScale = 1.0f;
        float hScale = 1.0f;

        //主要就是保证图片宽高比例不会变
        if (widthSpecMode != MeasureSpec.UNSPECIFIED && widthSpceSize < mBgBitmapWidth) {
            wScale = (float) widthSpceSize / (float) mBgBitmapWidth;
        }
        if (heightSpecMode != MeasureSpec.UNSPECIFIED && heightSpceSize < mBgBitmapHeight) {
            hScale = (float )heightSpceSize / (float) mBgBitmapHeight;
        }
        scale = Math.min(wScale, hScale);
        matrix = new Matrix();
        matrix.setScale(scale,scale);
        setMeasuredDimension((int)(mBgBitmapWidth* scale),(int)(mBgBitmapHeight* scale));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mBgBitmap,matrix,null);
        canvas.drawBitmap(mFgBitmap,0,0,null);
    }
}

