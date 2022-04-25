package life.chenshi.keepaccounts.module.setting.adapter

import life.chenshi.keepaccounts.module.common.base.BaseAdapter
import life.chenshi.keepaccounts.module.common.database.entity.AssetsAccount
import life.chenshi.keepaccounts.module.common.utils.DateUtil
import life.chenshi.keepaccounts.module.common.utils.ifNullOrEmpty
import life.chenshi.keepaccounts.module.setting.R
import life.chenshi.keepaccounts.module.setting.databinding.SettingItemAssetsAccountBinding
import life.chenshi.keepaccounts.module.setting.databinding.SettingItemAssetsAccountFooterBinding
import java.math.BigDecimal
import java.util.*
import javax.inject.Inject


class AssetsAccountAdapter @Inject constructor() :
    BaseAdapter<AssetsAccount, SettingItemAssetsAccountBinding>(emptyList()) {

    override fun onBindViewHolder(binding: SettingItemAssetsAccountBinding, itemData: AssetsAccount) {
        binding.tvAssetsName.text = itemData.name
        binding.tvAssetsNumber.text = itemData.number.ifNullOrEmpty { " / " }
        binding.tvAssetsBalance.text = itemData.balance.toPlainString()
        binding.tvAssetsExpireDate.text = DateUtil.date2String(itemData.expireTime, DateUtil.MONTH_DAY_FORMAT_2)
    }

    override fun setResLayoutId() = R.layout.setting_item_assets_account
}

class AssetsAccountFooterAdapter @Inject constructor() :
    BaseAdapter<AssetsAccount, SettingItemAssetsAccountFooterBinding>(
        listOf(
            AssetsAccount(null, "", BigDecimal(0), null, null, false, Date())
        )
    ) {

    override fun onBindViewHolder(binding: SettingItemAssetsAccountFooterBinding, itemData: AssetsAccount) {}

    override fun setResLayoutId() = R.layout.setting_item_assets_account_footer
}