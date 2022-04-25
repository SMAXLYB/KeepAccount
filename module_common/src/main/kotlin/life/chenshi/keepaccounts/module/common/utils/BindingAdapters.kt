package life.chenshi.keepaccounts.module.common.utils

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.text.DateFormat
import java.util.*

@BindingAdapter("textWithVisibility")
fun bindTextWithVisibility(view: TextView, text: CharSequence?) {
    view.visibility = if (text.isNullOrEmpty()) {
        View.GONE
    } else {
        view.text = text
        View.VISIBLE
    }
}

@BindingAdapter("timestamp", "format", requireAll = true)
fun bindTimeStampToText(view: TextView, timestamp: Long, format: DateFormat) {
    view.text = DateUtil.date2String(Date(timestamp), format)
}