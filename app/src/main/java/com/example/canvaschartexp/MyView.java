package com.example.canvaschartexp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

public class MyView extends View {
    private final Context mContext;
    //开始重绘
    private boolean start = true;

    private int mSection = 10; // 值域（mMax-mMin）等分份数
    private int mPortion = 10; // 一个mSection等分份数
    private float TICK_SPLIT_DEFAULT_ANGLE = 2.4f;
    private int screenWidth;//屏幕宽
    private int screenHeight;//屏幕高
    private int mDensityDpi;//屏幕dpi
    private Paint mPaint;//画笔
    private int mStartAngle = 150; // 起始角度
    private int mStopAngle = 30; // 结束角度
    private float mSweepAngle = 240; // 绘制角度
    private int raduis;//半径
    private int pointX, pointY;//圆心
    private Paint textPaint;//文字画笔
    private Paint speedAreaPaint;//速度扇形画笔
    private RectF speedRectF;//速度外切矩形
    private RectF speedRectFInner;
    private int speed;//速度
    // 速度文字 绘制的XY坐标
    private int baseX;
    private int baseY;
    //速度控制模式  1 加速  2 减速  3 手刹
    private int type;
    private RectF speedScaleRectF;
    private Paint ciclePaint;
    private Shader mShader;
    private Paint scale;
    private Paint innerPaint;
    private Paint mPaint_2;
    private RectF speedRectFInner_2;

    public MyView(Context context) {
        this(context, null);
    }

    public MyView(Context context, AttributeSet attrs) {
//        super(context,attrs);
        this(context, attrs, 0);
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;

        //获取屏幕宽高 和 屏幕密度dpi
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
        mDensityDpi = displayMetrics.densityDpi / 320;
        //关闭硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        //设置抗锯齿
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setAntiAlias(true);
        //设置画笔样式
        mPaint.setStyle(Paint.Style.FILL);
        //画笔宽
        mPaint.setStrokeWidth(5 * mDensityDpi);

        //初始化 半径
        raduis = screenWidth / 3;
        //圆心
        pointX = pointY = screenWidth / 2;


        //设置抗锯齿
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setAntiAlias(true);
        //设置画笔颜色
        Shader textShader = new LinearGradient(pointX, pointY - raduis, pointX, pointY + raduis, new int[]{0xffffffff, 0x00ffffff, 0xffffffff}, null, Shader.TileMode.CLAMP);
        textPaint.setShader(textShader);

        // 获取字体并设置画笔字体
//        Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), "kt.ttf");
//        textPaint.setTypeface(typeface);
        //设置抗锯齿
        speedAreaPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        speedAreaPaint.setAntiAlias(true);

        //设置画笔样式
        speedAreaPaint.setStrokeWidth(5 * mDensityDpi);
        speedAreaPaint.setStyle(Paint.Style.STROKE);


        // 设置速度范围扇形的渐变颜色
        mShader = new LinearGradient(pointX - raduis, pointY, pointX + raduis, pointY, new int[]{0xFF0000ff, 0xFF00ff00, 0xFFff0000}, null, Shader.TileMode.CLAMP);
        System.out.println(pointX+"-----R="+raduis+"-----Y="+pointY);

        speedAreaPaint.setShader(mShader);
        //短刻度画笔
        scale = new Paint(Paint.ANTI_ALIAS_FLAG);
        scale.setAntiAlias(true);
        scale.setColor(0xBF3F6AB5);
        //设置画笔样式
        scale.setStyle(Paint.Style.FILL);
        //画笔宽
        scale.setStrokeWidth(5 * mDensityDpi);

        //运动的点
        ciclePaint = new Paint();
        ciclePaint.setAntiAlias(true);
        ciclePaint.setStrokeWidth(20 * mDensityDpi);
        ciclePaint.setStyle(Paint.Style.FILL);
        ciclePaint.setShader(mShader);


        innerPaint = new Paint();
        innerPaint.setAntiAlias(true);
        Shader innerShader = new RadialGradient(pointX, pointY, raduis, new int[]{0x00ffffff, 0x44ffffff}, null, Shader.TileMode.CLAMP);
        innerPaint.setShader(innerShader);

        //虚线画笔
        mPaint_2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint_2.setAntiAlias(true);
        //设置画笔样式
        mPaint_2.setStyle(Paint.Style.STROKE);
        //画笔宽
        mPaint_2.setColor(0xBF3F6AB5);
        mPaint_2.setStrokeWidth(25 * mDensityDpi);


        // 初始化速度范围的2个扇形外切矩形
        speedRectF = new RectF(pointX - raduis + 10 * mDensityDpi, pointY - raduis + 10 * mDensityDpi, pointX + raduis - 10 * mDensityDpi, pointY + raduis - 10 * mDensityDpi);
        //刻度扫描
        speedScaleRectF = new RectF(pointX - raduis + 70 * mDensityDpi, pointY - raduis + 70 * mDensityDpi, pointX + raduis - 70 * mDensityDpi, pointY + raduis - 70 * mDensityDpi);

        //内园外切矩形
        speedRectFInner = new RectF(pointX - raduis / 2, pointY - raduis / 2, pointX + raduis / 2, pointY + raduis / 2);
        //虚线弧外切矩形
        speedRectFInner_2 = new RectF(pointX - raduis + 115 * mDensityDpi, pointY - raduis + 115 * mDensityDpi, pointX + raduis - 115 * mDensityDpi, pointY + raduis - 115 * mDensityDpi);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
//        widthSize = heightSize > widthSize ? widthSize : heightSize;
//        heightSize = heightSize > widthSize ? widthSize : heightSize;
        setMeasuredDimension(widthSize, heightSize);
    }




    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(0x00000000);

