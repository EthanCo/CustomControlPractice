package com.ethanco.myavloadingindicator;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Scroller;

/**
 * Created by EthanCo on 2016/9/6.
 */
public class CardViewPager extends ViewGroup {
    private int mScreenWidth;
    private Scroller mScroller;
    int mPadding = 20;
    private int mSwitchSize;

    public CardViewPager(Context context) {
        super(context);
        init(context);
    }

    public CardViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CardViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        mScreenWidth = dm.widthPixels;
        mScroller = new Scroller(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

//        int count = getChildCount();
//        for (int i = 0; i < count; i++) {
//            View childView = getChildAt(i);
//            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
//        }

        //设置默认大小，让当前的ViewGroup大小为充满屏幕
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec),
                getDefaultSize(0, heightMeasureSpec));
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();

        int childCount = getChildCount();
        //每个子child的宽度为屏幕的宽度减去与两边的间距
        int width = measuredWidth - (int) (mPadding * 2);
        int height = measuredHeight;
        int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        for (int i = 0; i < childCount; i++) {
            getChildAt(i).measure(childWidthMeasureSpec, childHeightMeasureSpec);
        }
        //切换一个page需要移动的距离为一个page的宽度
        mSwitchSize = width;
        //确定缩放比例
        //confirmScaleRatio(width, mGutterSize);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        LayoutParams mlp = getLayoutParams();
        int count = getChildCount();
        mlp.width = count * mScreenWidth;
        setLayoutParams(mlp);

        for (int i = 0; i < count; i++) {
            View childView = getChildAt(i);
            if (childView.getVisibility() != View.GONE) {
                childView.layout(mScreenWidth * i, t, mScreenWidth * (i + 1), b);
            }
        }
    }

    private int mStartX;
    private int mEndX;
    private int mLastX;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = x;
                mStartX = getScrollX();
                break;
            case MotionEvent.ACTION_MOVE:
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                int dx = mLastX - x;
                Log.i("Z-", "onTouchEvent : getScrollX():" + getScrollX() + " getWidth() - mScreenWidth:" + (getWidth() - mScreenWidth) + "");
                if (getScrollX() < 0) {
                    dx = 0;
                }
                if (getScrollX() > getWidth() - mScreenWidth) {
                    dx = 0;
                }
                scrollBy(dx, 0);
                mLastX = x;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mEndX = getScrollX();
                int dScrollX = mEndX - mStartX;
                if (dScrollX > 0) {
                    if (dScrollX < mScreenWidth / 3.5) {
                        mScroller.startScroll(getScrollX(), 0, -dScrollX, 0);
                    } else {
                        mScroller.startScroll(getScrollX(), 0, mScreenWidth - dScrollX, 0);
                    }
                } else {
                    if (-dScrollX < mScreenWidth / 3.5) {
                        mScroller.startScroll(getScrollX(), 0, -dScrollX, 0);
                    } else {
                        mScroller.startScroll(getScrollX(), 0, -mScreenWidth - dScrollX, 0);
                    }
                }
                break;
        }

        invalidate();
        //postInvalidate();
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), 0);
            postInvalidate();
        }
    }
}
