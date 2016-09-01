package com.ethanco.myavloadingindicator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * @Description 贝塞尔曲线
 * https://segmentfault.com/a/1190000000721127
 * Created by EthanCo on 2016/9/1.
 */
public class BezierCurveView extends View {
    private Paint mPaint;

    public BezierCurveView(Context context) {
        super(context);

        init(context);
    }

    public BezierCurveView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    private void init(Context context){
        mPaint = new Paint();
        mPaint.setColor(Color.BLUE);
        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(8);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Path mPath = new Path();

        //moveTo 不会进行绘制，只用于移动移动画笔

        //lineTo 用于进行直线绘制

        mPath.moveTo(100, 100);
        mPath.lineTo(300, 300);
        canvas.drawPath(mPath, mPaint);

        //quadTo 用于绘制圆滑曲线，即贝塞尔曲线
        //mPath.quadTo(x1, y1, x2, y2) (x1,y1) 为控制点，(x2,y2)为结束点。
        mPath.moveTo(100, 500);
        mPath.quadTo(300, 100, 600, 500);
        canvas.drawPath(mPath, mPaint);

        //cubicTo 同样是用来实现贝塞尔曲线的
        //cubicTo(x1, y1, x2, y2, x3, y3) (x1,y1) 为控制点，(x2,y2)为控制点，(x3,y3) 为结束点。

        mPath.moveTo(0, 0);
        mPath.cubicTo(100, 500, 300, 100, 600, 500);
        canvas.drawPath(mPath,mPaint);

        //arcTo 用于绘制弧线（实际是截取圆或椭圆的一部分）
        //arcTo(ovalRectF, startAngle, sweepAngle) , ovalRectF为椭圆的矩形，startAngle 为开始角度，sweepAngle 为结束角度
        RectF mRectF = new RectF(10, 10, 600, 600);
        mPath.arcTo(mRectF, 0, 90);
        canvas.drawPath(mPath, mPaint);
    }
}
