package life.chenshi.keepaccounts.module.common.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import life.chenshi.keepaccounts.module.common.constant.TB_ASSETS_ACCOUNT
import life.chenshi.keepaccounts.module.common.constant.TB_MAJOR_CATEGORIES
import life.chenshi.keepaccounts.module.common.constant.TB_MINOR_CATEGORIES
import life.chenshi.keepaccounts.module.common.constant.TB_RECORDS
import life.chenshi.keepaccounts.module.common.database.bean.*
import life.chenshi.keepaccounts.module.common.database.entity.AssetsAccount
import life.chenshi.keepaccounts.module.common.database.entity.Record
import java.util.*

@Dao
interface RecordDao {
    // 增
    @Insert
    suspend fun insertRecord(record: Record)

    @Transaction
    suspend fun insertRecordAndUpdateUseRate(record: Record) {
        insertRecord(record)
        updateUseRate(record.minorCategoryId)
        record.assetsAccountId?.let {
            getAssetsAccountById(it)?.apply {
                this.balance = this.balance.add(record.money)
                updateAssetsAccount(this)
            }
        }
    }

    @Query("SELECT * FROM $TB_ASSETS_ACCOUNT WHERE id = :id")
    suspend fun getAssetsAccountById(id: Long): AssetsAccount?

    /**
     * 更新频次
     */
    @Query("UPDATE $TB_MINOR_CATEGORIES SET use_rate = use_rate + 1 WHERE id = :id")
    suspend fun updateUseRate(id: Int)

    @Update
    suspend fun updateAssetsAccount(assetsAccount: AssetsAccount): Int

    // 删
    @Delete
    suspend fun deleteRecord(record: Record)

    // 改
    @Update
    suspend fun updateRecord(record: Record)

    // // 查 设计多个表查询可以使用@Transaction
    // @Transaction
    /**
     * 按日期范围 账本id查询记录
     */
    @Query("SELECT * FROM tb_records WHERE book_id = :bookId AND time BETWEEN :from AND :to ORDER BY time DESC")
    fun getRecordByDateRange(from: Date, to: Date, bookId: Int): LiveData<List<Record>>


    /**
     * 获取最近30条记录
     * @return LiveData<List<Record>>
     */
    @Transaction
    @Query("SELECT * FROM $TB_RECORDS WHERE time ORDER BY time DESC LIMIT 30")
    fun getRecentRecords(): Flow<List<RecordWithCategoryBean>>

    /**
     * 按日期范围查询金额总和 分组为支出/收入
     */
    @Query(
        "SELECT record_type AS recordType, sum(money) AS sumMoney FROM tb_records " +
                "WHERE time BETWEEN :from AND :to GROUP BY record_type"
    )
    fun getSumMoneyByDateRange(from: Date, to: Date): LiveData<List<SumMoneyByDateBean>>

    /**
     * 按日期范围, 收支类型查询金额总和 分组为日期 时间早的靠前
     */
    @Query(
        "SELECT strftime('%d',time,'unixepoch','localtime') AS date, sum(money) AS sumMoney FROM tb_records " +
                "WHERE book_id = :bookId AND time BETWEEN :from AND :to AND record_type = :type " +
                "GROUP BY date " +
                "ORDER BY date ASC"
    )
    fun getSumMoneyGroupByDate(
        from: Date,
        to: Date,
        type: Int,
        bookId: Int
    ): LiveData<List<SumMoneyGroupByDateBean>>

    /**
     * 按日期范围,收支类型查询金额总和 分组为月份 时间早的靠前
     */
    @Query(
        "SELECT strftime('%m',time,'unixepoch','localtime') AS date, sum(money) AS sumMoney FROM tb_records " +
                "WHERE book_id = :bookId AND time BETWEEN :from AND :to AND record_type = :type " +
                "GROUP BY date " +
                "ORDER BY date ASC"
    )
    fun getSumMoneyGroupByMonth(
        from: Date,
        to: Date,
        type: Int,
        bookId: Int
    ): LiveData<List<SumMoneyGroupByDateBean>>

    /**
     * 按日期范围, 收支类型查询金额总和 分组为category 金额大的靠前
     */
    @Query(
        "SELECT name AS majorCategory, sum(money) AS sumMoney ,count(major_category_id) AS count " +
                "FROM $TB_RECORDS LEFT JOIN $TB_MAJOR_CATEGORIES ON major_category_id = $TB_MAJOR_CATEGORIES.id " +
                "WHERE  book_id = :bookId AND time BETWEEN :from AND :to AND $TB_RECORDS.record_type = :type " +
                "GROUP BY major_category_id " +
                "ORDER BY sumMoney DESC"
    )
    fun getSumMoneyGroupByCategory(
        from: Date,
        to: Date,
        type: Int,
        bookId: Int
    ): LiveData<List<SumMoneyGroupByMajorCategoryBean>>

    /**
     * 搜索符合关键字的全部记录
     */
    @Transaction
    @Query("SELECT * FROM $TB_RECORDS WHERE book_id = :bookId AND remark LIKE '%'|| :keyword || '%' ORDER BY time DESC")
    fun getRecordByKeyword(keyword: String, bookId: Int): LiveData<List<RecordWithCategoryBean>>

    /**
     * 获取所有记录
     */
    @Query("SELECT COUNT(*) FROM $TB_RECORDS")
    suspend fun getTotalNumOfRecord(): Int

    /**
     * 按照日期范围查询每日的净资产, 在一个月之内, 否则会重复计算
     */
    @Query(
        "SELECT strftime('%d', rd.time,'unixepoch','localtime') AS date, sum(rd.money) AS netAssets " +
                "FROM $TB_RECORDS AS rd LEFT JOIN $TB_ASSETS_ACCOUNT AS aa ON rd.assets_account_id  = aa.id  " +
                "WHERE aa.include_in_all_asset = 1 AND rd.time BETWEEN :from AND :to " +
                "GROUP BY date " +
                "ORDER BY date DESC;"
    )
    fun getDailyBalanceByDateRange(from: Date, to: Date): Flow<List<DailyBalanceByDateRangeBean>>
}