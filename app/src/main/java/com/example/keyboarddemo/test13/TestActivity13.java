package com.example.keyboarddemo.test13;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import com.example.keyboarddemo.R;

/**
 * 演示使用 BaseWindowInsetRelativeLayout 和普通 RelativeLayout 的区别
 */
public class TestActivity13 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ===================================
        // 场景切换按钮
        // ===================================
        setContentView(R.layout.activity_comparison_selector);

        Button btnNormalMode = findViewById(R.id.btnNormalMode);
        Button btnEdgeToEdgeWithoutCustom = findViewById(R.id.btnEdgeToEdgeWithoutCustom);
        Button btnEdgeToEdgeWithCustom = findViewById(R.id.btnEdgeToEdgeWithCustom);

        btnNormalMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNormalMode();
            }
        });

        btnEdgeToEdgeWithoutCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEdgeToEdgeWithoutCustomLayout();
            }
        });

        btnEdgeToEdgeWithCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEdgeToEdgeWithCustomLayout();
            }
        });
    }

    // 场景1: 普通模式（系统自动处理）
    private void showNormalMode() {
        // 默认配置，系统自动处理 insets
        WindowCompat.setDecorFitsSystemWindows(getWindow(), true);  // 这是默认值

        setContentView(R.layout.activity_normal_mode);

        // 结果：使用 RelativeLayout 和 BaseWindowInsetRelativeLayout
        //      完全没区别！因为系统自动处理了
    }

    // 场景2: 边到边模式 + 普通 RelativeLayout（有问题）
    private void showEdgeToEdgeWithoutCustomLayout() {
        // 启用边到边显示
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        setContentView(R.layout.activity_edge_to_edge_normal);
        ViewCompat.requestApplyInsets(findViewById(android.R.id.content));

        // 结果：内容会延伸到状态栏和导航栏下面，被系统 UI 遮挡！
        //      点击底部EditText，软键盘弹出时也会遮挡输入框
    }

    // 场景3: 边到边模式 + BaseWindowInsetRelativeLayout（正确）
    private void showEdgeToEdgeWithCustomLayout() {
        // 启用边到边显示
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        setContentView(R.layout.activity_edge_to_edge_custom);
        ViewCompat.requestApplyInsets(findViewById(android.R.id.content));

        // 结果：内容会自动避开状态栏和导航栏，不会被遮挡！
        //      点击底部EditText，软键盘弹出时会自动给布局添加paddingBottom
        //      使EditText自动上移到软键盘上方，不被遮挡
    }


}