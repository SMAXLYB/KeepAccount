package life.chenshi.keepaccounts.module.common.database

import androidx.room.TypeConverter
import java.math.BigDecimal
import java.util.*

/**
 * 数据类型转换
 */
class Converters {

    // 时间戳转日期 取出
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it * 1000L) }
    }

    // 日期转时间戳 存入
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.div(1000)
    }

    // 取出，精确到后2位， 相当于除了100
    @TypeConverter
    fun fromLong(value: Long?): BigDecimal? {
        return value?.let { BigDecimal(it).movePointLeft(2) }
    }

    // 存入 sqlite3不支持BigDecimal, 需要手动转换, 为了方便sum计算, 存的类型是long
    @TypeConverter
    fun bigDecimalToLong(bigDecimal: BigDecimal?): Long? {
        return bigDecimal?.multiply(BigDecimal(100))?.toLong()
    }
}