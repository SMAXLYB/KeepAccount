package life.chenshi.keepaccounts.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import life.chenshi.keepaccounts.constant.STATE_NORMAL

@Entity(
    tableName = "tb_minor_categories",
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
    @ColumnInfo(name = "major_category_id")
    val majorCategoryId: Int
) : AbstractCategory(id, name, state) {

    override fun isMajorCategory(): Boolean = false

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MinorCategory

        if (id != other.id) return false
        if (name != other.name) return false
        if (state != other.state) return false
        if (majorCategoryId != other.majorCategoryId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id ?: 0
        result = 31 * result + name.hashCode()
        result = 31 * result + state
        result = 31 * result + majorCategoryId
        return result
    }
}