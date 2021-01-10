package life.chenshi.keepaccounts.bean

import java.math.BigDecimal

class SumMoneyByDateAndTypeBean(private val day: String, private val sumMoney: BigDecimal) {

    fun getDay(): Float {
        return day.toFloat()
    }

    fun getMoney(): Float {
        return sumMoney.toFloat()
    }
}