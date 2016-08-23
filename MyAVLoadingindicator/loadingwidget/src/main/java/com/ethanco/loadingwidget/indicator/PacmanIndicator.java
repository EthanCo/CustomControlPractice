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
 * Created by EthanCo on 2016/8/23.
 */
public class PacmanIndicator extends BaseIndicatorController {


    private static final long ANIM_DURATION = 650;
    private float translateX;
    private int alpha;
    private float degrees1;
    private float degrees2;

    @Override
    public void draw(Canvas canvas, Paint paint) {
        drawPacman(canvas, paint);
        drawPeas(canvas, paint);
    }

    private void drawPacman(Canvas canvas, Paint paint) {
        float x = getWidth() / 2.0F;
        float y = getHeight() / 2.0F;

        canvas.save();

        canvas.translate(x, y);
        canvas.rotate(degrees1);
        paint.setAlpha(255);
        RectF rectF1 = new RectF(-x / 1.7f, -y / 1.7f, x / 1.7f, y / 1.7f);
        canvas.drawArc(rectF1, 0, 270, true, paint);


        canvas.restore();

        canvas.save();
        canvas.translate(x, y);
        canvas.rotate(degrees2);
        paint.setAlpha(255);
        RectF rectF2 = new RectF(-x / 1.7f, -y / 1.7f, x / 1.7f, y / 1.7f);
        canvas.drawArc(rectF2, 90, 270, true, paint);
        canvas.restore();
    }

    private void drawPeas(Canvas canvas, Paint paint) {
        float radius = getWidth() / 11.0F;
        paint.setAlpha(alpha);
        canvas.drawCircle(translateX, getHeight() / 2.0F, radius, paint);
    }

    @Override
    public List<Animator> createAnimation() {
        List<Animator> animators = new ArrayList<>();
        float startT = getWidth() / 11;
        ValueAnimator transAnimator = ValueAnimator.ofFloat(getWidth() - startT, getWidth() / 2.0F);
        transAnimator.setDuration(ANIM_DURATION);
        transAnimator.setInterpolator(new LinearInterpolator());
        transAnimator.setRepeatCount(-1);
        transAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                translateX = (float) valueAnimator.getAnimatedValue();
                postInvalidate();
            }
        });

        ValueAnimator alphaAnimator = ValueAnimator.ofInt(255, 122);
        alphaAnimator.setDuration(ANIM_DURATION);
        alphaAnimator.setRepeatCount(-1);
        alphaAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                alpha = (int) valueAnimator.getAnimatedValue();
                postInvalidate();
            }
        });

        ValueAnimator rotateAnimator = ValueAnimator.ofFloat(0, 45, 0);
        rotateAnimator.setDuration(ANIM_DURATION);
        rotateAnimator.setRepeatCount(-1);
        rotateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                degrees1 = (float) valueAnimator.getAnimatedValue();
                degrees2 = -degrees1;
                postInvalidate();
            }
        });

        animators.add(transAnimator);
        animators.add(alphaAnimator);
        animators.add(rotateAnimator);

        return animators;
    }
}
