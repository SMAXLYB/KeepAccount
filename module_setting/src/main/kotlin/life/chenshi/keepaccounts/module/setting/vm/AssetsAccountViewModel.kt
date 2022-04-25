package life.chenshi.keepaccounts.module.setting.vm

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import life.chenshi.keepaccounts.module.common.base.BaseViewModel
import life.chenshi.keepaccounts.module.common.database.entity.AssetsAccount
import life.chenshi.keepaccounts.module.common.view.CustomDialog
import life.chenshi.keepaccounts.module.setting.repo.AssetsAccountRepo
import javax.inject.Inject

@HiltViewModel
class AssetsAccountViewModel @Inject constructor(private val repo: AssetsAccountRepo) : BaseViewModel() {
    val allAssetsAccount = repo.getAllAssetsAccounts().asLiveData()

    fun deleteAssetsAccountBy(assetsAccount: AssetsAccount) {
        viewModelScope.launch {
            repo.deleteAssetsAccount(assetsAccount)
        }
    }

    fun deleteAssetsAccountWithDialog(activity: FragmentActivity, assetsAccount: AssetsAccount) {
        CustomDialog.Builder(activity)
            .setCancelable(false)
            .setTitle("删除账户")
            .setMessage("\u3000\u3000您正在进行删除操作, 此操作不可逆, 确定继续吗?")
            .setClosedButtonEnable(false)
            .setPositiveButton("确定") { dialog, _ ->
                deleteAssetsAccountBy(assetsAccount)
                dialog.dismiss()
            }
            .setNegativeButton("取消")
            .build()
            .showNow()
    }
}