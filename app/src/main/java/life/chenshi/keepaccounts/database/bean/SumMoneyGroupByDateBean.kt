package life.chenshi.keepaccounts.database.bean

import java.math.BigDecimal

class SumMoneyGroupByDateBean(private val date: String, private val sumMoney: BigDecimal) {

    fun getDate(): Int {
        return date.toInt()
    }

    fun getMoney(): Float {
        return sumMoney.toFloat()
    }
}