package com.example.keyboarddemo.test15;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.keyboarddemo.R;
import com.example.keyboarddemo.utils.SoftInputUtils;

/**
 * adjustPan 模式下保持底层界面不往上移动
 * 
 * 在 Activity 层面监听软键盘状态，然后对 MyRelativeLayout15 应用 TranslationY 来固定位置
 */
public class TestActivity15 extends AppCompatActivity {

    private EditText edtvTest;
    private MyRelativeLayout15 myRelativeLayout15;
    private Runnable positionFixRunnable;
    private boolean isRunning = false;
    private int[] initialParentScreenLocation = new int[2];
    private boolean isInitialized = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test15);
        edtvTest = findViewById(R.id.edtv_test);
        
        // 查找 MyRelativeLayout15
        myRelativeLayout15 = findViewById(R.id.myrelativelayout15);
        
        if (myRelativeLayout15 != null) {
            initSoftInputListener();
        }
    }

    /**
     * 初始化软键盘监听，用于固定 MyRelativeLayout15 的位置
     */
    private void initSoftInputListener() {
        if (myRelativeLayout15 == null) {
            return;
        }
        
        final View rootView = getWindow().getDecorView();
        if (rootView == null) {
            return;
        }
        
        // 延迟初始化，记录初始位置
        rootView.post(new Runnable() {
            @Override
            public void run() {
                View parent = (View) myRelativeLayout15.getParent();
                if (parent != null) {
                    parent.getLocationOnScreen(initialParentScreenLocation);
                    isInitialized = true;
                    // 初始化完成后启动持续检查
                    startPositionFix();
                }
            }
        });
    }

    /**
     * 启动持续检查位置的 Runnable
     */
    private void startPositionFix() {
        if (isRunning || !isInitialized || myRelativeLayout15 == null) {
            return;
        }
        
        isRunning = true;
        final View rootView = getWindow().getDecorView();
        if (rootView == null) {
            isRunning = false;
            return;
        }
        
        positionFixRunnable = new Runnable() {
            @Override
            public void run() {
                if (!isInitialized || !isRunning || myRelativeLayout15 == null) {
                    return;
                }
                
                View parent = (View) myRelativeLayout15.getParent();
                if (parent == null) {
                    return;
                }
                
                // 检查父容器的屏幕坐标是否改变
                int[] currentParentScreenLocation = new int[2];
                parent.getLocationOnScreen(currentParentScreenLocation);
                int parentScreenYOffset = currentParentScreenLocation[1] - initialParentScreenLocation[1];
                
                // 如果父容器移动了，使用 TranslationY 来抵消
                if (parentScreenYOffset != 0) {
                    myRelativeLayout15.setTranslationY(-parentScreenYOffset);
                } else {
                    myRelativeLayout15.setTranslationY(0);
                }
                
                // 每4ms检查一次（约250fps，非常频繁）
                rootView.postDelayed(this, 4);
            }
        };
        
        rootView.post(positionFixRunnable);
    }

    /**
     * 停止持续检查
     */
    private void stopPositionFix() {
        isRunning = false;
        if (positionFixRunnable != null) {
            View rootView = getWindow().getDecorView();
            if (rootView != null) {
                rootView.removeCallbacks(positionFixRunnable);
            }
            positionFixRunnable = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopPositionFix();
    }

    public void onTest1(View v) {
        SoftInputUtils.showSoftInput(edtvTest);
    }

    public void onTest2(View v) {
        SoftInputUtils.hideSoftInput(edtvTest);
    }

}