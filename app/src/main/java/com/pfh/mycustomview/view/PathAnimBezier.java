package com.pfh.mycustomview.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.pfh.mycustomview.evaluator.BezierEvaluator;

/**
 * Created by Administrator on 2016/8/21.
 */
public class PathAnimBezier extends View implements View.OnClickListener{
    private Paint mPathPaint;
    private Paint mCirclePaint;

    private int mStartPointX;
    private int mStartPointY;
    private int mEndPointX;
    private int mEndPointY;

    private int mMovePointX;
    private int mMovePointY;

    private int mControlPointX;
    private int mControlPointY;

    private Path mPath;

    public PathAnimBezier(Context context) {
        this(context,null);
    }

    public PathAnimBezier(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PathAnimBezier(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPathPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPathPaint.setStyle(Paint.Style.STROKE);
        mPathPaint.setStrokeWidth(5);
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mStartPointX = 100;
        mStartPointY = 100;
        mEndPointX = 600;
        mEndPointY = 600;
        mMovePointX = mStartPointX;
        mMovePointY = mStartPointY;
        mControlPointX = 500;
        mControlPointY = 0;
        mPath = new Path();
        setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        BezierEvaluator bezierEvaluator = new BezierEvaluator(new PointF(mControlPointX, mControlPointY));
        ValueAnimator animator = ValueAnimator.ofObject(bezierEvaluator,
                new PointF(mStartPointX, mStartPointY),
                new PointF(mEndPointX, mEndPointY));
        animator.setDuration(1000);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF pointF = (PointF) animation.getAnimatedValue();
                mMovePointX = (int) pointF.x;
                mMovePointY = (int) pointF.y;
                invalidate();
            }
        });
        animator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath.reset();
        canvas.drawCircle(mStartPointX, mStartPointY, 30, mCirclePaint);
        canvas.drawCircle(mEndPointX, mEndPointY, 30, mCirclePaint);
        mPath.moveTo(mStartPointX, mStartPointY);
        mPath.quadTo(mControlPointX, mControlPointY, mEndPointX, mEndPointY);
        canvas.drawPath(mPath, mPathPaint);
        canvas.drawCircle(mMovePointX, mMovePointY, 30, mCirclePaint);
    }
}
