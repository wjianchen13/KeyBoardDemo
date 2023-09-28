package com.example.keyboarddemo;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.keyboarddemo.utils.SoftInputUtils;

/**
 * 弹出和收起键盘测试
 */
public class TestActivity1 extends AppCompatActivity {

    private EditText edtvTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test1);
        edtvTest = findViewById(R.id.edtv_test);
    }

    public void onTest1(View v) {
        SoftInputUtils.showSoftInput(edtvTest);
    }

    public void onTest2(View v) {
        SoftInputUtils.hideSoftInput(edtvTest);
    }

}