package life.chenshi.keepaccounts.database.entity

import androidx.room.PrimaryKey
import life.chenshi.keepaccounts.constant.STATE_NORMAL

abstract class AbstractCategory(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    var name: String,
    var state: Int = STATE_NORMAL
) {
    abstract fun isMajorCategory(): Boolean
}