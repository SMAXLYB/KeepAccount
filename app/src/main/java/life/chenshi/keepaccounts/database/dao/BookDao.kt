package life.chenshi.keepaccounts.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
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
    @Query("SELECT * FROM tb_books")
    fun getAllBook(): LiveData<List<Book>>
}