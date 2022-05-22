package life.chenshi.keepaccounts.module.setting.bean.assets

import life.chenshi.keepaccounts.module.setting.R

object ChinaBank : IAssetIcon {
    override val name: String
        get() = "中国银行"
    override val icon: Int
        get() = R.drawable.setting_icon_bank_china
    override val primaryColor: String
        get() = "#AF2434"
}