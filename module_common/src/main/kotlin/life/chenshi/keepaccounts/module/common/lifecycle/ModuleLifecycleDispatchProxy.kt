package life.chenshi.keepaccounts.module.common.lifecycle

import android.app.Application
import android.content.Context
import java.util.*

/**
 * 分发application生命周期
 */
class ModuleLifecycleDispatchProxy : ApplicationLifecycle {

    private var mLoader: ServiceLoader<ApplicationLifecycle> =
        ServiceLoader.load(ApplicationLifecycle::class.java)

    /**
     * 同[Application.attachBaseContext]
     * @param context Context
     */
    override fun onAttachBaseContext(context: Context) {
        mLoader.forEach {
            it.onAttachBaseContext(context)
        }
    }

    /**
     * 同[Application.onCreate]
     * @param application Application
     */
    override fun onCreate(application: Application) {
        mLoader.forEach { it.onCreate(application) }
    }

    /**
     * 同[Application.onTerminate]
     * @param application Application
     */
    override fun onTerminate(application: Application) {
        mLoader.forEach { it.onTerminate(application) }
    }

    /**
     * 主线程前台初始化
     * @return MutableList<() -> String> 初始化方法集合
     */
    override fun initInForeground(application: Application) {
        mLoader.forEach { it.initInForeground(application) }
    }

    /**
     * 不需要立即初始化的放在这里进行后台初始化
     */
    override fun initInBackground(application: Application) {
        mLoader.forEach { it.initInBackground(application) }
    }
}