package life.chenshi.keepaccounts.module.common.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import life.chenshi.keepaccounts.module.common.constant.TB_ASSETS_ACCOUNT
import java.math.BigDecimal
import java.util.*

@Entity(
    tableName = TB_ASSETS_ACCOUNT,
    indices = [
        Index(value = ["id"]),
        Index(value = ["name"], unique = true),
    ]
)
data class AssetsAccount constructor(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    // 账户名称
    val name: String,
    // 余额
    var balance: BigDecimal,
    // 备注
    val remark: String? = null,
    // 账号/卡号
    val number: String? = null,
    // 是否计入总资产
    @ColumnInfo(name = "include_in_all_asset")
    val includedInAllAsset: Boolean,
    @ColumnInfo(name = "expire_time")
    val expireTime: Date,
    val type: String
) : BaseEntity()