package life.chenshi.keepaccounts.module.common.constant

import androidx.annotation.StyleRes
import life.chenshi.keepaccounts.module.common.R

sealed class Theme(val name: String, @StyleRes val styRes: Int) {
    object Default : Theme("Default", R.style.common_Theme_KeepAccounts)
    object Blue : Theme("Blue", R.style.common_ThemeOverlay_KeepAccounts_Blue)
    object Pink : Theme("Pink", R.style.common_ThemeOverlay_KeepAccounts_Pink)
    object Green : Theme("Green", R.style.common_ThemeOverlay_KeepAccounts_Green)

    companion object {
        fun getThemeFromName(name: String): Theme {
            return when (name) {
                Blue.name -> Blue
                Pink.name -> Pink
                Green.name -> Green
                else -> Default
            }
        }
    }
}