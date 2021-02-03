package com.example.canvaschartexp;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;


import java.util.ArrayList;
import java.util.List;

public class Chart9 extends View {

    //自定义属性
    private int myTextSize = 15;
    private int myTextColor = Color.BLACK;
    private int myPlaceSize = 5;
    private int myPlaceColor = Color.RED;
    private int myBackground = R.drawable.t_blue280;
    int width, height;

    int mDestLeft = 0;//显示图片左边x轴坐标
    int mDestTop = 0;//显示图片上面Y轴坐标
    int mDestRight = 0;//显示图片右边x轴坐标
    int mDestBottom = 0;//显示图片底部y轴坐标
    float mXtext = 0, mYtext = 0;//横坐标的间隔，y坐标的间隔
    public List<AllPlace> list = new ArrayList();

    public Chart9(Context context) {
        this(context, null);
    }

    public Chart9(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Chart9(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //获取自定义属性
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.Chart9);

        myTextColor = array.getColor(R.styleable.Chart9_myTextColor, myTextColor);
        myTextSize = array.getDimensionPixelSize(R.styleable.Chart9_myTextSize, myTextSize);
        myPlaceColor = array.getColor(R.styleable.Chart9_myPlaceColor, myPlaceColor);
        myPlaceSize = array.getDimensionPixelSize(R.styleable.Chart9_myPlaceSize, myPlaceSize);

        //回收
        array.recycle();
    }


    //绘制画布的方法
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMySize(100, widthMeasureSpec);
        height = getMySize(50, widthMeasureSpec);
        mDestLeft = width * 10 / 128;//显示图片左边x轴坐标
        mDestTop = height * 10 / 224;//显示图片上面Y轴坐标
        mDestRight = width - mDestLeft / 2;//显示图片右边x轴坐标
        mDestBottom = width / 2;//显示图片底部y轴坐标
        mYtext = (mDestBottom - mDestTop) * 3 / 17;
        mXtext = (mDestRight - mDestLeft) * 2 / 19;
        setMeasuredDimension(width, height);
    }


    //绘画的动作
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setFilterBitmap(true);//设置过滤器位图
        paint.setDither(true);//设置发抖
        Resources resources = getResources();
        Bitmap bitmap = ((BitmapDrawable) resources.getDrawable(myBackground)).getBitmap();
        //mSrcRect：代表要绘制的bitmap 区域
        //mDestRect:第二个 Rect 代表的是要将bitmap 绘制在屏幕的什么地方
        int rawHeight = bitmap.getHeight();//获取图片的高度
        int rawWidth = bitmap.getWidth();//获取图片的宽度
        float density = getResources().getDisplayMetrics().density;
        Rect mSrcRect = new Rect(0, 0, rawWidth, rawHeight);//将图片完整画出来，不按照比例
        Rect mDestRect = new Rect(mDestLeft, mDestTop, mDestRight, mDestBottom);//图片在界面的哪里显示

        canvas.drawBitmap(bitmap, mSrcRect, mDestRect, paint);

        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(myTextColor);
        textPaint.setTextSize(myTextSize);


        /**
         * 测试间隔
         */
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        int yTextH = (int) (fontMetrics.descent - fontMetrics.ascent);

        //绘制Y轴坐标文字
        for (int i = 0; i < 6; i++) {
            //获取文字的宽度
            String text = String.valueOf(i * 50);
            float textw = textPaint.measureText(text);
            canvas.drawText(text, mDestLeft - textw - mDestLeft / 10, mDestBottom - i * mYtext + yTextH / 5, textPaint);

        }

        //绘制X轴坐标文字
        for (int i = 0; i < 10; i++) {
            String text = String.valueOf(5500 - i * 500);
            float textw = textPaint.measureText(text + "0");
            canvas.drawText(text, mDestRight - i * mXtext - textw / 2, mDestBottom + yTextH, textPaint);
        }

        Paint dotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dotPaint.setAntiAlias(false);
        dotPaint.setColor(myPlaceColor);
        list.add(new AllPlace(500, 100));
        list.add(new AllPlace(1200, 200));
        list.add(new AllPlace(1500, 200));
        list.add(new AllPlace(5500, 200));

        for (int i = 0; i < list.size(); i++) {
            canvas.drawCircle(getXplace(list.get(i).getPlaceX()), getYplace(list.get(i).getPlaceY()), myPlaceSize, dotPaint);

        }
    }

    //寻找Y坐标点
    private float getYplace(float y) {
//        if (y<=283){
//            return mDestBottom- Ytext/50 *y;
//        }else  if (y>283){
//            return mDestTop;
//        }

        return y < 0 ? mDestBottom : y > 283 ? mDestTop : mDestBottom - mYtext / 50 * y;
//        return mDestBottom;

    }

    //寻找X坐标点
    private float getXplace(float x) {
//        if (x > 0 && x < 1000) {
//            return  Xtext2/1000 * x +mDestLeft;
//        } else if (x >= 1000 && x <= 5500) {
//            return Xtext/500 * (x-999) +mDestLeft +Xtext2;
//        }else if (x>5500){
//            return mDestRight;
//        }
        return x < 0 ? mDestLeft : x > 5500 ? mDestRight : x < 1000 ? mXtext / 2000 * x + mDestLeft : mXtext / 500 * (x - 999) + mDestLeft + mXtext / 2;
//        return mDestLeft;
    }

    //定制自己的画布
    private int getMySize(int defaultSize, int measureSpec) {
        //获取设置的最小值当默认值
        int mySize = defaultSize;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        switch (mode) {
            case MeasureSpec.UNSPECIFIED://当没有指定大小的时候，就设置到默认值
                mySize = defaultSize;
                break;
            case MeasureSpec.AT_MOST://当有指定大小时，就设置为指定大小
                mySize = size;
                break;
            case MeasureSpec.EXACTLY://如果为固定大小，就设置为指定大小
                mySize = size;
                break;
        }
        return mySize;
    }

    public class AllPlace {
        float placeX, placeY;

        public AllPlace() {

        }

        public AllPlace(float x, float y) {
            this.placeX = x;
            this.placeY = y;
        }

        public float getPlaceX() {
            return placeX;
        }

        public void setPlaceX(float placeX) {
            this.placeX = placeX;
        }

        public float getPlaceY() {
            return placeY;
        }

        public void setPlaceY(float placeY) {
            this.placeY = placeY;
        }
    }
}
