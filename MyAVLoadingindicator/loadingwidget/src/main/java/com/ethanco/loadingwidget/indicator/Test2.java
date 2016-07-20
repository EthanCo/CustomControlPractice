package com.ethanco.loadingwidget.indicator;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by EthanCo on 2016/7/20.
 */
public class Test2 extends View {
    private static final int DEFAULT_SIZE = 50;
    private Paint mPaint;
    public static final float SCALE = 1.0f;
    private float[] scaleFloats = new float[]{SCALE, SCALE, SCALE};
    private ArrayList<Animator> animations;

    public Test2(Context context) {
        super(context);
        init(null, 0);
    }

    public Test2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public Test2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.FILL);
        initAnimation();
    }

    private void initAnimation() {
        animations = new ArrayList<>();
        int[] delays = new int[]{120, 240, 360};
        for (int i = 0; i < 3; i++) {
            ValueAnimator scaleAnim = ValueAnimator.ofFloat(1, 0.3F, 1);
            scaleAnim.setDuration(750);
            scaleAnim.setStartDelay(delays[i]);
            scaleAnim.setRepeatCount(-1);
            final int index = i;
            scaleAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    scaleFloats[index] = (float) valueAnimator.getAnimatedValue();
                    postInvalidate();
                }
            });
            animations.add(scaleAnim);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = measureDimension(dp2px(DEFAULT_SIZE), widthMeasureSpec);
        int height = measureDimension(dp2px(DEFAULT_SIZE), heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    private int measureDimension(int defaultSize, int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            result = Math.min(defaultSize, specSize);
        } else {
            result = defaultSize;
        }
        return result;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        for (Animator animator : animations) {
            animator.start();
        }
    }

    float x = 0;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float circleSpacing = 4; //圆之间的距离
        float radius = (Math.min(getWidth(), getHeight()) - circleSpacing * 2) / 6;
        float y = getHeight() / 2;

        for (int i = 0; i < 3; i++) {
            canvas.save();

            float translateX = x + radius * 2 + i + circleSpacing * i;
            canvas.translate(translateX, y);
            canvas.scale(scaleFloats[i], scaleFloats[i]);
            canvas.drawCircle(0, 0, radius, mPaint);
            canvas.restore();
        }
        x += 1*scaleFloats[0];
    }

    private int dp2px(int dpValue) {
        return (int) getContext().getResources().getDisplayMetrics().density * dpValue;
    }
}
