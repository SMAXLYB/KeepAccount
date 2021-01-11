package life.chenshi.keepaccounts.ui.index

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import life.chenshi.keepaccounts.bean.SumMoneyByDateBean
import life.chenshi.keepaccounts.database.Record
import life.chenshi.keepaccounts.database.RecordDatabase
import life.chenshi.keepaccounts.utils.DateUtil
import java.util.*

class IndexViewModel : ViewModel() {

    private val recordDAO by lazy { RecordDatabase.getDatabase().getRecordDao() }
    val recordsByDateRangeLiveData = MediatorLiveData<List<Record>>()
    val queryDateLiveData by lazy { MutableLiveData<Long>(System.currentTimeMillis()) }

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
        recordsByDateRangeLiveData.addSource(recordDAO.getRecordByDateRange(from, to)) {
            recordsByDateRangeLiveData.value = it
        }
    }

    // 下拉刷新记录
    fun getRecordAndSumMoneyByDataRange(){
        val monthStart = DateUtil.getMonthStart(queryDateLiveData.value!!)
        val monthEnd = DateUtil.getMonthEnd(queryDateLiveData.value!!)
        getRecordByDateRange(
            monthStart, monthEnd
        )
        getSumMoneyByDateRange(monthStart,monthEnd)
    }

    /**
     * 根据日期范围取出收支的总金额
     */
    fun getSumMoneyByDateRange(from: Date,to: Date):LiveData<List<SumMoneyByDateBean>>{
        return recordDAO.getSumMoneyByDateRange(from,to)
    }

    /**
     * 将数据库的list<>转为list<list<>>，按同一天放在一个List中
     */
    fun convert2RecordListGroupByDay(originList: List<Record>): List<List<Record>> {
        // 首次使用应用时数据库无数据
        if (originList.isNullOrEmpty()) {
            return Collections.emptyList()
        }

        val recordListGroupByDay: MutableList<MutableList<Record>> = mutableListOf()
        val tempRecordData: Date = originList[0].time
        val records: MutableList<Record> = mutableListOf(originList[0])
        recordListGroupByDay.add(records)
        // 对每条记录进行循环，以日期为单位，找出日期相同的记录，放在一起
        originList.forEach {
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