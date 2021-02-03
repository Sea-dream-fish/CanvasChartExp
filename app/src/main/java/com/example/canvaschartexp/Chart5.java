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

public class Chart5 extends View {
    public Chart5(Context context) {
        super(context);
    }

    public Chart5(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Chart5(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    float density;//屏幕尺寸大小
    int width, height;//画布宽度,高度
    int radius;//圆的半径（画布的中心点）
    int centerX = getLeft() + radius;//圆心的横坐标为当前的view左边起始位置+半径
    int centerY = getTop() + radius;//圆心的纵坐标为当前的view头部起始位置+半径

    Paint cPaint;//扇形画笔
    Paint lPaint;//长刻度画笔
    Paint tPaint;//字画笔
    Shader mShader;//画笔色彩设置
    float textNum = 3.2f;//数字大小
    String textUnit = "Km/h";//单位
    float sweepangle = 150;//结束角度


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
        density = getResources().getDisplayMetrics().density;
        width = getMySize(100, widthMeasureSpec);
        height = getMySize(100, heightMeasureSpec);
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
        radius = getMeasuredWidth() / 2;
        //圆心的横坐标为当前的view的左边起始位置+半径
        centerX = getLeft() + radius;
        //圆心的纵坐标为当前view的顶部起始位置+半径
        centerY = getTop() + radius;
        //开始绘制
        setPaint();

        /**
         * 绘制外层圆
         */
        drawCicle(canvas);

        /**
         * 扇形
         */
        drawArc(canvas);


        /**
         * 长刻度
         */
//        drawLine(canvas);

        /**
         * 绘制文字
         */
        drawText(canvas);

    }

    /**
     * 画笔设置
     */
    private void setPaint() {
        //扇形画笔设置
        cPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        cPaint.setAntiAlias(true);//取消锯齿
        cPaint.setStyle(Paint.Style.STROKE);//设置笔的样式
        cPaint.setColor(Color.parseColor("#82f6ff"));//设置笔的颜色
        cPaint.setStrokeWidth(width / 140);//设置笔的宽度
        mShader = new LinearGradient(
                width / 7f,
                width / 2f,
                width / 5f + width,
                width / 2f,
                new int[]{0xFF00ff00, 0xFF0000ff, 0xFFff0000},
                new float[]{0.11f, 0.5f, 0.62f},
                Shader.TileMode.CLAMP);


        //长刻度画笔设置
        lPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        lPaint.setAntiAlias(true);//取消锯齿
        lPaint.setStyle(Paint.Style.FILL);//画笔样式：填满
        lPaint.setColor(Color.WHITE);//画笔颜色
        lPaint.setStrokeWidth(width / 70);//画笔宽度
        lPaint.setColor(Color.parseColor("#abecf1"));
        lPaint.setStrokeWidth(width / 233);

        //字画笔设置
        tPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        tPaint.setColor(Color.WHITE);
    }

    /**
     * 外圈圆
     */
    private void drawCicle(Canvas canvas) {
        canvas.drawCircle(radius, radius, width / 2 - width / 70f, cPaint);
    }

    /**
     * 绘制扇形图
     */
    private void drawArc(Canvas canvas) {
        //刻度扇形
        cPaint.setStrokeWidth(width / 46f);
//        cPaint.setShader(null);
        float r1 = width / (8 + 75 / 100f);

        RectF rectF1 = new RectF(r1, r1, width - r1, width - r1);
        canvas.drawArc(rectF1, 144, 252, false, cPaint);

        //底部扇形
        cPaint.setStrokeWidth(width/10f);
//        cPaint.setShader(null);
        float r3 = width / 10f;
        RectF rectF3 = new RectF(r3, r3, width - r3, height - r3);
        canvas.drawArc(rectF3, 22, 136, false, cPaint);

        //圈内刻度扇形
        cPaint.setStrokeWidth(width / 20f);
        PathEffect effects2 = new DashPathEffect(new float[]{width/(5+66/100f),height/25f,width/(5+66/100f),height/25f}, 0);
        cPaint.setPathEffect(effects2);
        float r2 = width / (6 + 36 / 100f);
        RectF rectF2 = new RectF(r2, r2, width - r2, height - r2);
        canvas.drawArc(rectF2, 165, 213, false, cPaint);
        cPaint.setShader(mShader);
        canvas.drawArc(rectF2, 165, sweepangle, false, cPaint);

        //短刻度扇形
        float r4 = width / (17 + 1 / 2f);
        RectF rectF4 = new RectF(r4, r4, width - r4, height - r4);
        PathEffect effects3 = new DashPathEffect(new float[]{width/(8+1/10f),height/60f,width/(8+1/10f),height/60f}, 0);
        cPaint.setPathEffect(effects3);
        cPaint.setShader(null);
        canvas.drawArc(rectF4, 162, 216, false, cPaint);

        /*可以变化角度，改变值，渐变刻度渐变内圆*/
        cPaint.setShader(mShader);
        canvas.drawArc(rectF4, 162, sweepangle+3, false, cPaint);

    }


    /**
     * 绘制文字
     */
    private void drawText(Canvas canvas) {
        //中间数值
        tPaint.setTextSize(width / (2 + 4 / 5f));
        float tw = tPaint.measureText(String.valueOf(textNum));
        canvas.drawText(String.valueOf(textNum), radius - tw / 2, radius + tw / 4, tPaint);

        //单位
        tPaint.setTextSize(width / 10);
        canvas.drawText(textUnit, radius - tPaint.measureText(textUnit) / 2, radius / 2 + radius, tPaint);

    }



    //数字大小
    public float getTextNum() {
        return textNum;
    }

    public void setTextNum(float textNum) {
        this.textNum = textNum;
        if (textNum<120){
            setSweepangle(textNum*(1+77/100f));
        }else {
            setSweepangle(213);
        }
    }

    //单位
    public String getTextUnit() {
        return textUnit;
    }

    public void setTextUnit(String textUnit) {
        this.textUnit = textUnit;
    }

    //角度多少
    public float getSweepangle() {
        return sweepangle;
    }

    public void setSweepangle(float sweepangle) {
        this.sweepangle = sweepangle;
    }
}
