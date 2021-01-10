package life.chenshi.keepaccounts.ui.anaylze

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