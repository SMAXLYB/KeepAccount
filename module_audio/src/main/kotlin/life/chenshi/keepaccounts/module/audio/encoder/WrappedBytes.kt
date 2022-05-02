package life.chenshi.keepaccounts.module.audio.encoder

data class WrappedBytes(val bytes: ByteArray, val size: Int) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WrappedBytes

        if (!bytes.contentEquals(other.bytes)) return false
        if (size != other.size) return false

        return true
    }

    override fun hashCode(): Int {
        var result = bytes.contentHashCode()
        result = 31 * result + size
        return result
    }
}