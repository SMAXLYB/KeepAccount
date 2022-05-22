package life.chenshi.keepaccounts.module.setting.bean.assets

import life.chenshi.keepaccounts.module.setting.R

object CiticBank : IAssetIcon {
    override val name: String
        get() = "中信银行"
    override val icon: Int
        get() = R.drawable.setting_icon_bank_citic
    override val primaryColor: String
        get() = "#D7000F"
}