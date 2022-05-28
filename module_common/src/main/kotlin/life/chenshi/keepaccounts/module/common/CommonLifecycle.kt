package life.chenshi.keepaccounts.module.common

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.google.auto.service.AutoService
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import life.chenshi.keepaccounts.module.common.constant.APP_FIRST_USE_TIME
import life.chenshi.keepaccounts.module.common.constant.DAY_NIGHT_MODE
import life.chenshi.keepaccounts.module.common.crash.GlobalCrashHandler
import life.chenshi.keepaccounts.module.common.lifecycle.ApplicationLifecycle
import life.chenshi.keepaccounts.module.common.utils.storage.DataStoreUtil
import life.chenshi.keepaccounts.module.common.utils.storage.KVStoreHelper

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
        MMKV.initialize(application)
        initDayNightMode()
        DataStoreUtil.init(application)
        setFirstLoadedState()
    }

    private fun initDayNightMode() {
        // 如果没有设置默认跟随系统
        val mode = KVStoreHelper.read(DAY_NIGHT_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        AppCompatDelegate.setDefaultNightMode(mode)
    }

    private fun setFirstLoadedState() {
        KVStoreHelper.read(APP_FIRST_USE_TIME, 0).takeIf { it == 0 }?.let {
            KVStoreHelper.write(APP_FIRST_USE_TIME, System.currentTimeMillis())
        }
    }

    override fun initInBackground(application: Application) {
        GlobalCrashHandler.getInstance().init(application)
    }
}