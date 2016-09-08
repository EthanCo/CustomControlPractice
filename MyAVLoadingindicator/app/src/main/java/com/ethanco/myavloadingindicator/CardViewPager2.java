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
public class CardViewPager2 extends ViewGroup {
    private int mScreenWidth;
    private Scroller mScroller;

    public CardViewPager2(Context context) {
        super(context);
        init(context);
    }

    public CardViewPager2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CardViewPager2(Context context, AttributeSet attrs, int defStyleAttr) {
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

        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View childView = getChildAt(i);
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
        }
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
                Log.i("Z-", "onTouchEvent : getScrollX():" + getScrollX() + " getWidth() - mScreenWidth:" + (getWidth() - mScreenWidth) + " dx:"+dx);
                if (getScrollX() != 0) {
                    if (getScrollX() + dx < 0) {
                        dx = 0;
                    }else if (getScrollX() + dx > getWidth() - mScreenWidth) {
                        dx = 0;
                    }
                } else {
                    if (dx < 0) {
                        dx = 0;
                    }
                }

//                if ((getScrollX() - dx) < 0) {
//                    return false;
//                }
//
//                if ((getScrollX() + dx) > getWidth()) {
//                    return false;
//                }
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
