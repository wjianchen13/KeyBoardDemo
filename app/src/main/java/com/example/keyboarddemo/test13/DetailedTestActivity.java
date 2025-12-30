package com.example.keyboarddemo.test13;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.keyboarddemo.R;

/**
 * 详细的测试 Activity，显示实时的 Insets 信息
 */
public class DetailedTestActivity extends AppCompatActivity {

    private TextView tvInsetsInfo;
    private EditText etInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 启用边到边显示
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        setContentView(R.layout.activity_detailed_test);

        tvInsetsInfo = findViewById(R.id.tvInsetsInfo);
        etInput = findViewById(R.id.etInput);
        Button btnReset = findViewById(R.id.btnReset);

        // 监听 Window Insets 变化
        View rootView = findViewById(android.R.id.content);
        ViewCompat.setOnApplyWindowInsetsListener(rootView, new androidx.core.view.OnApplyWindowInsetsListener() {
            @NonNull
            @Override
            public WindowInsetsCompat onApplyWindowInsets(@NonNull View v, @NonNull WindowInsetsCompat insets) {
                updateInsetsInfo(insets);
                return insets;
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etInput.setText("");
                etInput.clearFocus();
            }
        });
    }

    private void updateInsetsInfo(WindowInsetsCompat insets) {
        Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
        Insets ime = insets.getInsets(WindowInsetsCompat.Type.ime());
        Insets statusBars = insets.getInsets(WindowInsetsCompat.Type.statusBars());
        Insets navigationBars = insets.getInsets(WindowInsetsCompat.Type.navigationBars());

        StringBuilder info = new StringBuilder();
        info.append("═══════════════════════════════\n");
        info.append("Window Insets 实时信息\n");
        info.append("═══════════════════════════════\n\n");

        info.append("📱 系统栏 (systemBars):\n");
        info.append("   left:   ").append(systemBars.left).append("px\n");
        info.append("   top:    ").append(systemBars.top).append("px\n");
        info.append("   right:  ").append(systemBars.right).append("px\n");
        info.append("   bottom: ").append(systemBars.bottom).append("px\n\n");

        info.append("📊 状态栏 (statusBars):\n");
        info.append("   top:    ").append(statusBars.top).append("px\n\n");

        info.append("📐 导航栏 (navigationBars):\n");
        info.append("   bottom: ").append(navigationBars.bottom).append("px\n\n");

        info.append("⌨️ 键盘 (IME):\n");
        info.append("   bottom: ").append(ime.bottom).append("px\n");
        info.append("   状态:   ").append(ime.bottom > 0 ? "已显示 ✅" : "未显示 ❌").append("\n\n");

        if (ime.bottom > 0) {
            info.append("💡 键盘高度: ").append(ime.bottom).append("px\n");
        }

        info.append("═══════════════════════════════\n");
        info.append("提示: 点击下方输入框测试键盘\n");

        tvInsetsInfo.setText(info.toString());

        // 根据键盘状态改变背景颜色
        if (ime.bottom > 0) {
            tvInsetsInfo.setBackgroundColor(Color.parseColor("#E8F5E9"));
        } else {
            tvInsetsInfo.setBackgroundColor(Color.parseColor("#E3F2FD"));
        }
    }
}
