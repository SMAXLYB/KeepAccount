package life.chenshi.keepaccounts.module.setting.bean

import life.chenshi.keepaccounts.module.setting.bean.assets.IAssetIcon

data class AssetIconGroupBean(val groupName: String, val icons: List<IAssetIcon>)


fun assetIconGroup(name: String, vararg icon: IAssetIcon): AssetIconGroupBean {
    return AssetIconGroupBean(name, icon.toList())
}