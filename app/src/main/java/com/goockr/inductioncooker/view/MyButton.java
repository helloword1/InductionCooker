package com.goockr.inductioncooker.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.Button;

import com.goockr.inductioncooker.MyApplication;
import com.goockr.inductioncooker.R;

/**
 * Created by CMQ on 2017/6/26.
 */

public class MyButton extends Button {

    private  int imageId;

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);

       setImageId(R.mipmap.btn_openkey_normal);


    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        float width= canvas.getWidth();
        float height=canvas.getHeight();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), getImageId());
        Paint paint=new Paint();
        float image_y=height/2-(bitmap.getHeight()/2);
        float image_x= width/2-(bitmap.getWidth());
        canvas.drawBitmap(bitmap,image_x, image_y, paint); //在10,60处开始绘制图片



        String s2="关机";

        Paint s2_paint=new Paint();
        //  设置字体大小
        s2_paint.setColor(Color.RED);
        s2_paint.setTextSize(18);
        float s2_width= s2_paint.measureText(s2);
        float s2_height = (float) (s2_paint.descent()-s2_paint.ascent());
        float s2_x=width/2-s2_width/2;
        float s2_y=image_y+bitmap.getHeight();
        canvas.drawText(s2, s2_x,s2_y+s2_height , s2_paint);


    }
}
