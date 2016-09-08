package com.ethanco.myavloadingindicator;

import android.content.Context;
import android.support.v4.widget.ScrollerCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Interpolator;

/**
 * Created by EthanCo on 2016/9/8.
 */
public class SlideViewPager extends ViewGroup {
    private final float mMarginLeftRight;
    private final float mGutterSize;
    private ScrollerCompat mScroller;
    private int mTouchSlop;
    private static float SCALE_RATIO = 0.8f;
    private int mSwitchSize = 0;

    private int mMinimumVelocity;
    private int mMaximumVelocity;
    private int mFlingDistance;

    private static final int MIN_FLING_VELOCITY = 400; // dips
    private static final int MIN_DISTANCE_FOR_FLING = 25; // dips

    public SlideViewPager(Context context) {
        this(context, null);
    }

    public SlideViewPager(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mMarginLeftRight = 150F;
        mGutterSize = 90F;

        init(context);
    }

    /**
     * Interpolator defining the animation curve for mScroller
     */
    private static final Interpolator sInterpolator = new Interpolator() {
        public float getInterpolation(float t) {
            t -= 1.0f;
            return t * t * t * t * t + 1.0f;
        }
    };

    private void init(Context context) {
        //解决自定义View的onDraw（）方法不被执行问题
        setWillNotDraw(false);
        mScroller = ScrollerCompat.create(context, sInterpolator);
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        mTouchSlop = viewConfiguration.getScaledTouchSlop();
        final float density = context.getResources().getDisplayMetrics().density;
        mMinimumVelocity = (int) (MIN_FLING_VELOCITY * density);
        mMaximumVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        mFlingDistance = (int) (MIN_DISTANCE_FOR_FLING * density);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec),getDefaultSize(0, heightMeasureSpec));
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();

        int childCount = getChildCount();
        int width = measuredWidth - (int) (mMarginLeftRight * 2);
        int height = measuredHeight;
        int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        for (int i = 0; i < childCount; i++) {
            getChildAt(i).measure(childWidthMeasureSpec, childHeightMeasureSpec);
        }
        mSwitchSize = width;
        confirmScaleRatio(width, mGutterSize);
    }

    private void confirmScaleRatio(int width, float gutterSize) {
        SCALE_RATIO = (width - gutterSize * 2) / width;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int originLeft = (int) mMarginLeftRight;

        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            int left = originLeft + child.getMeasuredWidth() * i;
            int right = originLeft + child.getMeasuredWidth() * (i + 1);
            int bottom = child.getMeasuredHeight();
            child.layout(left, 0, right, bottom);
            if (i != 0) {
                child.setScaleX(SCALE_RATIO);
                child.setScaleY(SCALE_RATIO);
                child.setTag(SCALE_RATIO);
            } else {
                child.setTag(1.0f);
            }
        }
    }
}
