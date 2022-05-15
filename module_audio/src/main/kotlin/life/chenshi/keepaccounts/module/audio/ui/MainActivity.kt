package life.chenshi.keepaccounts.module.audio.ui

import android.annotation.SuppressLint
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Environment.DIRECTORY_MUSIC
import android.util.Log
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import life.chenshi.keepaccounts.module.audio.R
import life.chenshi.keepaccounts.module.audio.databinding.AudioActivityMainBinding
import life.chenshi.keepaccounts.module.audio.encoder.AacEncoder
import life.chenshi.keepaccounts.module.audio.encoder.IEncoder
import life.chenshi.keepaccounts.module.audio.encoder.WrappedBytes
import life.chenshi.keepaccounts.module.audio.recorder.AudioRecorder
import life.chenshi.keepaccounts.module.audio.recorder.IRecorder
import life.chenshi.keepaccounts.module.audio.recorder.RecorderConfig
import life.chenshi.keepaccounts.module.common.base.BaseActivity
import life.chenshi.keepaccounts.module.common.utils.ToastUtil
import java.util.concurrent.LinkedBlockingQueue

@SuppressLint("MissingPermission", "SetTextI18n")
class MainActivity : BaseActivity() {

    private val tag = "录音测试"

    companion object {

        /** 声音来源: 麦克风 */
        private const val SOURCE = MediaRecorder.AudioSource.MIC

        /** 采样率: 44.1Hz, 这里改了ADST头部也要跟着改 */
        private const val SAMPLE = 8000

        /** 声道: 单声道 */
        private const val CHANNEL = AudioFormat.CHANNEL_IN_MONO
        private val CHANNEL_COUNT = if (CHANNEL == AudioFormat.CHANNEL_IN_MONO) {
            1
        } else {
            2
        }

        /** 位深: 8 bit */
        private const val AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT

        /** 存储声音数据的缓冲区大小: 2帧的大小
         *  getMinBufferSize(): 获取一帧的最小空间
         *  采集的数据也要给解码器用, 所以解码器的bufferSize不能小于采集的bufferSize, 输入缓冲区放不下的
         */
        private val BUFFER_SIZE = 2 * AudioRecord.getMinBufferSize(SAMPLE, CHANNEL, AUDIO_FORMAT)

        /** 码率 = 采样率*位深*声道数*压缩比例 */
        private val BIT_RATE = SAMPLE * AUDIO_FORMAT * CHANNEL_COUNT * 1f
    }

    private val mBinding by bindingContentView<AudioActivityMainBinding>(R.layout.audio_activity_main)

    /** 文件夹路径 */
    private val dir by lazy { getExternalFilesDir(DIRECTORY_MUSIC)?.path }

    private val mRecorderConfig by lazy {
        RecorderConfig(SOURCE, SAMPLE, CHANNEL, CHANNEL_COUNT, AUDIO_FORMAT, BUFFER_SIZE, BIT_RATE.toInt(), "$dir/")
    }

    /** true录音中, false已停止 */
    private var isRecording = false
        set(value) {
            mBinding.tvStatus.post {
                if (value) {
                    mBinding.tvStatus.text = "录音中"
                } else {
                    mBinding.tvStatus.text = "已停止"
                }
            }
            field = value
        }
    private var recorderThread: RecorderThread? = null

    override fun initView(savedInstanceState: Bundle?) {
    }

    override fun initListener() {
        mBinding.btnPermission.setOnClickListener {
            getPermission()
        }
        mBinding.btnStart.setOnClickListener {
            startRecord()
        }
        mBinding.btnStop.setOnClickListener {
            stopRecord()
        }
        mBinding.btnDelete.setOnClickListener {
        }
    }

    private fun startRecord() {
        recorderThread = RecorderThread(mRecorderConfig)
        recorderThread!!.start()
        isRecording = true
    }

    private fun stopRecord() {
        recorderThread?.stopRecording()
        recorderThread = null
        isRecording = false
    }

    private fun getPermission() {
        XXPermissions.with(this)
            .permission(Permission.RECORD_AUDIO, Permission.MANAGE_EXTERNAL_STORAGE)
            .request(object : OnPermissionCallback {
                override fun onGranted(permissions: MutableList<String>?, all: Boolean) {
                    if (all) {
                        ToastUtil.showSuccess("全部权限都已经获取")
                    } else {
                        ToastUtil.showLong("仅请求${permissions}成功, 其他失败!!!")
                    }
                }

                override fun onDenied(permissions: MutableList<String>?, never: Boolean) {
                    ToastUtil.showFail("权限${permissions}请求失败")
                }
            })
    }

    override fun initObserver() {}
}

class RecorderThread(
    private val recorderConfig: RecorderConfig
) : Thread("RecorderThread") {

    @Volatile
    private var recording = false
    private var recorder: IRecorder? = null
    private var encoderThread: EncoderThread? = null

    override fun run() {
        recorder = AudioRecorder()
        var bytes: ByteArray? = null
        if (recorder!!.start(recorderConfig)) {
            recording = true
        }
        if (recording) {
            bytes = ByteArray(recorderConfig.bufferSize)
            encoderThread = EncoderThread(recorderConfig)
            encoderThread!!.putRecorder("aac", AacEncoder())
            encoderThread!!.start()
        }
        while (recording) {
            val code = recorder!!.read(bytes!!, recorderConfig.bufferSize)
            if (code > 0) {
                encoderThread?.putPcmData(WrappedBytes(bytes, recorderConfig.bufferSize))
            }
        }
        recorder!!.stop()
        recorder = null
        encoderThread?.stopEncoding()
        encoderThread = null

    }

    fun stopRecording() {
        recording = false
    }
}

class EncoderThread(private val recorderConfig: RecorderConfig) : Thread("EncoderThread") {

    @Volatile
    private var encoding = false
    private val encoderMap = HashMap<String, IEncoder>()
    private val queue = LinkedBlockingQueue<WrappedBytes>()

    override fun run() {

        var wrapped: WrappedBytes? = null

        encoderMap.forEach {
            it.value.start(recorderConfig)
        }
        encoding = true
        do {
            wrapped = queue.poll()
            if (wrapped != null) {
                Log.d("录音测试", "run: size=${wrapped.bytes.size}")
                encoderMap.forEach {
                    it.value.encode(wrapped.bytes, wrapped.size)
                }
            }
        } while (encoding || wrapped != null)

        Log.d("录音测试", "run: 结束")
        encoderMap.forEach {
            it.value.stop()
        }
        encoderMap.clear()
    }

    fun putRecorder(key: String, encoder: IEncoder) {
        encoderMap[key] = encoder
    }

    fun putPcmData(wrappedBytes: WrappedBytes) {
        queue.put(wrappedBytes)
    }

    fun stopEncoding() {
        encoding = false
    }
}