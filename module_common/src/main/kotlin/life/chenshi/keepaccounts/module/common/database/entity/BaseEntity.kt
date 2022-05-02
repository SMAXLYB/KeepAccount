package life.chenshi.keepaccounts.module.common.database.entity

import androidx.room.ColumnInfo
import java.io.Serializable
import java.util.*

abstract class BaseEntity(
    @ColumnInfo(name = "modify_time")
    var modifyTime: Date = Date(),
    @ColumnInfo(name = "create_time")
    var createTime: Date = Date()
) : Serializable