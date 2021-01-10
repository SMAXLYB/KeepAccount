package life.chenshi.keepaccounts.ui.anaylze

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import life.chenshi.keepaccounts.bean.SumMoneyByDateAndTypeBean
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
    val proportionTypeLiveData by lazy { MutableLiveData<Int>(AnalyzeFragment.TYPE_QUERY_INCOME) }

    // 走势图类型
    val tendencyIncomeSelectedLiveData by lazy { MutableLiveData<Boolean>(true) }
    val tendencyOutcomeSelectedLiveData by lazy { MutableLiveData<Boolean>(false) }

    // 走势图收支数据
    val tendencyIncomeRecordsLiveData by lazy {
        MutableLiveData<List<SumMoneyByDateAndTypeBean>>(emptyList())
    }
    val tendencyOutcomeRecordsLiveData by lazy {
        MutableLiveData<List<SumMoneyByDateAndTypeBean>>(emptyList())
    }

    // 解决延时查询问题
    lateinit var tempTendencyIncomeLiveData: LiveData<List<SumMoneyByDateAndTypeBean>>
    lateinit var tempTendencyOutcomeLiveData: LiveData<List<SumMoneyByDateAndTypeBean>>

    init {
        val currentStartDate = DateUtil.getCurrentMonthStart()
        val currentEndDate = DateUtil.getCurrentMonthEnd()
        getTendencyRecords(currentStartDate, currentEndDate, RecordType.OUTCOME)
        getTendencyRecords(currentStartDate, currentEndDate, RecordType.INCOME)
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
            tempTendencyIncomeLiveData = recordDAO.getSumMoneyByDateAndType(from, to, type)
        } else {
            tempTendencyOutcomeLiveData = recordDAO.getSumMoneyByDateAndType(from, to, type)
        }
    }

    /**
     * 生成LineDataSet
     * @param entries 数据
     * @param label 线段标签
     * @param colorId 线段颜色
     */
    //Color.parseColor("#5f8bc34a")  Color.parseColor("#5fE91E63") 半透明
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
            mode = LineDataSet.Mode.CUBIC_BEZIER // 线段模式
            setCircleColor(Color.parseColor("#$color")) // 圆点颜色
        }
    }
}