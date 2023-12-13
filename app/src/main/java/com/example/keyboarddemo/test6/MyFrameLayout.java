package com.example.keyboarddemo.test6;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.keyboarddemo.utils.Utils;

public class MyFrameLayout extends FrameLayout {

    private OnLayoutListener mListener;

    public void setListener(OnLayoutListener listener) {
        this.mListener = listener;
    }

    public MyFrameLayout(@NonNull Context context) {
        super(context);
    }

    public MyFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Utils.log("MyFrameLayout onSizeChanged w: " + w + "  h: " + h + "  oldw: " + oldw + "  oldh: " + oldh);
        if(mListener != null)
            mListener.onSizeChanged(w, h, oldw, oldh);
    }

    public interface OnLayoutListener {
        void onSizeChanged(int w, int h, int oldw, int oldh);
    }
}
