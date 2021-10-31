package life.chenshi.keepaccounts.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import life.chenshi.keepaccounts.module.common.constant.TB_BOOKS

@Entity(tableName = TB_BOOKS,
    indices = [
        Index("id"),
        Index("name", unique = true)])
data class Book(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    var name: String,
    var description: String? = null
) {
}