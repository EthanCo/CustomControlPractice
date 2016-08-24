package com.ethanco.myavloadingindicator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by EthanCo on 2016/8/23.
 */
public class MyView extends View {
    private Paint mPaint;

    public MyView(Context context) {
        super(context);
        init(context);
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mPaint = new Paint();
        mPaint.setColor(Color.BLUE);
        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float left = 0;
        float top = 0;
        float diameter = 100;

        RectF rectF = new RectF(left, top, diameter, diameter);
        canvas.drawArc(rectF, 0, 270, true, mPaint);

        top += diameter;
        rectF.offsetTo(left, top);
        canvas.drawArc(rectF, 0, 210, true, mPaint);

        top += diameter;
        rectF.offsetTo(left, top);
        canvas.drawArc(rectF, 0, 90, true, mPaint);

        top += diameter;
        rectF.offsetTo(left, top);
        canvas.drawArc(rectF, 90, 90, true, mPaint);

        top += diameter;
        rectF.offsetTo(left, top);
        canvas.drawArc(rectF, 90, 90, false, mPaint);

        mPaint.setStyle(Paint.Style.STROKE);

        top += diameter+30;
        rectF.offsetTo(left, top);
        canvas.drawArc(rectF, 0, 270, true, mPaint);

        top += diameter;
        rectF.offsetTo(left, top);
        canvas.drawArc(rectF, 0, 210, true, mPaint);

        top += diameter;
        rectF.offsetTo(left, top);
        canvas.drawArc(rectF, 0, 90, true, mPaint);

        top += diameter;
        rectF.offsetTo(left, top);
        canvas.drawArc(rectF, 90, 90, true, mPaint);

        top += diameter;
        rectF.offsetTo(left, top);
        canvas.drawArc(rectF, 90, 90, false, mPaint);
    }
}
