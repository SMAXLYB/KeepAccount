package life.chenshi.keepaccounts.module.setting.repo

import life.chenshi.keepaccounts.module.common.database.dao.AssetsAccountDao
import life.chenshi.keepaccounts.module.common.database.entity.AssetsAccount
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AssetsAccountRepo @Inject constructor(private val assetsAccountDao: AssetsAccountDao) {
    fun getAllAssetsAccounts() = assetsAccountDao.getAllAssetsAccount()

    suspend fun insertAssetsAccount(assetsAccount: AssetsAccount): Long {
        val now = Date()
        assetsAccount.modifyTime = now
        assetsAccount.createTime = now
        return assetsAccountDao.insertAssetsAccount(assetsAccount)
    }

    suspend fun deleteAssetsAccount(assetsAccount: AssetsAccount) {
        assetsAccount.modifyTime = Date()
        assetsAccountDao.deleteAssetsAccountBy(assetsAccount)
    }

    suspend fun updateAssetsAccount(assetsAccount: AssetsAccount): Int {
        assetsAccount.modifyTime = Date()
        return assetsAccountDao.updateAssetsAccount(assetsAccount)
    }
}