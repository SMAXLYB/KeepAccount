package life.chenshi.keepaccounts.module.common.base

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

/**
 * 集成模式下使用的全局application
 */
open class BaseApplication : Application() {
    companion object {
        // 全局Context
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }
}