package life.chenshi.keepaccounts.ui.newrecord

import androidx.lifecycle.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import life.chenshi.keepaccounts.common.utils.DataStoreUtil
import life.chenshi.keepaccounts.constant.DB_CURRENT_BOOK_ID
import life.chenshi.keepaccounts.constant.RECORD_TYPE_OUTCOME
import life.chenshi.keepaccounts.database.AppDatabase
import life.chenshi.keepaccounts.database.entity.Book
import life.chenshi.keepaccounts.database.entity.Record
import java.util.*

class NewRecordViewModel : ViewModel() {

    private val mBookDao by lazy { AppDatabase.getDatabase().getBookDao() }
    private val mRecordDao by lazy { AppDatabase.getDatabase().getRecordDao() }

    // 当前所有选中的配置
    val currentBook = MutableLiveData<Book>()
    val currentRecordType = MutableLiveData<Int>(RECORD_TYPE_OUTCOME)
    val currentDateTime = MutableLiveData<Long>(System.currentTimeMillis())

    val books = mBookDao.getAllBooks()

    fun insertRecord(record: Record) {
        viewModelScope.launch {
            mRecordDao.insertRecord(record)
        }
    }

    fun updateRecord(record: Record) {
        viewModelScope.launch {
            mRecordDao.updateRecord(record)
        }
    }

    /**
     * @param doIfHas 有默认账本时的操作
     * @param doIfNot 无账本的操作
     */
    fun hasDefaultBook(doIfHas: (Int) -> Unit, doIfNot: (() -> Unit)?) {
        viewModelScope.launch {
            var currentBookId = -1
            DataStoreUtil.readFromDataStore(DB_CURRENT_BOOK_ID, -1)
                .take(1)
                .collect {
                    currentBookId = it
                }
            if (currentBookId == -1) {
                doIfNot?.invoke()
                return@launch
            }
            doIfHas(currentBookId)
        }
    }

    suspend fun getBookNameById(id: Int) = mBookDao.getBookNameById(id)
}