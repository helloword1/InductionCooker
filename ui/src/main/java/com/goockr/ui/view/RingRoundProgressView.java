package com.goockr.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 * Created by CMQ on 2017/6/28.
 */

public class RingRoundProgressView extends View {


    public int progress;

    public int maxCount;

    //float startAngle, float sweepAngle
    public int startAngle;

    public  int sweepAngle;

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;

        if (progress>maxCount)
        {
            this.progress=maxCount;
            return;
        }else if (progress<0){

            this.progress=0;
            return;
        }

        invalidate();

    }




    public RingRoundProgressView(Context context) {
        this(context, null);
    }

    public RingRoundProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.RingRoundProgressView);

        if (mTypedArray!=null)
        {
            progress=mTypedArray.getInt(R.styleable.RingRoundProgressView_progress,0);
            maxCount=mTypedArray.getInt(R.styleable.RingRoundProgressView_maxCount,10);
            startAngle=mTypedArray.getInt(R.styleable.RingRoundProgressView_startAngle,0);
            sweepAngle=mTypedArray.getInt(R.styleable.RingRoundProgressView_sweepAngle,360);
            mTypedArray.recycle();
        }

    }

    public RingRoundProgressView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.RingRoundProgressView);
//        maxColorNumber = mTypedArray.getInt(R.styleable.CircularRing_circleNumber, 40);
//        circleWidth = mTypedArray.getDimensionPixelOffset(R.styleable.CircularRing_circleWidth, getDpValue(180));
//        roundBackgroundColor = mTypedArray.getColor(R.styleable.CircularRing_roundColor, 0xffdddddd);
//        textColor = mTypedArray.getColor(R.styleable.CircularRing_circleTextColor, 0xff999999);
//        roundWidth = mTypedArray.getDimension(R.styleable.CircularRing_circleRoundWidth, 40);
//        textSize = mTypedArray.getDimension(R.styleable.CircularRing_circleTextSize, getDpValue(8));
//        colors[0] = mTypedArray.getColor(R.styleable.CircularRing_circleColor1, 0xffff4639);
//        colors[1] = mTypedArray.getColor(R.styleable.CircularRing_circleColor2, 0xffcdd513);
//        colors[2] = mTypedArray.getColor(R.styleable.CircularRing_circleColor3, 0xff3cdf5f);
//        initView();

        if (mTypedArray!=null)
        {
            progress=mTypedArray.getInt(R.styleable.RingRoundProgressView_progress,0);
            maxCount=mTypedArray.getInt(R.styleable.RingRoundProgressView_maxCount,10);
            startAngle=mTypedArray.getInt(R.styleable.RingRoundProgressView_startAngle,0);
            sweepAngle=mTypedArray.getInt(R.styleable.RingRoundProgressView_sweepAngle,360);
            mTypedArray.recycle();
        }




    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
//
//        float x = (getWidth() - getHeight() / 2) / 2;
//        float y = getHeight() / 4;

//        RectF oval = new RectF( x, y,
//                getWidth() - x, getHeight() - y);
        float w=40;
//        Paint paint=new Paint();
//        paint.setAntiAlias(true);
//        paint.setStrokeWidth((float) 1);
//        paint.setStyle(Paint.Style.STROKE);
//        paint.setStrokeCap(Paint.Cap.ROUND);
//        paint.setColor(Color.GREEN);
        Paint paint1=new Paint();
        paint1.setAntiAlias(true);
        paint1.setStrokeWidth((float) w);
        paint1.setStyle(Paint.Style.STROKE);
        paint1.setStrokeCap(Paint.Cap.ROUND);
        paint1.setColor(0x80ebeef3);

        RectF oval = new RectF(0,0,getWidth(),getHeight());
        //canvas.drawRect(oval,paint);
        oval.left=oval.left+w/2;
        oval.top=oval.top+w/2;
        oval.right =  oval.right-w/2;
        oval.bottom =  oval.bottom -w/2;
        canvas.drawArc(oval, 0,  360, false, paint1);//背景圆
//        paint.setColor(Color.BLUE);
//        canvas.drawRect(oval,paint);
        Paint p = new Paint();
        p.setColor(Color.YELLOW);// 设置红色
        p.setAntiAlias(true);// 设置画笔的锯齿效果。 true是去除，大家一看效果就明白了
       // canvas.drawCircle(getWidth()/2, w/2, w/2, p);// 小圆

       // canvas.drawCircle(100, 100, 100, p);// 大圆


//        Paint paint2 = new Paint();
//        paint2.setColor(Color.RED);
//
//        RectF rectF = new RectF();
//        rectF.left =30;
//        rectF.top = 190;
//        rectF.right = 120;
//        rectF.bottom = 280;
   //     canvas.drawRect(rectF, paint2);

//        rectF.left =160;
//        rectF.top = 190;
//        rectF.right = 250;
//        rectF.bottom = 280;


//        rectF.left =rectF.left+w;
//        rectF.top =  rectF.top+w;
//        rectF.right =  rectF.right-w;
//        rectF.bottom =  rectF.bottom -w;
//
//        paint2.setAntiAlias(true);
//        paint2.setStrokeWidth((float) w);
//        paint2.setStyle(Paint.Style.STROKE);
//        paint2.setStrokeCap(Paint.Cap.ROUND);
//        paint2.setColor(Color.BLACK);

      //  canvas.drawArc(rectF, 0, 360, true, paint2);
        float argAngle=sweepAngle/(maxCount-1);
        double bgRoundRedius=getWidth()/2;
     //   canvas.drawCircle(300, w/2, w/2, p);// 小圆

//        float degrees=startAngle+argAngle*0;
//        double angle =PI * (degrees)/ 180;
//
//        double a=cos(angle);
//
//        double b=sin(angle);
//
//        double x = bgRoundRedius * cos(angle) + bgRoundRedius;
//        double y = bgRoundRedius * sin(angle) + bgRoundRedius;
//
//        canvas.drawCircle((float)x, (float) y, w/2, p);// 小圆

        //Log.v("RingRoundProgressView","angle:"+angle+"                X:"+x+"             Y:"+y);
        for (int i=0;i<maxCount;i++)
        {
            float degrees=startAngle+argAngle*i;
            double angle =PI * (degrees)/ 180;

            int green=201-((110/maxCount-1)*i);

            int blue=51-((51/maxCount-1)*i);

            p.setColor(Color.rgb(255,green,blue));
          //  p.setColor(Color.YELLOW);
            double x = bgRoundRedius * cos(angle) + bgRoundRedius-w/2*cos(angle);
            double y = bgRoundRedius * sin(angle) + bgRoundRedius-w/2*sin(angle);
            canvas.drawCircle((float)x, (float) y, w/2, p);// 小圆

            if (i>=progress)
            {
              //  p.setColor(Color.GREEN);
                p.setColor(0xFFCCCCCC);
                canvas.drawCircle((float)x, (float) y, w/2, p);// 小圆
            }

        }



    }



}
