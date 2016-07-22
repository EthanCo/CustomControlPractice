package com.ethanco.loadingwidget.indicator;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EthanCo on 2016/7/22.
 */
public class SquareSpinIndicator extends BaseIndicatorController {
    @Override
    public void draw(Canvas canvas, Paint paint) {
        canvas.drawRect(new RectF(getWidth() / 5F, getHeight() / 5F, getWidth() * 4F / 5F, getHeight() * 4F / 5F), paint);
    }

    @Override
    public List<Animator> createAnimation() {
        List<Animator> animators = new ArrayList<>();

        //在 2500毫秒内执行 0, 180, 180, 0, 0
        //0 -> 180 ， 180 -> 180 ， 180 -> 0 ， 0 -> 0
        //PropertyValuesHolder p1 = PropertyValuesHolder.ofFloat("rotationX", 0, 180, 180, 0, 0); //上下 3D 旋转 ， 从 小 到大 -> 下 到 上 ， 从 大 到小 -> 上 到 下
        //PropertyValuesHolder p2 = PropertyValuesHolder.ofFloat("rotationY", 0, 0, 180, 180, 0); //左右 3D 旋转 ， 从 小 到大 -> 左 到 右 ， 从 大 到小 -> 右 到 左

        //PropertyValuesHolder p1 = PropertyValuesHolder.ofFloat("rotationX", 0, 180, 180, 0, 0);
        //PropertyValuesHolder p2 = PropertyValuesHolder.ofFloat("rotationY", 0, 180, 180, 180, 0);

        //PropertyValuesHolder p1 = PropertyValuesHolder.ofFloat("rotationX", 0, 180, 0);
        //PropertyValuesHolder p2 = PropertyValuesHolder.ofFloat("rotationY", 0, 180);

        PropertyValuesHolder p1 = PropertyValuesHolder.ofFloat("rotationX", 0, 180, 180, 0, 0); //上下 3D 旋转
        PropertyValuesHolder p2 = PropertyValuesHolder.ofFloat("rotationY", 0, 0, 180, 180, 0); //左右 3D 旋转

        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(getTarget(), p1, p2);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(-1);
        animator.setDuration(2500);
        animator.start();
        animators.add(animator);
        return animators;
    }
}
