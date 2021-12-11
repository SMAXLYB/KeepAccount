package life.chenshi.keepaccounts.module.common.database

import androidx.room.TypeConverter
import java.math.BigDecimal
import java.util.*

/**
 * 数据类型转换
 */
class Converters {

    // 时间戳转日期
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it * 1000L) }
    }

    // 日期转时间戳
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.div(1000)
    }

    @TypeConverter
    fun fromInt(value: Int?): BigDecimal? {
        return value?.let { BigDecimal(it).movePointLeft(2) }
    }

    @TypeConverter
    fun bigDecimalToInt(bigDecimal: BigDecimal?): Int? {
        return bigDecimal?.toInt()
    }
}