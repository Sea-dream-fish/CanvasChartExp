package com.example.canvaschartexp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class Chart8 extends View {
    public Chart8(Context context) {
        super(context);
    }

    public Chart8(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Chart8(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    
    int width ,height;
    boolean lock  =false;

    public boolean isLock() {
        return lock;
    }

    public void setLock(boolean lock) {
        this.lock = lock;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
         width = getMySize(50,widthMeasureSpec);
         height = getMySize(100,heightMeasureSpec);
        if (width<height/2){
            width = height/2;
        }else {
            height = 2*width;
        }
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(width/(6+6/10f));
        paint.setStyle(Paint.Style.FILL);
        RectF rectF = new RectF(0,height/2,width,height);
        canvas.drawRect(rectF,paint);
        paint.setStyle(Paint.Style.STROKE);

        RectF rectF1 = new RectF(width/10,height/6,width-width/10,width);
        canvas.drawArc(rectF1,175,190,false,paint);
        canvas.drawLine(width*9/10,height/3,width*9/10,width,paint);
       if (lock){
           canvas.drawLine(width/10,height/3,width/10,width,paint);
       }

    }

    private int getMySize(int defaultSize, int measuresSpec) {
        int mySize = defaultSize;
        int mode = MeasureSpec.getMode(measuresSpec);
        int size = MeasureSpec.getSize(measuresSpec);
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
