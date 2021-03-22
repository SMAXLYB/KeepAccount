package life.chenshi.keepaccounts.database.entity

import androidx.room.Entity
import androidx.room.Index
import life.chenshi.keepaccounts.constant.CATEGORY_TYPE_MAJOR
import life.chenshi.keepaccounts.constant.STATE_NORMAL

@Entity(tableName = "tb_major_categories", indices = [Index("name", unique = true)])
class MajorCategory(
    id: Int? = null,
    name: String,
    state: Int = STATE_NORMAL,
    recordType: Int
) : AbstractCategory(id, name, state, recordType, CATEGORY_TYPE_MAJOR) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MajorCategory

        if (id != other.id) return false
        // if (name != other.name) return false
        // if (state != other.state) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id ?: 0
        result = 31 * result + name.hashCode()
        result = 31 * result + state
        return result
    }
}