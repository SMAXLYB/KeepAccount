package life.chenshi.keepaccounts.module.common.base

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import life.chenshi.keepaccounts.module.common.lifecycle.ModuleLifecycleDispatchProxy
import life.chenshi.keepaccounts.module.common.utils.ActivityStackManager

/**
 * 集成模式下使用的全局application
 */
open class BaseApplication : Application(), ViewModelStoreOwner,
    Application.ActivityLifecycleCallbacks {

    private val mCoroutineScope by lazy(mode = LazyThreadSafetyMode.NONE) { MainScope() }
    private val mModuleLifecycleDispatchProxy by lazy(mode = LazyThreadSafetyMode.NONE) { ModuleLifecycleDispatchProxy() }
    private lateinit var mViewModelStore: ViewModelStore

    companion object {
        // 全局application
        @SuppressLint("StaticFieldLeak")
        @Deprecated("使用hilt来获取全局context")
        lateinit var application: BaseApplication
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        application = this
        mModuleLifecycleDispatchProxy.onAttachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
        mViewModelStore = ViewModelStore()
        registerActivityLifecycleCallbacks(this)
        mModuleLifecycleDispatchProxy.onCreate(this)

        // 策略初始化第三方依赖
        initDepends(this)
    }

    /**
     * 初始化第三方依赖
     */
    private fun initDepends(application: Application) {
        // 开启一个 Default Coroutine 进行初始化不会立即使用的第三方
        mCoroutineScope.launch(Dispatchers.Default) {
            mModuleLifecycleDispatchProxy.initInBackground(application)
        }

        // 前台初始化
        mModuleLifecycleDispatchProxy.initInForeground(application)
    }

    override fun onTerminate() {
        super.onTerminate()
        mModuleLifecycleDispatchProxy.onTerminate(this)
        mCoroutineScope.cancel()
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        ActivityStackManager.addActivityToStack(activity)
    }

    override fun onActivityStarted(activity: Activity) {}

    override fun onActivityResumed(activity: Activity) {}

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityStopped(activity: Activity) {}

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

    override fun onActivityDestroyed(activity: Activity) {
        ActivityStackManager.popActivityToStack(activity)
    }

    override fun getViewModelStore(): ViewModelStore {
        return mViewModelStore
    }
}
