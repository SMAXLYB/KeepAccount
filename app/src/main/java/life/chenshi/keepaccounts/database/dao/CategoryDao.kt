package life.chenshi.keepaccounts.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import life.chenshi.keepaccounts.database.entity.Category

@Dao
interface CategoryDao {
    @Insert
    suspend fun insertCategory(category: Category)

    @Query("")
    suspend fun deleteCategory(category: Category)

    @Delete
    suspend fun forceDeleteCategory(category: Category)

    @Update
    suspend fun updateCategory(category: Category)

    @Query("SELECT * FROM tb_categories where state = 0")
    fun getAllCategory(): Flow<List<Category>>
}