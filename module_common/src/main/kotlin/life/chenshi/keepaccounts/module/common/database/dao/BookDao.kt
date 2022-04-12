package life.chenshi.keepaccounts.module.common.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import life.chenshi.keepaccounts.module.common.database.entity.Book

@Dao
interface BookDao {
    // 增 suspend修饰-->后台执行 liveData返回-->后台执行
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
    fun getAllBooks(): Flow<List<Book>>

    @Query("SELECT COUNT(*) FROM tb_books")
    suspend fun getNumOfBooks(): Int

    // todo 可能会有问题,切换activity后更新不及时
    @Query("SELECT * FROM tb_books WHERE id = :id")
    suspend fun getBookById(id: Int): Book
}