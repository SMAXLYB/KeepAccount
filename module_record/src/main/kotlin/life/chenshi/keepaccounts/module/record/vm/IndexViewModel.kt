package life.chenshi.keepaccounts.module.record.vm

import androidx.lifecycle.*
import com.github.mikephil.charting.data.Entry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import life.chenshi.keepaccounts.module.common.constant.*
import life.chenshi.keepaccounts.module.common.database.bean.RecordWithCategoryBean
import life.chenshi.keepaccounts.module.common.database.entity.Record
import life.chenshi.keepaccounts.module.common.utils.DateUtil
import life.chenshi.keepaccounts.module.record.repo.IndexRepo
import java.util.*
import javax.inject.Inject

@HiltViewModel
class IndexViewModel @Inject constructor(private val indexRepo: IndexRepo) : ViewModel() {

    val recentRecordsLiveData = MediatorLiveData<List<RecordWithCategoryBean>>()
    private var mTempRecordsLiveData: LiveData<List<RecordWithCategoryBean>>? = null
    val currentShowType by lazy { MutableLiveData<Int>(SHOW_TYPE_ALL) }
    val currentSortType by lazy { MutableLiveData<Int>(SORT_BY_DATE_DESC) }
    val queryDateLiveData by lazy { MutableLiveData<Long>(System.currentTimeMillis()) }

    val confirmBeforeDelete = MutableLiveData<Boolean>(true)

    val sumBalance = indexRepo.getSumBalance()

    val dailyBalanceByDateRange =
        indexRepo.getDailyBalanceByDateRange(DateUtil.getCurrentMonthStart(), DateUtil.getCurrentMonthEnd())

    /**
     * 生成空实体数据
     */
    fun generateEmptyEntriesOfMonth(daysInMonth: Int): MutableList<Entry> {
        val entries: MutableList<Entry> = mutableListOf()
        for (i in 1..daysInMonth) {
            entries.add(Entry(i.toFloat(), 0.0f))
        }
        return entries
    }

    /**
     * 根据日期范围取出记录
     */
    val recentRecords = indexRepo.getRecentRecords()

    /**
     * 将数据库的list<>转为list<list<>>，按同一天放在一个List中, 并排好序
     * @param originList 原始数据库数据
     */
    suspend fun convert2RecordListGroupByDay(originList: List<RecordWithCategoryBean>): List<List<RecordWithCategoryBean>> =
        withContext(Dispatchers.Default) {
            // 首次使用应用时数据库无数据
            if (originList.isEmpty()) {
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
            indexRepo.deleteRecordAndUpdateBalance(record)
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
                indexRepo.getNumOfBooks()
            }
            if (numOfBooks == 0) {
                doIfNot?.invoke()
            } else {
                doIfHas()
            }
        }
    }
}