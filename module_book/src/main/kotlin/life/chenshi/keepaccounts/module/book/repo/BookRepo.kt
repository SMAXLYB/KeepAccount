package life.chenshi.keepaccounts.module.book.repo

import life.chenshi.keepaccounts.module.common.database.dao.BookDao
import life.chenshi.keepaccounts.module.common.database.entity.Book
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookRepo @Inject constructor(private val bookDao: BookDao) {
    suspend fun insertBook(book: Book) = bookDao.insertBook(book)
    suspend fun deleteBookById(bookId: Int) = bookDao.deleteBookById(bookId)
    fun getAllBooks() = bookDao.getAllBooks()
}