package life.chenshi.keepaccounts.module.audio.encoder

import android.media.MediaCodec
import android.media.MediaFormat
import android.os.Build
import android.util.Log
import life.chenshi.keepaccounts.module.audio.recorder.RecorderConfig
import java.io.FileOutputStream
import java.nio.ByteBuffer

class AacEncoder : IEncoder {
    private var encoder: MediaCodec? = null
    private var config: RecorderConfig? = null
    private var fos: FileOutputStream? = null

    override fun start(recorderConfig: RecorderConfig) {
        config = recorderConfig
        fos = FileOutputStream(config!!.dir + "${System.currentTimeMillis()}.aac")
        // 设置声音编码的参数
        val format = MediaFormat.createAudioFormat(
            MediaFormat.MIMETYPE_AUDIO_AAC, recorderConfig.sample, recorderConfig.channelCount
        )
        // 比特率
        format.setInteger(MediaFormat.KEY_BIT_RATE, recorderConfig.bitRate)
        // 输入缓冲区大小
        format.setInteger(MediaFormat.KEY_MAX_INPUT_SIZE, recorderConfig.bufferSize)
        // format.setInteger(MediaFormat.KEY_AAC_PROFILE, MediaCodecInfo.CodecProfileLevel.AACObjectLC)

        // 创建编码器
        encoder = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_AUDIO_AAC)
        encoder!!.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE)
        encoder!!.start()
    }

    override fun encode(byteArray: ByteArray, size: Int) {
        encoder?.let {
            val index = it.dequeueInputBuffer(-1)
            if (index >= 0) {
                val inputBuffer = it.getInBuffer(index)
                inputBuffer.clear()
                inputBuffer.limit(size)
                inputBuffer.put(byteArray)
                // 通知开始编码
                it.queueInputBuffer(index, 0, size, 0, 0)
            }

            val bufferInfo = MediaCodec.BufferInfo()
            var outputIndex = it.dequeueOutputBuffer(bufferInfo, 0)

            while (outputIndex >= 0) {
                val outSize = bufferInfo.size
                val packetSize = outSize + 7
                val outputBuffer = it.getOutBuffer(outputIndex)
                outputBuffer.position(bufferInfo.offset)
                outputBuffer.limit(bufferInfo.offset + outSize)
                val outData = ByteArray(packetSize)
                addADTSHeaderToPacket(outData, packetSize)
                outputBuffer.get(outData, 7, outSize)
                outputBuffer.position(bufferInfo.size)
                Log.d("录音测试", "写入文件: size=${outData.size}")
                fos?.write(outData, 0, outData.size)
                it.releaseOutputBuffer(outputIndex, false)
                outputIndex = it.dequeueOutputBuffer(bufferInfo, 0)
            }
        }
    }

    override fun stop() {
        encoder?.let {
            it.stop()
            it.release()
        }
        encoder = null
        fos?.let {
            it.flush()
            it.close()
        }
        fos = null
    }

    /**
     * 给aac帧加上ADTS帧, 原生的aac帧不能直接播放
     * @param packet 外部传进来的包大小是包含头部信息的, 只不过没有数据, 需要补上信息
     *               前7个字节是头部
     */
    private fun addADTSHeaderToPacket(packet: ByteArray, packetLen: Int) {

        /**
         * 采样率对应的index
         * 44100 = 4
         * 32000 = 5
         * 24000 = 6
         * 22050 = 7
         * 16000 = 8
         * 12000 = 9
         * 11025 = 10
         * 8000  = 11
         */
        val freqIdx = 4
        val profile = 2
        val chanCfg = config?.channelCount ?: 2

        packet[0] = 0xFF.toByte()
        packet[1] = 0xF9.toByte() //ios可能不能播放， f1才行
        packet[2] = (((profile - 1) shl 6) + (freqIdx shl 2) + (chanCfg shr 2)).toByte()
        packet[3] = (((chanCfg and 3) shl 6) + (packetLen shr 11)).toByte()
        packet[4] = ((packetLen and 0x7FF) shr 3).toByte()
        packet[5] = (((packetLen and 7) shl 5) + 0x1F).toByte()
        packet[6] = 0xFC.toByte()
    }
}

private fun MediaCodec.getInBuffer(index: Int): ByteBuffer {
    return if (Build.VERSION.SDK_INT >= 21) {
        getInputBuffer(index)!!
    } else {
        inputBuffers[index]
    }
}

private fun MediaCodec.getOutBuffer(index: Int): ByteBuffer {
    return if (Build.VERSION.SDK_INT >= 21) {
        getOutputBuffer(index)!!
    } else {
        outputBuffers[index]
    }
}