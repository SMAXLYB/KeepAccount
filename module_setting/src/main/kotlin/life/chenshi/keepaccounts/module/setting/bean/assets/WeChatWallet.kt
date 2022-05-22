package life.chenshi.keepaccounts.module.setting.bean.assets

import life.chenshi.keepaccounts.module.setting.R

object WeChatWallet : IAssetIcon {
    override val name: String
        get() = "微信钱包"
    override val icon: Int
        get() = R.drawable.setting_icon_wechat
    override val primaryColor: String
        get() = "#3BCA72"
}