package life.chenshi.keepaccounts.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import life.chenshi.keepaccounts.constant.TB_MINOR_CATEGORIES
import life.chenshi.keepaccounts.database.entity.MinorCategory

@Dao
interface MinorCategoryDao {
    @Insert
    suspend fun insertMinorCategory(minorCategory: MinorCategory)

    /**
     * 伪删除
     */
    @Query("UPDATE $TB_MINOR_CATEGORIES SET state = -1 WHERE id = :minorCategoryId")
    suspend fun deleteMinorCategoryBy(minorCategoryId: Int)

    /**
     * 伪删除所有
     */
    @Query("UPDATE $TB_MINOR_CATEGORIES SET state = -1 WHERE major_category_id = :majorCategoryId")
    suspend fun deleteAllMinorCategoryBy(majorCategoryId: Int)

    /**
     * 真正删除操作
     */
    @Delete
    suspend fun forceDeleteMinorCategory(minorCategory: MinorCategory)

    /**
     * 更新
     */
    @Update
    suspend fun updateMinorCategory(minorCategory: MinorCategory)

    /**
     * 更新频次
     */
    @Query("UPDATE $TB_MINOR_CATEGORIES SET use_rate = use_rate + 1 WHERE id = :id")
    suspend fun updateUseRate(id: Int)

    /**
     * 获取所有子类
     */
    @Query("SELECT * FROM $TB_MINOR_CATEGORIES")
    fun getAllMinorCategory(): Flow<List<MinorCategory>>

    /**
     * 根据状态获取所有子类
     */
    @Query("SELECT * FROM $TB_MINOR_CATEGORIES WHERE state = :state")
    fun getAllMinorCategoryBy(state: Int): Flow<List<MinorCategory>>

    /**
     * 获取使用频率前6的子类
     * @param state Int
     */
    @Query("SELECT * FROM $TB_MINOR_CATEGORIES WHERE state = :state ORDER BY use_rate DESC LIMIT 6")
    fun getTop6MinorCategoryBy(state: Int): Flow<MutableList<MinorCategory>>

    /**
     * 根据id查找子类
     * @param state Int
     * @param minorCategoryId Int
     * @return MinorCategory?
     */
    @Query("SELECT * FROM $TB_MINOR_CATEGORIES WHERE state = :state AND id = :minorCategoryId")
    suspend fun getMinorCategoryBy(state: Int, minorCategoryId: Int): MinorCategory?

    /**
     * 根据主类和状态获取所有子类
     */
    @Query("SELECT * FROM $TB_MINOR_CATEGORIES WHERE major_category_id = :majorCategoryId AND state = :state")
    fun getALLMinorCategoryBy(majorCategoryId: Int, state: Int): Flow<List<MinorCategory>>

    /**
     * 根据主类和名称精确查找子类
     */
    @Query("SELECT * FROM $TB_MINOR_CATEGORIES WHERE major_category_id = :majorCategoryId AND name LIKE :name LIMIT 1")
    fun getMinorCategoryBy(majorCategoryId: Int, name: String): MinorCategory?
}