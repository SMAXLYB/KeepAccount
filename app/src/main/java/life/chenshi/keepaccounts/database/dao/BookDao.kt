package life.chenshi.keepaccounts.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import life.chenshi.keepaccounts.database.entity.Book

@Dao
interface BookDao {
    // 增
    @Insert
    suspend fun insertBook(book: Book)

    // 删
    @Delete
    suspend fun deleteBook(book: Book)

    @Query("DELETE FROM tb_books where id = :id")
    suspend fun deleteBookById(id: Int)

    // 增
    @Update
    suspend fun updateBook(book: Book)

    // 查
    @Query("SELECT * FROM tb_books")
    fun getAllBooks(): Flow<MutableList<Book>>

    // todo 可能会有问题,切换activity后更新不及时
    @Query("SELECT name FROM tb_books WHERE id = :id")
    suspend fun getBookNameById(id: Int): String
}