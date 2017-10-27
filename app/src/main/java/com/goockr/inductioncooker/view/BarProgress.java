package com.goockr.inductioncooker.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.goockr.inductioncooker.R;
import com.goockr.inductioncooker.utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CMQ on 2017/6/29.
 */

public class BarProgress extends View {

    private int progress=2;

    private int maxCount=3;

    private List<String> tips=new ArrayList<String>();

    Paint paint;

    Paint linePaint;

    Paint textPaint;


    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
        invalidate();
    }

    public void setTips(List<String> tips) {
        this.tips = tips;
        invalidate();
    }

    public List<String> getTips() {
        return tips;
    }

    public BarProgress(Context context) {
        this(context, null);




    }

    private void initPaint() {

        paint=new Paint();
        linePaint=new Paint();
        textPaint=new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(DensityUtil.sp2px(getContext(),12)); //以px为单位
        textPaint.setAntiAlias(true);//取消锯齿

    }

    public BarProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

       // test1(canvas);

        float width= canvas.getWidth();
        float height=canvas.getHeight();

        int lineHeight=10;



        linePaint.setAntiAlias(true);//取消锯齿
        linePaint.setStrokeWidth(lineHeight);

        @SuppressLint("DrawAllocation") Bitmap editBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.message_edit);

        @SuppressLint("DrawAllocation") Bitmap editedBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.message_edited);

        @SuppressLint("DrawAllocation") Bitmap norEditedBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.message_nor_edited);

        float marginLeft=0;
        float marginRight=0;
        if (tips.size()>0)
        {
            String t=tips.get(0);
            Rect rect = new Rect();
            textPaint.getTextBounds(t, 0, t.length(), rect);

            if (progress==1){
                marginLeft=rect.width()/2;
            }else {
                marginLeft=rect.width()/2;
            }



            String t1=tips.get(tips.size()-1);
            textPaint.getTextBounds(t, 0, t1.length(), rect);
            marginRight=rect.width()/2;
        }


        float offWidth=0;
        if (progress==maxCount)
        {
            offWidth=editBitmap.getWidth();
        }else {
            offWidth=norEditedBitmap.getWidth();
        }


        float margin=(width-offWidth-marginLeft-marginRight)/(maxCount-1);

        for(int i=1;i<=maxCount;i++)
        {

            Bitmap bitmap=null;

            float imageX=0;

            if (i<progress)
            {
                bitmap=editedBitmap;
                linePaint.setColor(ContextCompat.getColor(getContext(),R.color.colorOrange1));
            }else if (i==progress)
            {
                linePaint.setColor(ContextCompat.getColor(getContext(),R.color.colorGray1));
                bitmap=editBitmap;
            }else {
                bitmap=norEditedBitmap;
                linePaint.setColor(ContextCompat.getColor(getContext(),R.color.colorGray1));
            }

            imageX=marginLeft+ (i-1)*margin;

            if (i<maxCount)
            {
                canvas.drawLine(imageX+bitmap.getWidth() , height/2 , imageX+margin , height/2 ,linePaint);
            }

            canvas.drawBitmap(bitmap,imageX, height/2-(bitmap.getHeight()/2), paint); //在10,60处开始绘制图片

            if (i<=tips.size())
            {
                String text=tips.get(i-1);
                Rect rect = new Rect();
                textPaint.getTextBounds(text, 0, text.length(), rect);
                canvas.drawText(text,0,text.length(), imageX-rect.width()/2+bitmap.getWidth()/2,  height/2+(editBitmap.getHeight()/2)+rect.height()+5, textPaint);
            }


        }

    }

//    private void test1(Canvas canvas)
//    {
//        float width= canvas.getWidth();
//        float height=canvas.getHeight();
//
//        int lineHeight=10;
//
//
//
//        linePaint.setAntiAlias(true);//取消锯齿
//        linePaint.setStrokeWidth(lineHeight);
//
//        Bitmap edit_bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.message_edit);
//
//        Bitmap edited_bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.message_edited);
//
//        Bitmap nor_edited_bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.message_nor_edited);
//
//        float marginLeft=0;
//        float marginRight=0;
//        if (tips.size()>0)
//        {
//            String t=tips.get(0);
//            Rect rect = new Rect();
//            textPaint.getTextBounds(t, 0, t.length(), rect);
//
//            if (progress==1){
//                marginLeft=rect.width()/2;
//            }else {
//                marginLeft=rect.width()/2;
//            }
//
//
//
//            String t1=tips.get(tips.size()-1);
//            textPaint.getTextBounds(t, 0, t1.length(), rect);
//            marginRight=rect.width()/2;
//        }
//
//
//        float offWidth=0;
//        if (progress==maxCount)
//        {
//            offWidth=edit_bitmap.getWidth();
//        }else {
//            offWidth=nor_edited_bitmap.getWidth();
//        }
//
//
//        float margin=(width-offWidth-marginLeft-marginRight)/(maxCount-1);
//
//        for(int i=1;i<=maxCount;i++)
//        {
//
//            Bitmap bitmap=null;
//
//            float image_x=0;
//
//            if (i<progress)
//            {
//                bitmap=edited_bitmap;
//                linePaint.setColor(getResources().getColor(R.color.colorOrange1));
//            }else if (i==progress)
//            {
//                linePaint.setColor(getResources().getColor(R.color.colorGray1));
//                bitmap=edit_bitmap;
//            }else {
//                bitmap=nor_edited_bitmap;
//                linePaint.setColor(getResources().getColor(R.color.colorGray1));
//            }
//
//            image_x=marginLeft+ (i-1)*margin;
//
//            if (i<maxCount)
//            {
//                canvas.drawLine(image_x+bitmap.getWidth() , height/2 , image_x+margin , height/2 ,linePaint);
//            }
//
//            canvas.drawBitmap(bitmap,image_x, height/2-(bitmap.getHeight()/2), paint); //在10,60处开始绘制图片
//
//            if (i<=tips.size())
//            {
//                String text=tips.get(i-1);
//                Rect rect = new Rect();
//                textPaint.getTextBounds(text, 0, text.length(), rect);
//                canvas.drawText(text,0,text.length(), image_x-rect.width()/2+bitmap.getWidth()/2,  height/2+(edit_bitmap.getHeight()/2)+rect.height()+5, textPaint);
//            }
//
//
//        }
//
//
//    }























}
