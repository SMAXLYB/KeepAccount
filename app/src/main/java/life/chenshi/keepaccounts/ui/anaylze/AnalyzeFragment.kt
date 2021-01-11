package life.chenshi.keepaccounts.ui.anaylze

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.loper7.date_time_picker.DateTimeConfig
import com.loper7.date_time_picker.dialog.CardDatePickerDialog
import life.chenshi.keepaccounts.R
import life.chenshi.keepaccounts.database.RecordType
import life.chenshi.keepaccounts.databinding.FragmentAnaylzeBinding
import life.chenshi.keepaccounts.utils.*
import java.util.*


class AnalyzeFragment : Fragment() {

    private lateinit var mBinding: FragmentAnaylzeBinding
    private val mAnalyzeViewModel by activityViewModels<AnalyzeViewModel>()

    companion object {
        const val TYPE_YEAR = 0
        const val TYPE_MONTH = 1

        const val TYPE_QUERY_INCOME = 10
        const val TYPE_QUERY_OUTCOME = 11
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate<FragmentAnaylzeBinding>(
            inflater,
            R.layout.fragment_anaylze,
            container,
            false
        )
        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initView()
        initListener()
        initObserver()

        val month =
            arrayOf("1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月")


        /* // 饼图
         mBinding.pieChart.apply {
             setUsePercentValues(true) // 百分比模式
             description.isEnabled = false
         }

         val pieChartEntries: List<PieEntry> = listOf(
             PieEntry(8.1f, "购物"),
             PieEntry(20f, "吃饭"),
             PieEntry(12f, "住房"),
             PieEntry(30f, "出行"),
             PieEntry(5f, "通讯"),
             PieEntry(25f, "其他")
         )

         val pieDataSet = PieDataSet(pieChartEntries, "暂无数据")
             .apply {
                 sliceSpace = 1f //间隔
                 yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE // 描述文字在外
                 setColors(
                     Color.parseColor("#FFCC00"),
                     Color.parseColor("#99CCFF"),
                     Color.parseColor("#ffc6ff"),
                     Color.parseColor("#FF9999"),
                     Color.parseColor("#CCCCFF"),
                     Color.parseColor("#aacc00")
                 )
             }

         val pieData = PieData(pieDataSet).apply {
             setValueFormatter(PercentFormatter(mBinding.pieChart))
             setDrawValues(true)
             setValueTextSize(12f)
         }

         mBinding.pieChart.apply {
             setUsePercentValues(true) // 使用百分比
             setNoDataText("暂无数据")
             holeRadius = 45f //空心半径
             transparentCircleRadius = 50f // 透明圆
             // setTouchEnabled(false) // 取消触摸
             isDragDecelerationEnabled = false
             centerText = "暂无数据"
             setExtraOffsets(0f, 5f, 30f, 5f)
             setDrawEntryLabels(false) // 隐藏label
             setBackgroundColor(Color.parseColor("#ffffff"))
             legend.apply {
                 horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
                 verticalAlignment = Legend.LegendVerticalAlignment.TOP
                 orientation = Legend.LegendOrientation.VERTICAL
             }
             data = pieData
             invalidate()
         }

         mBinding.lvAnalyzeProportionDetail.adapter = AnalyzeProportionAdapter(month.toList())*/
    }

    private fun initView() {
        // 线段图固定设置
        mBinding.lineChart.apply {
            setScaleEnabled(false) //禁止缩放
            description.isEnabled = false // 去除说明
            setBackgroundColor(Color.parseColor("#ffffff"))
            setGridBackgroundColor(Color.parseColor("#ffffff"))
            legend.apply {
                form = Legend.LegendForm.CIRCLE
                verticalAlignment = Legend.LegendVerticalAlignment.TOP
                horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER //图例标识位置
            }
            marker = MyMarkerView(requireActivity(), R.layout.layout_anaylze_marker)
            setNoDataText("暂无数据")

        }
        // 获取右轴,设置不可见
        val rightAxis = mBinding.lineChart.axisRight.apply {
            isEnabled = false
        }
        // X轴固定设置
        val xAxis = mBinding.lineChart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM // 设置位置
            setDrawGridLines(false) // 取消网格线
            setAvoidFirstLastClipping(true)
            axisMinimum = 1.0f // 起点
            // setLabelCount(6, true) // 坐标标签个数
        }
        // 获取左轴(Y轴)
        val yAxis = mBinding.lineChart.axisLeft.apply {
            setDrawZeroLine(true) //绘制0线
            setDrawAxisLine(false) //不绘制网格
            enableGridDashedLine(10f, 5f, 1f) // 虚线
            axisMinimum = 0.0f // 设置起始值
            // setLabelCount(6,true)
            // spaceTop = 20f
        }

