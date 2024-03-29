package life.chenshi.keepaccounts.module.analysis.vm

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieEntry
import life.chenshi.keepaccounts.module.analysis.ui.AnalysisFragment
import life.chenshi.keepaccounts.module.common.constant.CURRENT_BOOK_ID
import life.chenshi.keepaccounts.module.common.constant.RECORD_TYPE_INCOME
import life.chenshi.keepaccounts.module.common.database.bean.SumMoneyGroupByDateBean
import life.chenshi.keepaccounts.module.common.database.bean.SumMoneyGroupByMajorCategoryBean
import life.chenshi.keepaccounts.module.common.utils.storage.DataStoreUtil

class AnalyzeViewModel : ViewModel() {
    // private val recordDAO by lazy { AppDatabase.getDatabase().getRecordDao() }

    // 当前按年查看还是按月查看
    val currentTypeLiveData = MutableLiveData<Int>(AnalysisFragment.TYPE_MONTH)

    // 当前查询的时间范围
    val queryDateLiveData by lazy { MutableLiveData<Long>(System.currentTimeMillis()) }

    // 占比图类型
    val proportionTypeLiveData by lazy { MutableLiveData<Int>(RECORD_TYPE_INCOME) }

    // 走势图显示类型
    val tendencyIncomeSelectedLiveData by lazy { MutableLiveData<Boolean>(true) }
    val tendencyOutcomeSelectedLiveData by lazy { MutableLiveData<Boolean>(false) }
    private val currentBookId = DataStoreUtil.readFromDataStore(CURRENT_BOOK_ID, -1)

    // 走势图收支数据
    val tendencyIncomeRecordsLiveData by lazy {
        MediatorLiveData<List<SumMoneyGroupByDateBean>>()
    }
    val tendencyOutcomeRecordsLiveData by lazy {
        MediatorLiveData<List<SumMoneyGroupByDateBean>>()
    }
    private var mTempTendencyIncomeLiveData: LiveData<List<SumMoneyGroupByDateBean>>? = null
    private var mTempTendencyOutcomeLiveData: LiveData<List<SumMoneyGroupByDateBean>>? = null

    // 饼图收支数据
    val proportionIncomeRecordsLiveData by lazy {
        MediatorLiveData<List<SumMoneyGroupByMajorCategoryBean>>()
    }
    val proportionOutcomeRecordsLiveData by lazy {
        MediatorLiveData<List<SumMoneyGroupByMajorCategoryBean>>()
    }
    private var mTempProportionIncomeLiveData: LiveData<List<SumMoneyGroupByMajorCategoryBean>>? = null
    private var mTempProportionOutcomeLiveData: LiveData<List<SumMoneyGroupByMajorCategoryBean>>? = null

    init {
        getTendencyRecords()
        getProportionRecords()
    }


    /**
     * 根据时间范围,收支类型查询记录, 按天/月分组
     */
    fun getTendencyRecords() {
        // viewModelScope.launch {
        //     var bookId: Int = -1
        //     currentBookId.take(1)
        //         .collect {
        //             bookId = it
        //         }
        //     // 按月查询
        //     if (currentTypeLiveData.value == AnalysisFragment.TYPE_MONTH) {
        //         val from = DateUtil.getMonthStart(queryDateLiveData.value)
        //         val to = DateUtil.getMonthEnd(queryDateLiveData.value)
        //
        //         // 查询收入
        //         if (mTempTendencyIncomeLiveData != null) {
        //             tendencyIncomeRecordsLiveData.removeSource(mTempTendencyIncomeLiveData!!)
        //         }
        //
        //         mTempTendencyIncomeLiveData =
        //             recordDAO.getSumMoneyGroupByDate(from, to, RECORD_TYPE_INCOME, bookId)
        //         tendencyIncomeRecordsLiveData.addSource(
        //             mTempTendencyIncomeLiveData!!
        //         ) {
        //             tendencyIncomeRecordsLiveData.value = it
        //         }
        //
        //         // 查询支出
        //         if (mTempTendencyOutcomeLiveData != null) {
        //             tendencyOutcomeRecordsLiveData.removeSource(mTempTendencyOutcomeLiveData!!)
        //         }
        //         mTempTendencyOutcomeLiveData =
        //             recordDAO.getSumMoneyGroupByDate(from, to, RECORD_TYPE_OUTCOME, bookId)
        //         tendencyOutcomeRecordsLiveData.addSource(
        //             mTempTendencyOutcomeLiveData!!
        //         ) {
        //             tendencyOutcomeRecordsLiveData.value = it
        //         }
        //
        //     } else {
        //         val from = DateUtil.getYearStart(queryDateLiveData.value)
        //         val to = DateUtil.getYearEnd(queryDateLiveData.value)
        //
        //         // 查询收入
        //         if (mTempTendencyIncomeLiveData != null) {
        //             tendencyIncomeRecordsLiveData.removeSource(mTempTendencyIncomeLiveData!!)
        //         }
        //         mTempTendencyIncomeLiveData =
        //             recordDAO.getSumMoneyGroupByMonth(from, to, RECORD_TYPE_INCOME, bookId)
        //         tendencyIncomeRecordsLiveData.addSource(
        //             mTempTendencyIncomeLiveData!!
        //         ) {
        //             tendencyIncomeRecordsLiveData.value = it
        //         }
        //
        //         // 查询支出
        //         if (mTempTendencyOutcomeLiveData != null) {
        //             tendencyOutcomeRecordsLiveData.removeSource(mTempTendencyOutcomeLiveData!!)
        //         }
        //         mTempTendencyOutcomeLiveData =
        //             recordDAO.getSumMoneyGroupByMonth(from, to, RECORD_TYPE_OUTCOME, bookId)
        //         tendencyOutcomeRecordsLiveData.addSource(
        //             mTempTendencyOutcomeLiveData!!
        //         ) {
        //             tendencyOutcomeRecordsLiveData.value = it
        //         }
        //     }
        // }
    }

