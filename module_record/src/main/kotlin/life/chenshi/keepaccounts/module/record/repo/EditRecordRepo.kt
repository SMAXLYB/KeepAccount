package life.chenshi.keepaccounts.module.record.repo

import life.chenshi.keepaccounts.module.common.database.dao.*
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
}