package life.chenshi.keepaccounts.global

import android.app.Application
import life.chenshi.keepaccounts.utils.DataStoreUtil

class MyApplication : Application() {

    companion object {
        private var INSTANCE: MyApplication? = null

        fun getInstance(): Application = INSTANCE as MyApplication
    }

    override fun onCreate() {
        super.onCreate()
        DataStoreUtil.init(this)
        INSTANCE = this
    }
}