package life.chenshi.keepaccounts.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import life.chenshi.keepaccounts.database.entity.Category

@Dao
interface CategoryDao {
    @Insert
    suspend fun insertCategory(category: Category)

    /**
     * 伪删除
     */
    @Query("UPDATE tb_categories SET state = -1 WHERE id = :categoryId")
    suspend fun deleteCategory(categoryId: Int)

    @Delete
    suspend fun forceDeleteCategory(category: Category)

    @Update
    suspend fun updateCategory(category: Category)

    @Query("SELECT * FROM tb_categories")
    fun getAllCategory(): Flow<List<Category>>

    @Query("SELECT * FROM tb_categories where state = :state")
    fun getAllCategoryBy(state: Int): Flow<List<Category>>

    @Query("SELECT * FROM tb_categories WHERE name like :name LIMIT 1")
    fun getCategoryBy(name: String): Category?
}