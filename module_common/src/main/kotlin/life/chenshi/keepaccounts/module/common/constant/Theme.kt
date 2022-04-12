package life.chenshi.keepaccounts.module.common.constant

import androidx.annotation.StyleRes
import life.chenshi.keepaccounts.module.common.R

sealed class Theme(val name: String, val nameInCN: String, @StyleRes val styRes: Int) {
    object Default : Theme("Default", "天空蓝(默认)", R.style.CommonTheme_KeepAccounts)

    // object Blue : Theme("Blue", R.style.CommonThemeOverlay_KeepAccounts_Blue)
    object Pink : Theme("Pink", "少女粉", R.style.CommonThemeOverlay_KeepAccounts_Pink)
    object Green : Theme("Green", "青草绿", R.style.CommonThemeOverlay_KeepAccounts_Green)

    companion object {
        fun getThemeFromName(name: String): Theme {
            return when (name) {
                // Blue.name -> Blue
                Pink.name -> Pink
                Green.name -> Green
                else -> Default
            }
        }
    }
}