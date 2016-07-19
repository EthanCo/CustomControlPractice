package com.ethanco.loadingwidget.indicator;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jack on 2015/10/16.
 */
public class BallPulseIndicator extends BaseIndicatorController {

    public static final float SCALE = 1.0f;

    //scale x ,y
    private float[] scaleFloats = new float[]{SCALE,
            SCALE,
            SCALE};


    @Override
    public void draw(Canvas canvas, Paint paint) {
        float circleSpacing = 4;
        //半径
        float radius = (Math.min(getWidth(), getHeight()) - circleSpacing * 2) / 6;
        //x坐标
        float x = getWidth() / 2 - (radius * 2 + circleSpacing);
        //y坐标
        float y = getHeight() / 2;
        for (int i = 0; i < 3; i++) {
            //保存画布，将之前所有已绘制图像保存起来，让后续的操作就好像在一个新的图层上操作一样，这一点与PhotoShop中的图层理解基本一致
            canvas.save();
            //计算出平移距离
            float translateX = x + (radius * 2) * i + circleSpacing * i;
            //坐标的平移
            canvas.translate(translateX, y);
            //扩大。x为水平方向的放大倍数，y为竖直方向的放大倍数。
            canvas.scale(scaleFloats[i], scaleFloats[i]);
            canvas.drawCircle(0, 0, radius, paint);
            //可以理解为Photoshop中的合并图层操作，将我们在Save()之后绘制的所有图像与save()之前的图像进行合并
            canvas.restore();
        }
    }

    @Override
    public List<Animator> createAnimation() {
        //Animator 列表
        List<Animator> animators = new ArrayList<>();
        //延时
        int[] delays = new int[]{120, 240, 360};
        for (int i = 0; i < 3; i++) {
            final int index = i;

            ValueAnimator scaleAnim = ValueAnimator.ofFloat(1, 0.3f, 1);

            scaleAnim.setDuration(750);
            scaleAnim.setRepeatCount(-1); //无限循环
            scaleAnim.setStartDelay(delays[i]); //设置延迟

            scaleAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    scaleFloats[index] = (float) animation.getAnimatedValue();
                    postInvalidate();

                }
            });
            //scaleAnim.start();
            animators.add(scaleAnim);
        }
        return animators;
    }

}
