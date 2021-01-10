package life.chenshi.keepaccounts.database

import androidx.lifecycle.LiveData
import androidx.room.*
import life.chenshi.keepaccounts.bean.SumMoneyByDateAndTypeBean
import life.chenshi.keepaccounts.bean.SumMoneyByDateBean
import java.util.*

@Dao
interface RecordDao {
    // 增
    @Insert
    suspend fun insertRecord(record: Record)

    // 删
    @Delete
    suspend fun deleteRecord(record: Record)

    // 改
    @Update
    suspend fun updateRecord(record: Record)

    // // 查 设计多个表查询可以使用@Transaction
    // @Transaction
    /**
     * 按日期范围查询记录
     */
    @Query("SELECT * FROM tb_records WHERE time BETWEEN :from AND :to ORDER BY time DESC")
    fun getRecordByDateRange(from: Date, to: Date): LiveData<List<Record>>

    /**
     * 按日期范围查询金额总和 分组为支出/收入
     */
    @Query("SELECT record_type as recordType, sum(money) as sumMoney FROM tb_records WHERE time BETWEEN :from AND :to GROUP BY record_type")
    fun getSumMoneyByDateRange(from: Date, to: Date): LiveData<List<SumMoneyByDateBean>>

    /**
     * 按日期范围, 收支类型查询金额总和 分组为时间 时间早的靠前
     */
    @Query("select strftime('%d',time,'unixepoch','localtime') as day, sum(money) as sumMoney from tb_records where time between :from and :to and record_type = :type group by day order by day asc")
    fun getSumMoneyByDateAndType(
        from: Date,
        to: Date,
        type: Int
    ): LiveData<List<SumMoneyByDateAndTypeBean>>
}