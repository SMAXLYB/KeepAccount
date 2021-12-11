package life.chenshi.keepaccounts.module.common.database.bean

import java.math.BigDecimal

class SumMoneyGroupByMajorCategoryBean(
    internal val majorCategory: String,
    private val count: Int,
    private val sumMoney: BigDecimal) {

    fun getCategory(): String {
        return majorCategory
    }

    fun getCount(): Int = count

    fun getMoney(): Float {
        return sumMoney.toFloat()
    }
}