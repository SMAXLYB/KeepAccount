package life.chenshi.keepaccounts.ui.setting.book

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import life.chenshi.keepaccounts.database.AppDatabase
import life.chenshi.keepaccounts.database.entity.Book

class BookViewModel : ViewModel() {
    private val mBookDao by lazy { AppDatabase.getDatabase().getBookDao() }

    val booksLiveData: LiveData<MutableList<Book>> = mBookDao.getAllBooks().asLiveData()

    fun deleteBookById(bookId: Int) {
        viewModelScope.launch {
            mBookDao.deleteBookById(bookId)
        }
    }
}