package life.chenshi.keepaccounts.module.setting.bean.assets

import life.chenshi.keepaccounts.module.setting.R

object Cash : IAssetIcon {
    override val name: String
        get() = "现金"
    override val icon: Int
        get() = R.drawable.setting_icon_cash
    override val primaryColor: String
        get() = "#FFCA28"
}