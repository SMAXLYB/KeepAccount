package life.chenshi.keepaccounts.ui.newrecord

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import life.chenshi.keepaccounts.constant.DataStoreConstant
import life.chenshi.keepaccounts.database.AppDatabase
import life.chenshi.keepaccounts.database.entity.Record
import life.chenshi.keepaccounts.utils.DataStoreUtil
import java.util.*

class NewRecordViewModel : ViewModel() {

    private val mBookDao by lazy { AppDatabase.getDatabase().getBookDao() }
    private val mRecordDao by lazy { AppDatabase.getDatabase().getRecordDao() }

    // 选择好的时间
    val mCurrentChooseCalendar: Calendar = Calendar.getInstance()

    // 最后一次选择的类型
    var lastSelectedCategoryIndex = 0
    val categoryViews = mutableListOf<View>()

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
     * 是否有默认账本 侧重点在于兼容老版本
     * @param doIfHas 有默认账本时的操作
     * @param doIfNot 无账本的操作
     */
    fun hasDefaultBook(doIfHas: (Int) -> Unit, doIfNot: () -> Unit) {
        viewModelScope.launch {
            var currentBookId = -1
            DataStoreUtil.readFromDataStore(DataStoreConstant.CURRENT_BOOK_ID, -1)
                .take(1)
                .collect {
                    currentBookId = it
                }
            // 如果本地没有记录, 查询数据库
            if (currentBookId == -1) {
                val books = mBookDao.getAllBooks().first()
                // 如果数据库没有数据
                if (books.isEmpty()) {
                    doIfNot()
                    return@launch
                }
                // 如果数据库有记录, 写入到本地
                if (books.isNotEmpty()) {
                    currentBookId = books[0].id!!
                    DataStoreUtil.writeToDataStore(
                        DataStoreConstant.CURRENT_BOOK_ID, currentBookId
                    )
                }
            }
            doIfHas(currentBookId)
        }
    }
}