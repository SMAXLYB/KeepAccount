package life.chenshi.keepaccounts.module.common.utils.storage

import com.tencent.mmkv.MMKV

class MMKVStoreHelper : IKeyValueStoreHelper {
    // 不分业务, 统一存储
    private val mmkv = MMKV.mmkvWithID("keepaccounts")

    override fun <T : Any> read(key: String, defaultValueIfNull: T): T {
        val value = when (defaultValueIfNull) {
            is String -> {
                mmkv.decodeString(key, defaultValueIfNull)
            }
            is Boolean -> {
                mmkv.decodeBool(key, defaultValueIfNull)
            }
            is Int -> {
                mmkv.decodeInt(key, defaultValueIfNull)
            }
            is Long -> {
                mmkv.decodeLong(key, defaultValueIfNull)
            }
            is Double -> {
                mmkv.decodeDouble(key, defaultValueIfNull)
            }
            is Float -> {
                mmkv.decodeFloat(key, defaultValueIfNull)
            }
            else -> {
                throw IllegalArgumentException("暂不支持存储 ${defaultValueIfNull::class.java} 类型，请自行实现！")
            }
        }

        return value as T
    }

    override fun <T : Any> write(key: String, value: T) {
        when (value) {
            is String -> {
                mmkv.encode(key, value)
            }
            is Boolean -> {
                mmkv.encode(key, value)
            }
            is Int -> {
                mmkv.encode(key, value)
            }
            is Long -> {
                mmkv.encode(key, value)
            }
            is Double -> {
                mmkv.encode(key, value)
            }
            is Float -> {
                mmkv.encode(key, value)
            }
            else -> {
                throw IllegalArgumentException("暂不支持读取 ${value::class.java} 类型，请自行实现！")
            }
        }
    }
}