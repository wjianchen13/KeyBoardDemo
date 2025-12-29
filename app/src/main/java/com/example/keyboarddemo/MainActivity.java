package com.example.keyboarddemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.keyboarddemo.test11.TestActivity11;
import com.example.keyboarddemo.test12.TestActivity12;
import com.example.keyboarddemo.test4.TestActivity4;
import com.example.keyboarddemo.test5.TestActivity5;
import com.example.keyboarddemo.test6.TestActivity6;
import com.example.keyboarddemo.test7.TestActivity7;
import com.example.keyboarddemo.test8.TestActivity8;
import com.example.keyboarddemo.test9.TestActivity9;
import com.example.keyboarddemo.test10.TestActivity10;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * 弹出和收起键盘测试
     * @param v
     */
    public void onTest1(View v) {
        startActivity(new Intent(this, TestActivity1.class));
    }

    /**
     * 测试softInputMode
     */
    public void onTest2(View v) {
        startActivity(new Intent(this, TestActivity2.class));
    }

    /**
     * 测试softInputMode
     */
    public void onTest3(View v) {
        startActivity(new Intent(this, TestActivity3.class));
    }

    /**
     * EditText顶上去，ImageView保持不动
     */
    public void onTest4(View v) {
        startActivity(new Intent(this, TestActivity4.class));
    }

    /**
     * 模拟项目保持视频层不挤压
     */
    public void onTest5(View v) {
        startActivity(new Intent(this, TestActivity5.class));
    }

    /**
     * 模拟项目软键盘弹出消息列表上移
     */
    public void onTest6(View v) {
        startActivity(new Intent(this, TestActivity6.class));
    }

    /**
     * H5页面软键盘弹出H5上移
     */
    public void onTest7(View v) {
        startActivity(new Intent(this, TestActivity7.class));
    }

    /**
     * H5页面软键盘弹出H5上移
     */
    public void onTest8(View v) {
        startActivity(new Intent(this, TestActivity8.class));
    }

    /**
     * adjustPan ConstraintLayout 测试软键盘弹出情况
     */
    public void onTest9(View v) {
        startActivity(new Intent(this, TestActivity9.class));
    }

    /**
     * adjustResize ConstraintLayout 测试软键盘弹出情况
     */
    public void onTest10(View v) {
        startActivity(new Intent(this, TestActivity10.class));
    }

    /**
     * adjustPan RelativeLayout 测试软键盘弹出情况
     */
    public void onTest11(View v) {
        startActivity(new Intent(this, TestActivity11.class));
    }

    /**
     * adjustResize RelativeLayout 测试软键盘弹出情况
     */
    public void onTest12(View v) {
        startActivity(new Intent(this, TestActivity12.class));
    }


}