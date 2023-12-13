package com.example.keyboarddemo.utils;

import android.view.View;

public class Utils {

    public static void log(String str) {
        System.out.println("=========================> " + str);
    }

    /**
     * 获取某个View相对于屏幕的Y坐标
     * @param v
     * @return
     */
    public static int getLocationY(View v) {
        if(v != null) {
            int[] location = new int[2];
            v.getLocationOnScreen(location);
            return location[1]; // 获取当前位置的纵坐标
        }
        return 0;
    }

}
