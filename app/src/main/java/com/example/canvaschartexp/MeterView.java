package com.example.canvaschartexp;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Scroller;

import java.util.ArrayList;
import java.util.List;
/**
 * 半圆码表图
 * @author LLH
 *
 */
public class MeterView extends View {


    /**
     * 刻度颜色
     */
    private int mDegreeColor = Color.LTGRAY;

    /**
     * 主刻度颜色
     */
    private int mMajorDegreeColor = Color.GRAY;

    /**
     * 刻度数颜色
     */
    private int mDegreeTextColor = Color.BLUE;

    /**
     * 刻度数大小(px)
     */
    private int mDegreeTextSize;

    /**
     * 当前数值文字大小(px)
     */
    private int mContentTextSize;

    /**
     * 当前数值文字颜色
     */
    private int mContentTextColor = Color.BLUE;

    /**
     * 单位文字大小(px)
     */
    private int mUnitTextSize;

    /**
     * 单位文字颜色
     */
    private int mUnitTextColor = Color.RED;

    /**
     * 标题文字颜色
     */
    private int mTitleTextColor = Color.BLUE;

    /**
     * 标题文字大小(px)
     */
    private int mTitleTextSize;

    /**
     * 刻度渐变色起始RGB值
     */
    private int startColor_r = 0x00;
    private int startColor_g = 0xFF;
    private int startColor_b = 0x00;
    private int endColor_r = 0xFF;
    private int endColor_g = 0x00;
    private int endColor_b = 0x00;

    private Paint mDegreePaint;//刻度的笔

    private Paint mMajorDegreePaint;//主刻度笔

    private Paint mColorDegreePaint;//刻度数的画笔（有颜色的当前刻度）

    private Paint mUnitPaint;//单位文字画笔

    private Paint mContentTextPaint;//刻度值画笔

    private Paint mDegreeTextPaint;//刻度单位画笔

    private Paint mTitleTextPaint;//标题画笔

    /**
     * 当前数值
     */
    private float mContent = 0;

    /**
     * 前一数字对应的刻度索引
     */
    private int mLastDegreeIndex;

    private String mUnit = "W";

    private String mTiltl = "当前功率";

    /**
     * 刻度主刻度数据
     */
    private List<String> mDegrees = new ArrayList<String>();

    /**
     * 刻度最大值和最小值
     */
    private float degreeMax, degreeMin;

    /**
     * 刻度长度
     */
    private int mDegreeLen;

    /**
     * 刻度盘各刻度线的信息
     */
    private List<DegreeLine> mDegreeLines;

    private int width, height;
    private int radius;
    private int xPoint, yPoint;
    private float density;
    private Scroller mScroller;

