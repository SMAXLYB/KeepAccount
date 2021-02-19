package life.chenshi.keepaccounts.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "tb_books",
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