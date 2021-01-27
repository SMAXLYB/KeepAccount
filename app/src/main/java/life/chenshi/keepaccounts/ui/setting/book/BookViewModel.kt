package life.chenshi.keepaccounts.ui.setting.book

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import life.chenshi.keepaccounts.database.AppDatabase
import life.chenshi.keepaccounts.database.entity.Book

class BookViewModel : ViewModel() {
    private val mBookDao by lazy { AppDatabase.getDatabase().getBookDao() }

    val booksLiveData: LiveData<List<Book>> = mBookDao.getAllBooks().asLiveData()
    val currentBookId = MutableLiveData<Int>(0)

    fun deleteBookById(bookId: Int) {
        viewModelScope.launch {
            mBookDao.deleteBookById(bookId)
        }
    }

    fun insertBook(book: Book) {
        viewModelScope.launch() {
            mBookDao.insertBook(book)
        }
    }

    fun getCurrentBookId() {
        currentBookId
    }
}