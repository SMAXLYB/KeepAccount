package life.chenshi.keepaccounts.module.audio.recorder

import android.annotation.SuppressLint
import android.media.AudioRecord

class AudioRecorder : IRecorder {

    private var recorder: AudioRecord? = null

    @SuppressLint("MissingPermission")
    override fun start(recorderConfig: RecorderConfig): Boolean {
        recorder = AudioRecord(
            recorderConfig.source,
            recorderConfig.sample,
            recorderConfig.channel,
            recorderConfig.format,
            recorderConfig.bufferSize
        )

        if (recorder!!.state == AudioRecord.STATE_INITIALIZED) {
            recorder!!.startRecording()
            if (recorder!!.recordingState == AudioRecord.RECORDSTATE_RECORDING) {
                return true
            }
        }

        return false
    }

    override fun read(byteArray: ByteArray, size: Int): Int {
        return recorder?.read(byteArray, 0, size) ?: 0
    }

    override fun stop() {
        recorder?.stop()
        recorder?.release()
        recorder = null
    }
}