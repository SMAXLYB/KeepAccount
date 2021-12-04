package life.chenshi.keepaccounts.module.common.utils.storage

/**
 * 键值对存储框架需要实现的接口
 */
interface IKeyValueStoreHelper {
    fun <T : Any> read(key: String, defaultValueIfNull: T): T
    fun <T : Any> write(key: String, value: T)
}