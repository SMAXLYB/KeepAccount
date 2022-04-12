package life.chenshi.keepaccounts.module.common.database.bean

import androidx.room.Embedded
import androidx.room.Relation
import life.chenshi.keepaccounts.module.common.database.entity.MajorCategory
import life.chenshi.keepaccounts.module.common.database.entity.MinorCategory
import life.chenshi.keepaccounts.module.common.database.entity.Record

/**
 * @description:
 * @author lyb
 * @date 2021年04月12日 22:26
 */
data class RecordWithCategoryBean(
    @Embedded val record: Record,
    @Relation(
        parentColumn = "major_category_id",
        entityColumn = "id"
    )
    val majorCategory: MajorCategory,
    @Relation(
        parentColumn = "minor_category_id",
        entityColumn = "id"
    )
    val minorCategory: MinorCategory?,
)