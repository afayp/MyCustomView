package com.pfh.mycustomview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Administrator on 2016/8/20.
 */
public class ThreeOrderBezier extends View {

    private Paint mPaintBezier;//画贝塞尔曲线
    private Paint mPaintAuxiliary;//画辅助线
    private Paint mPaintAuxiliaryText;//写文字


    private PointF start, end, control1, control2;

    private boolean isSecondPoint = false;

    private Path mPath ;


    public ThreeOrderBezier(Context context) {
        this(context,null);
    }

    public ThreeOrderBezier(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ThreeOrderBezier(Context context, AttributeSet attrs, int defStyleAttr) {
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

        start = new PointF(0, 0);
        end = new PointF(0, 0);
        control1 = new PointF(0, 0);
        control2 = new PointF(0, 0);

        mPath = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        start.x = w/4;
        start.y = h/2;

        end.x = 3*w/4;
        end.y = h/2;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_POINTER_DOWN:
                isSecondPoint = true;
                break;
            case MotionEvent.ACTION_MOVE:
                control1.x = event.getX(0);
                control1.y = event.getY(0);
                if (isSecondPoint) {
                    control2.x = event.getX(1);
                    control2.y = event.getY(1);
                }
                invalidate();
                break;
            case MotionEvent.ACTION_POINTER_UP:
                isSecondPoint = false;
                break;

        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 辅助点
        canvas.drawPoint(control1.x, control1.y, mPaintAuxiliary);
        canvas.drawText("控制点1", control1.x, control1.y, mPaintAuxiliaryText);
        canvas.drawText("控制点2", control2.x, control2.y, mPaintAuxiliaryText);
        canvas.drawText("起始点", start.x, start.y, mPaintAuxiliaryText);
        canvas.drawText("终止点", end.x, end.y, mPaintAuxiliaryText);

        // 辅助线
        canvas.drawLine(start.x, start.y, control1.x, control1.y, mPaintAuxiliary);
        canvas.drawLine(end.x, end.y, control2.x, control2.y, mPaintAuxiliary);
        canvas.drawLine(control1.x, control1.y, control2.x, control2.y, mPaintAuxiliary);

        // 三阶贝塞尔曲线
        mPath.reset();
        mPath.moveTo(start.x,start.y);
        mPath.cubicTo(control1.x,control1.y,control2.x,control2.y,end.x,end.y);
        canvas.drawPath(mPath,mPaintBezier);
    }
}
