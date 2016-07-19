package com.ethanco.loadingwidget.indicator;

import android.animation.Animator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * 将View和Animator进行关联
 * 控制动画的状态
 * Created by EthanCo on 2016/7/19.
 */
public abstract class BaseIndicatorController {
    private WeakReference<View> mTargetRef;
    private List<Animator> mAnimators;

    public View getTarget() {
        return mTargetRef.get();
    }

    public void setTarget(View target) {
        this.mTargetRef = new WeakReference<>(target);
    }

    public int getWidth() {
        return null != getTarget() ? getTarget().getWidth() : 0;
    }

    public int getHeight() {
        return null != getTarget() ? getTarget().getHeight() : 0;
    }

    public void postInvalidate() {
        if (null != getTarget()) {
            getTarget().postInvalidate();
        }
    }

    public abstract void draw(Canvas canvas, Paint paint);

    public abstract List<Animator> createAnimation();

    public void initAnimation() {
        mAnimators = createAnimation();
        for (Animator animator : mAnimators) {
            animator.start();
        }
    }

    public void setAnimationStatus(AnimStatus animaStatus) {
        if (null == mAnimators) {
            return;
        }

        int count = mAnimators.size();
        for (int i = 0; i < count; i++) {
            Animator animator = mAnimators.get(i);
            boolean isRunning = animator.isRunning();
            switch (animaStatus) {
                case START:
                    if (!isRunning) {
                        animator.start();
                    }
                    break;
                case END:
                    if (isRunning) {
                        animator.end();
                    }
                    break;
                case CANCEL:
                    if (isRunning) {
                        animator.cancel();
                    }
                    break;
                default:
            }
        }
    }

    public enum AnimStatus {
        START, END, CANCEL
    }
}
