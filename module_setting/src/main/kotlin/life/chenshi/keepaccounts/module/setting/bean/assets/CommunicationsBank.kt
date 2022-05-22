package life.chenshi.keepaccounts.module.setting.bean.assets

import life.chenshi.keepaccounts.module.setting.R

object CommunicationsBank : IAssetIcon {
    override val name: String
        get() = "交通银行"
    override val icon: Int
        get() = R.drawable.setting_icon_bank_communications
    override val primaryColor: String
        get() = "#00367A"
}