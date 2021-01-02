package life.chenshi.keepaccounts.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

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
    // @Query()
    // suspend fun get

}