    /**
     * 根据时间范围,收支类型查询记录, 按category分组
     */
    fun getProportionRecords() {
        // viewModelScope.launch {
        //     var bookId = -1
        //     currentBookId.take(1)
        //         .collect {
        //             bookId = it
        //         }
        //
        //     if (currentTypeLiveData.value == AnalysisFragment.TYPE_MONTH) {
        //         val from = DateUtil.getMonthStart(queryDateLiveData.value)
        //         val to = DateUtil.getMonthEnd(queryDateLiveData.value)
        //
        //         if (mTempProportionIncomeLiveData != null) {
        //             proportionIncomeRecordsLiveData.removeSource(mTempProportionIncomeLiveData!!)
        //         }
        //         mTempProportionIncomeLiveData =
        //             recordDAO.getSumMoneyGroupByCategory(from, to, RECORD_TYPE_INCOME, bookId)
        //         proportionIncomeRecordsLiveData.addSource(
        //             mTempProportionIncomeLiveData!!
        //         ) {
        //             proportionIncomeRecordsLiveData.value = it
        //         }
        //
        //         if (mTempProportionOutcomeLiveData != null) {
        //             proportionOutcomeRecordsLiveData.removeSource(mTempProportionOutcomeLiveData!!)
        //         }
        //         mTempProportionOutcomeLiveData =
        //             recordDAO.getSumMoneyGroupByCategory(from, to, RECORD_TYPE_OUTCOME, bookId)
        //         proportionOutcomeRecordsLiveData.addSource(
        //             mTempProportionOutcomeLiveData!!
        //         ) {
        //             proportionOutcomeRecordsLiveData.value = it
        //         }
        //
        //     } else {
        //         val from = DateUtil.getYearStart(queryDateLiveData.value)
        //         val to = DateUtil.getYearEnd(queryDateLiveData.value)
        //
        //         if (mTempProportionIncomeLiveData != null) {
        //             proportionIncomeRecordsLiveData.removeSource(mTempProportionIncomeLiveData!!)
        //         }
        //         mTempProportionIncomeLiveData =
        //             recordDAO.getSumMoneyGroupByCategory(from, to, RECORD_TYPE_INCOME, bookId)
        //         proportionIncomeRecordsLiveData.addSource(
        //             mTempProportionIncomeLiveData!!
        //         ) {
        //             proportionIncomeRecordsLiveData.value = it
        //         }
        //
        //         if (mTempProportionOutcomeLiveData != null) {
        //             proportionOutcomeRecordsLiveData.removeSource(mTempProportionOutcomeLiveData!!)
        //         }
        //         mTempProportionOutcomeLiveData =
        //             recordDAO.getSumMoneyGroupByCategory(from, to, RECORD_TYPE_OUTCOME, bookId)
        //         proportionOutcomeRecordsLiveData.addSource(
        //             mTempProportionOutcomeLiveData!!
        //         ) {
        //             proportionOutcomeRecordsLiveData.value = it
        //         }
        //     }
        // }

    }


    /**
     * 生成LineDataSet
     * @param entries 数据
     * @param label 线段标签
     * @param color 线段颜色
     */
    fun generateLineDataSet(entries: List<Entry>, label: String, color: String): LineDataSet {
        return LineDataSet(entries, label).apply {
            axisDependency = YAxis.AxisDependency.LEFT // 依赖左轴
            setDrawFilled(true) //填充颜色
            fillDrawable = GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM, intArrayOf(
                    Color.parseColor("#5f$color"), Color.parseColor("#00ffffff")
                )
            )
            setDrawValues(false) //不绘制值
            setColor(Color.parseColor("#$color"), 255) //线段颜色
            mode = LineDataSet.Mode.LINEAR// 线段模式
            setCircleColor(Color.parseColor("#$color")) // 圆点颜色
        }
    }

    fun generateEmptyEntriesOfMonth(daysInMonth: Int): MutableList<Entry> {
        val entries: MutableList<Entry> = mutableListOf()
        for (i in 1..daysInMonth) {
            entries.add(Entry(i.toFloat(), 0.0f))
        }
        return entries
    }

    fun generateEmptyEntriesOfYear(): MutableList<Entry> {
        val entries: MutableList<Entry> = mutableListOf()
        for (i in 1..12) {
            entries.add(Entry(i.toFloat(), 0.0f))
        }
        return entries
    }

    fun generateEmptyEntriesForPieChart(): MutableList<PieEntry> {
        return mutableListOf(PieEntry(0f, "暂无数据"))
    }
}