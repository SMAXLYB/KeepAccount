package life.chenshi.keepaccounts.module.search.repo

import life.chenshi.keepaccounts.module.common.database.dao.RecordDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepo @Inject constructor(private val recordDao: RecordDao) {
    fun getRecordByKeyword(keyword: String, bookId: Int) = recordDao.getRecordByKeyword(keyword, bookId)
}