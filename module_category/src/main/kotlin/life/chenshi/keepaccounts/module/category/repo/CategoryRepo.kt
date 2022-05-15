package life.chenshi.keepaccounts.module.category.repo

import life.chenshi.keepaccounts.module.common.database.dao.MajorCategoryDao
import life.chenshi.keepaccounts.module.common.database.dao.MinorCategoryDao
import life.chenshi.keepaccounts.module.common.database.entity.MajorCategory
import life.chenshi.keepaccounts.module.common.database.entity.MinorCategory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepo @Inject constructor(
    private val majorCategoryDao: MajorCategoryDao,
    private val minorCategoryDao: MinorCategoryDao
) {
    fun getMajorCategoryBy(name: String) = majorCategoryDao.getMajorCategoryBy(name)

    suspend fun updateMajorCategory(majorCategory: MajorCategory) = majorCategoryDao.updateMajorCategory(majorCategory)

    suspend fun insertMajorCategory(majorCategory: MajorCategory) = majorCategoryDao.insertMajorCategory(majorCategory)

    suspend fun deleteMajorCategory(categoryId: Int) = majorCategoryDao.deleteMajorCategory(categoryId)

    fun getAllMajorCategoryBy(state: Int) = majorCategoryDao.getAllMajorCategoryBy(state)

    suspend fun deleteAllMinorCategoryBy(majorCategoryId: Int) =
        minorCategoryDao.deleteAllMinorCategoryBy(majorCategoryId)

    suspend fun deleteMinorCategoryBy(minorCategoryId: Int) = minorCategoryDao.deleteMinorCategoryBy(minorCategoryId)

    fun getMinorCategoryBy(majorCategoryId: Int, name: String) =
        minorCategoryDao.getMinorCategoryBy(majorCategoryId, name)

    suspend fun updateMinorCategory(minorCategory: MinorCategory) = minorCategoryDao.updateMinorCategory(minorCategory)

    suspend fun insertMinorCategory(minorCategory: MinorCategory) = minorCategoryDao.insertMinorCategory(minorCategory)

    fun getAllMinorCategoryBy(majorCategoryId: Int, state: Int) = minorCategoryDao.getAllMinorCategoryBy(majorCategoryId, state)
}