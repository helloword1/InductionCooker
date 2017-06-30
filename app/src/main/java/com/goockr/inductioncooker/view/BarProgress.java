package com.goockr.inductioncooker.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by CMQ on 2017/6/29.
 */

public class BarProgress extends View {

    private int progress=3;

    private int maxCount=5;

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public BarProgress(Context context) {
        this(context, null);
    }

    public BarProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        test1(canvas);





    }

    private void test1(Canvas canvas)
    {
        float width= canvas.getWidth();
        float height=canvas.getHeight();

        int lineHeight=10;

        Paint paint=new Paint();

        Paint linePaint=new Paint();

        linePaint.setAntiAlias(true);//取消锯齿
        linePaint.setStrokeWidth(lineHeight);

        Bitmap edit_bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.message_edit);

        Bitmap edited_bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.message_edited);

        Bitmap nor_edited_bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.message_nor_edited);


        float offWidth=0;
        if (progress==maxCount)
        {
            offWidth=edit_bitmap.getWidth();
        }else {
            offWidth=nor_edited_bitmap.getWidth();
        }


        float margin=(width-offWidth)/(maxCount-1);

        for(int i=1;i<=maxCount;i++)
        {


            Bitmap bitmap=null;

            if (i<progress)
            {
                bitmap=edited_bitmap;
                linePaint.setColor(Color.YELLOW);
            }else if (i==progress)
            {
                linePaint.setColor(Color.GRAY);
                bitmap=edit_bitmap;
            }else {
                bitmap=nor_edited_bitmap;
                linePaint.setColor(Color.GRAY);
            }

            if (i<maxCount)
            {
                canvas.drawLine((i-1)*margin+bitmap.getWidth(),height/2,i*margin,height/2,linePaint);
            }

            canvas.drawBitmap(bitmap, (i-1)*margin, height/2-(bitmap.getHeight()/2), paint); //在10,60处开始绘制图片




        }


    }























}
