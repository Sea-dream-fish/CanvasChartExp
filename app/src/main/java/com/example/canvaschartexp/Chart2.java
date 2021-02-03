package com.example.canvaschartexp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

public class Chart2 extends View {
    public Chart2(Context context) {
        super(context);
    }

    public Chart2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Chart2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 一个设置画布此尺寸的方法
     * measureSpec  :获取控件的状态
     * defaulsSize :设置的大小
     */
    private int getMySize(int defaultSize, int measureSpec) {
        int mySize = defaultSize;
        //获取是什么状态的宽度
        int mode = MeasureSpec.getMode(measureSpec);
        //获取宽度的具体值
        int size = MeasureSpec.getSize(measureSpec);
        switch (mode) {
            //如果没有指定大小，就设置默认大小
            case MeasureSpec.UNSPECIFIED: {
                mySize = defaultSize;
                break;
            }
            //如果测量模式的最大值为四则，
            case MeasureSpec.AT_MOST: {
                mySize = size;
                break;
            }
            //如果是固定的大小，那就不要改变
            case MeasureSpec.EXACTLY: {
                mySize = size;
                break;
            }

        }
        return mySize;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMySize(100, widthMeasureSpec);
        int height = getMySize(100, heightMeasureSpec);
        if (width < height) {
            height = width;
        } else {
            width = height;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //调用父类view的onDraw函数，因为view这个类帮我们实现了一些
        //基本的绘制功能，比如绘制背景颜色，背景图片等
        super.onDraw(canvas);
        int r = getMeasuredWidth() / 2;
        //圆心的横坐标为当前的view的左边起始位置+半径
        int centerX = getLeft() + r;
        //圆心的纵坐标为当前view的顶部起始位置+半径
        int centerY = getTop() + r;
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        //开始绘制
//        canvas.drawCircle(centerX,centerY,r,paint);
        Paint mPaint_2;
        mPaint_2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint_2.setAntiAlias(true);
        //设置画笔样式
        mPaint_2.setStyle(Paint.Style.STROKE);
        //画笔宽
        mPaint_2.setColor(0xFA2ED1C1);

        mPaint_2.setStrokeWidth(50);
        mPaint_2.setShader(null);
        PathEffect effects = new DashPathEffect(new float[]{5, 10, 5, 10}, 0);
        mPaint_2.setPathEffect(effects);
        RectF rectF = new RectF(0, 0, getMeasuredWidth() * 9 / 10, getMeasuredHeight() * 9 / 10);
//        canvas.drawArc(rectF, 30, 120, false, mPaint_2);

        Paint mPaint_3 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint_3.setAntiAlias(true);//取消锯齿
        mPaint_3.setStyle(Paint.Style.FILL);//画笔样式：填满
        mPaint_3.setColor(Color.WHITE);//画笔颜色
        mPaint_3.setStrokeWidth(10);//画笔宽度


//        canvas.drawLine(getMeasuredWidth() / 2, 0, getMeasuredWidth() / 2, getMeasuredHeight() * 1 / 10, mPaint_3);
        /**
         * 外圈圆
         */
        Paint outerCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
        outerCircle.setAntiAlias(true);//取消锯齿
        outerCircle.setStyle(Paint.Style.STROKE);//设置笔的样式
        outerCircle.setColor(Color.parseColor("#82f6ff"));//设置笔的颜色
        outerCircle.setStrokeWidth(5);//设置笔的宽度
        Shader mShader = new LinearGradient(100, 100, 650, 200, new int[]{ 0xFF00ff00,0xFF0000ff, 0xFFff0000}, null, Shader.TileMode.CLAMP);
        outerCircle.setShader(null);
        canvas.drawCircle(r, r, getMeasuredWidth() / 2 - 10, outerCircle);
        /**
         * 刻度外圆
         */
        outerCircle.setStrokeWidth(15);
        outerCircle.setShader(null);
        RectF rectF1 = new RectF(80, 80, getMeasuredWidth() - 80, getMeasuredHeight() - 80);
        canvas.drawArc(rectF1, 144, 252, false, outerCircle);


        /**
         * 底部扇形
         */
        outerCircle.setStrokeWidth(90);
        outerCircle.setShader(null);
        RectF rectF3 = new RectF(70,70,getMeasuredWidth()-70,getMeasuredHeight()-70);
        canvas.drawArc(rectF3,40,100,false,outerCircle);

        /**
         * 内圈大刻度
         */
        outerCircle.setStrokeWidth(35);

        PathEffect effects2 = new DashPathEffect(new float[]{51, 25, 51, 25}, 0);
        outerCircle.setPathEffect(effects2);
        RectF rectF2 = new RectF(110, 110, getMeasuredWidth() - 110, getMeasuredHeight() - 110);
        outerCircle.setShader(mShader);
        canvas.drawArc(rectF2, 147, 249, false, outerCircle);
//        canvas.drawArc(rectF2,147,35,false,outerCircle);
        /**
         * 长刻度
         */
        mPaint_3.setColor(Color.parseColor("#abecf1"));
        mPaint_3.setStrokeWidth(3);
        canvas.drawLine(getMeasuredWidth() / 2, 0, getMeasuredWidth() / 2, getMeasuredHeight() * 1 / 10, mPaint_3);
        canvas.save();
        float degree = 144 / 8;
        for (int i = 0; i < 7; i++) {
            canvas.rotate(-degree, r, r);
            canvas.drawLine(getMeasuredWidth() / 2, 0, getMeasuredWidth() / 2, getMeasuredHeight() * 1 / 10, mPaint_3);

        }
        canvas.restore();
        canvas.save();
        for (int i = 0; i < 7; i++) {
            canvas.rotate(degree, r, r);
            canvas.drawLine(getMeasuredWidth() / 2, 0, getMeasuredWidth() / 2, getMeasuredHeight() * 1 / 10, mPaint_3);

        }
        canvas.restore();

        /**
         * 短刻度
         */
        mPaint_3.setColor(Color.parseColor("#82f6ff"));
        mPaint_3.setStrokeWidth(20);
//        canvas.drawLine(r, 40, r, getMeasuredHeight() * 1 / 10, mPaint_3);
//        canvas.save();
//        float degree2 = 18 / 5;
//        for (int i = 0; i < 42; i++) {
//            canvas.rotate(-degree2, r, r);
//            canvas.drawLine(r, 40, r, getMeasuredHeight() * 1 / 10, mPaint_3);
//        }
//        canvas.restore();
//        canvas.save();
//        for (int i = 0; i < 42; i++) {
//            canvas.rotate(degree2, r, r);
//            canvas.drawLine(r, 40, r, getMeasuredHeight() * 1 / 10, mPaint_3);
//        }
//        canvas.restore();
        RectF rectF4 = new RectF(40,40,getMeasuredWidth()-40,getMeasuredHeight()-40);
        PathEffect effects3 = new DashPathEffect(new float[]{2, 6, 2,6}, 0);
        outerCircle.setPathEffect(effects3);
//        outerCircle.setShader(null);
        canvas.drawArc(rectF4,144,252,false,outerCircle);

//        //渐变的短刻度
//        for (int i = 0; i < mSection * mPortion; i++) {
//            float[] point = getCoordinatePoint(raduis - 80 * mDensityDpi, mStartAngle + TICK_SPLIT_DEFAULT_ANGLE * i);
//            float[] point1 = getCoordinatePoint(raduis - 50 * mDensityDpi, mStartAngle + TICK_SPLIT_DEFAULT_ANGLE * i);
////           控制颜色渐变
//            if (speed > i) {
//                scale.setShader(mShader);
//            } else {
//                scale.setShader(null);
//            }
//            //短刻度绘画
//            canvas.drawLine(point[0], point[1], point1[0], point1[1], scale);
//            mPaint_2.setPathEffect(effects);
//            mPaint_2.setShader(mShader);
//            //内圈圆绘制
//            canvas.drawArc(speedRectFInner_2, mStartAngle-1, speed*TICK_SPLIT_DEFAULT_ANGLE, false, mPaint_2);
//        }
        /**
         * 中间数值
         */
        Paint mPaintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintText.setColor(Color.WHITE);
        mPaintText.setTextSize(250);
        float tw = mPaintText.measureText(String.valueOf("70"));
        int py = (int) (r + Math.abs(mPaintText.descent() + mPaintText.ascent()) / 4);
        canvas.drawText("70", r - tw / 2, r+tw/4, mPaintText);

        /**
         * 单位
         */
        mPaintText.setTextSize(72);
        canvas.drawText("Km/h", r - mPaintText.measureText("Km/h") / 2, r / 2 + r, mPaintText);


    }
}
