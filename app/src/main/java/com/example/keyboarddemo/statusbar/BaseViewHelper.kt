package com.example.keyboarddemo.statusbar

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.LightingColorFilter
import android.graphics.Matrix
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.TouchDelegate
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.view.ViewStub
import android.view.Window
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import android.widget.ListView
import androidx.annotation.ColorInt
import androidx.appcompat.R
import androidx.core.content.ContextCompat
import com.gou.android.ckg.base.app.CommonApp
import java.util.concurrent.atomic.AtomicInteger

/**
 * @author cginechen
 * @date 2016-03-17
 */
object BaseViewHelper {
    // copy from View.generateViewId for API <= 16
    private val sNextGeneratedId = AtomicInteger(1)
    private val APPCOMPAT_CHECK_ATTRS = intArrayOf(
        R.attr.colorPrimary
    )

    fun checkBaseAppCompatTheme(context: Context) {
        val a = context.obtainStyledAttributes(APPCOMPAT_CHECK_ATTRS)
        val failed = !a.hasValue(0)
        a.recycle()
        require(!failed) {
            ("You need to use a Theme.AppCompat theme "
                    + "(or descendant) with the design library.")
        }
    }

    @Suppress("deprecation")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    fun setBaseBackgroundKeepingPadding(view: View, drawable: Drawable?) {
        val padding =
            intArrayOf(view.paddingLeft, view.paddingTop, view.paddingRight, view.paddingBottom)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.background = drawable
        } else {
            view.setBackgroundDrawable(drawable)
        }
        view.setPadding(padding[0], padding[1], padding[2], padding[3])
    }

    /**
     * 触发window的insets的广播，使得view的fitSystemWindows得以生效
     */
    @Suppress("deprecation")
    fun requestBaseApplyInsets(window: Window) {
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            window.decorView.requestFitSystemWindows()
        } else if (Build.VERSION.SDK_INT >= 21) {
            window.decorView.requestApplyInsets()
        }
    }

    /**
     * 扩展点击区域的范围
     *
     * @param view       需要扩展的元素，此元素必需要有父级元素
     * @param expendSize 需要扩展的尺寸（以sp为单位的）
     */
    fun expendBaseTouchArea(view: View?, expendSize: Int) {
        if (view != null) {
            val parentView = view.parent as View
            parentView.post {
                val rect = Rect()
                view.getHitRect(rect) //如果太早执行本函数，会获取rect失败，因为此时UI界面尚未开始绘制，无法获得正确的坐标
                rect.left -= expendSize
                rect.top -= expendSize
                rect.right += expendSize
                rect.bottom += expendSize
                parentView.touchDelegate = TouchDelegate(rect, view)
            }
        }
    }

    fun setBaseBackgroundColorKeepPadding(view: View, @ColorInt color: Int) {
        val padding =
            intArrayOf(view.paddingLeft, view.paddingTop, view.paddingRight, view.paddingBottom)
        view.setBackgroundColor(color)
        view.setPadding(padding[0], padding[1], padding[2], padding[3])
    }

    /**
     * 获取activity的根view
     */
    fun getBaseActivityRoot(activity: Activity): View {
        return (activity.findViewById<View>(Window.ID_ANDROID_CONTENT) as ViewGroup).getChildAt(0)
    }

    /**
     * 对 View 的做背景闪动的动画
     */
    fun playBaseBackgroundBlinkAnimation(v: View?, @ColorInt bgColor: Int) {
        if (v == null) {
            return
        }
        val alphaArray = intArrayOf(0, 255, 0)
        playBaseViewBackgroundAnimation(v, bgColor, alphaArray, 300)
    }

    @Suppress("deprecation")
    fun setBaseBackgroundKeepingPadding(view: View, backgroundResId: Int) {
        setBaseBackgroundKeepingPadding(
            view,
            ContextCompat.getDrawable(CommonApp.getInstance(), backgroundResId)
        )
    }

    /**
     * 对 View 做背景色变化的动作
     *
     * @param v            做背景色变化的View
     * @param bgColor      背景色
     * @param alphaArray   背景色变化的alpha数组，如 int[]{255,0} 表示从纯色变化到透明
     * @param stepDuration 每一步变化的时长
     * @param endAction    动画结束后的回调
     */
    fun playBaseViewBackgroundAnimation(
        v: View,
        @ColorInt bgColor: Int,
        alphaArray: IntArray,
        stepDuration: Int,
        endAction: Runnable?
    ): Animator {
        val animationCount = alphaArray.size - 1
        val bgDrawable: Drawable = ColorDrawable(bgColor)
        val oldBgDrawable = v.background
        setBaseBackgroundKeepingPadding(v, bgDrawable)
        val animatorList: MutableList<Animator> = ArrayList()
        for (i in 0 until animationCount) {
            val animator =
                ObjectAnimator.ofInt(v.background, "alpha", alphaArray[i], alphaArray[i + 1])
            animatorList.add(animator)
        }
        val animatorSet = AnimatorSet()
        animatorSet.duration = stepDuration.toLong()
        animatorSet.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationEnd(animation: Animator) {
                setBaseBackgroundKeepingPadding(v, oldBgDrawable)
                endAction?.run()
            }

            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
        animatorSet.playSequentially(animatorList)
        animatorSet.start()
        return animatorSet
    }

    /**
     * 对 View 做背景色变化的动作
     *
     * @param v            做背景色变化的View
     * @param startColor   动画开始时 View 的背景色
     * @param endColor     动画结束时 View 的背景色
     * @param duration     动画总时长
     * @param repeatCount  动画重复次数
     * @param setAnimTagId 将动画设置tag给view,若为0则不设置
     * @param endAction    动画结束后的回调
     */
    fun playBaseViewBackgroundAnimation(
        v: View,
        @ColorInt startColor: Int,
        @ColorInt endColor: Int,
        duration: Long,
        repeatCount: Int,
        setAnimTagId: Int,
        endAction: Runnable?
    ) {
        val oldBgDrawable = v.background // 存储旧的背景
        setBaseBackgroundColorKeepPadding(v, startColor)
        val anim = ValueAnimator()
        anim.setIntValues(startColor, endColor)
        anim.duration = duration / (repeatCount + 1)
        anim.repeatCount = repeatCount
        anim.repeatMode = ValueAnimator.REVERSE
        anim.setEvaluator(ArgbEvaluator())
        anim.addUpdateListener { animation ->
            setBaseBackgroundColorKeepPadding(
                v,
                animation.animatedValue as Int
            )
        }
        if (setAnimTagId != 0) {
            v.setTag(setAnimTagId, anim)
        }
        anim.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationEnd(animation: Animator) {
                setBaseBackgroundKeepingPadding(v, oldBgDrawable)
                endAction?.run()
            }

            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
        anim.start()
    }

    fun playBaseViewBackgroundAnimation(
        v: View,
        @ColorInt bgColor: Int,
        alphaArray: IntArray,
        stepDuration: Int
    ) {
        playBaseViewBackgroundAnimation(v, bgColor, alphaArray, stepDuration, null)
    }

    fun generateBaseViewId(): Int {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return View.generateViewId()
        } else {
            while (true) {
                val result = sNextGeneratedId.get()
                // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
                var newValue = result + 1
                if (newValue > 0x00FFFFFF) newValue = 1 // Roll over to 1, not 0.
                if (sNextGeneratedId.compareAndSet(result, newValue)) {
                    return result
                }
            }
        }
    }

    /**
     *
     * 对 View 做透明度变化的退场动画
     *
     * 相关方法 [.fadeBaseIn]
     *
     * @param view            做动画的 View
     * @param duration        动画时长(毫秒)
     * @param listener        动画回调
     * @param isNeedAnimation 是否需要动画
     */
    @JvmStatic
    fun fadeBaseOut(
        view: View?,
        duration: Int,
        listener: Animation.AnimationListener?,
        isNeedAnimation: Boolean
    ): AlphaAnimation? {
        if (view == null) {
            return null
        }
        return if (isNeedAnimation) {
            val alpha = AlphaAnimation(1f, 0f)
            alpha.interpolator = DecelerateInterpolator()
            alpha.duration = duration.toLong()
            alpha.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {
                    listener?.onAnimationStart(animation)
                }

                override fun onAnimationEnd(animation: Animation) {
                    view.visibility = View.GONE
                    listener?.onAnimationEnd(animation)
                }

                override fun onAnimationRepeat(animation: Animation) {
                    listener?.onAnimationRepeat(animation)
                }
            })
            view.startAnimation(alpha)
            alpha
        } else {
            view.visibility = View.GONE
            null
        }
    }

    fun clearBaseValueAnimator(animator: Animator?) {
        if (animator != null) {
            animator.removeAllListeners()
            if (animator is ValueAnimator) {
                animator.removeAllUpdateListeners()
            }
            if (Build.VERSION.SDK_INT >= 19) {
                animator.pause()
            }
            animator.cancel()
        }
    }

    /**
     *
     * 对 View 做透明度变化的进场动画。
     *
     * 相关方法 [.fadeBaseOut]
     *
     * @param view            做动画的 View
     * @param duration        动画时长(毫秒)
     * @param listener        动画回调
     * @param isNeedAnimation 是否需要动画
     */
    @JvmStatic
    fun fadeBaseIn(
        view: View?,
        duration: Int,
        listener: Animation.AnimationListener?,
        isNeedAnimation: Boolean
    ): AlphaAnimation? {
        if (view == null) {
            return null
        }
        return if (isNeedAnimation) {
            view.visibility = View.VISIBLE
            val alpha = AlphaAnimation(0f, 1f)
            alpha.interpolator = DecelerateInterpolator()
            alpha.duration = duration.toLong()
            alpha.fillAfter = true
            if (listener != null) {
                alpha.setAnimationListener(listener)
            }
            view.startAnimation(alpha)
            alpha
        } else {
            view.alpha = 1f
            view.visibility = View.VISIBLE
            null
        }
    }

    fun calcBaseViewScreenLocation(view: View): Rect {
        val location = IntArray(2)
        // 获取控件在屏幕中的位置，返回的数组分别为控件左顶点的 x、y 的值
        view.getLocationOnScreen(location)
        return Rect(
            location[0], location[1], location[0] + view.width,
            location[1] + view.height
        )
    }

    /**
     * 对 View 设置 paddingLeft
     *
     * @param view  需要被设置的 View
     * @param value 设置的值
     */
    fun setBasePaddingLeft(view: View, value: Int) {
        if (value != view.paddingLeft) {
            view.setPadding(value, view.paddingTop, view.paddingRight, view.paddingBottom)
        }
    }

    /**
     * 对 View 设置 paddingTop
     *
     * @param view  需要被设置的 View
     * @param value 设置的值
     */
    fun setBasePaddingTop(view: View, value: Int) {
        if (value != view.paddingTop) {
            view.setPadding(view.paddingLeft, value, view.paddingRight, view.paddingBottom)
        }
    }

    /**
     *
     * 对 View 做上下位移的进场动画
     *
     * 相关方法 [.slideOut]
     *
     * @param view            做动画的 View
     * @param duration        动画时长(毫秒)
     * @param listener        动画回调
     * @param isNeedAnimation 是否需要动画
     * @param direction       进场动画的方向
     * @return 动画对应的 Animator 对象, 注意无动画时返回 null
     */
    fun slideBaseIn(
        view: View?,
        duration: Int,
        listener: Animation.AnimationListener?,
        isNeedAnimation: Boolean,
        direction: BaseDirection?
    ): TranslateAnimation? {
        if (view == null) {
            return null
        }
        return if (isNeedAnimation) {
            var translate: TranslateAnimation? = null
            when (direction) {
                BaseDirection.LEFT_TO_RIGHT -> translate = TranslateAnimation(
                    Animation.RELATIVE_TO_SELF,
                    -1f,
                    Animation.RELATIVE_TO_SELF,
                    0f,
                    Animation.RELATIVE_TO_SELF,
                    0f,
                    Animation.RELATIVE_TO_SELF,
                    0f
                )

                BaseDirection.TOP_TO_BOTTOM -> translate = TranslateAnimation(
                    Animation.RELATIVE_TO_SELF,
                    0f,
                    Animation.RELATIVE_TO_SELF,
                    0f,
                    Animation.RELATIVE_TO_SELF,
                    -1f,
                    Animation.RELATIVE_TO_SELF,
                    0f
                )

                BaseDirection.RIGHT_TO_LEFT -> translate = TranslateAnimation(
                    Animation.RELATIVE_TO_SELF,
                    1f,
                    Animation.RELATIVE_TO_SELF,
                    0f,
                    Animation.RELATIVE_TO_SELF,
                    0f,
                    Animation.RELATIVE_TO_SELF,
                    0f
                )

                BaseDirection.BOTTOM_TO_TOP -> translate = TranslateAnimation(
                    Animation.RELATIVE_TO_SELF,
                    0f,
                    Animation.RELATIVE_TO_SELF,
                    0f,
                    Animation.RELATIVE_TO_SELF,
                    1f,
                    Animation.RELATIVE_TO_SELF,
                    0f
                )

                else -> {}
            }
            translate!!.interpolator = DecelerateInterpolator()
            translate.duration = duration.toLong()
            translate.fillAfter = true
            if (listener != null) {
                translate.setAnimationListener(listener)
            }
            view.visibility = View.VISIBLE
            view.startAnimation(translate)
            translate
        } else {
            view.clearAnimation()
            view.visibility = View.VISIBLE
            null
        }
    }

    /**
     * 对 View 设置 paddingBottom
     *
     * @param view  需要被设置的 View
     * @param value 设置的值
     */
    fun setBasePaddingBottom(view: View, value: Int) {
        if (value != view.paddingBottom) {
            view.setPadding(view.paddingLeft, view.paddingTop, view.paddingRight, value)
        }
    }

    val baseIsLastLineSpacingExtraError: Boolean
        /**
         * 判断是否需要对 LineSpacingExtra 进行额外的兼容处理
         * 安卓 5.0 以下版本中，LineSpacingExtra 在最后一行也会产生作用，因此会多出一个 LineSpacingExtra 的空白，可以通过该方法判断后进行兼容处理
         * if (ViewHelper.getISLastLineSpacingExtraError()) {
         * textView.bottomMargin = -3dp;
         * } else {
         * textView.bottomMargin = 0;
         * }
         */
        get() = Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP

    /**
     * 对 View 设置 paddingRight
     *
     * @param view  需要被设置的 View
     * @param value 设置的值
     */
    fun setBasePaddingRight(view: View, value: Int) {
        if (value != view.paddingRight) {
            view.setPadding(view.paddingLeft, view.paddingTop, value, view.paddingBottom)
        }
    }

    /**
     * inflate ViewStub 并返回对应的 View。
     */
    fun findBaseViewFromViewStub(
        parentView: View?,
        viewStubId: Int,
        inflatedViewId: Int,
        inflateLayoutResId: Int
    ): View? {
        if (null == parentView) {
            return null
        }
        var view = parentView.findViewById<View>(inflatedViewId)
        if (null == view) {
            val vs = parentView.findViewById<View>(viewStubId) as ViewStub
                ?: return null
            if (vs.layoutResource < 1 && inflateLayoutResId > 0) {
                vs.layoutResource = inflateLayoutResId
            }
            view = vs.inflate()
            if (null != view) {
                view = view.findViewById(inflatedViewId)
            }
        }
        return view
    }

    fun safeBaseSetImageViewSelected(imageView: ImageView, selected: Boolean) {
        // imageView setSelected 实现有问题。
        // resizeFromDrawable 中判断 drawable size 是否改变而调用 requestLayout，看似合理，但不会被调用
        // 因为 super.setSelected(selected) 会调用 refreshDrawableState
        // 而从 android 6 以后， ImageView 会重载refreshDrawableState，并在里面处理了 drawable size 改变的问题,
        // 从而导致 resizeFromDrawable 的判断失效
        val drawable = imageView.drawable ?: return
        val drawableWidth = drawable.intrinsicWidth
        val drawableHeight = drawable.intrinsicHeight
        imageView.isSelected = selected
        if (drawable.intrinsicWidth != drawableWidth || drawable.intrinsicHeight != drawableHeight) {
            imageView.requestLayout()
        }
    }

    /**
     * 把 ViewStub inflate 之后在其中根据 id 找 View
     *
     * @param parentView     包含 ViewStub 的 View
     * @param viewStubId     要从哪个 ViewStub 来 inflate
     * @param inflatedViewId 最终要找到的 View 的 id
     * @return id 为 inflatedViewId 的 View
     */
    fun findBaseViewFromViewStub(parentView: View?, viewStubId: Int, inflatedViewId: Int): View? {
        if (null == parentView) {
            return null
        }
        var view = parentView.findViewById<View>(inflatedViewId)
        if (null == view) {
            val vs = parentView.findViewById<View>(viewStubId) as ViewStub
                ?: return null
            view = vs.inflate()
            if (null != view) {
                view = view.findViewById(inflatedViewId)
            }
        }
        return view
    }

    fun setBaseImageViewTintColor(imageView: ImageView, @ColorInt tintColor: Int): ColorFilter {
        val colorFilter = LightingColorFilter(Color.argb(255, 0, 0, 0), tintColor)
        imageView.colorFilter = colorFilter
        return colorFilter
    }

    /**
     * Retrieve the transformed bounding rect of an arbitrary descendant view.
     * This does not need to be a direct child.
     *
     * @param descendant descendant view to reference
     * @param out        rect to set to the bounds of the descendant view
     */
    fun getBaseDescendantRect(parent: ViewGroup, descendant: View, out: Rect) {
        out[0, 0, descendant.width] = descendant.height
        ViewGroupHelper.offsetBaseDescendantRect(parent, descendant, out)
    }

    /**
     * 判断 ListView 是否已经滚动到底部。
     *
     * @param listView 需要被判断的 ListView。
     * @return ListView 已经滚动到底部则返回 true，否则返回 false。
     */
    fun isBaseListViewAlreadyAtBottom(listView: ListView): Boolean {
        if (listView.adapter == null || listView.height == 0) {
            return false
        }
        if (listView.lastVisiblePosition == listView.adapter.count - 1) {
            val lastItemView = listView.getChildAt(listView.childCount - 1)
            if (lastItemView != null && lastItemView.bottom == listView.height) {
                return true
            }
        }
        return false
    }

    private object ViewGroupHelper {
        private val sMatrix = ThreadLocal<Matrix>()
        private val sRectF = ThreadLocal<RectF>()
        fun offsetBaseDescendantRect(group: ViewGroup, child: View, rect: Rect) {
            var m = sMatrix.get()
            if (m == null) {
                m = Matrix()
                sMatrix.set(m)
            } else {
                m.reset()
            }
            offsetBaseDescendantMatrix(group, child, m)
            var rectF = sRectF.get()
            if (rectF == null) {
                rectF = RectF()
                sRectF.set(rectF)
            }
            rectF.set(rect)
            m.mapRect(rectF)
            rect[(rectF.left + 0.5f).toInt(), (rectF.top + 0.5f).toInt(), (rectF.right + 0.5f).toInt()] =
                (rectF.bottom + 0.5f).toInt()
        }

        fun offsetBaseDescendantMatrix(target: ViewParent, view: View, m: Matrix) {
            val parent = view.parent
            if (parent is View && parent !== target) {
                val vp = parent as View
                offsetBaseDescendantMatrix(target, vp, m)
                m.preTranslate(-vp.scrollX.toFloat(), -vp.scrollY.toFloat())
            }
            m.preTranslate(view.left.toFloat(), view.top.toFloat())
            if (!view.matrix.isIdentity) {
                m.preConcat(view.matrix)
            }
        }
    }
}