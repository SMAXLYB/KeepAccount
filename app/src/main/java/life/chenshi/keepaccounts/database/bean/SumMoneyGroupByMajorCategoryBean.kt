package life.chenshi.keepaccounts.database.bean

import java.math.BigDecimal

class SumMoneyGroupByMajorCategoryBean(
    internal val majorCategory: Int,
    private val count: Int,
    private val sumMoney: BigDecimal) {

    fun getCategory(): String {
        return "测试"
    }

    fun getCount(): Int = count

    fun getMoney(): Float {
        return sumMoney.toFloat()
    }
}