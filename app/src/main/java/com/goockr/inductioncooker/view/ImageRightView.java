package com.goockr.inductioncooker.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.goockr.inductioncooker.MyApplication;
import com.goockr.inductioncooker.R;
import com.goockr.inductioncooker.utils.DensityUtil;

import java.util.jar.Attributes;

/**
 * Created by CMQ on 2017/6/26.
 */

public class ImageRightView extends View {

    public ImageRightView(Context context) {
        super(context);
    }

    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;

    }

    public ImageRightView(Context context , AttributeSet attrs) {
        super(context,attrs);


        setText("L 1600W");



    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float width= canvas.getWidth();
        float height=canvas.getHeight();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.home_icon_warning);
        Paint paint=new Paint();
        float image_y=height/2-(bitmap.getHeight()/2);
        canvas.drawBitmap(bitmap, width-(bitmap.getWidth()), image_y, paint); //在10,60处开始绘制图片


        String s=getText();
        String s1=getText().substring(0,2);
        String s2=getText().substring(2,s.length());

        Paint s2_paint=new Paint();
        //  设置字体大小
        s2_paint.setColor(Color.RED);
        s2_paint.setTextSize(sp2px(MyApplication.getContext(),14));
        float s2_width= s2_paint.measureText(s2);
        float s2_height = (float) (s2_paint.descent()-s2_paint.ascent());
        float s2_x=width-bitmap.getWidth()-s2_width-5;
        float s2_y=image_y+bitmap.getHeight()-s2_height;
        canvas.drawText(s2, s2_x,s2_y+s2_height , s2_paint);


        Paint s1_paint=new Paint();
        //  设置字体大小
        s1_paint.setColor(Color.RED);
        s1_paint.setTextSize(sp2px(MyApplication.getContext(),18));
        float s1_width= s1_paint.measureText(s1);
        float s1_height = (float) (s1_paint.descent()-s1_paint.ascent());
        float s1_x=s2_x-s1_width;
        float s1_y=image_y+bitmap.getHeight()-s1_height;
        canvas.drawText(s1, s1_x,s1_y+s1_height, s1_paint);

    }


    public  int sp2px(Context context, float spValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

}
