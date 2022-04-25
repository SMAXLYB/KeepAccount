package life.chenshi.keepaccounts.module.common.database.entity

import androidx.room.*
import life.chenshi.keepaccounts.module.common.constant.TB_RECORDS
import java.io.Serializable
import java.math.BigDecimal
import java.util.*

/**
 * 收支记录实体, 外键包含账本, 主类型;
 * 当账本被删除,当下所有记录被删除;
 * 当主类型被伪删除,当下记录不会删除,当主类被真的删除,当下所有记录会被删除
 */
@Entity(
    tableName = TB_RECORDS,
    indices = [
        Index(value = ["id"]),
        Index(value = ["time"]),
        Index(value = ["major_category_id"]),
        Index(value = ["book_id"])],
    foreignKeys = [ForeignKey(
        entity = Book::class,
        parentColumns = ["id"],
        childColumns = ["book_id"],
        onDelete = ForeignKey.CASCADE
    ), ForeignKey(
        entity = MajorCategory::class,
        parentColumns = ["id"],
        childColumns = ["major_category_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Record constructor(
    // 账目的唯一id
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    // 金额
    var money: BigDecimal,
    // 备注
    var remark: String? = null,
    // 时间
    var time: Date,
    // 消费类型主类
    @ColumnInfo(name = "major_category_id")
    var majorCategoryId: Int,
    // 消费类型子类
    @ColumnInfo(name = "minor_category_id")
    var minorCategoryId: Int = 0,
    // 0支出/1收入
    @ColumnInfo(name = "record_type")
    var recordType: Int,
    // 账本id
    @ColumnInfo(name = "book_id")
    var bookId: Int,
    @ColumnInfo(name = "assets_account_id")
    var AssetsAccountId: Int? = null
) : Serializable {

    fun setAllData(
        money: BigDecimal,
        remark: String?,
        date: Date,
        majorCategoryId: Int,
        minorCategoryId: Int,
        recordType: Int,
        bookId: Int
    ) {
        this.money = money
        this.remark = remark
        this.time = date
        this.majorCategoryId = majorCategoryId
        this.minorCategoryId = minorCategoryId
        this.recordType = recordType
        this.bookId = bookId
    }
}


