package life.chenshi.keepaccounts.ui.search

import androidx.lifecycle.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import life.chenshi.keepaccounts.constant.*
import life.chenshi.keepaccounts.database.AppDatabase
import life.chenshi.keepaccounts.database.bean.RecordWithCategoryBean
import life.chenshi.keepaccounts.module.common.utils.DataStoreUtil
import life.chenshi.keepaccounts.module.common.utils.DateUtil
import java.util.*

class SearchViewModel : ViewModel() {

    private val mRecordDao by lazy { AppDatabase.getDatabase().getRecordDao() }

    // 默认查看全部类型
    val filterType by lazy { MutableLiveData<Int>(SHOW_TYPE_ALL) }

    // 默认不限时间
    val filterTime by lazy { MutableLiveData<Long>(0) }

    // 默认不限金额
    val filterOrder by lazy { MutableLiveData<Int>(SORT_BY_DATE_DESC) }

    // 数据源
    val recordsByKeywordLiveData by lazy { MediatorLiveData<List<RecordWithCategoryBean>>() }
    private var mTempRecordLiveData: LiveData<List<RecordWithCategoryBean>>? = null

    private val currentBookId =
        DataStoreUtil.readFromDataStore(DB_CURRENT_BOOK_ID, -1)

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
     * 将数据库的list<>转为list<list<>>，按同一天放在一个List中, 并排好序
     * @param originList 原始数据库数据
     */
    fun convert2RecordListGroupByDay(originList: List<RecordWithCategoryBean>): List<List<RecordWithCategoryBean>> {
        // 首次使用应用时数据库无数据
        if (originList.isNullOrEmpty()) {
            return Collections.emptyList()
        }
        var listAfterFilter = originList
        if (filterType.value == SHOW_TYPE_INCOME) {
            listAfterFilter = originList.filter { it.record.recordType == RECORD_TYPE_INCOME }
        }
        if (filterType.value == SHOW_TYPE_OUTCOME) {
            listAfterFilter = originList.filter { it.record.recordType == RECORD_TYPE_OUTCOME }
        }
        val recordListGroupByDay: MutableList<MutableList<RecordWithCategoryBean>> = mutableListOf()
        // 如果经过筛选后没有数据, 直接返回空数据
        if (listAfterFilter.isEmpty()) {
            return recordListGroupByDay
        }

        // 排序
        listAfterFilter = when (filterOrder.value) {
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
        return recordListGroupByDay
    }
}