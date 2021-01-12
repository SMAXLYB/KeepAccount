package life.chenshi.keepaccounts.bean

import java.math.BigDecimal

class SumMoneyGroupByDateBean(private val day: String, private val sumMoney: BigDecimal) {

    fun getDay(): Int {
        return day.toInt()
    }

    fun getMoney(): Float {
        return sumMoney.toFloat()
    }
}