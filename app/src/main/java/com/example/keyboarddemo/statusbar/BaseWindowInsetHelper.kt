package com.example.keyboarddemo.statusbar

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.lang.ref.WeakReference

/**
 * @author cginechen
 * @date 2017-09-13
 */
class BaseWindowInsetHelper(context: Context?, viewGroup: ViewGroup?, windowInsetLayout: IBaseWindowInsetLayout?) {
    private val BASE_KEYBOARD_HEIGHT_BOUNDARY: Int
    private val mBaseWindowInsetLayoutWR: WeakReference<IBaseWindowInsetLayout?>

    init {
        mBaseWindowInsetLayoutWR = WeakReference(windowInsetLayout)
        BASE_KEYBOARD_HEIGHT_BOUNDARY = dipToPx(100f, context)
        ViewCompat.setOnApplyWindowInsetsListener(
            viewGroup!!
        ) { v, insets -> setWindowInsets(insets) }
    }

    @TargetApi(21)
    fun defaultApplySystemWindowInsets21(
        viewGroup: ViewGroup,
        insets: WindowInsetsCompat
    ): Boolean {
        if (!insets.hasSystemWindowInsets()) {
            return false
        }
        var consumed = false
        var showKeyboard = false
        
        // 判断是否为软键盘（bottom >= 100dp）
        if (insets.systemWindowInsetBottom >= BASE_KEYBOARD_HEIGHT_BOUNDARY) {
            showKeyboard = true
            // 软键盘弹出时，给自身设置bottom padding，同时保留top/left/right padding
            viewGroup.setPadding(
                insets.systemWindowInsetLeft,
                insets.systemWindowInsetTop,
                insets.systemWindowInsetRight,
                insets.systemWindowInsetBottom
            )
        } else {
            // 没有软键盘时，给自身设置top/left/right/bottom padding来避开系统栏（状态栏、导航栏）
            viewGroup.setPadding(
                insets.systemWindowInsetLeft,
                insets.systemWindowInsetTop,
                insets.systemWindowInsetRight,
                insets.systemWindowInsetBottom
            )
        }
        
        for (i in 0 until viewGroup.childCount) {
            val child = viewGroup.getChildAt(i)
            if (jumpDispatch(child)) {
                continue
            }
            val childInsets = Rect(
                insets.systemWindowInsetLeft,
                insets.systemWindowInsetTop,
                insets.systemWindowInsetRight,
                if (showKeyboard) 0 else insets.systemWindowInsetBottom
            )
            computeInsetsWithGravity(child, childInsets)
            val windowInsetsCompat = ViewCompat.dispatchApplyWindowInsets(
                child,
                insets.replaceSystemWindowInsets(childInsets)
            )
            consumed = consumed || windowInsetsCompat.isConsumed
        }
        return consumed
    }

    private fun setWindowInsets(insets: WindowInsetsCompat): WindowInsetsCompat {
        if (Build.VERSION.SDK_INT >= 21 && mBaseWindowInsetLayoutWR.get() != null) {
            if (mBaseWindowInsetLayoutWR.get()!!.applySystemWindowInsets21(insets)) {
                return insets.consumeSystemWindowInsets()
            }
        }
        return insets
    }

    @SuppressLint("RtlHardcoded")
    private fun computeInsetsWithGravity(view: View, insets: Rect) {
        val lp = view.layoutParams
        var gravity = -1
        if (lp is FrameLayout.LayoutParams) {
            gravity = lp.gravity
        }
        /**
         * 因为该方法执行时机早于 FrameLayout.layoutChildren，
         * 而在 {FrameLayout#layoutChildren} 中当 gravity == -1 时会设置默认值为 Gravity.TOP | Gravity.START，
         * 所以这里也要同样设置
         */
        if (gravity == -1) {
            gravity = Gravity.TOP or Gravity.START
        }
        if (lp.width != FrameLayout.LayoutParams.MATCH_PARENT) {
            val horizontalGravity = gravity and Gravity.HORIZONTAL_GRAVITY_MASK
            when (horizontalGravity) {
                Gravity.START -> insets.right = 0
                Gravity.END -> insets.left = 0
            }
        }
        if (lp.height != FrameLayout.LayoutParams.MATCH_PARENT) {
            val verticalGravity = gravity and Gravity.VERTICAL_GRAVITY_MASK
            when (verticalGravity) {
                Gravity.TOP -> insets.bottom = 0
                Gravity.BOTTOM -> insets.top = 0
            }
        }
    }

    @Suppress("deprecation")
    @TargetApi(19)
    fun defaultApplySystemWindowInsets19(viewGroup: ViewGroup, insets: Rect): Boolean {
        var consumed = false
        if (insets.bottom >= BASE_KEYBOARD_HEIGHT_BOUNDARY) {
            BaseViewHelper.setBasePaddingBottom(viewGroup, insets.bottom) //insets.bottom
            insets.bottom = 0
        } else {
            BaseViewHelper.setBasePaddingBottom(viewGroup, 0)
        }
        for (i in 0 until viewGroup.childCount) {
            val child = viewGroup.getChildAt(i)
            if (jumpDispatch(child)) {
                continue
            }
            val childInsets = Rect(insets)
            computeInsetsWithGravity(child, childInsets)
            if (!isHandleContainer(child)) {
                child.setPadding(
                    childInsets.left,
                    childInsets.top,
                    childInsets.right,
                    childInsets.bottom
                )
            } else {
                consumed = if (child is IBaseWindowInsetLayout) {
                    val output =
                        (child as IBaseWindowInsetLayout).applySystemWindowInsets19(childInsets)
                    consumed || output
                } else {
                    val output = defaultApplySystemWindowInsets19(child as ViewGroup, childInsets)
                    consumed || output
                }
            }
        }
        return consumed
    }

    companion object {
        @Suppress("deprecation")
        @TargetApi(19)
        fun jumpDispatch(child: View): Boolean {
            return !child.fitsSystemWindows && !isHandleContainer(child)
        }

        fun isHandleContainer(child: View?): Boolean {
            return child is IBaseWindowInsetLayout
        }
    }

    fun dipToPx(dpValue: Float, context: Context?): Int {
        return (dpValue *  getAppDensity(context) + 0.5f).toInt()
    }

    /**
     * 返回屏幕密度
     */
    private fun getAppDensity(context: Context?): Float {
        return if (context != null && context.resources != null && context.resources.displayMetrics != null) {
            return context.resources.displayMetrics.density
        } else
            return 1f
    }

}