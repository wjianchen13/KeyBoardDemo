package com.example.keyboarddemo.test15;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class MyRelativeLayout15 extends RelativeLayout {

    private int myHeigh = 0;
    private Rect rect = new Rect();

    public MyRelativeLayout15(Context context) {
        super(context);
        init();
    }

    public MyRelativeLayout15(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyRelativeLayout15(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(){
        this.post(new Runnable() {
            @Override
            public void run() {
                myHeigh = MyRelativeLayout15.this.getHeight();
            }
        });
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        getWindowVisibleDisplayFrame(rect);

        int totalHeight = getRootView().getHeight();
        int nowHeight = rect.bottom - rect.top;
        if (totalHeight - nowHeight > totalHeight / 4) {    //软键盘弹起
//            super.onMeasure(widthSpec, MeasureSpec.makeMeasureSpec(totalHeight, MeasureSpec.EXACTLY));
            super.onMeasure(widthSpec, MeasureSpec.makeMeasureSpec(myHeigh, MeasureSpec.EXACTLY));
        } else {
            super.onMeasure(widthSpec, heightSpec);
        }
        int widthMode = MeasureSpec.getMode(widthSpec);
        int width = MeasureSpec.getSize(widthSpec);
        int heightMode = MeasureSpec.getMode(heightSpec);
        int height = MeasureSpec.getSize(heightSpec);
//        System.out.println("================> withMode: " + widthMode + "   width: " + width + "   heightMode: " + heightMode + "   height: " + height);

    }



}
