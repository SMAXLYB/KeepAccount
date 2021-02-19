package life.chenshi.keepaccounts.ui.index

import androidx.lifecycle.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import life.chenshi.keepaccounts.bean.SumMoneyByDateBean
import life.chenshi.keepaccounts.common.utils.DataStoreUtil
import life.chenshi.keepaccounts.common.utils.DateUtil
import life.chenshi.keepaccounts.constant.CURRENT_BOOK_ID
import life.chenshi.keepaccounts.database.AppDatabase
import life.chenshi.keepaccounts.database.entity.Record
import life.chenshi.keepaccounts.database.entity.RecordType
import java.util.*

class IndexViewModel : ViewModel() {

    private val recordDAO by lazy { AppDatabase.getDatabase().getRecordDao() }
    val recordsByDateRangeLiveData = MediatorLiveData<List<Record>>()
    private var mTempRecordsLiveData: LiveData<List<Record>>? = null
    val currentShowType by lazy { MutableLiveData<Int>(IndexFragment.SHOW_TYPE_ALL) }
    val queryDateLiveData by lazy { MutableLiveData<Long>(System.currentTimeMillis()) }
    private val currentBookId =
        DataStoreUtil.readFromDataStore(CURRENT_BOOK_ID, -1)

    init {
        getRecordByDateRange(
            DateUtil.getCurrentMonthStart(),
            DateUtil.getCurrentMonthEnd()
        )
    }

    /**
     * 根据日期范围取出记录
     */
    fun getRecordByDateRange(from: Date, to: Date) {
        if (mTempRecordsLiveData != null) {
            recordsByDateRangeLiveData.removeSource(mTempRecordsLiveData!!)
        }
        viewModelScope.launch {
            currentBookId.take(1)
                .collect {
                    mTempRecordsLiveData = recordDAO.getRecordByDateRange(from, to, it)
                }
            recordsByDateRangeLiveData.addSource(mTempRecordsLiveData!!) {
                recordsByDateRangeLiveData.value = it
            }
        }
    }

    // 下拉刷新记录
    fun getRecordAndSumMoneyByDataRange() {
        val monthStart = DateUtil.getMonthStart(queryDateLiveData.value!!)
        val monthEnd = DateUtil.getMonthEnd(queryDateLiveData.value!!)
        getRecordByDateRange(
            monthStart, monthEnd
        )
        getSumMoneyByDateRange(monthStart, monthEnd)
    }

    /**
     * 根据日期范围取出收支的总金额
     */
    fun getSumMoneyByDateRange(from: Date, to: Date): LiveData<List<SumMoneyByDateBean>> {
        return recordDAO.getSumMoneyByDateRange(from, to,)
    }

    /**
     * 将数据库的list<>转为list<list<>>，按同一天放在一个List中
     * @param originList 原始数据库数据
     */
    fun convert2RecordListGroupByDay(originList: List<Record>): List<List<Record>> {
        // 首次使用应用时数据库无数据
        if (originList.isNullOrEmpty()) {
            return Collections.emptyList()
        }
        var listAfterFilter = originList
        if (currentShowType.value == IndexFragment.SHOW_TYPE_INCOME) {
            listAfterFilter = originList.filter { it.recordType == RecordType.INCOME }
        }

        if (currentShowType.value == IndexFragment.SHOW_TYPE_OUTCOME) {
            listAfterFilter = originList.filter { it.recordType == RecordType.OUTCOME }
        }
        val recordListGroupByDay: MutableList<MutableList<Record>> = mutableListOf()
        // 如果经过筛选后没有数据, 直接返回空数据
        if (listAfterFilter.isEmpty()) {
            return recordListGroupByDay
        }
        val records: MutableList<Record> = mutableListOf(listAfterFilter[0])
        recordListGroupByDay.add(records)
        // 对每条记录进行循环，以日期为单位，找出日期相同的记录，放在一起
        listAfterFilter.forEach {
            val lastRecordListGroup = recordListGroupByDay[recordListGroupByDay.size - 1]
            val lastRecordDate = lastRecordListGroup[lastRecordListGroup.size - 1].time
            // 如果在同一天
            if (DateUtil.isSameDay(lastRecordDate, it.time)) {
                lastRecordListGroup.add(it)
            } else {
                recordListGroupByDay.add(mutableListOf(it))
            }
        }
        recordListGroupByDay[0].removeAt(0)
        return recordListGroupByDay
    }

    /**
     * 删除记录
     */
    fun deleteRecord(record: Record) {
        viewModelScope.launch{
            recordDAO.deleteRecord(record)
        }
    }

    /**
     * 是否有默认账本
     * @param doIfHas 有默认账本时的操作
     * @param doIfNot 无账本的操作
     */
    fun hasDefaultBook(doIfHas: (Int) -> Unit, doIfNot: (() -> Unit)? = null) {
        viewModelScope.launch {
            var currentBookId = -1
            DataStoreUtil.readFromDataStore(CURRENT_BOOK_ID, -1)
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
}