        // 可变设置
        yAxis.apply {
            // 设置限制线
            // addLimitLine(LimitLine(2000.00f, "月预算").apply {
            //     lineColor = Color.parseColor("#DC143C")
            //     textColor = Color.parseColor("#DC143C")
            // })
            // valueFormatter = object : ValueFormatter() {
            //     override fun getFormattedValue(value: Float): String {
            //         return "${value.div(1000)}k"
            //     }
            // }
        }

        val incomeLineDataSet = mAnalyzeViewModel.generateLineDataSet(
            mAnalyzeViewModel.generateEmptyEntriesOfMonth(
                DateUtil.getDaysInMonth(mAnalyzeViewModel.queryDateLiveData.value!!)
            ),
            "收入",
            "8bc34a"
        )
        val outcomeLineDataSet = mAnalyzeViewModel.generateLineDataSet(
            mAnalyzeViewModel.generateEmptyEntriesOfMonth(
                DateUtil.getDaysInMonth(mAnalyzeViewModel.queryDateLiveData.value!!)
            ),
            "支出",
            "E91E63"
        )

        mBinding.lineChart.apply {
            data = LineData(listOf(outcomeLineDataSet, incomeLineDataSet)) //填充数据
            invalidate() //刷新
        }
    }

    private fun initListener() {
        // 按年查看
        mBinding.analyzeTypeYear.setOnClickListener {
            mAnalyzeViewModel.currentTypeLiveData.value = TYPE_YEAR
        }
        // 按月查看
        mBinding.analyzeTypeMonth.setOnClickListener {
            mAnalyzeViewModel.currentTypeLiveData.value = TYPE_MONTH
        }

        // 时间
        mBinding.analyzeDate.setOnClickListener {
            activity?.let { activity ->
                CardDatePickerDialog.builder(activity)
                    .setMaxTime(System.currentTimeMillis())
                    .showBackNow(false)
                    .setDisplayType(
                        mutableListOf(
                            DateTimeConfig.YEAR,
                            DateTimeConfig.MONTH
                        )
                    )
                    // .setDefaultTime(mAnalyzeViewModel.queryDateLiveData.value!!)
                    .setThemeColor(Color.parseColor("#03A9F4"))
                    .setLabelText(year = "年", month = "月")
                    .setOnChoose { millisecond ->
                        mAnalyzeViewModel.queryDateLiveData.value = millisecond
                    }
                    .build()
                    .show()
            }
        }

        // 占比
        mBinding.analyzeProportionIncome.setOnClickListener {
            mAnalyzeViewModel.proportionTypeLiveData.value = TYPE_QUERY_INCOME
        }
        mBinding.analyzeProportionOutcome.setOnClickListener {
            mAnalyzeViewModel.proportionTypeLiveData.value = TYPE_QUERY_OUTCOME
        }

        // 走势
        mBinding.analyzeTendencyIncome.setOnClickListener {
            if (!mAnalyzeViewModel.tendencyOutcomeSelectedLiveData.value!!) {
                ToastUtil.showShort("至少选择一项")
                return@setOnClickListener
            }
            mAnalyzeViewModel.tendencyIncomeSelectedLiveData.apply {
                this.value = !this.value!!
            }
        }
        mBinding.analyzeTendencyOutcome.setOnClickListener {
            if (!mAnalyzeViewModel.tendencyIncomeSelectedLiveData.value!!) {
                ToastUtil.showShort("至少选择一项")
                return@setOnClickListener
            }
            mAnalyzeViewModel.tendencyOutcomeSelectedLiveData.apply {
                this.value = !this.value!!
            }
        }

        // 下拉刷新
        mBinding.srlAnalyzeRefresh.setOnRefreshListener {

        }
    }

    private fun initObserver() {
        mAnalyzeViewModel.apply {

            // 当前按年查看还是按月查看
            currentTypeLiveData.observe(viewLifecycleOwner) {
                val date = Date(mAnalyzeViewModel.queryDateLiveData.value!!)

                if (it == TYPE_YEAR) {
                    mBinding.analyzeTypeYear.setEnable(false)
                    mBinding.analyzeTypeMonth.setEnable(true)
                    mBinding.analyzeDate.text = DateUtil.date2String(date, DateUtil.YEAR_FORMAT)
                } else {
                    mBinding.analyzeTypeYear.setEnable(true)
                    mBinding.analyzeTypeMonth.setEnable(false)
                    mBinding.analyzeDate.text =
                        DateUtil.date2String(date, DateUtil.YEAR_MONTH_FORMAT)
                }
            }

            // 当前查看日期发生变化:新建fragment/选择时间
            queryDateLiveData.observe(viewLifecycleOwner) {
                mBinding.srlAnalyzeRefresh.isRefreshing = true
                // 改变文字
                val date = Date(it)
                if (mAnalyzeViewModel.currentTypeLiveData.value == TYPE_YEAR) {
                    mBinding.analyzeDate.text = DateUtil.date2String(date, DateUtil.YEAR_FORMAT)
                } else {
                    mBinding.analyzeDate.text =
                        DateUtil.date2String(date, DateUtil.YEAR_MONTH_FORMAT)
                }

                // 重新查询数据库
                mAnalyzeViewModel.apply {
                    val monthStart = DateUtil.getMonthStart(queryDateLiveData.value!!)
                    val monthEnd = DateUtil.getMonthEnd(queryDateLiveData.value!!)
                    getTendencyRecords(
                        monthStart, monthEnd, RecordType.INCOME
                    )
                    getTendencyRecords(
                        monthEnd, monthEnd, RecordType.OUTCOME
                    )
                }
            }

            // 占比类型
            proportionTypeLiveData.observe(viewLifecycleOwner) {
                if (it == TYPE_QUERY_INCOME) {
                    mBinding.analyzeProportionIncome.setEnableAndSelect(true)
                    mBinding.analyzeProportionOutcome.setEnableAndSelect(false)
                } else if (it == TYPE_QUERY_OUTCOME) {
                    mBinding.analyzeProportionIncome.setEnableAndSelect(false)
                    mBinding.analyzeProportionOutcome.setEnableAndSelect(true)
                }
            }

            // 走势类型
            tendencyIncomeSelectedLiveData.observe(viewLifecycleOwner) {
                mBinding.analyzeTendencyIncome.setSelect(it)
                if (it) {
                    mAnalyzeViewModel.tendencyIncomeRecordsLiveData.apply {
                        value = value
                    }
                } else {
                    clearLineChartDataSet(RecordType.INCOME)
                    notifyLineChartRedraw()
                }
            }
            tendencyOutcomeSelectedLiveData.observe(viewLifecycleOwner) {
                mBinding.analyzeTendencyOutcome.setSelect(it)
                if (it) {
                    mAnalyzeViewModel.tendencyOutcomeRecordsLiveData.apply {
                        value = value
                    }
                } else {
                    clearLineChartDataSet(RecordType.OUTCOME)
                    notifyLineChartRedraw()
                }
            }

            // 当数据记录发送改变时
            tendencyIncomeRecordsLiveData.observe(viewLifecycleOwner) { beans ->
                if (beans == null || beans.isEmpty()) {
                    ToastUtil.showShort("数据为空")
                } else {
                    if (mAnalyzeViewModel.tendencyIncomeSelectedLiveData.value!!) {
                        // todo 还要判断年视图还是月视图
                        val incomeLineDataSet =
                            mBinding.lineChart.lineData.getDataSetByIndex(RecordType.INCOME)
                        incomeLineDataSet.clear()
                        val entries = mAnalyzeViewModel.generateEmptyEntriesOfMonth(
                            DateUtil.getDaysInMonth(mAnalyzeViewModel.queryDateLiveData.value!!)
                        )
                        beans.forEach {
                            entries[it.getDay() - 1].y = it.getMoney()
                        }
                        entries.forEach {
                            incomeLineDataSet.addEntry(it)
                        }
                        mBinding.lineChart.xAxis.valueFormatter = DayValueFormatter()
                        notifyLineChartRedraw()
                    }
                }
                stopRefreshing()
            }
            tendencyOutcomeRecordsLiveData.observe(viewLifecycleOwner) { beans ->
                if (beans == null || beans.isEmpty()) {
                    ToastUtil.showShort("数据为空")
                } else {
                    if (mAnalyzeViewModel.tendencyOutcomeSelectedLiveData.value!!) {
                        val outcomeLineDataSet =
                            mBinding.lineChart.lineData.getDataSetByIndex(RecordType.OUTCOME)
                        outcomeLineDataSet.clear()
                        val entries = mAnalyzeViewModel.generateEmptyEntriesOfMonth(
                            DateUtil.getDaysInMonth(mAnalyzeViewModel.queryDateLiveData.value!!)
                        )
                        beans.forEach {
                            entries[it.getDay() - 1].y = it.getMoney()
                        }
                        entries.forEach {
                            outcomeLineDataSet.addEntry(it)
                        }
                        mBinding.lineChart.xAxis.valueFormatter = DayValueFormatter()
                        notifyLineChartRedraw()
                    }

                }
                stopRefreshing()
            }
        }
    }

    private fun stopRefreshing() {
        if (mBinding.srlAnalyzeRefresh.isRefreshing) {
            mBinding.srlAnalyzeRefresh.isRefreshing = false
        }
    }

    private fun notifyLineChartRedraw() {
        mBinding.lineChart.lineData.notifyDataChanged()
        mBinding.lineChart.notifyDataSetChanged()
        mBinding.lineChart.invalidate()
    }

    private fun clearLineChartDataSet(recordType: Int) {
        mBinding.lineChart.lineData.getDataSetByIndex(recordType).clear()
    }

}