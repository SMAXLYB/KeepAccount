package life.chenshi.keepaccounts.database.entity

import androidx.room.ColumnInfo
import androidx.room.Ignore
import androidx.room.PrimaryKey
import life.chenshi.keepaccounts.common.utils.isNull
import life.chenshi.keepaccounts.constant.STATE_NORMAL

abstract class AbstractCategory(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    var name: String,
    @ColumnInfo(defaultValue = "0")
    var state: Int = STATE_NORMAL,
    /**
     * 分类收支类型:收入/支出
     */
    @ColumnInfo(name = "record_type")
    var recordType: Int,
    /**
     * 分类类型:主类/子类
     */
    @Ignore
    val categoryType: String
) {
    override fun equals(other: Any?): Boolean {
        if (other.isNull()) {
            return false
        }

        if (javaClass != other?.javaClass) return false

        other as AbstractCategory

        if (id != other.id) return false
        if (categoryType != other.categoryType) {
            return false
        }

        return true
    }

    override fun hashCode(): Int {
        var result = id ?: 0
        result = 31 * result + categoryType.hashCode()
        return result
    }
}