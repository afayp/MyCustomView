package com.pfh.mycustomview.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.pfh.mycustomview.utils.DensityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by afayp on 2016/8/23.
 */
public class SlackLoadingView extends View {

    //静止状态
    private final int STATUS_STILL = 0;
    //加载状态
    private final int STATUS_LOADING = 1;
    //线条最大长度
    private final int MAX_LINE_LENGTH = DensityUtils.dp2px(getContext(), 120);
    //线条最短长度
    private final int MIN_LINE_LENGTH = DensityUtils.dp2px(getContext(), 40);
    //最大间隔时长
    private final int MAX_DURATION = 3000;
    //最小间隔时长
    private final int MIN_DURATION = 500;

    private Paint mPaint;
    private int[] mColors = new int[]{0xB07ECBDA, 0xB0E6A92C, 0xB0D6014D, 0xB05ABA94};
    private int mWidth, mHeight;
    //动画间隔时长
    private int mDuration = MIN_DURATION;
    //线条总长度
    private int mEntireLineLength = MIN_LINE_LENGTH;
    //圆半径
    private int mCircleRadius;
    //所有动画
    private List<Animator> mAnimList = new ArrayList<>();
    //Canvas起始旋转角度
    private final int CANVAS_ROTATE_ANGLE = 0;
    //动画当前状态
    private int mStatus = STATUS_STILL;
    //Canvas旋转角度
    private int mCanvasAngle;
    //线条长度
    private float mLineLength;
    //半圆Y轴位置
    private float mCircleY;
    //第几部动画
    private int mStep;
    public SlackLoadingView(Context context) {
        this(context,null);
    }

    public SlackLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SlackLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initData() {
        mCanvasAngle = CANVAS_ROTATE_ANGLE;
        mLineLength = mEntireLineLength;
        mCircleRadius = mEntireLineLength / 5;
        mPaint.setStrokeWidth(mCircleRadius * 2);
        mStep = 0;
    }

