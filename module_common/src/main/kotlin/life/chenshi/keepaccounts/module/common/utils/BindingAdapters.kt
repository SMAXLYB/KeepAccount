package life.chenshi.keepaccounts.module.common.utils

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import coil.load
import life.chenshi.keepaccounts.module.common.R
import java.text.DateFormat
import java.util.*

/**
 * 给textView设置文字, 如果为空则隐藏textView
 */
@BindingAdapter("textWithVisibility")
fun bindTextWithVisibility(view: TextView, text: CharSequence?) {
    view.visibility = if (text.isNullOrEmpty()) {
        View.GONE
    } else {
        view.text = text
        View.VISIBLE
    }
}

@BindingAdapter("nullableText", "defaultText", requireAll = true)
fun bindDefaultTextIfNullOrEmpty(view: TextView, nullableText: CharSequence?, defaultText: CharSequence) {
    view.text = nullableText.takeUnless { it.isNullOrBlank() } ?: defaultText
}

/**
 * 按照给定的格式来格式化时间戳, 然后展示在textView上
 */
@BindingAdapter("timestamp", "format", requireAll = true)
fun bindTimeStampToText(view: TextView, timestamp: Long, format: DateFormat) {
    view.text = DateUtil.date2String(Date(timestamp), format)
}

@BindingAdapter("drawableInt")
fun bindDrawableRes(imageView: ImageView, @DrawableRes drawableInt: Int) {
    imageView.load(drawableInt)
}

@BindingAdapter("gradientColorStr")
fun bindGradientColorStr(view: View, colorStr: String?) {
    if (colorStr == null) {
        view.setBackgroundResource(R.drawable.common_corner_all_large_primary_gradient)
        return
    }
    view.setShapeWithRippleBackground(
        view.context.resources.getDimension(R.dimen.common_large_corner_size),
        Color.parseColor(colorStr),
        Color.parseColor("#27707070"),
        true
    )
}