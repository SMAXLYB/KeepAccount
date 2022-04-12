package run_as_app

import android.app.Application
import android.content.Context
import com.google.auto.service.AutoService
import life.chenshi.keepaccounts.module.common.lifecycle.ApplicationLifecycle

@AutoService(ApplicationLifecycle::class)
class SearchLifecycle : ApplicationLifecycle {
    override fun onAttachBaseContext(context: Context) {
    }

    override fun onCreate(application: Application) {
    }

    override fun onTerminate(application: Application) {
    }

    override fun initInForeground(application: Application) {
    }

    override fun initInBackground(application: Application) {
    }
}