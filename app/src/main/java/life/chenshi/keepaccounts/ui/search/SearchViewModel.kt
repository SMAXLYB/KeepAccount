package life.chenshi.keepaccounts.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import life.chenshi.keepaccounts.database.Record
import life.chenshi.keepaccounts.database.RecordDatabase
import life.chenshi.keepaccounts.utils.DateUtil
import java.util.*

class SearchViewModel : ViewModel() {

    private val mRecordDao by lazy { RecordDatabase.getDatabase().getRecordDao() }

    // 默认查看全部类型
    val filterType by lazy { MutableLiveData<Int>(SearchActivity.FILTER_TYPE_ALL) }

    // 默认不限时间
    val filterTime by lazy { MutableLiveData<Long>(0) }

    // 默认不限金额
    val filterOrder by lazy { MutableLiveData<Int>(SearchActivity.ORDER_TYPE_DATE_DESC) }

    // 数据源
    val recordsByKeywordLiveData by lazy { MediatorLiveData<List<Record>>() }
    private var mTempRecordLiveData: LiveData<List<Record>>? = null

    fun getRecordByKeyword(keyword: String) {
        if (mTempRecordLiveData != null) {
            recordsByKeywordLiveData.removeSource(mTempRecordLiveData!!)
        }

        mTempRecordLiveData = mRecordDao.getRecordByKeyword(keyword)
        recordsByKeywordLiveData.addSource(mTempRecordLiveData!!) {
            recordsByKeywordLiveData.value = it
        }
    }

    /**
     * 将数据库的list<>转为list<list<>>，按同一天放在一个List中
     * @param originList 原始数据库数据
     * @param showType 展示类型
     */
    fun convert2RecordListGroupByDay(originList: List<Record>, showType: Int): List<List<Record>> {
        // 首次使用应用时数据库无数据
        if (originList.isNullOrEmpty()) {
            return Collections.emptyList()
        }
        var listAfterFilter = originList
        // if (showType == IndexFragment.SHOW_TYPE_INCOME) {
        //     listAfterFilter = originList.filter { it.recordType == RecordType.INCOME }
        // }

        // if (showType == IndexFragment.SHOW_TYPE_OUTCOME) {
        //     listAfterFilter = originList.filter { it.recordType == RecordType.OUTCOME }
        // }
        val recordListGroupByDay: MutableList<MutableList<Record>> = mutableListOf()
        // 如果经过筛选后没有数据, 直接返回空数据
        if (listAfterFilter.isEmpty()) {
            return recordListGroupByDay
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