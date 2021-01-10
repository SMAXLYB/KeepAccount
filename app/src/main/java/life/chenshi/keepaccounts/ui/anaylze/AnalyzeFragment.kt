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

        val outcomeEntries: List<Entry> = listOf(
            Entry(1.0f, 3000.51f),
            Entry(2.0f, 2500.0f),
            Entry(3.0f, 1800.0f),
            Entry(4.0f, 2000.0f),
            Entry(5.0f, 1951.0f),
            Entry(6.0f, 1545.12f),
            Entry(7.0f, 3121.0f),
            Entry(8.0f, 2214.0f),
            Entry(9.0f, 0.0f),
            Entry(10.0f, 1942.01f),
            Entry(11.0f, 1345.0f),
            Entry(12.0f, 2454.0f)
        )

        val incomeEntries: List<Entry> = listOf(
            Entry(1.0f, 3541.11f),
            Entry(2.0f, 2541.0f),
            Entry(3.0f, 3122.0f),
            Entry(4.0f, 2451.0f),
            Entry(5.0f, 1557.0f),
            Entry(6.0f, 1687.0f),
            Entry(7.0f, 2757.33f),
            Entry(8.0f, 2154.0f),
            Entry(10.0f, 2854.0f),
            Entry(11.0f, 2554.0f),
            Entry(12.0f, 1878.0f)
        )


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
            // labelCount = 6 // 标签个数
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
            generateEmptyEntriesOfMonth(DateUtil.getDaysInMonth(mAnalyzeViewModel.queryDateLiveData.value!!)),
            "收入",
            "8bc34a"
        )
        val outcomeLineDataSet =
            mAnalyzeViewModel.generateLineDataSet(generateEmptyEntriesOfMonth(DateUtil.getDaysInMonth(mAnalyzeViewModel.queryDateLiveData.value!!)), "支出", "E91E63")

        // val incomeLineDataSet = mAnalyzeViewModel.generateLineDataSet(mutableListOf(),"收入","8bc34a")
        // val outcomeLineDataSet = mAnalyzeViewModel.generateLineDataSet(mutableListOf(),"支出","E91E63")
        mBinding.lineChart.apply {
            data = LineData(listOf(incomeLineDataSet, outcomeLineDataSet)) //填充数据
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

            // 当前查看日期
            queryDateLiveData.observe(viewLifecycleOwner) {
                val date = Date(it)
                if (mAnalyzeViewModel.currentTypeLiveData.value == TYPE_YEAR) {
                    mBinding.analyzeDate.text = DateUtil.date2String(date, DateUtil.YEAR_FORMAT)
                } else {
                    mBinding.analyzeDate.text =
                        DateUtil.date2String(date, DateUtil.YEAR_MONTH_FORMAT)
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
            }
            tendencyOutcomeSelectedLiveData.observe(viewLifecycleOwner) {
                mBinding.analyzeTendencyOutcome.setSelect(it)
            }
            tempTendencyIncomeLiveData.observe(viewLifecycleOwner) {
                mAnalyzeViewModel.tendencyIncomeRecordsLiveData.value = it
            }
            tempTendencyOutcomeLiveData.observe(viewLifecycleOwner) {
                mAnalyzeViewModel.tendencyOutcomeRecordsLiveData.value = it
            }
            tendencyIncomeRecordsLiveData.observe(viewLifecycleOwner) { beans ->
                if (beans.isEmpty()) {

                } else {

                }
                stopRefreshing()
            }
            tendencyOutcomeRecordsLiveData.observe(viewLifecycleOwner) { beans ->
                if (beans.isEmpty()) {

                } else {
                    // todo 还要判断年视图还是月视图
                    /* val outcomeLineDataSet = mBinding.lineChart.lineData.getDataSetByIndex(1)
                     outcomeLineDataSet.clear()
                     // beans.forEach {
                     //     val entry = Entry(it.getDay(),it.getMoney())
                     //     outcomeLineDataSet.addEntry(entry)
                     // }
                     // mBinding.lineChart.xAxis.valueFormatter = DayValueFormatter()
                     mBinding.lineChart.notifyDataSetChanged()
                     mBinding.lineChart.invalidate()*/
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

    private fun generateEmptyEntriesOfMonth(daysInMonth: Int): List<Entry> {
        val entries: MutableList<Entry> = mutableListOf()
        for (i in 1..12) {
            entries.add(Entry(i.toFloat(), 0.0f))
        }
        return entries
    }

    private fun generateEmptyEntriesOfYear(): List<Entry> {
        val entries: MutableList<Entry> = mutableListOf()
        for (i in 1..12) {
            entries.add(Entry(i.toFloat(), 0.0f))
        }
        return entries
    }
}