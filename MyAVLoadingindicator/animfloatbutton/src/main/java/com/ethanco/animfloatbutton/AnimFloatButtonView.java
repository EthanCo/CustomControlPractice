package com.ethanco.animfloatbutton;

import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;

import com.ethanco.animfloatbutton.utils.DisplayUtil;

public class AnimFloatButtonView extends View {


    private int mWidth;
    private int mHeight;
    private int mXCenter;
    private int mYCenter;
    private float mRadius;
    private int finalX1;
    private int x1;
    private int y1;
    private int x2;
    private int y2;
    private int x3;
    private int y3;
    private int finalX4;
    private int x4;
    private int y4;
    private Paint mPaintPrimary;
    private Paint mPaintSecond;
    private final int STATE_OPEN = 0;
    private final int STATE_CLOSE = 1;
    private int STATE = STATE_OPEN;

    public AnimFloatButtonView(Context context) {
        super(context);

        init(context);
    }

    public AnimFloatButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    private void init(Context context) {
        //default width and height
        mWidth = DisplayUtil.dip2px(context, 45);
        mHeight = DisplayUtil.dip2px(context, 45);

        initPaint(context);
    }

    private void initPaint(Context context) {
        mPaintPrimary = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintPrimary.setColor(Color.BLUE);

        mPaintSecond = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaintSecond.setColor(Color.WHITE);
        mPaintSecond.setStrokeWidth(DisplayUtil.dip2px(context, 5));
        mPaintSecond.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = getViewWidth(widthMeasureSpec);
        mHeight = getViewHeight(heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);

        mXCenter = mWidth / 2;
        mYCenter = mHeight / 2;
        mRadius = Math.min(mXCenter, mYCenter);

        float lineHalt = mRadius / 4.0F;

        finalX1 = (int) (mXCenter + lineHalt * Math.sqrt(2));
        y1 = (int) (mYCenter - lineHalt * Math.sqrt(2));
        x2 = (int) (mXCenter + lineHalt * Math.sqrt(2));
        y2 = (int) (mYCenter + lineHalt * Math.sqrt(2));
        x3 = (int) (mXCenter - lineHalt * Math.sqrt(2));
        y3 = (int) (mYCenter + lineHalt * Math.sqrt(2));
        finalX4 = (int) (mXCenter - lineHalt * Math.sqrt(2));
        y4 = (int) (mYCenter - lineHalt * Math.sqrt(2));

        x1 = finalX1;
        x4 = finalX4;
    }

    private int getViewHeight(int heightMeasureSpec) {
        int result = 0;
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = mHeight;
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size);
            }
        }
        return result;
    }

    private int getViewWidth(int widthMeasureSpec) {
        int result = 0;
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = mWidth;
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size);
            }
        }
        return result;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(mXCenter, mYCenter, mRadius, mPaintPrimary);

        canvas.drawLine(x1, y1, x3, y3, mPaintSecond);
        canvas.drawLine(x2, y2, x4, y4, mPaintSecond);
    }

    private void performAnimateX1(final int start, final int end) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(1, 100);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            private IntEvaluator mEvaluator = new IntEvaluator();

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                int currentValue = (Integer) animator.getAnimatedValue();
                float fraction = currentValue / 100f;
                x1 = mEvaluator.evaluate(fraction, start, end);
                invalidate();
            }
        });
        valueAnimator.setInterpolator(new AnticipateOvershootInterpolator());
        valueAnimator.setDuration(1000).start();
    }

    private void performAnimateX4(final int start, final int end) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(1, 100);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            private IntEvaluator mEvaluator = new IntEvaluator();

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                int currentValue = (Integer) animator.getAnimatedValue();
                float fraction = currentValue / 100f;
                x4 = mEvaluator.evaluate(fraction, start, end);
                invalidate();
            }
        });
        valueAnimator.setInterpolator(new AnticipateOvershootInterpolator());
        valueAnimator.setDuration(1000).start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                int clickX = (int) event.getRawX();
                int clickY = (int) event.getRawY();

                int[] location = new int[2];
                this.getLocationInWindow(location);
                //XY是控件在屏幕的XY，而不是定义的圆心
                float cenY = location[1] + mYCenter;

                if (Math.pow(mXCenter - clickX, 2) + Math.pow(cenY - clickY, 2) <= Math.pow(mRadius, 2)) {
                    doAnim();
                    return true;
                }
                break;

            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        return true;
    }

    private void doAnim() {
        switch (STATE) {
            case STATE_OPEN:
                //当前是打开，关闭操作
                //  (View 的宽 - 圆半径）/2 - left
                performAnimateX1(x1, mXCenter);
                performAnimateX4(x4, mXCenter);

                STATE = STATE_CLOSE;
                break;
            case STATE_CLOSE:
                //当前是关闭，打开操作
                performAnimateX1(mXCenter, finalX1);
                performAnimateX4(mXCenter, finalX4);

                STATE = STATE_OPEN;
                break;
        }
    }
}
