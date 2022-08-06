package life.chenshi.keepaccounts.module.search.repo

import life.chenshi.keepaccounts.module.common.database.dao.AssetsAccountDao
import life.chenshi.keepaccounts.module.common.database.dao.BookDao
import life.chenshi.keepaccounts.module.common.database.dao.RecordDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepo @Inject constructor(
    private val recordDao: RecordDao,
    private val assetsAccountDao: AssetsAccountDao,
    private val bookDao: BookDao
) {
    fun getRecordByKeyword(keyword: String, bookId: Int) = recordDao.getRecordByKeyword(keyword, bookId)

    fun getAllAssetsAccount() = assetsAccountDao.getAllAssetsAccount()

    fun getAllBook() = bookDao.getAllBooks()
}