package life.chenshi.keepaccounts.database.entity

import androidx.room.*
import life.chenshi.keepaccounts.constant.DatabaseConstant

@Entity(
    tableName = "tb_sub_categories",
    indices = [Index("name", unique = true)],
    foreignKeys = [ForeignKey(entity = Category::class, parentColumns = ["id"], childColumns = ["category_id"], onDelete = ForeignKey.CASCADE)]
)
data class SubCategory(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    var name: String,
    val state: Int = DatabaseConstant.STATE_NORMAL,
    @ColumnInfo(name = "category_id")
    val categoryId: Int = 0
) {
}