        //绘制外层圆
        drawCicle(canvas);

        //绘制速度范围扇形区域
        drawSpeedArea(canvas);


        //变换画笔颜色 绘制刻度
        mPaint.setColor(0xBF3F6AB5);
        drawScale(canvas);

        //绘制中间文字内容
        drawCenter(canvas);

    }

    private void drawCenter(Canvas canvas) {
        //速度
        textPaint.setTextSize(250  * mDensityDpi);
        float tw = textPaint.measureText(String.valueOf(speed));
        baseX = (int) (pointX - tw / 2);
        baseY = (int) (pointY + Math.abs(textPaint.descent() + textPaint.ascent()) / 4);
        Shader textShader = new LinearGradient(baseX, baseY - raduis / 4, baseX, baseY, new int[]{0x55ffffff, 0xffffffff}, null, Shader.TileMode.MIRROR);
        textPaint.setShader(textShader);
        canvas.drawText(String.valueOf(speed), baseX, baseY, textPaint);
    }

    private void drawScale(Canvas canvas) {
        mPaint.setStrokeWidth(5 * mDensityDpi);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
//        第一个中心的，12点刻度位置长刻度
        canvas.drawLine(pointX, pointY - raduis + 40 * mDensityDpi, pointX, pointY - raduis + 80 * mDensityDpi, mPaint);
        /**
         * 画长刻度
         * 画好起始角度的一条刻度后通过canvas绕着原点旋转来画剩下的长刻度
         */
        //逆时针，绘画左边长刻度
        canvas.save();
        float degree = mSweepAngle / mSection;
        for (int i = 0; i < mSection / 2; i++) {
            canvas.rotate(-degree, pointX, pointY);
            canvas.drawLine(pointX, pointY - raduis + 40 * mDensityDpi, pointX, pointY - raduis + 80 * mDensityDpi, mPaint);
        }
        canvas.restore();
        //顺时针
        canvas.save();
        for (int i = 0; i < mSection / 2; i++) {
            canvas.rotate(degree, pointX, pointY);
            canvas.drawLine(pointX, pointY - raduis + 40 * mDensityDpi, pointX, pointY - raduis + 80 * mDensityDpi, mPaint);
        }
        canvas.restore();
        /**
         * 画短刻度
         * 同样采用canvas的旋转原理
         */
        //
        //        // 逆时针到开始处
        //        mPaint.setShader(null);
        //        canvas.save();
        //        float a = calculateRelativeAngleWithValue(speed);
        //        float b = mSweepAngle / 2f;
        //        degree = mSweepAngle / (mSection * mPortion);
        //        for (int i = 0; i < (mSection * mPortion) / 2 - 1; i++) {
        //            Integer integer = (Integer) mArgbEvaluator.evaluate(i * (degree + degree) / (240), 0xff0000ff, 0xffff0000);
        //                    canvas.rotate(-degree, pointX, pointY);
        //            b -= degree;
        //            mPaint.setColor(a>=b?integer:0xBF3F6AB5);
        //            canvas.drawLine(pointX, pointY - raduis + 60 * mDensityDpi, pointX, pointY - raduis + 80 * mDensityDpi, mPaint);
        //        }
        //        canvas.restore();
        //        //         顺时针到结尾处
        //        canvas.save();
        //        b = mSweepAngle / 2f;
        //        for (int i = 0; i < (mSection * mPortion) / 2 - 1; i++) {
        //            canvas.rotate(degree, pointX, pointY);
        //            Integer integer = (Integer) mArgbEvaluator.evaluate(i * (degree + degree) / (240), 0xff0000ff, 0xffff0000);
        //            b += degree;
        //            mPaint.setColor(a>=b?integer:0xBF3F6AB5);
        //            canvas.drawLine(pointX, pointY - raduis + 60 * mDensityDpi, pointX, pointY - raduis + 80 * mDensityDpi, mPaint);
        //        }
        //        canvas.restore();
        //        mPaint.setShader(null);

        mPaint_2.setShader(null);
        PathEffect effects = new DashPathEffect(new float[]{45, 20, 45, 20}, 0);
        mPaint_2.setPathEffect(effects);
        //内圈绘制扇形
        canvas.drawArc(speedRectFInner_2, mStartAngle-1, mSweepAngle, false, mPaint_2);

        //渐变的短刻度
        for (int i = 0; i < mSection * mPortion; i++) {
            float[] point = getCoordinatePoint(raduis - 80 * mDensityDpi, mStartAngle + TICK_SPLIT_DEFAULT_ANGLE * i);
            float[] point1 = getCoordinatePoint(raduis - 50 * mDensityDpi, mStartAngle + TICK_SPLIT_DEFAULT_ANGLE * i);
//           控制颜色渐变
            if (speed > i) {
                scale.setShader(mShader);
            } else {
                scale.setShader(null);
            }
            //短刻度绘画
            canvas.drawLine(point[0], point[1], point1[0], point1[1], scale);
            mPaint_2.setPathEffect(effects);
            mPaint_2.setShader(mShader);
            //内圈圆绘制
            canvas.drawArc(speedRectFInner_2, mStartAngle-1, speed*TICK_SPLIT_DEFAULT_ANGLE, false, mPaint_2);
        }



    }

    private void drawSpeedArea(Canvas canvas) {
        float degree;
        if (speed < mSweepAngle) {
            degree = speed * mSweepAngle / (mSection * mPortion);
        } else {
            degree = mStopAngle * 36 / 30;
        }
//        外圈圆颜色改变
        canvas.drawArc(speedRectF, mStartAngle, degree, false, speedAreaPaint);
        speedAreaPaint.setStrokeWidth(20 * mDensityDpi);
        if (degree > 1) {
            //运动的的点变化
            float[] coordinatePoint = getCoordinatePoint(raduis - 10 * mDensityDpi, degree + 150);
            canvas.drawCircle(coordinatePoint[0], coordinatePoint[1], 12 * mDensityDpi, ciclePaint);
            //数值变化是起点变为起始色
            mPaint.setColor(0xFF0000ff);
            float[] point = getCoordinatePoint(raduis - 10 * mDensityDpi, mStartAngle + 1);
            mPaint.setStrokeWidth(1 * mDensityDpi);
            mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            canvas.drawCircle(point[0], point[1], 15 * mDensityDpi, mPaint);
        }


    }

    /**
     * 绘制外层圆
     *
     * @param canvas
     */
    private void drawCicle(Canvas canvas) {
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAlpha(80);
        mPaint.setStrokeWidth(16 * mDensityDpi);
        mPaint.setColor(0xff646464);
        //绘制外层圆
        canvas.drawArc(speedRectF, mStartAngle + 1, mSweepAngle - 2, false, mPaint);
        //起点
        float[] point = getCoordinatePoint(raduis - 10 * mDensityDpi, mStartAngle + 1);
        mPaint.setStrokeWidth(1 * mDensityDpi);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawCircle(point[0], point[1], 15 * mDensityDpi, mPaint);
        //启始的大小
        textPaint.setTextSize(60  * mDensityDpi);
        canvas.drawText("0",point[0]-80*mDensityDpi,point[1]+20*mDensityDpi,textPaint);
        //尾点
        float[] point1 = getCoordinatePoint(raduis - 10 * mDensityDpi, mStartAngle + 240-1);
        mPaint.setStrokeWidth(1 * mDensityDpi);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawCircle(point1[0], point1[1], 15 * mDensityDpi, mPaint);
        //尾部的大小
        textPaint.setTextSize(60  * mDensityDpi);
        canvas.drawText("90",point1[0]+50*mDensityDpi,point1[1]+10*mDensityDpi,textPaint);

        //内圈园
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(pointX, pointY, raduis - 150 * mDensityDpi, innerPaint);

        //画内圈园内刻度
        mPaint.setStrokeWidth(3 * mDensityDpi);
        for (int i = 0; i < 100; i++) {
            float[] point3 = getCoordinatePoint(raduis - 170 * mDensityDpi, (float) (mStartAngle + 3.6 * i));
            float[] point4 = getCoordinatePoint(raduis - 150 * mDensityDpi, (float) (mStartAngle + 3.6 * i));
            canvas.drawLine(point3[0], point3[1], point4[0], point4[1], mPaint);
        }


    }


    public void setStart(boolean start) {
        this.start = start;
    }

    // 设置速度 并重绘视图
    public void setSpeed(int speed) {
        this.speed = speed;
        postInvalidate();
    }

    //设置速度控制模式
    public void setType(int type) {
        this.type = type;
    }


    //将角度和半径转为点
    private float[] getCoordinatePoint(float radius, float angle) {
        float[] point = new float[2];

        double arcAngle = Math.toRadians(angle); //将角度转换为弧度
        if (angle < 90) {
            point[0] = (float) (pointX + Math.cos(arcAngle) * radius);
            point[1] = (float) (pointY + Math.sin(arcAngle) * radius);
        } else if (angle == 90) {
            point[0] = pointX;
            point[1] = pointY + radius;
        } else if (angle > 90 && angle < 180) {
            arcAngle = Math.PI * (180 - angle) / 180.0;
            point[0] = (float) (pointX - Math.cos(arcAngle) * radius);
            point[1] = (float) (pointY + Math.sin(arcAngle) * radius);
        } else if (angle == 180) {
            point[0] = pointX - radius;
            point[1] = pointY;
        } else if (angle > 180 && angle < 270) {
            arcAngle = Math.PI * (angle - 180) / 180.0;
            point[0] = (float) (pointX - Math.cos(arcAngle) * radius);
            point[1] = (float) (pointY - Math.sin(arcAngle) * radius);
        } else if (angle == 270) {
            point[0] = pointX;
            point[1] = pointY - radius;
        } else {
            arcAngle = Math.PI * (360 - angle) / 180.0;
            point[0] = (float) (pointX + Math.cos(arcAngle) * radius);
            point[1] = (float) (pointY - Math.sin(arcAngle) * radius);
        }
        return point;
    }
}
