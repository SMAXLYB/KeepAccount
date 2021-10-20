package life.chenshi.keepaccounts.module.search

import android.app.Application
import android.content.Context
import com.google.auto.service.AutoService
import life.chenshi.keepaccounts.module.common.lifecycle.ApplicationLifecycle

@AutoService(ApplicationLifecycle::class)
class SearchLifecycle : ApplicationLifecycle {
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