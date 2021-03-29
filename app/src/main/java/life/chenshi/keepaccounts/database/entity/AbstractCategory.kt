package life.chenshi.keepaccounts.database.entity

import androidx.room.ColumnInfo
import androidx.room.Ignore
import androidx.room.PrimaryKey
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
}