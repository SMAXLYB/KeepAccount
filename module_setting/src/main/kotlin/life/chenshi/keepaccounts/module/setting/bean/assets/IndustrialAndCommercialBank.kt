package life.chenshi.keepaccounts.module.setting.bean.assets

import life.chenshi.keepaccounts.module.setting.R

object IndustrialAndCommercialBank : IAssetIcon {
    override val name: String
        get() = "工商银行"
    override val icon: Int
        get() = R.drawable.setting_icon_bank_industrial_and_commercial
    override val primaryColor: String
        get() = "#C42B25"
}