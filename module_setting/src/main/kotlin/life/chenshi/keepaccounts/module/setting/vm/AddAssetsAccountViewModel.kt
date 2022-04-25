package life.chenshi.keepaccounts.module.setting.vm

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableLong
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import life.chenshi.keepaccounts.module.common.base.BaseViewModel
import life.chenshi.keepaccounts.module.common.database.entity.AssetsAccount
import life.chenshi.keepaccounts.module.common.utils.BigDecimalUtil
import life.chenshi.keepaccounts.module.setting.repo.AssetsAccountRepo
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddAssetsAccountViewModel @Inject constructor(private val repo: AssetsAccountRepo) : BaseViewModel() {

    val assetsAccount = MutableLiveData<AssetsAccount>()

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
            BigDecimalUtil.yuan2FenBD(assetBalance.get()),
            assetRemark.get(),
            assetNumber.get(),
            includedInAll.get(),
            Date(assetExpireDate.get())
        )
        if (assets.id == null) {
            repo.insertAssetsAccount(assets)
        } else {
            repo.updateAssetsAccount(assets)
        }
    }

    fun deleteAssetsAccountBy(assetsAccount: AssetsAccount) {
        viewModelScope.launch {
            repo.deleteAssetsAccount(assetsAccount)
        }
    }
}