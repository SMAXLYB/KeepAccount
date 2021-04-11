package life.chenshi.keepaccounts.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import life.chenshi.keepaccounts.constant.CATEGORY_TYPE_MINOR
import life.chenshi.keepaccounts.constant.STATE_NORMAL
import life.chenshi.keepaccounts.constant.TB_MINOR_CATEGORIES

@Entity(
    tableName = TB_MINOR_CATEGORIES,
    indices = [
        Index(value = ["name", "major_category_id"], unique = true),
        Index(value = ["major_category_id"])],
    foreignKeys = [ForeignKey(
        entity = MajorCategory::class,
        parentColumns = ["id"],
        childColumns = ["major_category_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
class MinorCategory(
    id: Int? = null,
    name: String,
    state: Int = STATE_NORMAL,
    recordType: Int,
    @ColumnInfo(name = "major_category_id")
    val majorCategoryId: Int,
    @ColumnInfo(name = "use_rate", defaultValue = "0")
    val useRate: Int = 0
) : AbstractCategory(id, name, state, recordType, CATEGORY_TYPE_MINOR) {
    override fun hashCode(): Int {
        var result = id ?: 0
        result = 31 * result + name.hashCode()
        result = 31 * result + state
        result = 31 * result + majorCategoryId
        return result
    }
}