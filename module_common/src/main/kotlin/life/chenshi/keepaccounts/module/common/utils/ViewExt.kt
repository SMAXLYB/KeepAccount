package life.chenshi.keepaccounts.module.common.utils

import android.app.Activity
import android.content.ContextWrapper
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.os.SystemClock
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import life.chenshi.keepaccounts.module.common.R

/*------------------属性-------------------------*/


/*------------------方法-------------------------*/
fun View.visible() {
    visibility = View.VISIBLE
}

fun View.setVisibility(visible: Boolean) {
    visibility = if (visible) {
        View.VISIBLE
    } else {
        View.INVISIBLE
    }
}

fun View.inVisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

/**
 * 禁止快速点击
 */
fun View.setNoDoubleClickListener(
    helper: NoDoubleClickHelper.() -> Unit
) {
    setOnClickListener {
        val tempHelper = NoDoubleClickHelper().apply(helper)
        val thisViewOrDecorView = if (tempHelper.share)
            getActivity()?.window?.decorView ?: this else this
        val millis = thisViewOrDecorView.getTag(R.id.common_no_double_click_millis) as? Long ?: 0
        if (SystemClock.uptimeMillis() - millis >= tempHelper.interval) {
            thisViewOrDecorView.setTag(R.id.common_no_double_click_millis, SystemClock.uptimeMillis())
            tempHelper.listener?.invoke(this)
        }
    }
}

data class NoDoubleClickHelper(
    var interval: Int = 500,
    var share: Boolean = false,
    internal var listener: ((View) -> Unit)? = null
) {
    fun listener(block: (View) -> Unit) {
        this.listener = block
    }
}

fun TextView.setVisibilityWithText(text: String) {
    visibility = if (text.isNotBlank()) {
        setText(text)
        View.VISIBLE
    } else {
        View.INVISIBLE
    }
}

fun ImageView.setVisibleWithDrawable(drawable: Drawable? = null) {
    visibility = View.VISIBLE
    setImageDrawable(drawable)
}

fun View.setShapeWithRippleBackground(
    cornerSize: Float,
    @ColorInt backgroundColor: Int,
    @ColorInt rippleColor: Int
) {
    val stateList = arrayOf(
        intArrayOf(android.R.attr.state_pressed),
        intArrayOf(android.R.attr.state_focused),
        intArrayOf(android.R.attr.state_activated),
        intArrayOf()
    )

    val stateColorList = intArrayOf(
        rippleColor,
        rippleColor,
        rippleColor,
        backgroundColor
    )
    val colorStateList = ColorStateList(stateList, stateColorList)

    // 四个角的弧度
    val outRadius =
        floatArrayOf(cornerSize, cornerSize, cornerSize, cornerSize, cornerSize, cornerSize, cornerSize, cornerSize)
    val roundRectShape = RoundRectShape(outRadius, null, null)
    // 蒙层
    // val maskDrawable = ShapeDrawable()
    // maskDrawable.shape = roundRectShape
    // maskDrawable.paint.color = Color.BLACK
    // maskDrawable.paint.style = Paint.Style.FILL

    // 内容
    val contentDrawable = ShapeDrawable()
    contentDrawable.shape = roundRectShape
    contentDrawable.paint.color = backgroundColor
    contentDrawable.paint.style = Paint.Style.FILL

    this.background = RippleDrawable(colorStateList, contentDrawable, null)
}

fun Int.dp2px(): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), Resources.getSystem().displayMetrics
    )
        .toInt()
}

fun Int.sp2px(): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    )
}

/**
 * 获取view当时的activity
 */
fun View.getActivity(): Activity? {
    var context = this.context
    while (context is ContextWrapper) {
        if (context is Activity) {
            return context
        }
        context = context.baseContext
    }
    return null
}

/*------------------业务相关-------------------------*/
// 大分类标签使用
fun TextView.setEnable(enabled: Boolean) {
    isEnabled = enabled
    if (enabled) {
        setTextColor(Color.parseColor("#515151"))
    } else {
        setTextColor(Color.parseColor("#ffffff"))
    }
}

// 小分类标签使用
fun TextView.setEnableAndSelect(selected: Boolean) {
    isSelected = selected
    isEnabled = !selected
    if (selected) {
        setTextColor(Color.parseColor("#03A9F4"))
    } else {
        setTextColor(Color.parseColor("#515151"))
    }
}

fun TextView.setSelect(selected: Boolean) {
    isSelected = selected
    if (selected) {
        setTextColor(Color.parseColor("#03A9F4"))
    } else {
        setTextColor(Color.parseColor("#515151"))
    }
}