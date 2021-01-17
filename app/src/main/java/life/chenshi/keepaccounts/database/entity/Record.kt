package life.chenshi.keepaccounts.database.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.*
import java.math.BigDecimal
import java.util.*

@Entity(
    tableName = "tb_records",
    indices = [Index(value = ["money", "time", "category", "record_type", "book_id"])],
    foreignKeys = [ForeignKey(
        entity = Book::class,
        parentColumns = ["id"],
        childColumns = ["book_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Record @JvmOverloads constructor(
    // 账目的唯一id
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    // 金额
    var money: BigDecimal,
    // 备注
    var remark: String? = null,
    // 时间
    var time: Date,
    // 类型暂时用文字代替，后续可以升级改造单独一个表出来
    var category: Int,
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
        source.readInt()
    )

    // 序列化
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(id!!)
        dest.writeString(money.toString())
        dest.writeString(remark)
        dest.writeLong(time.time)
        dest.writeInt(category)
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
        return "Record(bookid=${bookId}, id=$id, money=$money, remark=$remark, time=$time, category=$category, recordType=$recordType)"
    }
}

object RecordType {
    const val OUTCOME = 0;
    const val INCOME = 1;
}

object Category {
    const val GOUWU = 0;
    const val CHIFAN = 1;
    const val JIAOTONG = 2;
    const val YILIAO = 3;
    const val ZHUFANG = 4;
    const val TONGXUN = 5;
    const val GONGZI = 6;
    const val HONGBAO = 7;
    const val JIJIN = 8;
    const val JIANZHI = 9;
    const val LIXI = 10;

    fun convert2String(category: Int): String {
        return when (category) {
            GOUWU -> "购物"
            CHIFAN -> "吃饭"
            JIAOTONG -> "交通"
            YILIAO -> "医疗"
            ZHUFANG -> "住房"
            TONGXUN -> "通讯"
            GONGZI -> "工资"
            HONGBAO -> "红包"
            JIJIN -> "基金"
            JIANZHI -> "兼职"
            else -> "利息"
        }
    }
}


