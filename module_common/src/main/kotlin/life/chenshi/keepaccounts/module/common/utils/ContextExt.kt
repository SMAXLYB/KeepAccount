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
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.content.ContextCompat
import com.google.android.material.color.MaterialColors

/**
 * 获取颜色
 */
@ColorInt
fun Context.getColorFromRes(@ColorRes color: Int) = ContextCompat.getColor(this, color)

@ColorInt
fun Context.getColorFromAttr(@AttrRes attr: Int) = MaterialColors.getColor(this, attr, 0)

@ColorInt
fun Context.getColorFromTheme(@StyleRes themeResId: Int, @AttrRes attr: Int) =
    MaterialColors.getColor(ContextThemeWrapper(this, themeResId), attr, 0)

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
    val mode = this.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
    return mode == Configuration.UI_MODE_NIGHT_YES
}