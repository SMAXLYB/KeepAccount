package life.chenshi.keepaccounts.module.audio.encoder

import life.chenshi.keepaccounts.module.audio.recorder.RecorderConfig

interface IEncoder {
    fun start(recorderConfig: RecorderConfig)
    fun encode(byteArray: ByteArray, size: Int)
    fun stop()
}