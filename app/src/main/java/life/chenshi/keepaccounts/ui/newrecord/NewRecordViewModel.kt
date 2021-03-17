package life.chenshi.keepaccounts.ui.newrecord

import androidx.lifecycle.*
import com.example.gson_extra.RuntimeTypeAdapterFactory
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import life.chenshi.keepaccounts.common.utils.DataStoreUtil
import life.chenshi.keepaccounts.constant.*
import life.chenshi.keepaccounts.database.AppDatabase
import life.chenshi.keepaccounts.database.entity.*
import java.util.*

class NewRecordViewModel : ViewModel() {

    private val mBookDao by lazy { AppDatabase.getDatabase().getBookDao() }
    private val mRecordDao by lazy { AppDatabase.getDatabase().getRecordDao() }

    // 当前所有选中的配置
    val currentBook = MutableLiveData<Book>()
    val currentRecordType = MutableLiveData<Int>(RECORD_TYPE_OUTCOME)
    val currentDateTime = MutableLiveData<Long>(System.currentTimeMillis())
    val currentAbstractCategory = MutableLiveData<AbstractCategory>()

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

    /**
     * 获取常用类别
     */
    fun getCommonCategory(): Flow<List<AbstractCategory>> {
        val defaultCommonCategoryStr = """
            [{"id":1,"name":"你好啊","state":0,"type":"major_category"},{"id":1,"name":"你好啊啊啊","state":0,"type":"major_category"},{"majorCategoryId":1,"id":1,"name":"第二","state":1,"type":"minor_category"},{"id":1,"name":"你好","state":0,"type":"major_category"},{"id":1,"name":"你好","state":0,"type":"major_category"},{"id":1,"name":"你好","state":0,"type":"major_category"}]
        """.trimIndent()

        return DataStoreUtil.readFromDataStore(COMMON_CATEGORY, defaultCommonCategoryStr)
            .take(1)
            .map {
                val typeFactory = RuntimeTypeAdapterFactory
                    .of(AbstractCategory::class.java, "categoryType")
                    .registerSubtype(MajorCategory::class.java, CATEGORY_TYPE_MAJOR)
                    .registerSubtype(MinorCategory::class.java, CATEGORY_TYPE_MINOR)
                val gson = GsonBuilder().registerTypeAdapterFactory(typeFactory).create()
                gson.fromJson<List<AbstractCategory>>(it, object : TypeToken<List<AbstractCategory>>() {}.type)
            }
    }

    suspend fun getBookById(id: Int) = mBookDao.getBookById(id)
}