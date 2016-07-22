package com.ethanco.loadingwidget.indicator;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EthanCo on 2016/7/21.
 */
public class BallClipRotate extends BaseIndicatorController {

    private float scan = 1.0F;
    private float degrees;

    @Override
    public void draw(Canvas canvas, Paint paint) {

        if (1 != paint.getFlags()) {
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(3);
            paint.setFlags(1);
        }

        canvas.save();

        int spacing = 20;
        float radius = Math.min(getHeight(), getWidth()) / 2.0F - spacing;
        radius = radius * scan;
        //canvas.scale(scan, scan);
        float cx = getWidth() / 2.0F;
        float cy = getHeight() / 2.0F;
        //canvas.drawCircle(cx, cy, radius, paint);
        canvas.rotate(degrees, cx, cy);
        RectF rectF = new RectF(cx - radius, cy - radius, cx + radius, cy + radius);
        canvas.drawArc(rectF, -45, 270, false, paint);

        canvas.restore();
    }

    @Override
    public List<Animator> createAnimation() {
        List<Animator> animators = new ArrayList<>();
        ValueAnimator scanAnim = ValueAnimator.ofFloat(1, 0.6f, 0.5f, 1);
        scanAnim.setDuration(750);
        scanAnim.setRepeatCount(-1);
        scanAnim.setStartDelay(0);

        scanAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                scan = (float) valueAnimator.getAnimatedValue();
                postInvalidate();
            }
        });

        ValueAnimator rotateAnim = ValueAnimator.ofFloat(0, 180, 360);
        rotateAnim.setDuration(750);
        rotateAnim.setRepeatCount(-1);
        rotateAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                degrees = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });

        animators.add(scanAnim);
        animators.add(rotateAnim);
        return animators;
    }
}
