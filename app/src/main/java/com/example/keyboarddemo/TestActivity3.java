package com.example.keyboarddemo;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.keyboarddemo.utils.SoftInputUtils;

/**
 * 测试softInputMode
 * SOFT_INPUT_ADJUST_UNSPECIFIED
 * 不指定调整方式，系统自行决定使用哪种调整方式
 *
 * SOFT_INPUT_ADJUST_RESIZE
 * 调整方式为布局需要重新计算大小适配当前可见区域
 *
 * SOFT_INPUT_ADJUST_PAN
 * 调整方式为布局需要整体移动
 *
 * SOFT_INPUT_ADJUST_NOTHING
 * 不做任何操作
 */
public class TestActivity3 extends AppCompatActivity {

    private EditText edtvTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test3);
        edtvTest = findViewById(R.id.edtv_test);
    }

    public void onTest1(View v) {
        SoftInputUtils.showSoftInput(edtvTest);
    }

    public void onTest2(View v) {
        SoftInputUtils.hideSoftInput(edtvTest);
    }

}