package life.chenshi.keepaccounts.bean

import life.chenshi.keepaccounts.database.entity.Category
import java.math.BigDecimal

class SumMoneyGroupByCategoryBean(
        private val category: Int,
        private val count: Int,
        private val sumMoney: BigDecimal) {

    fun getCategory(): String {
        return Category.convert2String(category)
    }

    fun getCount(): Int = count

    fun getMoney(): Float {
        return sumMoney.toFloat()
    }
}