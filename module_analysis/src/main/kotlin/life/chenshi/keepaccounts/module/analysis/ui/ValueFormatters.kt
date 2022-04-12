package life.chenshi.keepaccounts.module.analysis.ui

import com.github.mikephil.charting.formatter.ValueFormatter

class MonthValueFormatter: ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        return "${value.toInt()}月"
    }
}

class DayValueFormatter: ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        return "${value.toInt()}日"
    }
}