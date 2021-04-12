package life.chenshi.keepaccounts.ui.index

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import life.chenshi.keepaccounts.common.utils.DateUtil
import life.chenshi.keepaccounts.constant.*
import life.chenshi.keepaccounts.database.AppDatabase
import life.chenshi.keepaccounts.database.bean.RecordWithCategoryBean
import life.chenshi.keepaccounts.database.bean.SumMoneyByDateBean
import life.chenshi.keepaccounts.database.entity.Record
import java.util.*

class IndexViewModel : ViewModel() {

    private val recordDAO by lazy { AppDatabase.getDatabase().getRecordDao() }
    private val bookDao by lazy { AppDatabase.getDatabase().getBookDao() }
    val recentRecordsLiveData = MediatorLiveData<List<RecordWithCategoryBean>>()
    private var mTempRecordsLiveData: LiveData<List<RecordWithCategoryBean>>? = null
    val currentShowType by lazy { MutableLiveData<Int>(SHOW_TYPE_ALL) }
    val currentSortType by lazy { MutableLiveData<Int>(SORT_BY_DATE_DESC) }
    val queryDateLiveData by lazy { MutableLiveData<Long>(System.currentTimeMillis()) }

    val confirmBeforeDelete = MutableLiveData<Boolean>(true)

    init {
        // 默认查询本月的记录
        getRecentRecords()
    }

    /**
     * 根据日期范围取出记录
     */
    private fun getRecentRecords() {
        if (mTempRecordsLiveData != null) {
            recentRecordsLiveData.removeSource(mTempRecordsLiveData!!)
        }
        // viewModelScope.launch {
        //     currentBookId.take(1)
        //         .collect {
        //             mTempRecordsLiveData = recordDAO.getRecordByDateRange(from, to, it)
        //         }
        //     recordsByDateRangeLiveData.addSource(mTempRecordsLiveData!!) {
        //         recordsByDateRangeLiveData.value = it
        //     }
        // }
        mTempRecordsLiveData = recordDAO.getRecentRecords()
        recentRecordsLiveData.addSource(mTempRecordsLiveData!!) {
            recentRecordsLiveData.value = it
        }
    }

    /**
     * 根据日期范围取出收支的总金额
     */
    fun getSumMoneyByDateRange(from: Date, to: Date): LiveData<List<SumMoneyByDateBean>> {
        return recordDAO.getSumMoneyByDateRange(from, to)
    }

    /**
     * 将数据库的list<>转为list<list<>>，按同一天放在一个List中, 并排好序
     * @param originList 原始数据库数据
     */
    suspend fun convert2RecordListGroupByDay(originList: List<RecordWithCategoryBean>): List<List<RecordWithCategoryBean>> = withContext(Dispatchers.Default){
        // 首次使用应用时数据库无数据
        if (originList.isNullOrEmpty()) {
            return@withContext Collections.emptyList()
        }
        var listAfterFilter = originList
        // 只看收入
        if (currentShowType.value == SHOW_TYPE_INCOME) {
            listAfterFilter = originList.filter { it.record.recordType == RECORD_TYPE_INCOME }
        }
        // 只看支出
        if (currentShowType.value == SHOW_TYPE_OUTCOME) {
            listAfterFilter = originList.filter { it.record.recordType == RECORD_TYPE_OUTCOME }
        }

        val recordListGroupByDay: MutableList<MutableList<RecordWithCategoryBean>> = mutableListOf()
        // 如果经过筛选后没有数据, 直接返回空数据
        if (listAfterFilter.isEmpty()) {
            return@withContext recordListGroupByDay
        }

        // 排序
        listAfterFilter = when (currentSortType.value) {
            SORT_BY_MONEY_ASC -> {
                listAfterFilter.sortedBy { it.record.money }
            }
            SORT_BY_MONEY_DESC -> {
                listAfterFilter.sortedByDescending { it.record.money }
            }
            SORT_BY_DATE_ASC -> {
                listAfterFilter.sortedBy { it.record.time }
            }
            else ->
                listAfterFilter
        }

        val records: MutableList<RecordWithCategoryBean> = mutableListOf(listAfterFilter[0])
        recordListGroupByDay.add(records)
        // 对每条记录进行循环，以日期为单位，找出日期相同的记录，放在一起
        listAfterFilter.forEach {
            val lastRecordListGroup = recordListGroupByDay[recordListGroupByDay.size - 1]
            val lastRecordDate = lastRecordListGroup[lastRecordListGroup.size - 1].record.time
            // 如果在同一天
            if (DateUtil.isSameDay(lastRecordDate, it.record.time)) {
                lastRecordListGroup.add(it)
            } else {
                recordListGroupByDay.add(mutableListOf(it))
            }
        }
        recordListGroupByDay[0].removeAt(0)
        return@withContext recordListGroupByDay
    }

    /**
     * 删除记录
     */
    fun deleteRecord(record: Record) {
        viewModelScope.launch {
            recordDAO.deleteRecord(record)
        }
    }

    /**
     * 是否有账本
     * @param doIfHas 有账本时的操作
     * @param doIfNot 无账本的操作
     */
    fun hasBook(doIfHas: () -> Unit, doIfNot: (() -> Unit)? = null) {
        viewModelScope.launch {
            val numOfBooks = withContext(Dispatchers.IO) {
                bookDao.getNumOfBooks()
            }
            if (numOfBooks == 0) {
                doIfNot?.invoke()
            } else {
                doIfHas()
            }
        }
    }
}