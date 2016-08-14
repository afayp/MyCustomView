package com.pfh.mycustomview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.pfh.mycustomview.model.PieData;

import java.util.List;

/**
 *
 */
public class PieView extends View {

    private float mStartAngle;//开始的角度

    private Paint mPaint;

    private List<PieData> mData;

    public PieView(Context context) {
        this(context, null);
    }

    public PieView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PieView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    private void init() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec),measureHeight(heightMeasureSpec));

    }

    private int measureWidth(int widthMeasureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);

        if(specMode == MeasureSpec.EXACTLY){
            result = specSize;
        }else {
            result = 500;//这个值使我们自己定的，表示wrap_content的时候view的默认大小
            if(specMode == MeasureSpec.AT_MOST){
                result = Math.min(result,specSize);
            }
        }
        return  result;
    }

    private int measureHeight(int heightMeasureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(heightMeasureSpec);
        int specSize = MeasureSpec.getSize(heightMeasureSpec);

        if(specMode == MeasureSpec.EXACTLY){
            result = specSize;
        }else {
            result = 500;//这个值使我们自己定的，表示wrap_content的时候view的默认大小
            if(specMode == MeasureSpec.AT_MOST){
                result = Math.min(result,specSize);
            }
        }
        return  result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();//该view的宽高
        canvas.translate(width/2,height/2);//移到中心
        float r = (float) (Math.min(width/2,height/2)*0.8);//半径
        RectF rectF = new RectF(-r, -r, r, r);//外接矩形位置

        float currentStartAngle = mStartAngle;

        for (int i = 0;i<mData.size();i++){
            PieData pieData = mData.get(i);
            mPaint.setColor(pieData.getColor());
            canvas.drawArc(rectF, currentStartAngle, pieData.getAngle(), true, mPaint);
            currentStartAngle = currentStartAngle + pieData.getAngle();
        }
    }

    // 设置起始角度
    public void setStartAngle(int mStartAngle) {
        this.mStartAngle = mStartAngle;
        invalidate();   // 刷新
    }

    public void setData(List<PieData> data){
        mData = data;
        initData(mData);
        invalidate();
    }

    private void initData(List<PieData> data) {

        if(data == null || data.size() == 0){
            return;
        }

        float sum = 0;
        for (int i=0;i<data.size();i++){
            Log.e("TAG",data.size()+"");
            sum += data.get(i).getValue();
        }

        for (int i=0;i<data.size();i++){
            float percentage = data.get(i).getValue()/sum;
            data.get(i).setAngle(percentage * 360);
        }
    }
}
