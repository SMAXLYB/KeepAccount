package life.chenshi.keepaccounts.module.setting.repo

import life.chenshi.keepaccounts.module.common.database.dao.BookDao
import life.chenshi.keepaccounts.module.common.database.dao.RecordDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserProfileRepo @Inject constructor(private val bookDao: BookDao, private val recordDao: RecordDao) {
    suspend fun getTotalNumOfBook() = bookDao.getNumOfBooks()

    suspend fun getTotalNumOfRecord() = recordDao.getTotalNumOfRecord()
}