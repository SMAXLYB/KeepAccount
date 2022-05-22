package life.chenshi.keepaccounts.module.setting.bean.assets

import life.chenshi.keepaccounts.module.setting.R

object ConstructionBank : IAssetIcon {
    override val name: String
        get() = "建设银行"
    override val icon: Int
        get() = R.drawable.setting_icon_bank_construction
    override val primaryColor: String
        get() = "#004F9C"
}