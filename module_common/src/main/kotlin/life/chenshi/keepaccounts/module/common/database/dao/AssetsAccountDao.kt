package life.chenshi.keepaccounts.module.common.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import life.chenshi.keepaccounts.module.common.constant.TB_ASSETS_ACCOUNT
import life.chenshi.keepaccounts.module.common.database.entity.AssetsAccount

@Dao
interface AssetsAccountDao {

    @Insert
    suspend fun insertAssetsAccount(assetsAccount: AssetsAccount)

    @Query("SELECT * FROM $TB_ASSETS_ACCOUNT")
    fun getAllAssetsAccount(): Flow<List<AssetsAccount>>

    @Delete
    suspend fun deleteAssetsAccountBy(assetsAccount: AssetsAccount)

    @Update
    suspend fun updateAssetsAccount(assetsAccount: AssetsAccount)
}