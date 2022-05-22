package life.chenshi.keepaccounts.module.setting.adapter

import androidx.recyclerview.widget.GridLayoutManager
import coil.load
import com.jeremyliao.liveeventbus.LiveEventBus
import life.chenshi.keepaccounts.module.common.base.BaseAdapter
import life.chenshi.keepaccounts.module.common.constant.ASSET_ICON
import life.chenshi.keepaccounts.module.setting.R
import life.chenshi.keepaccounts.module.setting.bean.AssetIconGroupBean
import life.chenshi.keepaccounts.module.setting.bean.assets.IAssetIcon
import life.chenshi.keepaccounts.module.setting.databinding.SettingItemAssetsIconBinding
import life.chenshi.keepaccounts.module.setting.databinding.SettingItemAssetsIconGroupBinding
import javax.inject.Inject

class AssetIconGroupAdapter @Inject constructor() : BaseAdapter<AssetIconGroupBean, SettingItemAssetsIconGroupBinding>
    (emptyList()) {

    override fun setResLayoutId(): Int = R.layout.setting_item_assets_icon_group

    override fun onBindViewHolder(binding: SettingItemAssetsIconGroupBinding, itemData: AssetIconGroupBean) {
        binding.tvSelectAssetIconTitle.text = itemData.groupName
        binding.rvAssetIcons.layoutManager = GridLayoutManager(binding.root.context, 5)
        val adapter = IconAdapter()
        binding.rvAssetIcons.adapter = adapter
        binding.rvAssetIcons.setHasFixedSize(true)
        adapter.setData(itemData.icons)
        adapter.setOnItemClickListener { _, iAssetIcon, _ ->
            LiveEventBus.get(ASSET_ICON, IAssetIcon::class.java).post(iAssetIcon)
        }
    }
}

class IconAdapter() : BaseAdapter<IAssetIcon, SettingItemAssetsIconBinding>(emptyList()) {

    override fun onBindViewHolder(binding: SettingItemAssetsIconBinding, itemData: IAssetIcon) {
        binding.sivItemAssetsLogo.load(itemData.icon)
        binding.tvItemAssetsName.text = itemData.name
    }

    override fun setResLayoutId(): Int = R.layout.setting_item_assets_icon
}