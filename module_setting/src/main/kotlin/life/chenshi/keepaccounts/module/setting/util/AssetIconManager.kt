package life.chenshi.keepaccounts.module.setting.util

import androidx.collection.ArrayMap
import life.chenshi.keepaccounts.module.setting.bean.assets.*

object AssetIconManager {
    private val arrayMap: ArrayMap<String, IAssetIcon> by lazy {
        ArrayMap<String, IAssetIcon>().apply {
            put(Alipay.name, Alipay)
            put(WeChatWallet.name, WeChatWallet)
            put(Cash.name, Cash)
            put(AgriculturalBank.name, AgriculturalBank)
            put(ChinaBank.name, ChinaBank)
            put(CiticBank.name, CiticBank)
            put(CommunicationsBank.name, CommunicationsBank)
            put(ConstructionBank.name, ConstructionBank)
            put(IndustrialAndCommercialBank.name, IndustrialAndCommercialBank)
            put(MerchantsBank.name, MerchantsBank)
            put(PostalSavingsBank.name, PostalSavingsBank)
        }
    }

    fun get(name: String): IAssetIcon? {
        return arrayMap[name]
    }
}