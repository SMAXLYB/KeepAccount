package life.chenshi.keepaccounts.module.common

import life.chenshi.keepaccounts.module.common.utils.DateUtil
import org.junit.Test

class TestDate {

    @Test
    fun testNumBetweenTwoDate() {
        println(DateUtil.getDaysBetween(1650440308991 , 1650440307991))
    }
}