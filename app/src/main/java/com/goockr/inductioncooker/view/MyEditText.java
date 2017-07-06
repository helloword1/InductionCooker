package com.goockr.inductioncooker.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.goockr.inductioncooker.R;

import butterknife.ButterKnife;

/**
 * Created by CMQ on 2017/7/4.
 */

public class MyEditText extends RelativeLayout implements TextWatcher, View.OnFocusChangeListener {

    View contentView;

    EditText editText;

    ImageButton cear_ib;

    ImageView bg_iv;

    public void setBgImageVisibility(boolean visit)
    {
        if (visit)
        {
            bg_iv.setVisibility(VISIBLE);
        }else {
            bg_iv.setVisibility(INVISIBLE);
        }

    }

    public  void setInputType(int inputType)
    {
        editText.setInputType(inputType);
    }

    public void setHint(String text)
    {
        editText.setHint(text);
    }

    public String getText()
    {
        return editText.getText().toString();
    }

    public MyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        contentView= LayoutInflater.from(context).inflate(R.layout.myeditor, this, true);

        initUI();

        initEvent();

    }

    private void initUI() {

        cear_ib=(ImageButton) contentView.findViewById(R.id.myeditor_cear_ib);

        editText=(EditText)contentView.findViewById(R.id.myeditor_et);
        editText.addTextChangedListener(this);
        editText.setOnFocusChangeListener(this);
        editText.setMaxLines(1);


        bg_iv=(ImageView)contentView.findViewById(R.id.myeditor_bg_iv);



    }

    private void initEvent() {

        cear_ib.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
            }
        });

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {




    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

//        if (editText.getText().length()==0)
//        {
//            cear_ib.setVisibility(INVISIBLE);
//        }else {
//            cear_ib.setVisibility(VISIBLE);
//        }



    }

    @Override
    public void afterTextChanged(Editable s) {

        if (s.toString().length() != 0) {
            cear_ib.setVisibility(View.VISIBLE);
        } else {
            cear_ib.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        if(hasFocus) {

          // 此处为得到焦点时的处理内容
         if (editText.getText().length()==0)
        {
            cear_ib.setVisibility(INVISIBLE);
        }else {
            cear_ib.setVisibility(VISIBLE);
        }

        } else {

           // 此处为失去焦点时的处理内容
            cear_ib.setVisibility(View.INVISIBLE);

        }


    }
}
