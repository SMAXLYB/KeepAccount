package life.chenshi.keepaccounts

import android.app.Application
import android.content.Context
import com.alibaba.android.arouter.launcher.ARouter
import com.google.auto.service.AutoService
import life.chenshi.keepaccounts.module.common.lifecycle.ApplicationLifecycle

@AutoService(ApplicationLifecycle::class)
class AppLifecycle : ApplicationLifecycle {
    override fun onAttachBaseContext(context: Context) {

    }

    override fun onCreate(application: Application) {
    }

    override fun onTerminate(application: Application) {
    }

    override fun initInForeground(application: Application) {
        if (BuildConfig.DEBUG) {
            ARouter.openLog()
            ARouter.openDebug()
        }
        ARouter.init(application)
    }

    override fun initInBackground(application: Application) {
    }
}