    private void initView() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mColors[0]);
    }

    /**
     * 动画一: 在线条变短的同时旋转view
     */
    private void startCRLCAnim(){
        ValueAnimator canvasRotateAnim = ValueAnimator.ofInt(CANVAS_ROTATE_ANGLE, CANVAS_ROTATE_ANGLE + 360);
        canvasRotateAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mCanvasAngle = (int) valueAnimator.getAnimatedValue();
            }
        });

        ValueAnimator lineWidthAnim = ValueAnimator.ofFloat(mEntireLineLength, -mEntireLineLength);
        lineWidthAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mLineLength = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(canvasRotateAnim,lineWidthAnim);
        animatorSet.setDuration(mDuration);
        animatorSet.setInterpolator(new LinearInterpolator());
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                Log.d("@=>", "动画1结束");
                if (mStatus == STATUS_LOADING){
                    mStep++;
                    startCRAnim();
                }
            }
        });
        animatorSet.start();
        mAnimList.add(animatorSet);
    }

    /**
     * 动画二：变成四个点之后画布的旋转
     */
    private void startCRAnim() {
        ValueAnimator canvasRotateAnim = ValueAnimator.ofInt(mCanvasAngle, mCanvasAngle + 180);
        canvasRotateAnim.setDuration(mDuration /2);
        canvasRotateAnim.setInterpolator(new LinearInterpolator());
        canvasRotateAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mCanvasAngle = (int) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        canvasRotateAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                Log.d("@=>", "动画2结束");
                if (mStatus == STATUS_LOADING) {
                    mStep++;
                    startCRCCAnim();
                }
            }
        });
        canvasRotateAnim.start();
        mAnimList.add(canvasRotateAnim);
    }


    /**
     * 动画三，旋转画布，同时画四个圆，圆的y值先变小，后变大，看起来就像先缩进去，再扩张出来
     */
    private void startCRCCAnim() {
        ValueAnimator canvasRotateAnim = ValueAnimator.ofInt(mCanvasAngle, mCanvasAngle + 90, mCanvasAngle + 180);
        canvasRotateAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mCanvasAngle = (int) valueAnimator.getAnimatedValue();
            }
        });

        ValueAnimator circleYAnim = ValueAnimator.ofFloat(mEntireLineLength, mEntireLineLength / 4, mEntireLineLength);
        circleYAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mCircleY = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(canvasRotateAnim,circleYAnim);
        animatorSet.setInterpolator(new LinearInterpolator());
        animatorSet.setDuration(mDuration);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                Log.d("@=>", "动画3结束");
                if (mStatus == STATUS_LOADING) {
                    mStep++;
                    startLCAnim();
                }
            }
        });
        animatorSet.start();
        mAnimList.add(animatorSet);
    }

    /**
     * 动画四：重新画出四条线，线条慢慢变长
     */
    private void startLCAnim() {
        ValueAnimator lineWidthAnim = ValueAnimator.ofFloat(mEntireLineLength, -mEntireLineLength);
        lineWidthAnim.setDuration(mDuration);
        lineWidthAnim.setInterpolator(new LinearInterpolator());
        lineWidthAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mLineLength = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        lineWidthAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                Log.d("@=>", "动画4结束");
                if (mStatus == STATUS_LOADING) {
                    mStep++;
                    startCRLCAnim();
                }
            }
        });
        lineWidthAnim.start();

        mAnimList.add(lineWidthAnim);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (mStep%4){
            case 0:
                for (int i = 0; i < mColors.length; i++) {
                    mPaint.setColor(mColors[i]);
                    //每一帧画四条线，每画一条线转90度，画布旋转，坐标不用变，随着动画进行，mLineLength会变大，线条会逐渐缩成一个圆
                    drawCRLC(canvas,
                            mWidth/2 - mEntireLineLength/2.2f,
                            mHeight/2 - mLineLength,
                            mWidth/2 - mEntireLineLength / 2.2f,
                            mHeight/2 + mEntireLineLength,
                            mPaint, mCanvasAngle + i * 90);
                }
                break;
            case 1:
                for (int i = 0; i < mColors.length; i++) {
                    mPaint.setColor(mColors[i]);
                    drawCR(canvas,
                            mWidth/2-mEntireLineLength/2.2f,
                            mHeight/2 +mEntireLineLength,
                            mPaint,mCanvasAngle + i * 90);
                }
                break;
            case 2:
                for (int i = 0; i < mColors.length; i++) {
                    mPaint.setColor(mColors[i]);
                    drawCRCC(canvas,mWidth / 2 - mEntireLineLength / 2.2f, mHeight / 2 + mCircleY, mPaint, mCanvasAngle + i * 90);
                }
                break;
            case 3:
                for (int i = 0; i < mColors.length; i++) {
                    mPaint.setColor(mColors[i]);
                    drawLC(canvas, mWidth / 2 - mEntireLineLength / 2.2f, mHeight / 2 + mEntireLineLength, mWidth / 2 - mEntireLineLength / 2.2f, mHeight / 2 + mLineLength, mPaint, mCanvasAngle + i * 90);
                }
                break;
        }
    }

    /**
     *动画四，终点y值慢慢变小，起点y值不变，线条慢慢变长（从下往上画的）
     */
    private void drawLC(Canvas canvas, float startX, float startY, float stopX, float stopY, Paint paint, int rotate) {
        canvas.rotate(rotate, mWidth / 2, mHeight / 2);
        canvas.drawArc(new RectF(startX - mCircleRadius, startY - mCircleRadius, startX + mCircleRadius, startY + mCircleRadius), 0, 180, true, mPaint);
        canvas.drawLine(startX, startY, stopX, stopY, paint);
        canvas.drawArc(new RectF(stopX - mCircleRadius, stopY - mCircleRadius, stopX + mCircleRadius, stopY + mCircleRadius), 180, 180, true, mPaint);
        canvas.rotate(-rotate, mWidth / 2, mHeight / 2);

    }

    /**
     *动画三：旋转的同时改变圆点y坐标，达到往里面缩的效果
     */
    private void drawCRCC(Canvas canvas, float x, float y, Paint paint, int rotate) {
        canvas.rotate(rotate, mWidth / 2, mHeight / 2);
        canvas.drawCircle(x, y, mCircleRadius, paint);
        canvas.rotate(-rotate, mWidth / 2, mHeight / 2);
    }

    /**
     * 动画二：画布旋转，画四个圆
     */
    private void drawCR(Canvas canvas, float x, float y, Paint paint, int rotate) {
        canvas.rotate(rotate, mWidth / 2, mHeight / 2);
        canvas.drawCircle(x, y, mCircleRadius, paint);
        canvas.rotate(-rotate, mWidth / 2, mHeight / 2);
    }

    /**
     *第一个动画，画四条带圆的线
     */
    private void drawCRLC(Canvas canvas, float startX, float startY, float stopX, float stopY, @NonNull Paint paint, int rotate){

        canvas.rotate(rotate,mWidth/2,mHeight/2);
        canvas.drawArc(new RectF(startX - mCircleRadius,startY - mCircleRadius,startX +mCircleRadius,startY + mCircleRadius),
                180,180,true,paint);//上边半圆
        canvas.drawLine(startX,startY,stopX,stopY, paint);//画线
        canvas.drawArc(new RectF(stopX - mCircleRadius, stopY - mCircleRadius, stopX + mCircleRadius, stopY + mCircleRadius), 0, 180, true, mPaint);
        canvas.rotate(-rotate, mWidth / 2, mHeight / 2);//下边半圆
    }

    public void start() {
        if (mStatus == STATUS_STILL) {
            mAnimList.clear();
            mStatus = STATUS_LOADING;
            startCRLCAnim();
        }
    }

    public void reset() {
        if (mStatus == STATUS_LOADING) {
            mStatus = STATUS_STILL;
            for (Animator anim : mAnimList) {
                anim.cancel();
            }
        }
        initData();
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        initData();
    }

    public void setLineLength(float scale) {
        mEntireLineLength = (int) (scale * (MAX_LINE_LENGTH - MIN_LINE_LENGTH)) + MIN_LINE_LENGTH;
        reset();
    }

    public void setDuration(float scale) {
        mDuration = (int) (scale * (MAX_DURATION - MIN_DURATION)) + MIN_DURATION;
        reset();
    }

}
