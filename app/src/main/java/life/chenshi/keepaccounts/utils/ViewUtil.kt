package life.chenshi.keepaccounts.utils

import android.content.res.Resources
import android.graphics.Color
import android.util.TypedValue
import android.view.View
import android.widget.TextView

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.inVisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

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

fun Int.dp2px(): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    )
        .toInt()
}