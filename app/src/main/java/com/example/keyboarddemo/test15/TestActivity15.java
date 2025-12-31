package com.example.keyboarddemo.test15;

import android.animation.ValueAnimator;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

import com.example.keyboarddemo.R;

/**
 * adjustPan 模式下保持底层界面不往上移动, 测试之后发现不好实现，失败
 */
public class TestActivity15 extends AppCompatActivity {

    private View videoLayer;
    private View infoLayer;
    private View rootView;
    private EditText editText;

    private int screenHeight;
    private ValueAnimator currentAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test15);

        videoLayer = findViewById(R.id.videoLayer);
        infoLayer = findViewById(R.id.infoLayer);
        rootView = findViewById(android.R.id.content);
        editText = findViewById(R.id.editText);
        Button btnMessage = findViewById(R.id.btnMessage);

        screenHeight = getResources().getDisplayMetrics().heightPixels;

        btnMessage.setOnClickListener(v -> {
            findViewById(R.id.inputContainer).setVisibility(View.VISIBLE);
            editText.requestFocus();
            showKeyboard();
        });

        setupLayoutListener();
    }

    private void showKeyboard() {
        editText.postDelayed(() -> {
            android.view.inputmethod.InputMethodManager imm =
                    (android.view.inputmethod.InputMethodManager)
                            getSystemService(INPUT_METHOD_SERVICE);
            imm.showSoftInput(editText, 0);
        }, 100);
    }

    private void setupLayoutListener() {
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        Rect rect = new Rect();
                        rootView.getWindowVisibleDisplayFrame(rect);

                        int visibleHeight = rect.bottom - rect.top;
                        int heightDiff = screenHeight - visibleHeight;

                        if (heightDiff > 200) {
                            int compensateAmount = calculateCompensateAmount(heightDiff);
                            animateCompensation(compensateAmount);
                        } else {
                            animateCompensation(0);
                        }
                    }
                });
    }

    /**
     * 使用动画平滑过渡
     */
    private void animateCompensation(int targetMargin) {
        if (currentAnimator != null && currentAnimator.isRunning()) {
            currentAnimator.cancel();
        }

        ViewGroup.MarginLayoutParams videoParams =
                (ViewGroup.MarginLayoutParams) videoLayer.getLayoutParams();
        int currentMargin = videoParams.topMargin;

        currentAnimator = ValueAnimator.ofInt(currentMargin, targetMargin);
        currentAnimator.setDuration(250); // 250ms 动画
        currentAnimator.addUpdateListener(animation -> {
            int animatedValue = (int) animation.getAnimatedValue();

            // 更新视频层
            ViewGroup.MarginLayoutParams vParams =
                    (ViewGroup.MarginLayoutParams) videoLayer.getLayoutParams();
            vParams.topMargin = animatedValue;
            videoLayer.setLayoutParams(vParams);

            // 更新信息层
            ViewGroup.MarginLayoutParams iParams =
                    (ViewGroup.MarginLayoutParams) infoLayer.getLayoutParams();
            iParams.topMargin = animatedValue;
            infoLayer.setLayoutParams(iParams);
        });
        currentAnimator.start();
    }

    private int calculateCompensateAmount(int keyboardHeight) {
        int[] location = new int[2];
        editText.getLocationOnScreen(location);
        int editTextTop = location[1];

        int visibleHeight = screenHeight - keyboardHeight;
        int needMoveUp = editTextTop + editText.getHeight() - visibleHeight;

        return needMoveUp > 0 ? (int)(needMoveUp * 0.6f) : 0;
    }
}