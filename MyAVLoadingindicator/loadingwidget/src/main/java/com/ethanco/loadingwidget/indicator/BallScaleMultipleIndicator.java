package com.ethanco.loadingwidget.indicator;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EthanCo on 2016/7/22.
 */
public class BallScaleMultipleIndicator extends BaseIndicatorController {
    private static final String TAG = "Z-";
    private float DEFAULT_SCAN = 1F;
    private int DEFAULT_ALPHA = 255;
    private int[] delays=new int[]{0, 200, 400};

    private float[] scans = new float[]{DEFAULT_SCAN, DEFAULT_SCAN, DEFAULT_SCAN};
    private int[] alphas = new int[]{DEFAULT_ALPHA, DEFAULT_ALPHA, DEFAULT_ALPHA};

    @Override
    public void draw(Canvas canvas, Paint paint) {
//        int circleSpacing = 4;
//        float radius = Math.min(getHeight(), getWidth()) / 2 - circleSpacing * 2;
//        float tx = getWidth() / 2.0F;
//        float ty = getHeight() / 2.0F;
//
//        for (int i = 0; i < 3; i++) {
//            canvas.save();
//            canvas.translate(tx, ty);
//            canvas.scale(scans[i], scans[i]);
//            paint.setAlpha(alphas[i]);
//            canvas.drawCircle(0, 0, radius, paint);
//            canvas.restore();
//        }

        float circleSpacing=4;
        for (int i = 0; i < 3; i++) {
            paint.setAlpha(alphas[i]);
            canvas.scale(scans[i],scans[i],getWidth()/2,getHeight()/2);
            canvas.drawCircle(getWidth()/2,getHeight()/2,getWidth()/2-circleSpacing,paint);
        }
    }

    @Override
    public List<Animator> createAnimation() {
//        List<Animator> animators = new ArrayList<>();
//
//        for (int i = 0; i < 3; i++) {
//            final int index = i;
//            final ValueAnimator scanAnim = ValueAnimator.ofFloat(0, 1);
//            scanAnim.setInterpolator(new LinearInterpolator());
//            scanAnim.setRepeatCount(-1);
//            scanAnim.setDuration(1000);
//            scanAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                @Override
//                public void onAnimationUpdate(ValueAnimator valueAnimator) {
//                    scans[index] = (float) valueAnimator.getAnimatedValue();
//                    postInvalidate();
//                }
//            });
//            scanAnim.setStartDelay(delays[i]);
//
//            ValueAnimator alphaAnim = ValueAnimator.ofInt(255, 0);
//            alphaAnim.setInterpolator(new LinearInterpolator());
//            alphaAnim.setRepeatCount(-1);
//            alphaAnim.setDuration(1000);
//            alphaAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                @Override
//                public void onAnimationUpdate(ValueAnimator valueAnimator) {
//                    alphas[index] = (int) valueAnimator.getAnimatedValue();
//                    postInvalidate();
//                }
//            });
//            alphaAnim.setStartDelay(delays[i]);
//
//            animators.add(scanAnim);
//            animators.add(alphaAnim);
//        }
//
//
//        return animators;

        List<Animator> animators=new ArrayList<>();
        long[] delays=new long[]{0, 200, 400};
        for (int i = 0; i < 3; i++) {
            final int index=i;
            ValueAnimator scaleAnim=ValueAnimator.ofFloat(0,1);
            scaleAnim.setInterpolator(new LinearInterpolator());
            scaleAnim.setDuration(1000);
            scaleAnim.setRepeatCount(-1);
            scaleAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    scans[index] = (float) animation.getAnimatedValue();
                    postInvalidate();
                }
            });
            scaleAnim.setStartDelay(delays[i]);
            scaleAnim.start();

            ValueAnimator alphaAnim=ValueAnimator.ofInt(255,0);
            alphaAnim.setInterpolator(new LinearInterpolator());
            alphaAnim.setDuration(1000);
            alphaAnim.setRepeatCount(-1);
            alphaAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    alphas[index] = (int) animation.getAnimatedValue();
                    postInvalidate();
                }
            });
            scaleAnim.setStartDelay(delays[i]);
            alphaAnim.start();

            animators.add(scaleAnim);
            animators.add(alphaAnim);
        }
        return animators;
    }
}
