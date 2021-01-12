package life.chenshi.keepaccounts.ui.anaylze

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
import life.chenshi.keepaccounts.bean.SumMoneyGroupByCategory
import life.chenshi.keepaccounts.bean.SumMoneyGroupByDateBean
import life.chenshi.keepaccounts.database.RecordDatabase
import life.chenshi.keepaccounts.database.RecordType
import life.chenshi.keepaccounts.utils.DateUtil
import java.util.*

class AnalyzeViewModel : ViewModel() {
    private val recordDAO by lazy { RecordDatabase.getDatabase().getRecordDao() }

    // 当前按年查看还是按月查看
    val currentTypeLiveData = MutableLiveData<Int>(AnalyzeFragment.TYPE_YEAR)

    // 当前查询的时间范围
    val queryDateLiveData by lazy { MutableLiveData<Long>(System.currentTimeMillis()) }

    // 占比图类型
    val proportionTypeLiveData by lazy { MutableLiveData<Int>(RecordType.INCOME) }

    // 走势图显示类型
    val tendencyIncomeSelectedLiveData by lazy { MutableLiveData<Boolean>(true) }
    val tendencyOutcomeSelectedLiveData by lazy { MutableLiveData<Boolean>(false) }

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
        MediatorLiveData<List<SumMoneyGroupByCategory>>()
    }
    val proportionOutcomeRecordsLiveData by lazy {
        MediatorLiveData<List<SumMoneyGroupByCategory>>()
    }
    private var mTempProportionIncomeLiveData: LiveData<List<SumMoneyGroupByCategory>>? = null
    private var mTempProportionOutcomeLiveData: LiveData<List<SumMoneyGroupByCategory>>? = null

    init {
        val currentStartDate = DateUtil.getCurrentMonthStart()
        val currentEndDate = DateUtil.getCurrentMonthEnd()
        getTendencyRecords(currentStartDate, currentEndDate, RecordType.OUTCOME)
        getTendencyRecords(currentStartDate, currentEndDate, RecordType.INCOME)
        getProportionRecords(currentStartDate, currentEndDate, RecordType.INCOME)
        getProportionRecords(currentStartDate, currentEndDate, RecordType.OUTCOME)
    }


    /**
     * 根据时间范围,收支类型查询记录, 按天分组
     */
    fun getTendencyRecords(
        from: Date,
        to: Date,
        type: Int
    ) {
        if (type == RecordType.INCOME) {
            if (mTempTendencyIncomeLiveData != null) {
                tendencyIncomeRecordsLiveData.removeSource(mTempTendencyIncomeLiveData!!)
            }
            mTempTendencyIncomeLiveData = recordDAO.getSumMoneyGroupByDate(from, to, type)
            tendencyIncomeRecordsLiveData.addSource(
                mTempTendencyIncomeLiveData!!
            ) {
                tendencyIncomeRecordsLiveData.value = it
            }
        } else {
            if (mTempTendencyOutcomeLiveData != null) {
                tendencyOutcomeRecordsLiveData.removeSource(mTempTendencyOutcomeLiveData!!)
            }
            mTempTendencyOutcomeLiveData = recordDAO.getSumMoneyGroupByDate(from, to, type)
            tendencyOutcomeRecordsLiveData.addSource(
                mTempTendencyOutcomeLiveData!!
            ) {
                tendencyOutcomeRecordsLiveData.value = it
            }
        }
    }

    /**
     * 根据时间范围,收支类型查询记录, 按category分组
     */
    fun getProportionRecords(
        from: Date,
        to: Date,
        type: Int
    ) {
        if (type == RecordType.INCOME) {
            if (mTempProportionIncomeLiveData != null) {
                proportionIncomeRecordsLiveData.removeSource(mTempProportionIncomeLiveData!!)
            }
            mTempProportionIncomeLiveData = recordDAO.getSumMoneyGroupByCategory(from, to, type)
            proportionIncomeRecordsLiveData.addSource(
                mTempProportionIncomeLiveData!!
            ) {
                proportionIncomeRecordsLiveData.value = it
            }
        } else {
            if (mTempProportionOutcomeLiveData != null) {
                proportionOutcomeRecordsLiveData.removeSource(mTempProportionOutcomeLiveData!!)
            }
            mTempProportionOutcomeLiveData = recordDAO.getSumMoneyGroupByCategory(from, to, type)
            proportionOutcomeRecordsLiveData.addSource(
                mTempProportionOutcomeLiveData!!
            ) {
                proportionOutcomeRecordsLiveData.value = it
            }
        }
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