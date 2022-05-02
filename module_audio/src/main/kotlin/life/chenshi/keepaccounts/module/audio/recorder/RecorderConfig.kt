package life.chenshi.keepaccounts.module.audio.recorder

data class RecorderConfig(
    val source: Int,
    val sample: Int,
    val channel: Int,
    val channelCount: Int,
    val format: Int,
    val bufferSize: Int,
    val bitRate: Int,
    val dir: String
)