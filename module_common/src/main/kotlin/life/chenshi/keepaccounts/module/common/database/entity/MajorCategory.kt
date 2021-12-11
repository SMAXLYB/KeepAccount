package life.chenshi.keepaccounts.module.common.database.entity

import androidx.room.Entity
import androidx.room.Index
import life.chenshi.keepaccounts.module.common.constant.CATEGORY_TYPE_MAJOR
import life.chenshi.keepaccounts.module.common.constant.STATE_NORMAL
import life.chenshi.keepaccounts.module.common.constant.TB_MAJOR_CATEGORIES

@Entity(tableName = TB_MAJOR_CATEGORIES, indices = [Index("name","record_type", unique = true)])
class MajorCategory(
    id: Int? = null,
    name: String,
    state: Int = STATE_NORMAL,
    recordType: Int
) : AbstractCategory(id, name, state, recordType, CATEGORY_TYPE_MAJOR) {

    override fun hashCode(): Int {
        var result = id ?: 0
        result = 31 * result + name.hashCode()
        result = 31 * result + state
        return result
    }
}