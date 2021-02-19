package life.chenshi.keepaccounts.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import life.chenshi.keepaccounts.database.entity.SubCategory

@Dao
interface SubCategoryDao {
    @Insert
    suspend fun insertSubCategory(subCategory: SubCategory)

    /**
     * 伪删除
     */
    @Query("UPDATE tb_sub_categories SET state = -1 WHERE id = :subCategoryId")
    suspend fun deleteSubCategoryBy(subCategoryId: Int)

    @Query("UPDATE tb_sub_categories SET state = -1 WHERE category_id = :categoryId")
    suspend fun deleteAllSubCategoryBy(categoryId: Int)

    @Delete
    suspend fun forceDeleteSubCategory(subCategory: SubCategory)

    @Update
    suspend fun updateSubCategory(subCategory: SubCategory)

    @Query("SELECT * FROM tb_sub_categories")
    fun getAllSubCategory(): Flow<List<SubCategory>>

    @Query("SELECT * FROM tb_sub_categories WHERE state = :state")
    fun getAllSubCategoryBy(state: Int): Flow<List<SubCategory>>

    @Query("SELECT * FROM tb_sub_categories WHERE category_id = :categoryId AND state = :state")
    fun getALLSubCategoryBy(categoryId: Int, state: Int): Flow<List<SubCategory>>

    @Query("SELECT * FROM tb_sub_categories WHERE category_id = :categoryId AND name LIKE :name LIMIT 1")
    fun getSubCategoryBy(categoryId: Int, name: String): SubCategory?
}