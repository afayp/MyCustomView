package com.pfh.mycustomview.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.BounceInterpolator;

/**
 * Created by Administrator on 2016/8/20.
 */
public class PathMorphBezier extends View implements View.OnClickListener{

    private Paint mPaintBezier;
    private Paint mPaintAuxiliary;
    private Paint mPaintAuxiliaryText;

    private float mAuxiliaryOneX;
    private float mAuxiliaryOneY;
    private float mAuxiliaryTwoX;
    private float mAuxiliaryTwoY;

    private float mStartPointX;
    private float mStartPointY;

    private float mEndPointX;
    private float mEndPointY;

    private Path mPath ;
    private ValueAnimator mAnimator;

    public PathMorphBezier(Context context) {
        this(context,null);
    }

    public PathMorphBezier(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PathMorphBezier(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaintBezier = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintBezier.setStyle(Paint.Style.STROKE);
        mPaintBezier.setStrokeWidth(8);

        mPaintAuxiliary = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintAuxiliary.setStyle(Paint.Style.STROKE);
        mPaintAuxiliary.setStrokeWidth(2);

        mPaintAuxiliaryText = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintAuxiliaryText.setStyle(Paint.Style.STROKE);
        mPaintAuxiliaryText.setTextSize(20);

        mPath = new Path();
        setOnClickListener(this);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //起点
        mStartPointX = w / 4;
        mStartPointY = h / 2 - 200;
        //终点
        mEndPointX = w / 4 * 3;
        mEndPointY = h / 2 - 200;
        //辅助点，一开始和起点终点重合
        mAuxiliaryOneX = mStartPointX;
        mAuxiliaryOneY = mStartPointY;
        mAuxiliaryTwoX = mEndPointX;
        mAuxiliaryTwoY = mEndPointY;

        mAnimator = ValueAnimator.ofFloat(mStartPointY,h*0.75f);
        mAnimator.setInterpolator(new BounceInterpolator());
        mAnimator.setDuration(1000);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAuxiliaryOneY = (float) animation.getAnimatedValue();
                mAuxiliaryTwoY = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 辅助点
        canvas.drawPoint(mAuxiliaryOneX, mAuxiliaryOneY, mPaintAuxiliary);
        canvas.drawText("辅助点1", mAuxiliaryOneX, mAuxiliaryOneY, mPaintAuxiliaryText);
        canvas.drawText("辅助点2", mAuxiliaryTwoX, mAuxiliaryTwoY, mPaintAuxiliaryText);
        canvas.drawText("起始点", mStartPointX, mStartPointY, mPaintAuxiliaryText);
        canvas.drawText("终止点", mEndPointX, mEndPointY, mPaintAuxiliaryText);
        // 辅助线
        canvas.drawLine(mStartPointX, mStartPointY, mAuxiliaryOneX, mAuxiliaryOneY, mPaintAuxiliary);
        canvas.drawLine(mEndPointX, mEndPointY, mAuxiliaryTwoX, mAuxiliaryTwoY, mPaintAuxiliary);
        canvas.drawLine(mAuxiliaryOneX, mAuxiliaryOneY, mAuxiliaryTwoX, mAuxiliaryTwoY, mPaintAuxiliary);

        // 三阶贝塞尔曲线
        mPath.reset();
        mPath.moveTo(mStartPointX, mStartPointY);
        mPath.cubicTo(mAuxiliaryOneX, mAuxiliaryOneY, mAuxiliaryTwoX, mAuxiliaryTwoY, mEndPointX, mEndPointY);
        canvas.drawPath(mPath,mPaintBezier);
    }

    @Override
    public void onClick(View v) {
        mAnimator.start();
    }
}
