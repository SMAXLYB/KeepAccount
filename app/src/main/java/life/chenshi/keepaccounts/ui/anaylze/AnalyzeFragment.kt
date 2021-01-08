package life.chenshi.keepaccounts.ui.anaylze

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.components.*
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import life.chenshi.keepaccounts.R
import life.chenshi.keepaccounts.databinding.FragmentAnaylzeBinding

class AnalyzeFragment : Fragment() {
    private lateinit var mBinding: FragmentAnaylzeBinding
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

        val outcomeEntries: List<Entry> = listOf(
                Entry(1.0f, 6.0f),
                Entry(2.0f, 8.0f),
                Entry(3.0f, 7.0f),
                Entry(4.0f, 6.0f),
                Entry(5.0f, 5.0f),
                Entry(6.0f, 4.0f),
                Entry(7.0f, 3.0f),
                Entry(8.0f, 2.0f),
                Entry(9.0f, 6.0f),
                Entry(10.0f,0.0f),
                Entry(11.0f,0.0f),
                Entry(12.0f,0.0f)
        )

        val incomeEntries: List<Entry> = listOf(
                Entry(1.0f, 2.0f),
                Entry(2.0f, 2.0f),
                Entry(3.0f, 3.0f),
                Entry(4.0f, 4.0f),
                Entry(5.0f, 5.0f),
                Entry(6.0f, 6.0f),
                Entry(7.0f, 7.0f),
                Entry(8.0f, 8.0f),
                Entry(9.0f, 5.0f),
                Entry(10.0f, 8.0f),
                Entry(11.0f, 5.0f),
                Entry(12.0f, 8.0f)
        )

        val month = arrayOf("1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月")

        // 获取X轴
        val xAxis = mBinding.lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM // 设置位置
        // xAxis.setDrawGridLines(false) // 取消网格线
        xAxis.axisMinimum = 1.0f
        xAxis.setLabelCount(14, true)
        xAxis.setAvoidFirstLastClipping(true)
        // 格式化文字
        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return month[value.toInt() - 1]
            }
        }

        // 获取左轴(Y轴)
        val yAxis = mBinding.lineChart.axisLeft
        yAxis.addLimitLine(LimitLine(5.0f, "月预算").apply { lineColor = Color.parseColor("#515151") }) // 设置限制线
        yAxis.axisMinimum = 0.0f // 设置起始值
        yAxis.setDrawLabels(false)
        yAxis.setDrawAxisLine(false)
        yAxis.setDrawGridLines(false)
        // yAxis.enableGridDashedLine(20f, 10f, 1f) // 虚线

        // 获取右轴,设置不可见
        val rightAxis = mBinding.lineChart.axisRight.apply { isEnabled = false }

        // 设置数据
        val incomeLineDataSet = LineDataSet(incomeEntries, "收入").apply { axisDependency = YAxis.AxisDependency.LEFT }
        incomeLineDataSet.setColor(Color.parseColor("#8bc34a"), 255)
        incomeLineDataSet.setCircleColor(Color.parseColor("#8bc34a"))
        incomeLineDataSet.valueTextColor = Color.parseColor("#8bc34a") // 设置点上文字颜色
        incomeLineDataSet.valueTextSize = 10.0f
        incomeLineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER

        val outcomeLineDataSet = LineDataSet(outcomeEntries, "支出").apply { axisDependency = YAxis.AxisDependency.LEFT }
        outcomeLineDataSet.setColor(Color.parseColor("#E91E63"), 255)
        outcomeLineDataSet.setCircleColor(Color.parseColor("#E91E63"))
        outcomeLineDataSet.valueTextColor = Color.parseColor("#E91E63") // 设置点上文字颜色
        outcomeLineDataSet.valueTextSize = 10.0f
        outcomeLineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER

        val lineDataSet = listOf(incomeLineDataSet, outcomeLineDataSet)

        val lineData = LineData(lineDataSet)
        mBinding.lineChart.data = lineData
        mBinding.lineChart.setBackgroundColor(Color.parseColor("#ffffff"))
        mBinding.lineChart.setGridBackgroundColor(Color.parseColor("#ffffff"))
        mBinding.lineChart.legend.apply {
            form = Legend.LegendForm.CIRCLE
            horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER //图例标识位置
        }
        mBinding.lineChart.description = Description().apply {
            text= "注:金额单位为千"
        }
        mBinding.lineChart.setScaleEnabled(false) //禁止缩放
        mBinding.lineChart.invalidate()

        // val chartModel = AAChartModel()
        //         .chartType(AAChartType.Line)
        //         .subtitle("2020年收支情况")
        //         .categories(arrayOf("1月","2月","3月","4月","5月","6月","7月","8月"))
        //         .dataLabelsEnabled(true)
        //         .series(
        //                 arrayOf(
        //                         AASeriesElement()
        //                                 .name("收入")
        //                                 .data(arrayOf(2.0,5.0,6.0,4.0,7.0,8.0,9.0,5.0)),
        //                         AASeriesElement()
        //                                 .name("支出")
        //                                 .data(arrayOf(8.0,5.0,6.0,5.0,4.0,5.0,6.0,3.0))
        //                 )
        //         ).colo
        // mBinding.chartView.aa_drawChartWithChartModel(chartModel)
    }
}