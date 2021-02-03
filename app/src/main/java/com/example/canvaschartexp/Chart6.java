package com.example.canvaschartexp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class Chart6 extends View {
    int width, height;
    Paint paint;
    int radius;
    int leftX, rightX, centerY;

    float leftR = 114.5f;//左边半径
    float rightR = 133.25f;//右边半径
    int startAngle = 90;//内圈圆旋转


    public int getStartAngle() {
        return startAngle;
    }

    public void setStartAngle(int startAngle) {
        this.startAngle = startAngle;
    }

    public Chart6(Context context) {
        this(context,null);
    }

    public Chart6(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public Chart6(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private int getMySize(int defaultSize, int mesureSpec) {
        int mySize = defaultSize;
        int mode = MeasureSpec.getMode(mesureSpec);
        int size = MeasureSpec.getSize(mesureSpec);
        switch (mode) {
            case MeasureSpec.UNSPECIFIED:
                mySize = defaultSize;
                break;
            case MeasureSpec.AT_MOST:
                mySize = size;
                break;
            case MeasureSpec.EXACTLY:
                mySize = size;
                break;
        }
        return mySize;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        float density = getResources().getDisplayMetrics().density;
        width = getMySize(100, widthMeasureSpec);
        height = getMySize(50, heightMeasureSpec);
        if (width > 2 * height) {
            width = 2 * height;
        } else {
            height = width / 2;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(1);//笔宽
        paint.setAntiAlias(true);//消除锯齿
        paint.setStyle(Paint.Style.FILL);//画笔样式
        paint.setColor(Color.parseColor("#2195c7"));//画笔颜色


        radius = height / 2;
        leftX = centerY = radius;
        rightX = 3* radius;

        getActionRadius(this.x);
        /**
         * 绘制最底层圆
         */
        canvas.drawCircle(leftX, centerY, radius, paint);
        canvas.drawCircle(rightX, centerY, radius, paint);
        paint.setColor(0xff222222);
        paint.setStyle(Paint.Style.STROKE);

        for (int i = 1; i < 11; i++) {
            canvas.drawCircle(leftX, centerY, radius * i / 10, paint);
            canvas.drawCircle(rightX, centerY, radius * i / 10, paint);
        }

        /**
         * 绘制灰色圆盘
         */

        paint.setColor(0xff555555);
        paint.setStyle(Paint.Style.FILL);//画笔样式
        canvas.drawCircle(leftX, centerY, radius / 3, paint);
        canvas.drawCircle(rightX, centerY, radius / 3, paint);
        /**
         * 绘制最里的圆
         */
        paint.setColor(0xffffffff);
        canvas.drawCircle(leftX, centerY, radius / 5, paint);
        canvas.drawCircle(rightX, centerY, radius / 5, paint);
        /**
         * 给里面的圆填充颜色
         */
        paint.setColor(0xff000000);
        RectF leftRectf1 = new RectF(radius * 4 / 5, radius * 4 / 5, radius * 6 / 5, radius * 6 / 5);
        canvas.drawArc(leftRectf1, startAngle, 90, true, paint);
        canvas.drawArc(leftRectf1, startAngle * 3, 90, true, paint);
        RectF rightRectf1 = new RectF(height + radius * 4 / 5, radius * 4 / 5, height + radius * 6 / 5, radius * 6 / 5);
        canvas.drawArc(rightRectf1, startAngle, 90, true, paint);
        canvas.drawArc(rightRectf1, startAngle * 3, 90, true, paint);

        /**
         * 绘制可以改变的线条，履带
         */
        RectF leftF = new RectF(radius - leftR, radius - leftR, radius + leftR, radius + leftR);
        paint.setColor(0xff00ff00);
        paint.setStrokeWidth(height / 30);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawArc(leftF, 90, 180, false, paint);
        RectF rightF = new RectF(height + radius - rightR, radius - rightR, height + radius + rightR, radius + rightR);
        canvas.drawArc(rightF, 270, 180, false, paint);
        canvas.drawLine(radius - height / 100, radius - leftR, height + radius + height / 100, radius - rightR, paint);
        canvas.drawLine(radius - height / 100, radius + leftR, height + radius + height / 100, radius + rightR, paint);


    }

    public float x;

    @Override
    public float getX() {
        return x;
    }

    @Override
    public void setX(float x) {
        this.x = x;

    }

    private void getActionRadius(float x) {
        float cancelWidth = height / 30 / 2;
        float maxRadius = radius - cancelWidth;
        float minRadius = maxRadius / 2.63f;
        float diferNum = maxRadius - minRadius;
        float centerNum = minRadius + diferNum / 2;
        float num = diferNum / 4;
        if (2 < x && x < 3) {
            leftR = maxRadius / x;
            rightR = minRadius * x;
        } else if (1 <= x && x <= 2) {
            leftR = centerNum - num * (x - 1);
            rightR = centerNum + num * (x - 1);
        } else if (0 < x && x < 1) {
            leftR = radius - num * 2 * x;
            rightR = minRadius + num * 2 * x;
        }

    }
}

