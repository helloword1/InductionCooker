package com.jinlin.zxing;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import cn.jinlin.zxing.R;

public class InputNumberActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_number);
        findViewById(R.id.capture_back_tv).setOnClickListener(this);
        findViewById(R.id.capture_add_bt).setOnClickListener(this);
        editText = (EditText) findViewById(R.id.etCapicture);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.capture_back_tv) {
            finish();
        } else if (id == R.id.capture_add_bt) {
            String a = editText.getText().toString();
            if (TextUtils.equals(a, "")) {
                Toast.makeText(this, "请输入正确的设备号", Toast.LENGTH_SHORT).show();
            } else {
                setResult(RESULT_OK, new Intent().putExtra("INPUT_NUMBER", a));
                finish();
            }

        }
    }
}
