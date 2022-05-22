package life.chenshi.keepaccounts.module.setting.bean.assets

import life.chenshi.keepaccounts.module.setting.R

object AgriculturalBank : IAssetIcon {
    override val name: String
        get() = "农业银行"
    override val icon: Int
        get() = R.drawable.setting_icon_bank_agricultural
    override val primaryColor: String
        get() = "#009C96"
}