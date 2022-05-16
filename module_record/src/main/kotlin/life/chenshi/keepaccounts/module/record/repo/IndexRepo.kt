package life.chenshi.keepaccounts.module.record.repo

import life.chenshi.keepaccounts.module.common.database.dao.AssetsAccountDao
import life.chenshi.keepaccounts.module.common.database.dao.BookDao
import life.chenshi.keepaccounts.module.common.database.dao.RecordDao
import life.chenshi.keepaccounts.module.common.database.entity.Record
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IndexRepo @Inject constructor(
    private val recordDao: RecordDao,
    private val assetsAccountDao: AssetsAccountDao,
    private val bookDao: BookDao
) {

    fun getDailyBalanceByDateRange(from: Date, to: Date) =
        recordDao.getDailyBalanceByDateRange(from, to)

    suspend fun getAssetsAccountById(assetsAccountId: Long) = assetsAccountDao.getAssetsAccountById(assetsAccountId)

    fun getSumBalance() = assetsAccountDao.getSumBalance()

    fun getRecentRecords() = recordDao.getRecentRecords()

    suspend fun deleteRecordAndUpdateBalance(record: Record) = recordDao.deleteRecordAndUpdateBalance(record)

    suspend fun getNumOfBooks() = bookDao.getNumOfBooks()
}