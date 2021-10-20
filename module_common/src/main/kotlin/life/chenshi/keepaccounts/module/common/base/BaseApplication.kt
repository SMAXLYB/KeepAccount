package life.chenshi.keepaccounts.module.common.base

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import life.chenshi.keepaccounts.module.common.lifecycle.LoadModuleProxy

/**
 * 集成模式下使用的全局application
 */
open class BaseApplication : Application() {

    private val mCoroutineScope by lazy(mode = LazyThreadSafetyMode.NONE) { MainScope() }
    private val mLoadModuleProxy by lazy(mode = LazyThreadSafetyMode.NONE) { LoadModuleProxy() }

    companion object {
        // 全局application
        @SuppressLint("StaticFieldLeak")
        lateinit var application: Application
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        application = this
        mLoadModuleProxy.onAttachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()

        mLoadModuleProxy.onCreate(this)

        // 策略初始化第三方依赖
        initDepends()
    }

    /**
     * 初始化第三方依赖
     */
    private fun initDepends() {
        // 开启一个 Default Coroutine 进行初始化不会立即使用的第三方
        mCoroutineScope.launch(Dispatchers.Default) {
            mLoadModuleProxy.initByBackstage()
        }

        // 前台初始化
        val depends = mLoadModuleProxy.initByFrontDesk()
        depends.forEach { it() }
    }

    override fun onTerminate() {
        super.onTerminate()
        mLoadModuleProxy.onTerminate(this)
        mCoroutineScope.cancel()
    }
}
