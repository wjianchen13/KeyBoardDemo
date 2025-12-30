package com.example.keyboarddemo.test16;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.keyboarddemo.R;

/**
 * 直播间界面
 * 需求：键盘弹出时，只有 EditText 上移，其他内容保持不动
 */
public class LiveRoomActivity extends AppCompatActivity {

    private View videoLayer;           // 视频层
    private View infoLayer;            // 主播信息+公聊区域
    private View menuLayer;            // 菜单
    private EditText editText;         // 输入框
    private View inputContainer;       // 输入框容器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. 启用边到边显示，关键配置！
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        setContentView(R.layout.activity_live_room);

        // 2. 初始化视图
        videoLayer = findViewById(R.id.videoLayer);
        infoLayer = findViewById(R.id.infoLayer);
        menuLayer = findViewById(R.id.menuLayer);
        editText = findViewById(R.id.editText);
        inputContainer = findViewById(R.id.inputContainer);
        Button btnMessage = findViewById(R.id.btnMessage);

        // 3. 默认隐藏输入框
        inputContainer.setVisibility(View.GONE);

        // 4. 点击消息按钮
        btnMessage.setOnClickListener(v -> showInputAndKeyboard());

        // 5. 监听键盘高度变化
        setupKeyboardListener();
    }

    /**
     * 显示输入框并弹出键盘
     */
    private void showInputAndKeyboard() {
        inputContainer.setVisibility(View.VISIBLE);
        editText.requestFocus();

        // 延迟弹出键盘，确保布局已完成
        editText.postDelayed(() -> {
            android.view.inputmethod.InputMethodManager imm =
                    (android.view.inputmethod.InputMethodManager)
                            getSystemService(android.content.Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editText, 0);
        }, 100);
    }

    /**
     * 核心方法：监听键盘高度，只移动输入框
     */
    private void setupKeyboardListener() {
        View rootView = findViewById(android.R.id.content);

        ViewCompat.setOnApplyWindowInsetsListener(rootView,
                new androidx.core.view.OnApplyWindowInsetsListener() {
                    @NonNull
                    @Override
                    public WindowInsetsCompat onApplyWindowInsets(
                            @NonNull View v,
                            @NonNull WindowInsetsCompat insets) {

                        // 获取键盘高度
                        Insets imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime());
                        Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());

                        int keyboardHeight = imeInsets.bottom;

                        if (keyboardHeight > 0) {
                            // 键盘弹出：移动输入框到键盘上方
                            moveInputAboveKeyboard(keyboardHeight);
                        } else {
                            // 键盘隐藏：恢复输入框位置
                            resetInputPosition();
                        }

                        // 其他层不处理，保持原位
                        return insets;
                    }
                });
    }

    /**
     * 将输入框移动到键盘上方
     */
    private void moveInputAboveKeyboard(int keyboardHeight) {
        ViewGroup.MarginLayoutParams params =
                (ViewGroup.MarginLayoutParams) inputContainer.getLayoutParams();

        // 设置底部margin = 键盘高度
        params.bottomMargin = keyboardHeight;
        inputContainer.setLayoutParams(params);

        // 或者使用 translationY（两种方式选一种）
        // inputContainer.setTranslationY(-keyboardHeight);
    }

    /**
     * 恢复输入框原始位置
     */
    private void resetInputPosition() {
        ViewGroup.MarginLayoutParams params =
                (ViewGroup.MarginLayoutParams) inputContainer.getLayoutParams();
        params.bottomMargin = 120;
        inputContainer.setLayoutParams(params);

        // inputContainer.setTranslationY(0);
    }
}