package life.chenshi.keepaccounts.global

import android.app.Application

class MyApplication : Application() {

    companion object {
        private var INSTANCE: MyApplication? = null

        fun getInstance(): Application = INSTANCE as MyApplication
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }
}