    public MeterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //获取屏幕像素大小
        density = getResources().getDisplayMetrics().density;
        mScroller = new Scroller(context);

    }
    public MeterView(Context context) {
        super(context);
    }


    private void initPaint() {
        mDegreeTextSize = radius / 10;
        mContentTextSize = radius / 4;
        mUnitTextSize = radius / 6;
        mTitleTextSize = radius / 8;

        mDegreePaint = new Paint();
        mDegreePaint.setStyle(Paint.Style.FILL);
        mDegreePaint.setAntiAlias(true);
        mDegreePaint.setColor(mDegreeColor);
        mDegreePaint.setStrokeWidth(2f * density);

        mMajorDegreePaint = new Paint();
        mMajorDegreePaint.setStyle(Paint.Style.FILL);
        mMajorDegreePaint.setAntiAlias(true);
        mMajorDegreePaint.setColor(mMajorDegreeColor);
        mMajorDegreePaint.setStrokeWidth(2.5f * density);

        mColorDegreePaint = new Paint();
        mColorDegreePaint.setStyle(Paint.Style.FILL);
        mColorDegreePaint.setAntiAlias(true);
        mColorDegreePaint.setStrokeWidth(2.5f * density);

        mUnitPaint = new Paint();
        mUnitPaint.setStyle(Paint.Style.FILL);
        mUnitPaint.setAntiAlias(true);
        mUnitPaint.setColor(mUnitTextColor);
        mUnitPaint.setStrokeWidth(2);
        mUnitPaint.setTextAlign(Align.CENTER);
        mUnitPaint.setTextSize(mUnitTextSize);

        mContentTextPaint = new Paint();
        mContentTextPaint.setStyle(Paint.Style.FILL);
        mContentTextPaint.setAntiAlias(true);
        mContentTextPaint.setColor(mContentTextColor);
        mContentTextPaint.setStrokeWidth(2);
        mContentTextPaint.setTextAlign(Align.CENTER);
        mContentTextPaint.setTextSize(mContentTextSize);

        mDegreeTextPaint = new Paint();
        mDegreeTextPaint.setStyle(Paint.Style.FILL);
        mDegreeTextPaint.setAntiAlias(true);
        mDegreeTextPaint.setColor(mDegreeTextColor);
        mDegreeTextPaint.setStrokeWidth(2);
        mDegreeTextPaint.setTextAlign(Align.CENTER);
        mDegreeTextPaint.setTextSize(mDegreeTextSize);

        mTitleTextPaint = new Paint();
        mTitleTextPaint.setStyle(Paint.Style.FILL);
        mTitleTextPaint.setAntiAlias(true);
        mTitleTextPaint.setColor(mTitleTextColor);
        mTitleTextPaint.setStrokeWidth(2);
        mTitleTextPaint.setTextSize(mTitleTextSize);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            int x = mScroller.getCurrX();
            for (int i = 0; i < mDegreeLines.size(); i++) {
                if (i <= x) {
                    mDegreeLines.get(i).showColor = true;
                } else {
                    mDegreeLines.get(i).showColor = false;
                }
            }
            postInvalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < mDegreeLines.size(); i++) {
//            内格标尺颜色设置
            if (mDegreeLines.get(i).showColor) {
                mColorDegreePaint.setColor(mDegreeLines.get(i).color);
            }
//            横线添加
            canvas.drawLine(mDegreeLines.get(i).x1, mDegreeLines.get(i).y1,
                    mDegreeLines.get(i).x2, mDegreeLines.get(i).y2,
                    mDegreeLines.get(i).showColor ? mColorDegreePaint
                            : (mDegreeLines.get(i).isMajor ? mMajorDegreePaint : mDegreePaint));
            if (mDegreeLines.get(i).isMajor) {
                int x = (int) ((5 * mDegreeLines.get(i).x2 - 2 * mDegreeLines.get(i).x1) / 3);
                int y = (int) ((5 * mDegreeLines.get(i).y2 - 2 * mDegreeLines.get(i).y1) / 3);
                canvas.drawText(mDegrees.get(i / 10), x, y, mDegreeTextPaint);
            }
        }
        canvas.drawText(mTiltl, 0, mTitleTextSize, mTitleTextPaint);
        canvas.drawText(mUnit, xPoint, yPoint, mUnitPaint);
        canvas.drawText(String.valueOf(mContent), xPoint, yPoint - mUnitTextSize - 5 * density,
                mContentTextPaint);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        initPaint();
        initDegrees();
    }


    /**
     * 获取宽高
     */

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        density = getResources().getDisplayMetrics().density;
        width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);//获取宽度
        height = getDefaultSize(getSuggestedMinimumWidth(), heightMeasureSpec);//获得高度
        if (width / 2 + 15 * density < height) {
            height = (int) (width / 2 + 15 * density);
        } else {
            width = (int) ((height - 15 * density) * 2);
        }
        radius = (int) (width / 2 - 1 * density);
        xPoint = width / 2;
        yPoint = (int) (height - 1 * density);
        setMeasuredDimension(width, height);
    }

    /**
     * 设置当前刻度数值
     *
     * @param content
     */
    public void setContent(float content) {
        this.mContent = content;
        changeDegree();
    }

    public float getContent() {
        return mContent;
    }

    /**
     * 设置主刻度数值
     *
     * @param degrees
     */
    public void setDegrees(List<String> degrees, float degreeMax, float degreeMin) {
        this.mDegrees = degrees;
        this.degreeMax = degreeMax;
        this.degreeMin = degreeMin;
        mDegreeLen = degrees.size();
        mLastDegreeIndex = 0;
        mContent = 0;
        initDegrees();
        invalidate();
    }

    public String getTiltl() {
        return mTiltl;
    }

    /**
     * 设置标题
     *
     * @param mTiltl
     */
    public void setTiltl(String mTiltl) {
        this.mTiltl = mTiltl;
        invalidate();
    }

    /**
     * 设置小刻度颜色
     *
     * @param mDegreeColor
     */
    public void setDegreeColor(int mDegreeColor) {
        this.mDegreeColor = mDegreeColor;
        invalidate();
    }

    /**
     * 设置大刻度颜色
     *
     * @param mMajorDegreeColor
     */
    public void setMajorDegreeColor(int mMajorDegreeColor) {
        this.mMajorDegreeColor = mMajorDegreeColor;
        invalidate();
    }

    /**
     * 设置刻度数值文字颜色
     *
     * @param mDegreeTextColor
     */
    public void setDegreeTextColor(int mDegreeTextColor) {
        this.mDegreeTextColor = mDegreeTextColor;
        invalidate();
    }

    /**
     * 设置单位颜色
     *
     * @param mUnitTextColor
     */
    public void setUnitTextColor(int mUnitTextColor) {
        this.mUnitTextColor = mUnitTextColor;
        invalidate();
    }

    /**
     * 设置标题颜色
     *
     * @param mTitleTextColor
     */
    public void setTitleTextColor(int mTitleTextColor) {
        this.mTitleTextColor = mTitleTextColor;
        invalidate();
    }

    /**
     * 设置标题大小(px)
     *
     * @param mTitleTextSize
     */
    public void setTitleTextSize(int mTitleTextSize) {
        this.mTitleTextSize = mTitleTextSize;
        invalidate();
    }

    /**
     * 设置单位
     *
     * @param mUnit
     */
    public void setUnit(String mUnit) {
        this.mUnit = mUnit;
        invalidate();
    }

    private void changeDegree() {
        int degreeIndex = (int) (10f * (mDegreeLen - 1) * (mContent - degreeMin) / (degreeMax - degreeMin));
        mScroller.startScroll(mLastDegreeIndex, 0, degreeIndex - mLastDegreeIndex, 0, 1500);
        mLastDegreeIndex = degreeIndex;
        invalidate();
    }

    private void initDegrees() {
        mDegreeLines = new ArrayList<DegreeLine>();
        double deltaAngle = Math.PI / (10 * (mDegreeLen - 1));
        float deltaColor_r = (endColor_r - startColor_r) / (5f * (mDegreeLen - 1));
        float deltaColor_g = (endColor_g - startColor_g) / (5f * (mDegreeLen - 1));
        float deltaColor_b = (endColor_b - startColor_b) / (10f * (mDegreeLen - 1));
        for (int i = 0; i < 10 * (mDegreeLen - 1) + 1; i++) {
            DegreeLine line = new DegreeLine();
            int smallRadius = 7 * radius / 8;
            int k = -1;
            if (deltaAngle * i > Math.PI / 2) {
                k = 1;
            }
            if (i % 10 == 0) {
                line.isMajor = true;
                smallRadius = 5 * radius / 6;
            }
            line.y1 = (float) (yPoint - radius * Math.sin(deltaAngle * i));
            line.x1 = (float) (k
                    * Math.sqrt(radius * radius - (yPoint - line.y1) * (yPoint - line.y1)) + xPoint);
            line.y2 = (float) (yPoint - smallRadius * Math.sin(deltaAngle * i));
            line.x2 = (float) (k
                    * Math.sqrt(smallRadius * smallRadius - (yPoint - line.y2) * (yPoint - line.y2)) + xPoint);

            int color_r = (int) (startColor_r + i * deltaColor_r);
            int color_g = (int) (startColor_g + i * deltaColor_g);
            int color_b = (int) (startColor_b + i * deltaColor_b);
            if (i < 5 * (mDegreeLen - 1) + 1) {
                color_g = startColor_g;
            } else {
                color_r = endColor_r;
            }
            line.color = Color.rgb(color_r, color_g, color_b);
            mDegreeLines.add(line);
        }
    }

    public class DegreeLine {

        /**
         * 是否大刻度
         */
        public boolean isMajor;

        /**
         * 是否显示颜色
         */
        public boolean showColor;

        /**
         * 颜色
         */
        public int color;

        /**
         * 端点坐标
         */
        public float x1;
        public float y1;
        public float x2;
        public float y2;
    }

}
