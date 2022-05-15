package life.chenshi.keepaccounts.module.record.repo

import life.chenshi.keepaccounts.module.common.database.dao.*
import life.chenshi.keepaccounts.module.common.database.entity.Record
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EditRecordRepo @Inject constructor(
    private val bookDao: BookDao,
    private val recordDao: RecordDao,
    private val assetsAccountDao: AssetsAccountDao,
    private val majorCategoryDao: MajorCategoryDao,
    private val minorCategoryDao: MinorCategoryDao
) {
    fun getAllBooks() = bookDao.getAllBooks()

    fun getAllAssetsAccount() = assetsAccountDao.getAllAssetsAccount()

    suspend fun getBookById(bookId: Int) = bookDao.getBookById(bookId)

    suspend fun getAssetsAccountById(id: Long) = assetsAccountDao.getAssetsAccountById(id)

    suspend fun insertRecordAndUpdateUseRate(record: Record) = recordDao.insertRecordAndUpdateUseRate(record)

    suspend fun updateRecord(record: Record) = recordDao.updateRecord(record)

    suspend fun getMinorCategoryBy(state: Int, minorCategoryId: Int) =
        minorCategoryDao.getMinorCategoryBy(state, minorCategoryId)

    fun getTop6MinorCategoryBy(state: Int) = minorCategoryDao.getTop6MinorCategoryBy(state)

    suspend fun getMajorCategoryBy(state: Int, majorCategoryId: Int) =
        majorCategoryDao.getMajorCategoryBy(state, majorCategoryId)
}