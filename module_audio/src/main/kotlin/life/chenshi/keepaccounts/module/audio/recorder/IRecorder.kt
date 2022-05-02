package life.chenshi.keepaccounts.module.audio.recorder

interface IRecorder {
    fun start(recorderConfig: RecorderConfig): Boolean
    fun read(byteArray: ByteArray, size:Int): Int
    fun stop()
}