package life.chenshi.keepaccounts.module.common.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import life.chenshi.keepaccounts.module.common.constant.TB_ASSETS_ACCOUNT
import life.chenshi.keepaccounts.module.common.database.entity.AssetsAccount
import java.math.BigDecimal

@Dao
interface AssetsAccountDao {

    @Insert
    suspend fun insertAssetsAccount(assetsAccount: AssetsAccount): Long

    @Query("SELECT * FROM $TB_ASSETS_ACCOUNT")
    fun getAllAssetsAccount(): Flow<List<AssetsAccount>>

    @Delete
    suspend fun deleteAssetsAccountBy(assetsAccount: AssetsAccount)

    @Update
    suspend fun updateAssetsAccount(assetsAccount: AssetsAccount): Int

    @Query("SELECT * FROM $TB_ASSETS_ACCOUNT WHERE id = :id")
    suspend fun getAssetsAccountById(id: Long): AssetsAccount?

    // 使用sum查询如果没查到会返回Null, 使用total会返回0
    @Query("SELECT TOTAL(balance) FROM $TB_ASSETS_ACCOUNT WHERE include_in_all_asset = 1")
    fun getSumBalance(): Flow<BigDecimal>
}