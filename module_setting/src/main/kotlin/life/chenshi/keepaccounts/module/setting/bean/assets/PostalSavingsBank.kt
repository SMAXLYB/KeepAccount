package life.chenshi.keepaccounts.module.setting.bean.assets

import life.chenshi.keepaccounts.module.setting.R

object PostalSavingsBank : IAssetIcon {
    override val name: String
        get() = "邮政银行"
    override val icon: Int
        get() = R.drawable.setting_icon_bank_postal_savings
    override val primaryColor: String
        get() = "#007E3E"
}