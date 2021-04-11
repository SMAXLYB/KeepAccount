package life.chenshi.keepaccounts.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import life.chenshi.keepaccounts.constant.TB_MAJOR_CATEGORIES
import life.chenshi.keepaccounts.database.entity.MajorCategory

@Dao
interface MajorCategoryDao {
    @Insert
    suspend fun insertMajorCategory(majorCategory: MajorCategory)

    /**
     * 伪删除
     */
    @Query("UPDATE $TB_MAJOR_CATEGORIES SET state = -1 WHERE id = :categoryId")
    suspend fun deleteMajorCategory(categoryId: Int)

    /**
     * 真正删除操作
     * @param majorCategory MajorCategory
     */
    @Delete
    suspend fun forceDeleteMajorCategory(majorCategory: MajorCategory)

    /**
     * 更新操作
     * @param majorCategory MajorCategory
     */
    @Update
    suspend fun updateMajorCategory(majorCategory: MajorCategory)

    /**
     * 获取所有主类
     * @return Flow<List<MajorCategory>>
     */
    @Query("SELECT * FROM $TB_MAJOR_CATEGORIES")
    fun getAllMajorCategory(): Flow<List<MajorCategory>>

    /**
     * 根据状态获取所有主类
     * @param state Int
     * @return Flow<List<MajorCategory>>
     */
    @Query("SELECT * FROM $TB_MAJOR_CATEGORIES WHERE state = :state")
    fun getAllMajorCategoryBy(state: Int): Flow<List<MajorCategory>>

    /**
     * 根据状态,id获取所有主类
     * @param state Int
     * @param majorCategoryId Int
     * @return MajorCategory
     */
    @Query("SELECT * FROM $TB_MAJOR_CATEGORIES WHERE state = :state AND id = :majorCategoryId")
    suspend fun getMajorCategoryBy(state: Int, majorCategoryId: Int): MajorCategory?

    /**
     * 根据名字获取主类
     * @param name String
     * @return MajorCategory?
     */
    @Query("SELECT * FROM $TB_MAJOR_CATEGORIES WHERE name like :name LIMIT 1")
    fun getMajorCategoryBy(name: String): MajorCategory?
}