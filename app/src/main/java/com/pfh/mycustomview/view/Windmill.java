package com.pfh.mycustomview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.pfh.mycustomview.utils.DensityUtils;

/**
 * Created by Administrator on 2016/8/20.
 */
public class Windmill extends View {

    private static final int DEFAULT_COLOR = Color.WHITE;
    private static final int DEFAULT_WIDTH = 1;//画笔宽度1dp
    private static final float LENGTH_1 = 4;//下面三角形高度5dp
    private static final float ALPHA = (float) (Math.PI / 6);//旋转角度 π/6
    private static final int DELAY = 30;

    private Paint mPaint;
    private Path mPath;
    private float mAngle = 0;//旋转角度 通过改变角度实现旋转动画

    private int mDefalutWidth = 300;
    private int mDefalutHeight = 450;//px
    private float mDefalutScale = 1.5f;//默认 高/宽=1.5(好看一点)
    private float length;
    private float length1;
    private float centerX;
    private float centerY;


    public Windmill(Context context) {
        this(context,null);
    }

    public Windmill(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public Windmill(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPath = new Path();
        mPaint.setColor(DEFAULT_COLOR);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(DensityUtils.dp2px(getContext(), DEFAULT_WIDTH));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpceSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode=MeasureSpec.getMode(heightMeasureSpec);
        int heightSpceSize=MeasureSpec.getSize(heightMeasureSpec);

        if(widthSpecMode==MeasureSpec.AT_MOST&&heightSpecMode==MeasureSpec.AT_MOST){
            setMeasuredDimension(mDefalutWidth, mDefalutHeight);
        }else if(widthSpecMode==MeasureSpec.AT_MOST){
            setMeasuredDimension((int) (heightSpceSize/mDefalutScale), heightSpceSize);
        }else if(heightSpecMode==MeasureSpec.AT_MOST){
            setMeasuredDimension(widthSpceSize, (int) (widthSpceSize*mDefalutScale));
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //中心点
        centerX = getWidth() / 2;
        centerY = getHeight() * 4 / 9.0f;
        //叶片由两个三角形组成 length1是下面三角形的高 length为整个叶片长度
        length = (float) (DensityUtils.dp2px(getContext(), LENGTH_1) * Math.sin(ALPHA)
                + getHeight() * 2 / 9.0f);
        length1 = DensityUtils.dp2px(getContext(), LENGTH_1);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        mAngle = (float) (mAngle + 3 * Math.PI / 360);//每次转一度

        //绘制风车的身体
        canvas.drawLine(centerX, centerY, centerX - getWidth() / 10, getHeight(), mPaint);
        canvas.drawLine(centerX, centerY, centerX + getWidth() / 10, getHeight(), mPaint);

        float alpha = (float) (Math.PI / 2 - ALPHA + mAngle);
        drawWindMill(canvas, centerX, centerY, alpha);

        canvas.save();
        canvas.rotate(120,centerX,centerY);
        drawWindMill(canvas, centerX, centerY, alpha);
        canvas.restore();

        canvas.save();
        canvas.rotate(240,centerX,centerY);
        drawWindMill(canvas, centerX, centerY, alpha);
        canvas.restore();

        mPath.reset();
        postInvalidateDelayed(DELAY);
    }

    private void drawWindMill(Canvas canvas, float centerX, float centerY, float alpha) {
        mPath.moveTo(centerX, centerY);
        mPath.lineTo((float) (centerX + length1 * Math.cos(alpha)),
                (float) (centerY - length1 * Math.sin(alpha)));

        mPath.lineTo((float) (centerX + length * Math.cos(Math.PI / 2 + mAngle)),
                (float) (centerY - length * Math.sin(Math.PI / 2 + mAngle)));

        mPath.lineTo((float) (centerX + length1 * Math.cos(alpha + 2 * ALPHA)),
                (float) (centerY - length1 * Math.sin(alpha + 2 * ALPHA)));
        mPath.close();
        canvas.drawPath(mPath, mPaint);
    }
}
