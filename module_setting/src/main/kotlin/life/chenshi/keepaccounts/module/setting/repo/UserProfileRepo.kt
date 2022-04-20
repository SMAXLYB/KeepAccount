package life.chenshi.keepaccounts.module.setting.repo

import life.chenshi.keepaccounts.module.common.database.dao.BookDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserProfileRepo @Inject constructor(private val bookDao: BookDao) {
    suspend fun getTotalNumOfBook() = bookDao.getNumOfBooks()
}