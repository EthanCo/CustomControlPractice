package com.ethanco.loadingwidget.indicator;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EthanCo on 2016/7/22.
 */
public class BallPulseRiseIndicator extends BaseIndicatorController {
    @Override
    public void draw(Canvas canvas, Paint paint) {
//        float radius = Math.min(getHeight(), getWidth()) / 15.0F;
//
//        canvas.drawCircle(getWidth() / 4.0F, getHeight() / 4.0F, radius, paint);
//        canvas.drawCircle(getWidth() * 3 / 4.0F, getHeight() / 4.0F, radius, paint);
//
//        canvas.drawCircle(getWidth() / 8.0F, getHeight() * 3 / 4.0F, radius, paint);
//        canvas.drawCircle(getWidth() / 2.0F, getHeight() * 3 / 4.0F, radius, paint);
//        canvas.drawCircle(getWidth() * 7 / 8.0F, getHeight() * 3 / 4.0F, radius, paint);

        canvas.save();

        float radius = getWidth() / 10;
        canvas.drawCircle(getWidth() / 4, radius * 2, radius, paint);
        canvas.drawCircle(getWidth() * 3 / 4, radius * 2, radius, paint);

        canvas.drawCircle(radius, getHeight() - 2 * radius, radius, paint);
        canvas.drawCircle(getWidth() / 2, getHeight() - 2 * radius, radius, paint);
        canvas.drawCircle(getWidth() - radius, getHeight() - 2 * radius, radius, paint);

        canvas.restore();
    }

    @Override
    public List<Animator> createAnimation() {
        List<Animator> animators = new ArrayList<>();
        PropertyValuesHolder p1 = PropertyValuesHolder.ofFloat("rotationX", 0, 360);
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(getTarget(), p1);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(1500);
        animator.setRepeatCount(-1);
        animators.add(animator);
        return animators;
    }
}
