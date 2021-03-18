package life.chenshi.keepaccounts.global

import android.app.Application
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import life.chenshi.keepaccounts.common.utils.DataStoreUtil
import life.chenshi.keepaccounts.constant.APP_FIRST_LOADED

class MyApplication : Application() {

    companion object {
        private var INSTANCE: MyApplication? = null

        fun getInstance(): Application = INSTANCE as MyApplication
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        DataStoreUtil.init(this)
        setFirstLoadedState()
        GlobalCrashHandler.getInstance().init(this)

    }

    private fun setFirstLoadedState() {
        MainScope().launch {
            DataStoreUtil.readFromDataStore(APP_FIRST_LOADED, true)
                .take(1)
                .collect { b ->
                    takeIf { b }?.run {
                        DataStoreUtil.writeToDataStore(APP_FIRST_LOADED, false)
                    }
                }
        }
    }
}