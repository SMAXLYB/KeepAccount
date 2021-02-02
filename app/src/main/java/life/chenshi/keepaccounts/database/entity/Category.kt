package life.chenshi.keepaccounts.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import life.chenshi.keepaccounts.constant.DatabaseConstant

@Entity(tableName = "tb_categories", indices = [Index("name", unique = true)])
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    var name: String,
    val state: Int = DatabaseConstant.STATE_NORMAL,
) {
}