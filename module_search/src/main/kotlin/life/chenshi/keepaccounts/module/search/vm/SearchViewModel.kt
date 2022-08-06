package life.chenshi.keepaccounts.module.search.vm

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import life.chenshi.keepaccounts.module.common.constant.*
import life.chenshi.keepaccounts.module.common.database.bean.RecordWithCategoryBean
import life.chenshi.keepaccounts.module.common.database.entity.AssetsAccount
import life.chenshi.keepaccounts.module.common.utils.DateUtil
import life.chenshi.keepaccounts.module.common.utils.storage.DataStoreUtil
import life.chenshi.keepaccounts.module.search.repo.SearchRepo
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val repo: SearchRepo) : ViewModel() {

    var keyword: String? = null
    val sortOptions = arrayListOf<SortOption>()
    var startTime = MutableLiveData(0L)
    var endTime = MutableLiveData(0L)

    val allAssetsAccount = MutableLiveData<List<AssetsAccount>>()

    // 默认查看全部类型
    val filterType by lazy { MutableLiveData<Int>(SHOW_TYPE_ALL) }

    // 默认不限金额
    val filterOrder by lazy { MutableLiveData<Int>(SORT_BY_DATE_DESC) }

    // 数据源
    val recordsByKeywordLiveData by lazy { MediatorLiveData<List<RecordWithCategoryBean>>() }
    private var mTempRecordLiveData: LiveData<List<RecordWithCategoryBean>>? = null

    private val currentBookId =
        DataStoreUtil.readFromDataStore(CURRENT_BOOK_ID, -1)


    fun searchRecord() {

    }

    fun getAllAssetsAccountAndBook() {
        viewModelScope.launch {
            val assetsAccountsRequest = async {
                repo.getAllAssetsAccount()
            }
            val bookRequest = async {
                repo.getAllBook()
            }
            val assetsAccounts = assetsAccountsRequest.await()
            val books = bookRequest.await()

            assetsAccounts.collect {

            }
        }
    }

    fun getRecordByKeyword(keyword: String) {
        if (mTempRecordLiveData != null) {
            recordsByKeywordLiveData.removeSource(mTempRecordLiveData!!)
        }

        viewModelScope.launch {
            currentBookId.take(1)
                .collect { id ->
                    mTempRecordLiveData = repo.getRecordByKeyword(keyword, id)
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