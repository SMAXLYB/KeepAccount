package life.chenshi.keepaccounts.module.setting.bean.assets

import life.chenshi.keepaccounts.module.setting.R

object Alipay : IAssetIcon {
    override val name: String
        get() = "支付宝"
    override val icon: Int
        get() = R.drawable.setting_icon_alipay
    override val primaryColor: String
        get() = "#03A9F4"
}