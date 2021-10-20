package life.chenshi.keepaccounts.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Process
import life.chenshi.keepaccounts.R
import life.chenshi.keepaccounts.common.utils.ToastUtil
import life.chenshi.keepaccounts.databinding.ActivityCrashBinding
import life.chenshi.keepaccounts.module.common.base.BaseActivity
import life.chenshi.keepaccounts.module.common.utils.StatusBarUtil
import kotlin.system.exitProcess

class CrashActivity : BaseActivity() {
    private val mBinding by bindingContentView<ActivityCrashBinding>(R.layout.activity_crash)
    private var exceptionInfo: String = "应用崩溃了"

    override fun initView() {
        StatusBarUtil.init(this)
            .setColor(R.color.global_background_gray, false)
            .setDarkMode(true)

        intent.getStringExtra("exception_info")?.let {
            exceptionInfo = it
        }
        mBinding.tvCrashInfo.text = exceptionInfo
    }

    override fun initListener() {
        mBinding.btnExit.setOnClickListener {
            restart()
        }
        mBinding.btnCopy.setOnClickListener {
            copyToClipboard(exceptionInfo)
        }
    }

    override fun initObserver() {
    }

    override fun onBackPressed() {
        super.onBackPressed()
        exit()
    }

    private fun exit() {
        Process.killProcess(Process.myPid())
        exitProcess(1)
    }

    private fun restart() {
        val intent = baseContext.packageManager.getLaunchIntentForPackage(baseContext.packageName)
        intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
        startActivity(intent)
        exit()
    }

    private fun copyToClipboard(str: String) {
        val clipboardManager = this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val data = ClipData.newPlainText("Crash", str)
        clipboardManager.setPrimaryClip(data)
        ToastUtil.showShort("复制成功!")
    }
}