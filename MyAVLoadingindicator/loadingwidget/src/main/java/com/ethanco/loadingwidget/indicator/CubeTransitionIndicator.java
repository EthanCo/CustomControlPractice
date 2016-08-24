package com.ethanco.loadingwidget.indicator;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EthanCo on 2016/8/24.
 */
public class CubeTransitionIndicator extends BaseIndicatorController {
    private static final long DURATION = 1600;
    float[] translateX = new float[2], translateY = new float[2];
    float degrees, scaleFloat = 1.0f;

    @Override
    public void draw(Canvas canvas, Paint paint) {
        float rWidth = getWidth() / 5.0F;
        float rHeight = getHeight() / 5.0F;

        for (int i = 0; i < 2; i++) {
            canvas.save();
            canvas.translate(translateX[i], translateY[i]);
            canvas.rotate(degrees);
            canvas.scale(scaleFloat, scaleFloat);
            RectF rectF = new RectF(-rWidth / 2.0F, -rHeight / 2.0F, rWidth / 2.0F, rHeight / 2.0F);
            canvas.drawRect(rectF, paint);
            canvas.restore();
        }
    }

    @Override
    public List<Animator> createAnimation() {
        List<Animator> animators = new ArrayList<>();
        float startX = getWidth() / 5.0F;
        float startY = getHeight() / 5.0F;
        for (int i = 0; i < 2; i++) {
            final int index = i;
            translateX[index] = startX;

            ValueAnimator translationXAnim;
            if (i == 0) {
                translationXAnim = ValueAnimator.ofFloat(startX,
                        getWidth() - startX, getWidth() - startX, startX, startX);
            } else {
                translationXAnim = ValueAnimator.ofFloat(getWidth() - startX,
                        startX, startX, getWidth() - startX, getWidth() - startX);
            }

            translationXAnim.setInterpolator(new LinearInterpolator());
            translationXAnim.setDuration(DURATION);
            translationXAnim.setRepeatCount(-1);
            translationXAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    translateX[index] = (float) valueAnimator.getAnimatedValue();
                    postInvalidate();
                }
            });
            translateY[index] = startY;
            ValueAnimator translationYAnim = null;
            if (i == 0) {
                translationYAnim = ValueAnimator.ofFloat(startY, startY, getHeight() - startY, getHeight() - startY, startY);
            } else {
                translationYAnim = ValueAnimator.ofFloat(getHeight() - startY, getHeight() - startY, startY, startY, getHeight() - startY);
            }

            translationYAnim.setDuration(1600);
            translationYAnim.setInterpolator(new LinearInterpolator());
            translationYAnim.setRepeatCount(-1);
            translationYAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    translateY[index] = (float) animation.getAnimatedValue();
                    postInvalidate();
                }
            });

            animators.add(translationXAnim);
            animators.add(translationYAnim);
        }

        ValueAnimator scaleAnim = ValueAnimator.ofFloat(1, 0.5f, 1, 0.5f, 1);
        scaleAnim.setDuration(1600);
        scaleAnim.setInterpolator(new LinearInterpolator());
        scaleAnim.setRepeatCount(-1);
        scaleAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                scaleFloat = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });

        ValueAnimator rotateAnim = ValueAnimator.ofFloat(0, 180, 360, 1.5f * 360, 2 * 360);
        rotateAnim.setDuration(1600);
        rotateAnim.setInterpolator(new LinearInterpolator());
        rotateAnim.setRepeatCount(-1);
        rotateAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                degrees = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });

        animators.add(scaleAnim);
        animators.add(rotateAnim);

        return animators;
    }
}
