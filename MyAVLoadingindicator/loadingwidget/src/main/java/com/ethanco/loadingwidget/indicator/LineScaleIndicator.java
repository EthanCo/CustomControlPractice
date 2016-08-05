package com.ethanco.loadingwidget.indicator;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EthanCo on 2016/8/5.
 */
public class LineScaleIndicator extends BaseIndicatorController {
    public static final float SCALE = 1.0f;

    float[] scaleYFloats = new float[]{SCALE,
            SCALE,
            SCALE,
            SCALE,
            SCALE,};

    @Override
    public void draw(Canvas canvas, Paint paint) {
        float translateX = getWidth() / 11;
        float translateY = getHeight() / 2;

        for (int i = 0; i < 5; i++) {
            canvas.save();

            canvas.translate((2 + i * 2) * translateX - translateX / 2, translateY);
            canvas.scale(SCALE, scaleYFloats[i]);
            RectF reacF = new RectF(-translateX / 2F, -translateY / 2.1F, translateX / 2F, translateY / 2.1F);
            canvas.drawRoundRect(reacF, 5, 5, paint);

            canvas.restore();
        }
    }

    @Override
    public List<Animator> createAnimation() {
        List<Animator> animators = new ArrayList<>();
        long[] delays = new long[]{100, 200, 300, 400, 500};

        for (int i = 0; i < 5; i++) {
            ValueAnimator scaleAnim = ValueAnimator.ofFloat(1F, 0.4F, 1F);
            scaleAnim.setDuration(1000);
            scaleAnim.setRepeatCount(-1);
            scaleAnim.setStartDelay(delays[i]);
            final int index = i;
            scaleAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    scaleYFloats[index] = (float) valueAnimator.getAnimatedValue();
                    postInvalidate();
                }
            });
            animators.add(scaleAnim);
        }

        return animators;
    }
}
