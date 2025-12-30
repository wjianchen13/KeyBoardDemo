package com.example.keyboarddemo.statusbar

import android.graphics.Rect
import androidx.core.view.WindowInsetsCompat

/**
 * @author cginechen
 * @date 2017-09-13
 */
interface IBaseWindowInsetLayout {
    fun applySystemWindowInsets21(insets: WindowInsetsCompat): Boolean
    fun applySystemWindowInsets19(insets: Rect): Boolean
}