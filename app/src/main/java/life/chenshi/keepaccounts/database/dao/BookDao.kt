package life.chenshi.keepaccounts.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import life.chenshi.keepaccounts.database.entity.Book

@Dao
interface BookDao {
    // 增
    @Insert
    fun insertBook(book: Book)

    // 删
    @Delete
    fun deleteBook(book: Book)

    // 增
    @Update
    fun updateBook(book: Book)

    // 查
}