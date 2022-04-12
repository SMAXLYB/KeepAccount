package life.chenshi.keepaccounts.module.common.crash

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Process
import java.io.PrintWriter
import java.io.StringWriter
import kotlin.system.exitProcess

class GlobalCrashHandler private constructor() : Thread.UncaughtExceptionHandler {
    private lateinit var mContext: Context
    private var mDefaultHandler: Thread.UncaughtExceptionHandler? = null

    companion object {
        fun getInstance(): GlobalCrashHandler {
            return SingleHolder.holder
        }
    }

    fun init(context: Context) {
        mContext = context
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    override fun uncaughtException(t: Thread, e: Throwable) {
        if (!handleException(e) && mDefaultHandler != null) {
            mDefaultHandler?.uncaughtException(t, e)
        } else {
            val stringWriter = StringWriter()
            val printWriter = PrintWriter(stringWriter)
            e.printStackTrace(printWriter)
            var cause = e.cause
            while (cause != null) {
                cause.printStackTrace(printWriter)
                cause = cause.cause
            }
            printWriter.close()
            val exceptionInfo = stringWriter.toString()

            val intent = Intent(mContext, CrashActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.putExtra("exception_info", exceptionInfo)
            mContext.startActivity(intent)
            Process.killProcess(Process.myPid())
            exitProcess(1)
        }
    }

    private fun handleException(e: Throwable): Boolean {
        // 收集处理异常信息
        return true
    }

    private object SingleHolder {
        @SuppressLint("StaticFieldLeak")
        val holder = GlobalCrashHandler()
    }
}