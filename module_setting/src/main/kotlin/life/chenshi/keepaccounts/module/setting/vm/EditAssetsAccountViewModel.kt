package life.chenshi.keepaccounts.module.setting.vm

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableLong
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import life.chenshi.keepaccounts.module.common.base.BaseViewModel
import life.chenshi.keepaccounts.module.common.constant.CURRENT_ASSET_ACCOUNT_ID
import life.chenshi.keepaccounts.module.common.database.entity.AssetsAccount
import life.chenshi.keepaccounts.module.common.utils.BigDecimalUtil
import life.chenshi.keepaccounts.module.common.utils.ifNullOrBlank
import life.chenshi.keepaccounts.module.common.utils.storage.KVStoreHelper
import life.chenshi.keepaccounts.module.setting.bean.assets.IAssetIcon
import life.chenshi.keepaccounts.module.setting.repo.AssetsAccountRepo
import java.util.*
import javax.inject.Inject

@HiltViewModel
class EditAssetsAccountViewModel @Inject constructor(private val repo: AssetsAccountRepo) : BaseViewModel() {

    val assetsAccount = MutableLiveData<AssetsAccount>()
    val assetType = ObservableField<IAssetIcon>()
    val assetName = ObservableField<String>()
    val assetNumber = ObservableField<String>()
    val assetRemark = ObservableField<String>()
    val assetExpireDate = ObservableLong(System.currentTimeMillis())
    val assetBalance = ObservableField<String>()
    val usedDefault = ObservableBoolean()
    val includedInAll = ObservableBoolean()

    suspend fun insertOrUpdateAssetsAccount() {
        val assets = AssetsAccount(
            assetsAccount.value?.id,
            assetName.get() ?: "",
            BigDecimalUtil.fromString(assetBalance.get()),
            assetRemark.get(),
            assetNumber.get().ifNullOrBlank { null },
            includedInAll.get(),
            Date(assetExpireDate.get()),
            assetType.get()?.name ?: ""
        )
        assetsAccount.value?.createTime?.let {
            assets.createTime = it
        }
        val id = if (assets.id == null) {
            repo.insertAssetsAccount(assets)
        } else {
            repo.updateAssetsAccount(assets)
            requireNotNull(assets.id) { "更新操作必须有id" }
        }
        if (usedDefault.get()) {
            KVStoreHelper.write(CURRENT_ASSET_ACCOUNT_ID, id)
        } else {
            if (KVStoreHelper.read(CURRENT_ASSET_ACCOUNT_ID, -1L) == id) {
                KVStoreHelper.remove(CURRENT_ASSET_ACCOUNT_ID)
            }
        }
    }

    fun deleteAssetsAccountBy(assetsAccount: AssetsAccount) {
        viewModelScope.launch {
            repo.deleteAssetsAccount(assetsAccount)
        }
    }
}