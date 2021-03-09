package life.chenshi.keepaccounts.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import life.chenshi.keepaccounts.database.entity.MinorCategory

@Dao
interface MinorCategoryDao {
    @Insert
    suspend fun insertMinorCategory(minorCategory: MinorCategory)

    /**
     * 伪删除
     */
    @Query("UPDATE tb_minor_categories SET state = -1 WHERE id = :minorCategoryId")
    suspend fun deleteMinorCategoryBy(minorCategoryId: Int)

    /**
     * 伪删除所有
     */
    @Query("UPDATE tb_minor_categories SET state = -1 WHERE major_category_id = :majorCategoryId")
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
     * 获取所有子类
     */
    @Query("SELECT * FROM tb_minor_categories")
    fun getAllMinorCategory(): Flow<List<MinorCategory>>

    /**
     * 根据状态获取所有子类
     */
    @Query("SELECT * FROM tb_minor_categories WHERE state = :state")
    fun getAllMinorCategoryBy(state: Int): Flow<List<MinorCategory>>

    /**
     * 根据主类和状态获取所有子类
     */
    @Query("SELECT * FROM tb_minor_categories WHERE major_category_id = :majorCategoryId AND state = :state")
    fun getALLMinorCategoryBy(majorCategoryId: Int, state: Int): Flow<List<MinorCategory>>

    /**
     * 根据主类和名称精确查找子类
     */
    @Query("SELECT * FROM tb_minor_categories WHERE major_category_id = :majorCategoryId AND name LIKE :name LIMIT 1")
    fun getMinorCategoryBy(majorCategoryId: Int, name: String): MinorCategory?
}