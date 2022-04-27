package life.chenshi.keepaccounts.module.book.vm

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import life.chenshi.keepaccounts.module.common.constant.CURRENT_BOOK_ID
import life.chenshi.keepaccounts.module.common.database.AppDatabase
import life.chenshi.keepaccounts.module.common.database.entity.Book
import life.chenshi.keepaccounts.module.common.utils.storage.DataStoreUtil

class AllBookViewModel : ViewModel() {
    companion object{
        private const val TAG = "BookViewModel"
    }
    private val mBookDao by lazy { AppDatabase.getDatabase().getBookDao() }

    val booksLiveData = MediatorLiveData<List<Book>>()

    lateinit var currentBookId: LiveData<Int>

    init {
        booksLiveData.addSource(mBookDao.getAllBooks().asLiveData()) {
            booksLiveData.value = it ?: emptyList()
        }
        getCurrentBookId()
    }

    /**
     * 删除账本
     */
    fun deleteBookById(bookId: Int) {
        viewModelScope.launch {
            mBookDao.deleteBookById(bookId)
            if (currentBookId.value == bookId) {
                setCurrentBookId(-1)
            }
        }
    }

    /**
     * 新增账本
     */
    suspend fun insertBook(book: Book) {
        mBookDao.insertBook(book)
    }

    /**
     * 设置当前账本id
     */
    fun setCurrentBookId(id: Int) {
        viewModelScope.launch {
            DataStoreUtil.writeToDataStore(CURRENT_BOOK_ID, id)
        }
    }

    /**
     * 获取当前账本id
     */
    private fun getCurrentBookId() {
        viewModelScope.launch {
            currentBookId =
                DataStoreUtil.readFromDataStore(CURRENT_BOOK_ID, -1).asLiveData()
        }
    }

    fun getCurrentBookPositionInListById(id: Int): Int {
        booksLiveData.value?.forEachIndexed { index, book ->
            if (book.id == id) {
                return index
            }
        }
        return -1
    }
}