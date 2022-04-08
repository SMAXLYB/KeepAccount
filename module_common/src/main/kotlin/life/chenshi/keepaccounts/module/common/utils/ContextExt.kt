package life.chenshi.keepaccounts.module.common.utils

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.use

/**
 * 获取颜色
 */
@ColorInt
fun Context.getColorFromRes(@ColorRes color: Int) = ContextCompat.getColor(this, color)

@ColorInt
fun Context.getColorFromAttr(@AttrRes attr: Int): Int {
    return obtainStyledAttributes(intArrayOf(attr)).use {
        it.getColor(0, 0)
    }
}

@StyleRes
fun Context.getStyleFromAttr(@AttrRes attr: Int): Int {
    val tv = TypedValue()
    theme.resolveAttribute(attr, tv, true)
    return tv.data
}

/**
 * 振动
 */
fun Context.vibrate() {
    val vibrator = getSystemService(AppCompatActivity.VIBRATOR_SERVICE) as Vibrator
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
        vibrator.vibrate(30)
    } else {
        vibrator.vibrate(VibrationEffect.createOneShot(30, 200))
    }
}

fun Context.nightMode(): Boolean {
    return when (this.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
        Configuration.UI_MODE_NIGHT_YES -> true
        Configuration.UI_MODE_NIGHT_NO -> false
        else -> false
    }
}