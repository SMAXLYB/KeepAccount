package life.chenshi.keepaccounts.module.common.lifecycle

import android.app.Application
import android.content.Context

/**
 * 子模块如果需要感知application生命周期并做初始化操作，实现此接口即可
 */
interface ApplicationLifecycle {
    /**
     * 同[Application.attachBaseContext]
     */
    fun onAttachBaseContext(context: Context)

    /**
     * 同[Application.onCreate]
     */
    fun onCreate(application: Application)

    /**
     * 同[Application.onTerminate]
     */
    fun onTerminate(application: Application)

    /**
     * 主线程前台初始化
     */
    fun initInForeground(application: Application)

    /**
     * 不需要立即初始化的放在这里进行后台初始化
     */
    fun initInBackground(application: Application)

}