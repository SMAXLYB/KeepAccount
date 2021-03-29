package life.chenshi.keepaccounts.database.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.*
import life.chenshi.keepaccounts.constant.TB_RECORDS
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
    var bookId: Int
) : Parcelable {

    // 构造函数
    constructor(source: Parcel) : this(
        source.readInt(),
        BigDecimal(source.readString()),
        source.readString(),
        Date(source.readLong()),
        source.readInt(),
        source.readInt(),
        source.readInt(),
        source.readInt()
    )

    // 序列化
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(id!!)
        dest.writeString(money.toString())
        dest.writeString(remark)
        dest.writeLong(time.time)
        dest.writeInt(majorCategoryId)
        dest.writeInt(minorCategoryId)
        dest.writeInt(recordType)
        dest.writeInt(bookId)
    }

    // 描述信息
    override fun describeContents(): Int {
        return 0
    }

    // 反序列化
    companion object CREATOR : Parcelable.Creator<Record> {
        override fun createFromParcel(parcel: Parcel): Record {
            return Record(parcel)
        }

        override fun newArray(size: Int): Array<Record?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return "Record(bookId=${bookId}, id=$id, money=$money, remark=$remark, time=$time, majorCategoryId=$majorCategoryId, minorCategoryId=${minorCategoryId}, recordType=$recordType)"
    }
}


