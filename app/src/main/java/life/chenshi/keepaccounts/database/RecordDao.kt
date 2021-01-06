package life.chenshi.keepaccounts.database

import androidx.lifecycle.LiveData
import androidx.room.*
import life.chenshi.keepaccounts.bean.SumMoneyBean
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
    @Query("SELECT * FROM tb_records WHERE time BETWEEN :from AND :to ORDER BY time DESC")
    fun getRecordByDateRange(from: Date, to: Date): LiveData<List<Record>>

    @Query("SELECT record_type as recordType, sum(money) as sumMoney FROM tb_records WHERE time BETWEEN :from AND :to GROUP BY record_type")
    fun getSumMoneyByDataRange(from: Date, to: Date):LiveData<List<SumMoneyBean>>
}