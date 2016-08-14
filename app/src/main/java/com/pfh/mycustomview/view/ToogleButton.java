package com.pfh.mycustomview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 *
 */
public class ToogleButton extends View {
    private Paint mGrayPaint;
    private Paint mGreenPaint;
    private Paint mCirclePaint;
    private int mDefaultWidth;
    private int mDefaultHeight;
    private boolean isOpen;
    public ToogleButton(Context context) {
        super(context);
        init();
    }

    public ToogleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ToogleButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mGrayPaint = new Paint();
        mGrayPaint.setColor(Color.GRAY);
        mGrayPaint.setStyle(Paint.Style.FILL);
        mGreenPaint = new Paint();
        mGreenPaint.setColor(Color.GREEN);
        mGreenPaint.setStyle(Paint.Style.FILL);
        mCirclePaint = new Paint();
        mCirclePaint.setColor(Color.WHITE);
        mCirclePaint.setStyle(Paint.Style.FILL);
        mDefaultWidth = 200;
        mDefaultHeight = mDefaultWidth/2;
        isOpen = false;

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                isOpen = !isOpen;
                invalidate();
            }
        });

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mDefaultWidth,mDefaultHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        int r = height/2;
        int distance = width -2*r;
        int currentDistance = 0;
        int times = 101;
        canvas.translate(width/2,height/2);
        RectF rectF = new RectF(-width / 2 , -height / 2 ,
                width / 2 , height / 2 );
        if(isOpen){
            for (int i = 1;i<times;i++){
                canvas.drawRoundRect(rectF,r,r,mGreenPaint);
                currentDistance = i*distance/(times-1);
                canvas.drawCircle(-width/2+r+currentDistance,0,r,mCirclePaint);
            }
        }else {
            for (int i = 1;i<times;i++){
                canvas.drawRoundRect(rectF,r,r,mGrayPaint);
                currentDistance = i*distance/(times-1);
                canvas.drawCircle(width/2-r-currentDistance,0,r,mCirclePaint);
            }
        }
    }
}
