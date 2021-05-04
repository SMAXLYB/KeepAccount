package life.chenshi.keepaccounts.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import life.chenshi.keepaccounts.constant.TB_MAJOR_CATEGORIES
import life.chenshi.keepaccounts.constant.TB_MINOR_CATEGORIES
import life.chenshi.keepaccounts.constant.TB_RECORDS
import life.chenshi.keepaccounts.database.bean.RecordWithCategoryBean
import life.chenshi.keepaccounts.database.bean.SumMoneyByDateBean
import life.chenshi.keepaccounts.database.bean.SumMoneyGroupByDateBean
import life.chenshi.keepaccounts.database.bean.SumMoneyGroupByMajorCategoryBean
import life.chenshi.keepaccounts.database.entity.Record
import java.util.*

@Dao
interface RecordDao {
    // 增
    @Insert
    suspend fun insertRecord(record: Record)

    @Transaction
    suspend fun insertRecordAndUpdateUseRate(record: Record){
        insertRecord(record)
        updateUseRate(record.minorCategoryId)
    }

    /**
     * 更新频次
     */
    @Query("UPDATE $TB_MINOR_CATEGORIES SET use_rate = use_rate + 1 WHERE id = :id")
    suspend fun updateUseRate(id: Int)

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
    @Query("SELECT * FROM tb_records WHERE book_id = :bookId and time BETWEEN :from AND :to ORDER BY time DESC")
    fun getRecordByDateRange(from: Date, to: Date, bookId: Int): LiveData<List<Record>>

    /**
     * 获取最近30条记录
     * @return LiveData<List<Record>>
     */
    @Transaction
    @Query("SELECT * FROM $TB_RECORDS WHERE time ORDER BY time DESC LIMIT 30")
    fun getRecentRecords():LiveData<List<RecordWithCategoryBean>>

    /**
     * 按日期范围查询金额总和 分组为支出/收入
     */
    @Query("SELECT record_type as recordType, sum(money) as sumMoney FROM tb_records WHERE time BETWEEN :from AND :to GROUP BY record_type")
    fun getSumMoneyByDateRange(from: Date, to: Date): LiveData<List<SumMoneyByDateBean>>

    /**
     * 按日期范围, 收支类型查询金额总和 分组为日期 时间早的靠前
     */
    @Query("select strftime('%d',time,'unixepoch','localtime') as date, sum(money) as sumMoney from tb_records where book_id = :bookId and time between :from and :to and record_type = :type group by date order by date asc")
    fun getSumMoneyGroupByDate(
        from: Date,
        to: Date,
        type: Int,
        bookId: Int
    ): LiveData<List<SumMoneyGroupByDateBean>>

    /**
     * 按日期范围,收支类型查询金额总和 分组为月份 时间早的靠前
     */
    @Query("select strftime('%m',time,'unixepoch','localtime') as date, sum(money) as sumMoney from tb_records where book_id = :bookId and time between :from and :to and record_type = :type group by date order by date asc")
    fun getSumMoneyGroupByMonth(
        from: Date,
        to: Date,
        type: Int,
        bookId: Int
    ): LiveData<List<SumMoneyGroupByDateBean>>

    /**
     * 按日期范围, 收支类型查询金额总和 分组为category 金额大的靠前
     */
    @Query("select name as majorCategory, sum(money) as sumMoney ,count(major_category_id) as count from $TB_RECORDS LEFT JOIN $TB_MAJOR_CATEGORIES ON major_category_id = $TB_MAJOR_CATEGORIES.id where  book_id = :bookId and time between :from and :to and $TB_RECORDS.record_type = :type group by major_category_id order by sumMoney desc")
    fun getSumMoneyGroupByCategory(
        from: Date,
        to: Date,
        type: Int,
        bookId: Int
    ): LiveData<List<SumMoneyGroupByMajorCategoryBean>>

    /**
     * 搜索符合关键字的全部记录
     */
    @Query("select * from $TB_RECORDS where book_id = :bookId and remark like '%'|| :keyword || '%' order by time desc")
    fun getRecordByKeyword(keyword: String, bookId: Int): LiveData<List<RecordWithCategoryBean>>
}