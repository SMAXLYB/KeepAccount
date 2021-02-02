package life.chenshi.keepaccounts.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import life.chenshi.keepaccounts.database.entity.SubCategory

@Dao
interface SubCategoryDao {
    @Insert
    suspend fun insertSubCategory(subCategory: SubCategory)

    @Query("")
    suspend fun deleteSubCategory(subCategory: SubCategory)

    @Delete
    suspend fun forceDeleteSubCategory(subCategory: SubCategory)

    @Update
    suspend fun updateSubCategory(subCategory: SubCategory)

    @Query("SELECT * FROM tb_sub_categories where state = 0")
    fun getAllCategory(): Flow<List<SubCategory>>
}