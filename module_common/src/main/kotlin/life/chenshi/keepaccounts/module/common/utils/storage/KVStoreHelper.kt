package life.chenshi.keepaccounts.module.common.utils.storage

/**
 * 键值对存储工具类
 */
object KVStoreHelper : IKeyValueStoreHelper {

    private val delegate: IKeyValueStoreHelper = MMKVStoreHelper()

    override fun <T : Any> read(key: String, defaultValueIfNull: T): T {
        return delegate.read(key, defaultValueIfNull)
    }

    inline fun <reified T : Any> read(key: String): T {
        return when (T::class) {
            String::class -> {
                read(key, "") as T
            }
            Boolean::class -> {
                read(key, false) as T
            }
            Int::class -> {
                read(key, 0) as T
            }
            Long::class -> {
                read(key, 0L) as T
            }
            Double::class -> {
                read(key, 0.0) as T
            }
            Float::class -> {
                read(key, 0.0f) as T
            }
            else -> {
                throw IllegalArgumentException("暂不支持存储 ${T::class.java} 类型，请自行实现！")
            }
        }
    }

    override fun <T : Any> write(key: String, value: T) {
        delegate.write(key, value)
    }
}