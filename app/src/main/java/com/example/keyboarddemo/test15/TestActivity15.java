package com.example.keyboarddemo.test15;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.keyboarddemo.R;
import com.example.keyboarddemo.utils.SoftInputUtils;

/**
 * adjustPan 模式下保持底层界面不往上移动
 */
public class TestActivity15 extends AppCompatActivity {

    private EditText edtvTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test15);
        edtvTest = findViewById(R.id.edtv_test);
    }

    public void onTest1(View v) {
        SoftInputUtils.showSoftInput(edtvTest);
    }

    public void onTest2(View v) {
        SoftInputUtils.hideSoftInput(edtvTest);
    }

}