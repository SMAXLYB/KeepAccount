package life.chenshi.keepaccounts.module.common

import android.app.Application
import android.content.Context
import com.google.auto.service.AutoService
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import life.chenshi.keepaccounts.module.common.constant.APP_FIRST_LOADED
import life.chenshi.keepaccounts.module.common.crash.GlobalCrashHandler
import life.chenshi.keepaccounts.module.common.lifecycle.ApplicationLifecycle
import life.chenshi.keepaccounts.module.common.utils.DataStoreUtil

/**
 * 继承模式时需要和宿主的生命周期同步
 */
@AutoService(ApplicationLifecycle::class)
class CommonLifecycle : ApplicationLifecycle {
    private val mCoroutineScope by lazy(mode = LazyThreadSafetyMode.NONE) { MainScope() }

    override fun onAttachBaseContext(context: Context) {
    }

    override fun onCreate(application: Application) {
    }

    override fun onTerminate(application: Application) {
        mCoroutineScope.cancel()
    }

    override fun initInForeground(application: Application) {
        DataStoreUtil.init(application)
        setFirstLoadedState()
    }

    private fun setFirstLoadedState() {
        mCoroutineScope.launch {
            DataStoreUtil.readFromDataStore(APP_FIRST_LOADED, true)
                .take(1)
                .collect { b ->
                    takeIf { b }?.run {
                        DataStoreUtil.writeToDataStore(APP_FIRST_LOADED, false)
                    }
                }
        }
    }

    override fun initInBackground(application: Application) {
        GlobalCrashHandler.getInstance().init(application)
    }
}