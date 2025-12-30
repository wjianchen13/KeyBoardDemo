package com.example.keyboarddemo.test15;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * 在 adjustPan 模式下，通过标记需要固定的 View，由 Activity 层面来修复位置
 * 
 * 这个类本身不做任何修复，只是提供一个标记，让 Activity 知道这个 View 需要固定
 * Activity 会监听软键盘状态，然后对这个 View 应用 TranslationY 来固定位置
 */
public class MyRelativeLayout15 extends RelativeLayout {

    public MyRelativeLayout15(Context context) {
        super(context);
    }

    public MyRelativeLayout15(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRelativeLayout15(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
