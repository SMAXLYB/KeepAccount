package life.chenshi.keepaccounts.module.common.utils

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity

/**
 * 获取颜色
 */
fun Context.getColorById(@ColorRes color: Int) =
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
        resources.getColor(color)
    } else {
        getColor(color)
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