package com.pfh.mycustomview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Scroller;

/**
 * 用scroller实现的左右滑动删除item效果的ListView,参考：http://blog.csdn.net/xiaanming/article/details/17539199
 * 注意的地方：item的布局要再嵌套一层布局，忽然滑动的只是item的内容，而不是整个item
 * 还存在的问题：剧烈滑动时莫名其妙的崩溃...
 * 大神作品：https://github.com/daimajia/AndroidSwipeLayout
 *
 */


public class SlideCutListView extends ListView {

    private int screenWidth;
    private Scroller scroller;
    private int scaledTouchSlop;

    public static final int DIRECTION_LEFT = 1;
    public static final int DIRECTION_RIGHT = 2;
    private int CURR_DIRECTION;
    //超过这个速度，表示用户想删除，即不用滑动超过一半也删除
    private static final int SNAP_VELOCITY = 600;

    private RemoveListener mRemoveListener;
    //记录按下的坐标
    private int downX;
    private int downY;
    //根据按下的坐标判断是哪个position
    private int slidePosition;
    private View itemView;
    //滑动状态
    private boolean isSlide = false;
    private VelocityTracker velocityTracker;


    public SlideCutListView(Context context) {
        this(context, null);
    }

    public SlideCutListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideCutListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        WindowManager windowmanager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        screenWidth = windowmanager.getDefaultDisplay().getWidth();
        scroller = new Scroller(context);
        scaledTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();//判定为滑动的最小距离
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:

                //如果还在滚动，直接返回
                if (!scroller.isFinished()) {
                    return super.dispatchTouchEvent(ev);
                }

                downX = (int) ev.getX();
                downY = (int) ev.getY();
                slidePosition = pointToPosition(downX, downY);
                //如果是无效position直接返回(不知道有什么用？)
                if (slidePosition == AdapterView.INVALID_POSITION) {
                    return super.dispatchTouchEvent(ev);
                }
                //拿到点击的itemView 参数不理解？下面两种都可以？
                itemView = getChildAt(slidePosition - getFirstVisiblePosition());
//                itemView = getChildAt(slidePosition);

                break;
            case MotionEvent.ACTION_MOVE:
                //过滤 左右滑动的距离要大，上下滑动的距离要小，这样你上下滑动的时候不小心左右滑动了也不会触发删除
                if (Math.abs(ev.getX() - downX) > scaledTouchSlop && Math.abs(ev.getY()-downY)<scaledTouchSlop) {
                    isSlide = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    //向右滑动删除
    private void scrollRight() {
        CURR_DIRECTION = DIRECTION_RIGHT;
        //向右滑动，scrollX变为负值。screenWidth相当于item的宽度，减去已经滚动的距离，就是剩下要滚动的距离。
        int delta = itemView.getScrollX() + screenWidth;
        scroller.startScroll(itemView.getScrollX(), 0, -delta, 0,500);
        postInvalidate();
    }

    //向左滑动删除
    private void scrollLeft() {
        CURR_DIRECTION = DIRECTION_LEFT;
        int delta = screenWidth - itemView.getScrollX();
        scroller.startScroll(itemView.getScrollX(), 0, delta, 0, 500);
        postInvalidate();
    }

    //根据itemview目前的位置判断是否删除
    private void scrollByDistance() {
        //如果向左滚动的距离大于一半，就删除
        if (itemView.getScrollX() >= screenWidth / 2) {
            scrollLeft();
        } else if (itemView.getScrollX() <= -screenWidth / 2) {
            scrollRight();
        } else {
            // 回到原来的位置
            itemView.scrollTo(0, 0);
        }
    }


    /**
     * 拖动item的实现
     *
     * @param ev
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isSlide && slidePosition != AdapterView.INVALID_POSITION) {
            //请求父viewgroup不再拦截，即不在调用onInterceptTouchEvent（）
//            requestDisallowInterceptTouchEvent(true);
            addVelocityTracker(ev);
            int x = (int) ev.getX();
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_MOVE:
                    int delta = downX - x;
                    downX = x;
                    itemView.scrollBy(delta, 0);
                    return true;
//                    break;
                case MotionEvent.ACTION_UP:
                    //判断抬起时的速度，如果速度很快表示用户想直接删除，否则按滑动的距离判断是否删除
                    velocityTracker.computeCurrentVelocity(1000);
                    int velocityX = (int) velocityTracker.getXVelocity();
                    if (velocityX > SNAP_VELOCITY) {
                        scrollRight();

                    } else if (velocityX < -SNAP_VELOCITY) {
                        scrollLeft();

                    } else {
                        scrollByDistance();
                    }
                    //最后要记得回收
                    recycleVelocityTracker();
                    //手指离开不响应滑动
                    isSlide = false;
                    break;
            }

        }

        return super.onTouchEvent(ev);
    }

    @Override
    public void computeScroll() {
        //这个都是这么写滴
        if (scroller.computeScrollOffset()) {
            itemView.scrollTo(scroller.getCurrX(), scroller.getCurrY());
            postInvalidate();

            //滚动结束时回调接口
            if (scroller.isFinished()) {
                if (mRemoveListener == null) {
                    throw new NullPointerException("RemoveListener is null, we should called setRemoveListener()");
                }
                //回到原来的位置，然后在回调中从数据源删除数据，如果不加这个那么删除的itemview的位置会一直空着，不会被填充
                itemView.scrollTo(0, 0);
                mRemoveListener.removeItem(CURR_DIRECTION, slidePosition);

            }
        }
    }

    /**
     * 添加用户的速度跟踪器
     *
     * @param event
     */
    private void addVelocityTracker(MotionEvent event) {
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }

        velocityTracker.addMovement(event);
    }

    /**
     * 移除用户速度跟踪器
     */
    private void recycleVelocityTracker() {
        if (velocityTracker != null) {
            velocityTracker.recycle();
            velocityTracker = null;
        }
    }

    /**
     * 回调接口
     *
     * @param listener
     */
    public void setRemoveListener(RemoveListener listener) {
        this.mRemoveListener = listener;
    }

    public interface RemoveListener {
        //提供这些参数给调用者使用
        public void removeItem(int direction, int position);
    }
}
