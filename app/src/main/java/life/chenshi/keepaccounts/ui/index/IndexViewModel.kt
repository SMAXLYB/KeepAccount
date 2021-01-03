package life.chenshi.keepaccounts.ui.index

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import life.chenshi.keepaccounts.database.Record
import life.chenshi.keepaccounts.database.RecordDatabase
import life.chenshi.keepaccounts.utils.DateUtils
import java.util.*

class IndexViewModel : ViewModel() {

    private val recordDAO by lazy { RecordDatabase.getDatabase().getRecordDao() }

    /**
     * 根据日期范围取出记录
     */
    fun getRecordByDateRange(from: Date, to: Date): LiveData<List<Record>> {
        return recordDAO.getRecordByDateRange(from, to)
    }

    /**
     * 将数据库的list<>转为list<list<>>，按同一天放在一个List中
     */
    fun convert2RecordListGroupByDay(originList:List<Record>):List<List<Record>>{
        // 首次使用应用时数据库无数据
        if(originList.isNullOrEmpty()){
            return Collections.emptyList()
        }

        val recordListGroupByDay: MutableList<MutableList<Record>> = mutableListOf()
        val tempRecordData: Date = originList[0].time
        val records: MutableList<Record> = mutableListOf(originList[0])
        recordListGroupByDay.add(records)
        // 对每条记录进行循环，以日期为单位，找出日期相同的记录，放在一起
        originList.forEach {
            val lastRecordListGroup = recordListGroupByDay[recordListGroupByDay.size -1]
            val lastRecordDate = lastRecordListGroup[lastRecordListGroup.size -1].time
            // 如果在同一天
            if(DateUtils.isSameDay(lastRecordDate,it.time)){
                lastRecordListGroup.add(it)
            }else{
                recordListGroupByDay.add(mutableListOf(it))
            }
        }
        recordListGroupByDay[0].removeAt(0)
        return recordListGroupByDay
    }
}