package life.chenshi.keepaccounts.ui.search

import androidx.lifecycle.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import life.chenshi.keepaccounts.common.utils.DataStoreUtil
import life.chenshi.keepaccounts.common.utils.DateUtil
import life.chenshi.keepaccounts.constant.CURRENT_BOOK_ID
import life.chenshi.keepaccounts.database.AppDatabase
import life.chenshi.keepaccounts.database.entity.Record
import life.chenshi.keepaccounts.database.entity.RecordType
import life.chenshi.keepaccounts.ui.index.IndexFragment
import java.util.*

class SearchViewModel : ViewModel() {

    private val mRecordDao by lazy { AppDatabase.getDatabase().getRecordDao() }

    // 默认查看全部类型
    val filterType by lazy { MutableLiveData<Int>(SearchActivity.FILTER_TYPE_ALL) }

    // 默认不限时间
    val filterTime by lazy { MutableLiveData<Long>(0) }

    // 默认不限金额
    val filterOrder by lazy { MutableLiveData<Int>(SearchActivity.ORDER_TYPE_DATE_DESC) }

    // 数据源
    val recordsByKeywordLiveData by lazy { MediatorLiveData<List<Record>>() }
    private var mTempRecordLiveData: LiveData<List<Record>>? = null

    private val currentBookId =
        DataStoreUtil.readFromDataStore(CURRENT_BOOK_ID, -1)

    fun getRecordByKeyword(keyword: String) {
        if (mTempRecordLiveData != null) {
            recordsByKeywordLiveData.removeSource(mTempRecordLiveData!!)
        }

        viewModelScope.launch {
            currentBookId.take(1)
                .collect { id ->
                    mTempRecordLiveData = mRecordDao.getRecordByKeyword(keyword, id)
                    recordsByKeywordLiveData.addSource(mTempRecordLiveData!!) {
                        recordsByKeywordLiveData.value = it
                    }
                }
        }

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
        if (filterType.value == IndexFragment.SHOW_TYPE_INCOME) {
            listAfterFilter = originList.filter { it.recordType == RecordType.INCOME }
        }
        if (filterType.value == IndexFragment.SHOW_TYPE_OUTCOME) {
            listAfterFilter = originList.filter { it.recordType == RecordType.OUTCOME }
        }
        val recordListGroupByDay: MutableList<MutableList<Record>> = mutableListOf()
        // 如果经过筛选后没有数据, 直接返回空数据
        if (listAfterFilter.isEmpty()) {
            return recordListGroupByDay
        }

        listAfterFilter = when (filterOrder.value) {
            SearchActivity.ORDER_TYPE_MONEY_ASC -> {
                listAfterFilter.sortedBy { it.money }
            }
            SearchActivity.ORDER_TYPE_MONEY_DESC -> {
                listAfterFilter.sortedByDescending { it.money }
            }
            SearchActivity.ORDER_TYPE_DATE_ASC -> {
                listAfterFilter.sortedBy { it.time }
            }
            else ->
                listAfterFilter
        }
        val tempRecordData: Date = listAfterFilter[0].time
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
}