package life.chenshi.keepaccounts.database.entity

import androidx.room.*
import life.chenshi.keepaccounts.constant.STATE_NORMAL

@Entity(
    tableName = "tb_sub_categories",
    indices = [
        Index(value = ["name", "category_id"], unique = true),
        Index(value = ["category_id"])],
    foreignKeys = [ForeignKey(
        entity = Category::class,
        parentColumns = ["id"],
        childColumns = ["category_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class SubCategory(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    var name: String,
    var state: Int = STATE_NORMAL,
    @ColumnInfo(name = "category_id")
    val categoryId: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SubCategory

        if (id != other.id) return false
        if (name != other.name) return false
        if (state != other.state) return false
        if (categoryId != other.categoryId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id ?: 0
        result = 31 * result + name.hashCode()
        result = 31 * result + state
        result = 31 * result + categoryId
        return result
    }
}