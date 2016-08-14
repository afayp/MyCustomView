package com.pfh.mycustomview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/8/14.
 */
public class ShiningTextView extends TextView {
    private int mViewWidth;
    private int mTranslate;
    private Paint mPaint;
    private LinearGradient mLinearGradient;
    private Matrix mGradientMatrix;


    public ShiningTextView(Context context) {
        super(context);
    }

    public ShiningTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ShiningTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if(mViewWidth == 0){
            mViewWidth = getMeasuredWidth();
            if(mViewWidth > 0){
                //getPaint()获取当前绘制TextView的Paint对象，并给这个Paint对象设置原生TextView没有的属性。
                mPaint = getPaint();
                mLinearGradient = new LinearGradient(0,0,mViewWidth,0,
                        new int[]{Color.BLUE,0xffffffff,Color.BLUE},null, Shader.TileMode.CLAMP);
                mPaint.setShader(mLinearGradient);
                mGradientMatrix = new Matrix();
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //在回调父类方法前，可以实现自己的逻辑，比如在文字外加个框，把画布平移一段距离
        super.onDraw(canvas);
        //回调父类方法之后，可以再加些效果
        //下面的实现原理还不懂...
        if(mGradientMatrix != null){
            mTranslate += mViewWidth/5;
            if(mTranslate > 2*mViewWidth){
                mTranslate =- mViewWidth;
            }
            mGradientMatrix.setTranslate(mTranslate,0);//通过矩阵的方式来不断平移渐变效果
            mLinearGradient.setLocalMatrix(mGradientMatrix);
            postInvalidateDelayed(100);
        }
    }
}

