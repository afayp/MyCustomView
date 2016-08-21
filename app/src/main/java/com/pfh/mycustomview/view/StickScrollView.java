package com.pfh.mycustomview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Scroller;

/**
 * Created by Administrator on 2016/8/20.
 */
public class StickScrollView extends ViewGroup {

    private int mScreenHeight;
    private int mStartY;
    private int mEnd;
    private Scroller mScroller;
    private int mLastY;
    private int childCount;
    private int realChildCount;

    public StickScrollView(Context context) {
        this(context,null);
    }

    public StickScrollView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public StickScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mScreenHeight = windowManager.getDefaultDisplay().getHeight();
        mScroller = new Scroller(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec,heightMeasureSpec);
//        int childCount = getChildCount();
//        for (int i = 0; i < childCount; i++) {
//            View childView = getChildAt(i);
//            measureChild(childView,widthMeasureSpec,heightMeasureSpec);
//        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        realChildCount = 0;
        childCount = getChildCount();

        MarginLayoutParams params = (MarginLayoutParams) getLayoutParams();
        params.height = childCount * mScreenHeight;
        setLayoutParams(params);

        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            if (view.getVisibility() != View.GONE){
                realChildCount++;
                view.layout(l,i*mScreenHeight,r,(i+1)*mScreenHeight);//从上到下一次排列
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int y = (int) event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished()){
                    return true;
                }
                mLastY = y;
                mStartY = getScrollY();//按下时已经滚动的距离
                break;
            case MotionEvent.ACTION_MOVE:
                if (!mScroller.isFinished()){
                    mScroller.abortAnimation();
                }
                int dy = mLastY -y;
                if (getScrollY() < 0){//超过上边缘
                    dy /= 3;//手指一动3cm，view仅一移动1cm，就会有种滑不动的感觉
                }
                if (getScrollY() > (realChildCount-1)*mScreenHeight){//下边缘
                    dy /= 3;
                }
                scrollBy(0,dy);
                mLastY = y;
                break;
            case MotionEvent.ACTION_UP:
                mEnd = getScrollY();
                int dScrollY = mEnd - mStartY;
                if (dScrollY > 0){
                    //向上滚动
                    if (dScrollY < mScreenHeight/2 || getScrollY() <0){
                        mScroller.startScroll(0,getScrollY(),0,-dScrollY);
                    }else {
                        mScroller.startScroll(0,getScrollY(),0,mScreenHeight-dScrollY);
                    }
                }else {
                    //向下滚动
                    if (-dScrollY < mScreenHeight/2){
                        mScroller.startScroll(0,getScrollY(),0,-dScrollY);
                    }else {
                        mScroller.startScroll(0,getScrollY(),0,-(mScreenHeight + dScrollY));
                    }
                }
                break;
        }
        postInvalidate();
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(mScroller.computeScrollOffset()){
            scrollTo(0,mScroller.getCurrY());
            postInvalidate();
        }
    }
}
