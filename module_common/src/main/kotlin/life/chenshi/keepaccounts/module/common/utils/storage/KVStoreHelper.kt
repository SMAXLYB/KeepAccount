package life.chenshi.keepaccounts.module.common.utils.storage

/**
 * 键值对存储工具类
 */
object KVStoreHelper : IKeyValueStoreHelper {

    private val delegate: IKeyValueStoreHelper = MMKVStoreHelper()

    override fun <T : Any> read(key: String, defaultValueIfNull: T): T {
        return delegate.read(key, defaultValueIfNull)
    }

    override fun <T : Any> write(key: String, value: T) {
        delegate.write(key, value)
    }
}