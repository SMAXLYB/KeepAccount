package life.chenshi.keepaccounts.database

import androidx.lifecycle.LiveData
import androidx.room.*
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
    @Query("SELECT * from tb_records WHERE time BETWEEN :from AND :to ORDER BY time DESC")
    fun getRecordByDateRange(from: Date, to: Date): LiveData<List<Record>>

}