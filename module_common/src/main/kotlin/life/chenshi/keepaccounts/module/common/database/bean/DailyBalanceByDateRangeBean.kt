package life.chenshi.keepaccounts.module.common.database.bean

import java.math.BigDecimal

data class DailyBalanceByDateRangeBean(
    private val date: String,
    private val netAssets: BigDecimal,
) {

    fun getDate(): Int {
        return date.toInt()
    }

    fun getNetAssets(): BigDecimal {
        return netAssets
    }
}
