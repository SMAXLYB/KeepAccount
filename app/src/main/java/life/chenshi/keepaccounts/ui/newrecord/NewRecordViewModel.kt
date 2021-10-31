package life.chenshi.keepaccounts.ui.newrecord

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import life.chenshi.keepaccounts.database.AppDatabase
import life.chenshi.keepaccounts.database.entity.AbstractCategory
import life.chenshi.keepaccounts.database.entity.Book
import life.chenshi.keepaccounts.database.entity.Record
import life.chenshi.keepaccounts.module.common.constant.DB_CURRENT_BOOK_ID
import life.chenshi.keepaccounts.module.common.constant.RECORD_TYPE_OUTCOME
import life.chenshi.keepaccounts.module.common.constant.STATE_NORMAL
import life.chenshi.keepaccounts.module.common.constant.SWITCHER_CONFIRM_BEFORE_DELETE
import life.chenshi.keepaccounts.module.common.utils.DataStoreUtil

class NewRecordViewModel : ViewModel() {

    //　record更新 控制数据
    var record: Record? = null

    // 当前是否为查看详情模式 控制界面
    val detailMode = MutableLiveData<Boolean>(false)

    private val mBookDao by lazy { AppDatabase.getDatabase().getBookDao() }
    private val mRecordDao by lazy { AppDatabase.getDatabase().getRecordDao() }
    private val mMinorCategoryDao by lazy { AppDatabase.getDatabase().getMinorCategoryDao() }
    private val mMajorCategoryDao by lazy { AppDatabase.getDatabase().getMajorCategoryDao() }

    // 当前所有选中的配置
    val currentBook = MutableLiveData<Book>()
    val currentRecordType = MutableLiveData<Int>(RECORD_TYPE_OUTCOME)
    val currentDateTime = MutableLiveData<Long>(System.currentTimeMillis())
    val currentAbstractCategory = MutableLiveData<AbstractCategory>()

    // 常用类别
    val commonMinorCategory = MutableLiveData<MutableList<AbstractCategory>>()

    // 所有账本
    val books = mBookDao.getAllBooks()

    init {
        viewModelScope.launch {
            // 只监听一次,后续不管是否删除,都继续使用
            mMinorCategoryDao.getTop6MinorCategoryBy(STATE_NORMAL)
                .take(1)
                .collect {
                    val list = mutableListOf<AbstractCategory>()
                    list.addAll(it)
                    commonMinorCategory.value = list
                }
        }
    }

    /**
     * 新建记录
     * @param record Record
     */
    fun insertRecord(record: Record) {
        viewModelScope.launch {
            mRecordDao.insertRecordAndUpdateUseRate(record)
        }
    }

    /**
     * 更新记录
     * @param record Record
     */
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
     * 根据id查找账本
     * @param id Int
     * @return Book
     */
    suspend fun getBookById(id: Int) = mBookDao.getBookById(id)

    /**
     * 根据id查找主类
     * @param majorCategoryId Int
     */
    suspend fun getMajorCategoryById(majorCategoryId: Int) =
        mMajorCategoryDao.getMajorCategoryBy(STATE_NORMAL, majorCategoryId)

    /**
     * 根据id查找子类
     * @param minorCategoryId Int
     * @return MinorCategory
     */
    suspend fun getMinorCategoryById(minorCategoryId: Int) =
        mMinorCategoryDao.getMinorCategoryBy(STATE_NORMAL, minorCategoryId)

    /**
     * 如果常用类型不存在指定类型,则增加
     * @param abstractCategory AbstractCategory
     */
    fun insertIfNotExistInCommonCategory(abstractCategory: AbstractCategory) {
        viewModelScope.launch {
            commonMinorCategory.apply {
                // 取出原数据
                val list = value
                // 如果原数据没有,直接放入,如果已经存在,不做处理
                // 判断是否有id相等,在判断类型是否一致
                withContext(Dispatchers.Default) {
                    list?.firstOrNull { it == abstractCategory } ?: run { list?.add(abstractCategory) }
                }
                // 更新
                value = list!!
            }
        }
    }

    /**
     * 是否要删除前确认
     * @param callback Function1<Boolean, Unit>
     */
    fun confirmBeforeDelete(callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            DataStoreUtil.readFromDataStore(SWITCHER_CONFIRM_BEFORE_DELETE, true)
                .take(1)
                .collect {
                    callback.invoke(it)
                }
        }
    }
}