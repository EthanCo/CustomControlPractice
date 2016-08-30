package com.ethanco.buttonviewlib;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import com.ethanco.buttonviewlib.utils.DisplayUtil;

/**
 * Created by EthanCo on 2016/8/30.
 */
public class ButtonView extends View implements View.OnClickListener {
    public static final int DEFAULT_DURATION = 500;
    private int paddingOffset;
    private int width = 100;
    private int height = 100;
    private float drawWidth;
    private int bigF;
    private float smallF;
    private Paint strokePaint;
    private TextPaint textPaint;
    private float startAngle = 5f;
    private Paint arcPaint;
    private ValueAnimator valueAnimator;
    private String text = "点我";

    private State state = State.RECT2CIRCLE;

    //    记录状态
    public enum State {
        RECT2CIRCLE,
        CIRCLE2RECT
    }

    public ButtonView(Context context) {
        this(context, null);
    }

    public ButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        paddingOffset = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, paddingOffset, dm);
        initPaint(context);
        setOnClickListener(this);
    }

    private void initPaint(Context context) {
        strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        strokePaint.setColor(Color.RED);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(DisplayUtil.dip2px(context, 2));

        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.LINEAR_TEXT_FLAG);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(Color.RED);
        textPaint.setTextSize(DisplayUtil.sp2px(context, 25));
        textPaint.setTypeface(Typeface.MONOSPACE);

        arcPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        arcPaint.setColor(Color.BLUE);
        arcPaint.setStyle(Paint.Style.STROKE);
        arcPaint.setStrokeWidth(DisplayUtil.dip2px(context, 2));

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getViewWidth(widthMeasureSpec);
        height = getViewHeight(heightMeasureSpec);

        drawWidth = width;
        setMeasuredDimension(width, height);
        bigF = width - getPaddingLeft() - getPaddingRight();
        smallF = (width + height) / 2F;
    }

    private int getViewHeight(int heightMeasureSpec) {
        int result = 0;
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = height;
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
            result = width;
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size);
            }
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawRoundRect(getRectF(), getRectF().height() / 2, getRectF().height() / 2, strokePaint);
        if (drawWidth != smallF) {
            if (drawWidth >= bigF) {
                canvas.drawText(text, width / 2, height / 2 + Math.abs((textPaint.descent() + textPaint.ascent()) / 2), textPaint);
            }
        } else {
            canvas.drawArc(getRectF(), startAngle, 45, false, arcPaint);
            startAngle += 5;
            invalidate();
        }
    }

    private RectF getRectF() {
        float strokeWidth = strokePaint.getStrokeWidth();
        return new RectF(width - drawWidth + getPaddingLeft() + paddingOffset + strokeWidth,
                getPaddingTop() + paddingOffset + strokeWidth,
                drawWidth - getPaddingRight() - paddingOffset - strokeWidth,
                height - getPaddingBottom() - paddingOffset - strokeWidth);
    }

    private void circle2Rect() {
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        valueAnimator = ValueAnimator.ofFloat(smallF, bigF);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                drawWidth = (float) animation.getAnimatedValue();
                if (drawWidth == bigF) {
                    state = State.RECT2CIRCLE;
                    setFocusable(true);
                    setEnabled(true);
                    return;
                }
                postInvalidate();
            }
        });
        valueAnimator.setDuration(DEFAULT_DURATION);
        valueAnimator.setInterpolator(new AccelerateInterpolator(1.0f));
        valueAnimator.start();
    }

    private void rect2Circle() {
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        valueAnimator = ValueAnimator.ofFloat(bigF, smallF);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                drawWidth = (float) animation.getAnimatedValue();
                if (drawWidth == smallF) {
                    state = State.CIRCLE2RECT;
                    setFocusable(true);
                    setEnabled(true);
                    return;
                }

                postInvalidate();
            }
        });
        valueAnimator.setDuration(DEFAULT_DURATION);
        valueAnimator.setInterpolator(new AccelerateInterpolator(1.0f));
        valueAnimator.start();
    }

    @Override
    public void onClick(View v) {
        Log.d("aa", "on click!");
        v.setFocusable(false);
        v.setEnabled(false);
        switch (state) {
            case RECT2CIRCLE:
                rect2Circle();
                break;
            case CIRCLE2RECT:
                circle2Rect();
                break;
        }
    }
}
