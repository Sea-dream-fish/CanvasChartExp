package com.example.canvaschartexp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class Chart7 extends View {

    float number = 2.63f;
    int width,height;

    public float getNumber() {
        return number;
    }

    public void setNumber(float number) {
        this.number = number;
    }

    public Chart7(Context context) {
        this(context, null);
    }

    public Chart7(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Chart7(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
         width = getMySize(120,widthMeasureSpec);
         height = getMySize(40,widthMeasureSpec);
        if (width<height*3){
            width = height*3;
        }else {
            height = width/3;
        }
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float LineY = height*2/3;
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(width/20);
        paint.setColor(0xff98b3c0);
        float leftX = paint.measureText("MAX")/3;
        float rightX = width-leftX;
        canvas.drawLine(leftX,LineY,rightX,LineY,paint);
        /**
         * 绘画条长
         */
        paint.setColor(0xff4cfd69);
        canvas.drawLine(leftX,LineY,getMyLine(number),LineY,paint);
        Paint tPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        tPaint.setStrokeWidth(height/200);
        tPaint.setColor(Color.parseColor("#57f3ff"));
        RectF rectF = new RectF(
                width/2-width/7,
                height/4-height/7,
                width/2+width/7,
                height/4+height/7);
        /**
         * 绘制数值
         */

        canvas.drawRoundRect(rectF, height/10f,height/10f,tPaint);

        tPaint.setColor(Color.WHITE);
        tPaint.setTextSize(height/4f);
        canvas.drawText(String.valueOf(number),width/2-tPaint.measureText(String.valueOf(number))/2,height/4+height/10,tPaint);

        /**
         * 绘制单位
         */
        tPaint.setTextSize(height/13f);
        canvas.drawText("MIN",0,height-height/7,tPaint);
        canvas.drawText("MAX",width-tPaint.measureText("MAX"),height-height/7,tPaint);

    }

    private float getMyLine(float num){
        float Mylong  = num/2.7f;
        return width*Mylong;
    }

    private int getMySize(int defaultSize, int measureSpec) {
        int mySize = defaultSize;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
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
}
