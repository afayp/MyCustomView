package com.pfh.mycustomview.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.text.format.Time;
import android.util.AttributeSet;
import android.view.View;

import com.pfh.mycustomview.R;

import java.util.TimeZone;

/**
 * Created by Administrator on 2016/8/14.
 */
public class AnalogClock extends View {

    private Time mCalendar;    //用来记录当前时间
    //用来存放三张图片资源
    private Drawable mHourHand;
    private Drawable mMinuteHand;
    private Drawable mDial;

    //记录整个view的宽高
    private int mDialWidth;
    private int mDialHeight;

    /**
     * 用来记录View是否被加入到了Window中，我们在View attached到Window时注册监听器，监听时间的变更，并根据时间的变更，改变自己
     * 的绘制，在View从Window中剥离时，解除注册，因为我们不需要再监听时间变更了，没人能看得到我们的View了。
     */
    private boolean mAttached;

    //记录时间
    private float mMinutes;
    private float mHour;

    /**
     * 用来跟踪我们的View 的尺寸的变化，当发生尺寸变化时，我们在绘制自己时要进行适当的缩放
     */
    private boolean mChanged;

    public AnalogClock(Context context) {
        this(context, null);
    }

    public AnalogClock(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnalogClock(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        Resources resources = context.getResources();
        if (mDial == null) {
            mDial = resources.getDrawable(R.drawable.clock_dial);
        }
        if (mHourHand == null) {
            mHourHand = resources.getDrawable(R.drawable.clock_hand_hour);
        }
        if (mMinuteHand == null) {
            mMinuteHand = resources.getDrawable(R.drawable.clock_hand_minute);
        }

        mDialWidth = mDial.getIntrinsicWidth();//图片本来的宽高
        mDialHeight = mDial.getIntrinsicHeight();

        mCalendar = new Time();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpceSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode=MeasureSpec.getMode(heightMeasureSpec);
        int heightSpceSize=MeasureSpec.getSize(heightMeasureSpec);

        float wScale = 1.0f;
        float hScale = 1.0f;

        //主要就是保证图片宽高比例不会变
        if (widthSpecMode != MeasureSpec.UNSPECIFIED && widthSpceSize < mDialWidth) {
            wScale = (float) widthSpceSize / (float) mDialWidth;
        }
        if (heightSpecMode != MeasureSpec.UNSPECIFIED && heightSpceSize < mDialHeight) {
            hScale = (float )heightSpceSize / (float) mDialHeight;
        }
        float scale = Math.min(wScale, hScale);
        setMeasuredDimension(
                resolveSizeAndState((int) (mDialWidth * scale), widthMeasureSpec, 0),
                resolveSizeAndState((int) (mDialHeight * scale), heightMeasureSpec, 0)
        );
//        setMeasuredDimension((int)(mDialWidth*scale),(int)(mDialHeight*scale));

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mChanged = true;//每次绘制时，可以先判断下尺寸是否发生
    }

    private void onTimeChanged(){
        mCalendar.setToNow();
        int hour = mCalendar.hour;
        int minute = mCalendar.minute;
        int second = mCalendar.second;
        /*这里我们为什么不直接把minute设置给mMinutes，而是要加上
            second /60.0f呢，这个值不是应该一直为0吗？
            这里又涉及到Calendar的 一个知识点，
            也就是它可以是Linient模式，
            此模式下，second和minute是可能超过60和24的，具体这里就不展开了，
            如果不是很清楚，建议看看Google的官方文档中讲Calendar的部分*/
        mMinutes = minute + second / 60.0f;
        mHour = hour + mMinutes / 60.0f;
        mChanged = true;
    }

    /**
     * 实现一个广播接收器，接收系统发出的时间变化广播，然后更新该View
     */
    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //这个if判断主要是用来在时区发生变化时，更新mCalendar的时区的，这
            //样，我们的自定义View在全球都可以使用了。
            if (intent.getAction().equals(Intent.ACTION_TIMEZONE_CHANGED)) {
                String tz = intent.getStringExtra("time-zone");
                mCalendar = new Time(TimeZone.getTimeZone(tz).getID());
            }
            //进行时间的更新
            onTimeChanged();
            //invalidate当然是用来引发重绘了。
            invalidate();
        }
    };

    /**
     * 给我们的View动态地注册广播接收器，在onAttachedToWindow中注册，在onDetachedFromWindow中注销
     */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!mAttached) {
            mAttached = true;
            IntentFilter filter = new IntentFilter();
            //这里确定我们要监听的三种系统广播
            filter.addAction(Intent.ACTION_TIME_TICK);
            filter.addAction(Intent.ACTION_TIME_CHANGED);
            filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
            getContext().registerReceiver(mIntentReceiver,   filter);
        }

        mCalendar = new Time();
        onTimeChanged();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAttached) {
            getContext().unregisterReceiver(mIntentReceiver);
            mAttached = false;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //View尺寸变化后，我们用changed变量记录下来，
        //同时，恢复mChanged为false，以便继续监听View的尺寸变化。
        boolean changed = mChanged;
        if (changed) {
            mChanged = false;
        }
        /* 请注意，这里的availableWidth和availableHeight，
           每次绘制时是可能变化的，
           我们可以从mChanged变量的值判断它是否发生了变化，
           如果变化了，说明View的尺寸发生了变化，
           那么就需要重新为时针、分针设置Bounds，
           因为我们需要时针，分针始终在View的中心。*/
        int availableWidth = getWidth();
        int availableHeight = getHeight();

        /* 这里的x和y就是View的中心点的坐标，
          我们把drawable放在view的中心 */
        int x = availableWidth / 2;
        int y = availableHeight / 2;

        /*为什么不直接用mDial？
        因为final它可以确保能够完整地绘制完mDial，可以防止正在绘制过程中时mDial被销毁，
        和匿名内部类访问局部变量时为它加上final的意图是一样的。
        */
        final Drawable dial = mDial;
        int w = dial.getIntrinsicWidth();
        int h = dial.getIntrinsicHeight();
        boolean scaled = false;
        /*如果可用的宽高小于表盘图片的实际宽高，
        就要进行缩放，不过这里，我们是通过坐标系的缩放来实现的。
        而且，这个缩放效果影响是全局的，
        也就是下面绘制的表盘、时针、分针都会受到缩放的影响。*/
        if (availableWidth < w || availableHeight < h) {
            scaled = true;
            float scale = Math.min((float) availableWidth / (float) w,
                    (float) availableHeight / (float) h);
            canvas.save();
            canvas.scale(scale, scale, x, y);
        }
        /*如果尺寸发生变化，我们要重新为表盘设置Bounds。
        这里的Bounds就相当于是为Drawable在View中确定位置，
        只是确定的方式更直接，直接在View中框出一个与Drawable大小相同的矩形，
        Drawable就在这个矩形里绘制自己。
        这里框出的矩形，是以(x,y)为中心的，宽高等于表盘图片的宽高的一个矩形，
        不用担心表盘图片太大绘制不完整，
        因为我们已经提前进行了缩放了。*/
        if (changed) {
            dial.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y + (h / 2));
        }
        dial.draw(canvas);
        canvas.save();
          /*根据小时数，以点(x,y)为中心旋转坐标系。
            如果你对来回旋转的坐标系感到头晕，摸不着头脑，
            建议你看一下**徐宜生**《安卓群英传》中讲解2D绘图部分中的Canvas一节。*/

        canvas.rotate(mHour / 12.0f * 360.0f, x, y);
        final Drawable hourHand = mHourHand;

        //同样，根据变化重新设置时针的Bounds
        if (changed) {
            w = hourHand.getIntrinsicWidth();
            h = hourHand.getIntrinsicHeight();

            /* 仔细体会这里设置的Bounds，我们所画出的矩形，
                同样是以(x,y)为中心的
                矩形，时针图片放入该矩形后，时针的根部刚好在点(x,y)处，
                因为我们之前做时针图片时，
                已经让图片中的时针根部在图片的中心位置了，
                虽然，看起来浪费了一部分图片空间（就是时针下半部分是空白的），
                但却换来了建模的简单性，还是很值的。*/
            hourHand.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y + (h / 2));
        }
        hourHand.draw(canvas);
        canvas.restore();

        canvas.save();
        //根据分针旋转坐标系
        canvas.rotate(mMinutes / 60.0f * 360.0f, x, y);
        final Drawable minuteHand = mMinuteHand;

        if (changed) {
            w = minuteHand.getIntrinsicWidth();
            h = minuteHand.getIntrinsicHeight();
            minuteHand.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y + (h / 2));
        }
        minuteHand.draw(canvas);
        canvas.restore();
        //最后，我们把缩放的坐标系复原。
        if (scaled) {
            canvas.restore();
        }
    }
}
