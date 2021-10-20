package life.chenshi.keepaccounts.module.common

import android.app.Application
import android.content.Context
import life.chenshi.keepaccounts.module.common.lifecycle.ApplicationLifecycle

/**
 * 继承模式时需要和宿主的生命周期同步
 */
class CommonLifecycle : ApplicationLifecycle {
    override fun onAttachBaseContext(context: Context) {
        TODO("Not yet implemented")
    }

    override fun onCreate(application: Application) {
        TODO("Not yet implemented")
    }

    override fun onTerminate(application: Application) {
        TODO("Not yet implemented")
    }

    override fun initByFrontDesk(): MutableList<() -> String> {
        TODO("Not yet implemented")
    }

    override fun initByBackstage() {
        TODO("Not yet implemented")
    }
}