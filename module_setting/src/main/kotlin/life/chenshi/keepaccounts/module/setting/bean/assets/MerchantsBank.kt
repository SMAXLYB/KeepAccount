package life.chenshi.keepaccounts.module.setting.bean.assets

import life.chenshi.keepaccounts.module.setting.R

object MerchantsBank : IAssetIcon {
    override val name: String
        get() = "招商银行"
    override val icon: Int
        get() = R.drawable.setting_icon_bank_merchants
    override val primaryColor: String
        get() = "#E50012"
}