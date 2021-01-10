package life.chenshi.keepaccounts.utils

import android.graphics.Color
import android.widget.TextView

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

fun TextView.setSelect(selected: Boolean){
    isSelected = selected
    if (selected) {
        setTextColor(Color.parseColor("#03A9F4"))
    } else {
        setTextColor(Color.parseColor("#515151"))
    }
}