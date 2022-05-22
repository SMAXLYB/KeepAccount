package life.chenshi.keepaccounts.module.setting.adapter

import androidx.core.view.ViewCompat
import life.chenshi.keepaccounts.module.common.base.BaseAdapter
import life.chenshi.keepaccounts.module.common.database.entity.AssetsAccount
import life.chenshi.keepaccounts.module.common.utils.ifNullOrBlank
import life.chenshi.keepaccounts.module.setting.R
import life.chenshi.keepaccounts.module.setting.databinding.SettingItemAssetsAccountBinding
import life.chenshi.keepaccounts.module.setting.databinding.SettingItemAssetsAccountFooterBinding
import java.math.BigDecimal
import java.util.*
import javax.inject.Inject


class AssetsAccountAdapter @Inject constructor() :
    BaseAdapter<AssetsAccount, SettingItemAssetsAccountBinding>(emptyList()) {

    companion object {
        const val TRANSITION_NAME_BACKGROUND = "background"
        const val TRANSITION_NAME_LOGO = "logo"
        const val TRANSITION_NAME_TITLE = "title"
    }

    override fun onBindViewHolder(binding: SettingItemAssetsAccountBinding, itemData: AssetsAccount) {
        binding.item = itemData
        binding.tvAssetsNumber.text = itemData.number.ifNullOrBlank { " / " }

        ViewCompat.setTransitionName(binding.card, TRANSITION_NAME_BACKGROUND + "-${itemData.id}")
        ViewCompat.setTransitionName(binding.sivAssetsLogo, TRANSITION_NAME_LOGO + "-${itemData.id}")
        ViewCompat.setTransitionName(binding.tvAssetsName, TRANSITION_NAME_TITLE + "-${itemData.id}")
    }

    override fun setResLayoutId() = R.layout.setting_item_assets_account
}

class AssetsAccountFooterAdapter @Inject constructor() :
    BaseAdapter<AssetsAccount, SettingItemAssetsAccountFooterBinding>(
        listOf(
            AssetsAccount(null, "", BigDecimal(0), null, null, false, Date(), "")
        )
    ) {

    override fun onBindViewHolder(binding: SettingItemAssetsAccountFooterBinding, itemData: AssetsAccount) {}

    override fun setResLayoutId() = R.layout.setting_item_assets_account_footer
}