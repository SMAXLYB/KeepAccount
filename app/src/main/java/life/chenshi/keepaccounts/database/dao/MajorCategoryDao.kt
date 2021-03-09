package life.chenshi.keepaccounts.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import life.chenshi.keepaccounts.database.entity.MajorCategory

@Dao
interface MajorCategoryDao {
    @Insert
    suspend fun insertMajorCategory(majorCategory: MajorCategory)

    /**
     * 伪删除
     */
    @Query("UPDATE tb_major_categories SET state = -1 WHERE id = :categoryId")
    suspend fun deleteMajorCategory(categoryId: Int)

    @Delete
    suspend fun forceDeleteMajorCategory(majorCategory: MajorCategory)

    @Update
    suspend fun updateMajorCategory(majorCategory: MajorCategory)

    @Query("SELECT * FROM tb_major_categories")
    fun getAllMajorCategory(): Flow<List<MajorCategory>>

    @Query("SELECT * FROM tb_major_categories where state = :state")
    fun getAllMajorCategoryBy(state: Int): Flow<List<MajorCategory>>

    @Query("SELECT * FROM tb_major_categories WHERE name like :name LIMIT 1")
    fun getMajorCategoryBy(name: String): MajorCategory?
}