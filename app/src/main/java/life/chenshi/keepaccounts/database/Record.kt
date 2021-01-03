package life.chenshi.keepaccounts.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.util.*

@Entity(
    tableName = "tb_records",
    indices = [Index(value = ["money", "time", "category", "record_type"])]
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
    var recordType: Int
)

object RecordType{
    const val OUTCOME =0;
    const val INCOME = 1;
}


