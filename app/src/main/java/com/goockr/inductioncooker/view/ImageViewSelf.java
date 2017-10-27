package com.goockr.inductioncooker.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.goockr.inductioncooker.R;

/**
 * Created by CMQ on 2017/6/26.
 */

public class ImageViewSelf extends ImageView {

    //实例化一个paint对象，其可以设置canvas绘制图形的颜色等属性
    Paint paint = new Paint();
    //获得piece.jpg图片的资源，创建Bitmap对象
    private Bitmap bitmap;

    //以下三个构造方法本人暂时不懂啥意思
    public ImageViewSelf(Context context) {
        super(context);
    }

    public ImageViewSelf(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ImageViewSelf(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //设置bitmap的图片资源
    public void initImage(){
        bitmap = Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.home_icon_warning));
    }

    //重写onDraw方法
    @Override
    public void onDraw(Canvas canvas)
    {
        //执行父类的onDraw方法
        super.onDraw(canvas);
        if(bitmap!=null){
            //在150，150坐标处画图bitmap图形
            canvas.drawBitmap(bitmap, 150, 150, paint);
        }

    }


}
