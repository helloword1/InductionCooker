package com.goockr.inductioncooker.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.goockr.inductioncooker.R;
import com.goockr.inductioncooker.models.Moden;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by CMQ on 2017/6/27.
 */

public class ImageTopButton extends LinearLayout implements View.OnTouchListener, View.OnClickListener {


    View contentView;

    private ImageTopButtonOnClickListener callback;

    public Moden moden;

    @BindView(R.id.imagetop_button_title_tv)
    TextView title_tv;
    @BindView(R.id.imagetop_button_top_iv)
    ImageView top_iv;
    @BindView(R.id.imagetop_button_top_ll)
    LinearLayout m_ll;

    private int normImageId;

    private int selImageId;

    private int hightLightImageId = 0;

    private int disabledImageId;

    private boolean select = false;

    private String text;

    private int normTextCoclor;

    private int selTextColor;

    private boolean enabledStatus = true;

    public int getNormImageId() {
        return normImageId;
    }

    public void setNormImageId(int normImageId) {
        this.normImageId = normImageId;
        top_iv.setImageResource(normImageId);
    }

    public int getHightLightImageId() {
        return hightLightImageId;
    }

    public void setHightLightImageId(int hightLightImageId) {
        this.hightLightImageId = hightLightImageId;
    }

    public int getSelImageId() {
        return selImageId;
    }

    public void setSelImageId(int selImageId) {
        this.selImageId = selImageId;
    }

    public void setSelect(boolean select) {
        this.select = select;

        if (select) {
            top_iv.setImageResource(selImageId);
        } else {
            top_iv.setImageResource(normImageId);
        }

    }

    public boolean isSelect() {
        return select;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        title_tv.setText(text);
    }

    public int getNormTextCoclor() {
        return normTextCoclor;
    }

    public void setNormTextCoclor(int normTextCoclor) {
        this.normTextCoclor = normTextCoclor;
        title_tv.setTextColor(getResources().getColor(normTextCoclor));

        // title_tv.setTextColor(R.color.colorRed);

    }

    public int getSelTextColor() {
        return selTextColor;
    }

    public void setSelTextColor(int selTextColor) {
        this.selTextColor = selTextColor;
    }

    public boolean isEnabledStatus() {
        return enabledStatus;
    }

    public void setEnabledStatus(boolean enabledStatus) {
        this.enabledStatus = enabledStatus;

        setEnabled(enabledStatus);

        if (enabledStatus) {
            top_iv.setImageResource(normImageId);
        } else {
            top_iv.setImageResource(disabledImageId);
        }
    }

    public int getDisabledImageId() {
        return disabledImageId;
    }

    public void setDisabledImageId(int disabledImageId) {
        this.disabledImageId = disabledImageId;
    }


    public ImageTopButton(Context context) {
        this(context, null);

    }


    public ImageTopButton(Context context, AttributeSet attrs) {
        super(context, attrs);


        contentView = LayoutInflater.from(context).inflate(R.layout.imagetop_button, this, true);

        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.ImageTopButton);

        ButterKnife.bind(this);

        if (mTypedArray != null) {
            setNormImageId(mTypedArray.getResourceId(R.styleable.ImageTopButton_normImage, 0));
            selImageId = mTypedArray.getResourceId(R.styleable.ImageTopButton_selImage, 0);
            disabledImageId = mTypedArray.getResourceId(R.styleable.ImageTopButton_displayImage, 0);
            int a = mTypedArray.getColor(R.styleable.ImageTopButton_textColor, Color.RED);
            title_tv.setTextColor(a);
            // setNormTextCoclor(mTypedArray.getColor(R.styleable.ImageTopButton_textColor,Color.RED));
            setText(mTypedArray.getString(R.styleable.ImageTopButton_text));
            mTypedArray.recycle();
        }

        m_ll.setClickable(true);
        m_ll.setOnClickListener(this);
        m_ll.setOnTouchListener(this);
        // m_ll.setBackgroundColor(Color.YELLOW);

    }


    public void buttonOnClickListener(ImageTopButtonOnClickListener callback) {
        this.callback = callback;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // m_ll.setBackgroundColor(Color.rgb(127,127,127));
            if (hightLightImageId != 0) {
                top_iv.setImageResource(hightLightImageId);
            }

        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            //m_ll.setBackgroundColor(Color.TRANSPARENT);

            if (isSelect()) {
                top_iv.setImageResource(selImageId);
            } else {

                top_iv.setImageResource(normImageId);
            }


        }

        return false;
    }

    @Override
    public void onClick(View v) {

        // Toast.makeText(MyApplication.getContext(),"onClick",Toast.LENGTH_SHORT);

        if (this.callback != null) {
            this.callback.imageTopButtonOnClickListener(this);
        }

    }

    public interface ImageTopButtonOnClickListener {
        public void imageTopButtonOnClickListener(ImageTopButton button);
    }

}
