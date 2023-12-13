package com.example.keyboarddemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.keyboarddemo.test4.TestActivity4;
import com.example.keyboarddemo.test5.TestActivity5;
import com.example.keyboarddemo.test6.TestActivity6;

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

}