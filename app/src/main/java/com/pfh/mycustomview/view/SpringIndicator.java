package com.pfh.mycustomview.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * 贝塞尔曲线实现的一个Indicator
 */
public class SpringIndicator extends View
{
    private Paint mStaticCirclePaint;//静态圆形的画笔
    private Paint mDynamicBezierPaint;//贝塞尔曲线的画笔
    private Path mBezierPath;//贝塞尔曲线

    private final int MAXCIRCLERADIUS = 20;//最大圆半径
    private final int MINCIRCLERADIUS = 10;//最小圆半径

    private float mLeftCircleRadius;//左圆的半径
    private float mRightCircleRadius;//右边圆半径

    //贝塞尔原点
    private float MORIGINCENTERX = 20;
    private float MORIGINCENTERY = 20;

    private float mCircleGap = 20*8;

    private float mLeftX;//贝塞尔曲线的左边起点X
    private float mRightX;//贝塞尔曲线的右边边终X

    private float mLeftTopY;//贝塞尔曲线的左上起点y
    private float mLeftBottomY;//贝塞尔曲线的右上边终y
    private float mRightBottomY;//贝塞尔曲线的右上边终y
    private float mRightTopY;//贝塞尔曲线的右上上起点y

    private float mControlX;//贝塞尔曲线的控制点X
    private float mControlTopY;//贝塞尔曲线的控制点
    private float mControlBottomY;

    private float bezierFraction;//控制点在水平方向相对小圆半径偏移比例

    public SpringIndicator(Context context){
        super(context);
        init();

    }
    public SpringIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public float getBezierFraction() {
        return bezierFraction;
    }

    //动画测试，更新View逻辑
    public void setBezierFraction(float bezierFraction) {
        this.bezierFraction = bezierFraction;
        //更新右圆的位置
        mRightX = MORIGINCENTERX + bezierFraction*mCircleGap;
        //更新左圆的位置
        mLeftX = MORIGINCENTERX + leftCircleDelayTranslationXLogic(bezierFraction);

        //更新控制点
        mControlX = (mLeftX + mRightX)/2;
        mControlTopY = MORIGINCENTERY - MAXCIRCLERADIUS *(1-bezierFraction);
        mControlBottomY = MORIGINCENTERY + MAXCIRCLERADIUS *(1-bezierFraction);

        //更新俩圆半径
        mLeftCircleRadius = MAXCIRCLERADIUS + (MINCIRCLERADIUS - MAXCIRCLERADIUS)*bezierFraction;
        mRightCircleRadius = MINCIRCLERADIUS + (MAXCIRCLERADIUS - MINCIRCLERADIUS)*bezierFraction;

        //更新起止点
        mLeftTopY = MORIGINCENTERY - mLeftCircleRadius;
        mLeftBottomY = MORIGINCENTERY + mLeftCircleRadius;
        mRightTopY = MORIGINCENTERY - mRightCircleRadius;
        mRightBottomY = MORIGINCENTERY + mRightCircleRadius;


        invalidate();
    }

    //左边的球延时移动
    private float leftCircleDelayTranslationXLogic(float elapsedFraction){
        if(elapsedFraction < 0.6f){
            return 0;
        }
        float leftTranslatinX = ((2*elapsedFraction - 1)*mCircleGap);
        return leftTranslatinX;
    }

    private void init(){
        mStaticCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mStaticCirclePaint.setStyle(Paint.Style.FILL);

        mDynamicBezierPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDynamicBezierPaint.setColor(Color.BLUE);
        mDynamicBezierPaint.setStyle(Paint.Style.FILL);

        mBezierPath = new Path();

        //半径
        mLeftCircleRadius = MAXCIRCLERADIUS;
        mRightCircleRadius = MINCIRCLERADIUS;

        //初始化贝塞尔曲线的6个点
        //4个起止点
        mLeftX = MORIGINCENTERX;
        mRightX = MORIGINCENTERX;

        mLeftTopY = MORIGINCENTERY - mLeftCircleRadius;
        mLeftBottomY = MORIGINCENTERY + mLeftCircleRadius;
        mRightTopY = MORIGINCENTERY - mRightCircleRadius;
        mRightBottomY = MORIGINCENTERY + mRightCircleRadius;

        //控制点
        mControlX = (mLeftX + mRightX)/2;
        mControlTopY = MORIGINCENTERY - MAXCIRCLERADIUS *(1-bezierFraction);
        mControlBottomY = MORIGINCENTERY + MAXCIRCLERADIUS *(1-bezierFraction);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        int hMode = MeasureSpec.getMode(heightMeasureSpec);
        int wSize = MeasureSpec.getSize(widthMeasureSpec);
        int hSize = MeasureSpec.getSize(heightMeasureSpec);
        int resultWidth = wSize;
        int resultHeight = hSize;
        //lp = wrapcontent时 指定默认值
        if(wMode == MeasureSpec.AT_MOST){
            resultWidth = (int) (MORIGINCENTERX + MAXCIRCLERADIUS *2+mCircleGap);
        }
        if(hMode == MeasureSpec.AT_MOST){
            resultHeight = (int) (MAXCIRCLERADIUS + MORIGINCENTERY);
        }
        setMeasuredDimension(resultWidth, resultHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mStaticCirclePaint.setColor(Color.BLUE);
        //左右的圆
        canvas.drawCircle(mLeftX, MORIGINCENTERY, mLeftCircleRadius, mStaticCirclePaint);
        canvas.drawCircle(mRightX, MORIGINCENTERY, mRightCircleRadius, mStaticCirclePaint);
        mBezierPath.reset();

        //上下两条控制点
        mBezierPath.moveTo(mLeftX, mLeftTopY);
        mBezierPath.quadTo(mControlX, mControlTopY, mRightX, mRightTopY);

        mBezierPath.lineTo(mRightX, mRightBottomY);
        mBezierPath.quadTo(mControlX,mControlBottomY, mLeftX, mLeftBottomY);
        canvas.drawPath(mBezierPath, mDynamicBezierPaint);
    }

    /**
     * 启动变幻测试
     */
    public void start(){
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "bezierFraction", 0, 1.0f);
        animator.setDuration(2000);
        animator.start();

    }
}
