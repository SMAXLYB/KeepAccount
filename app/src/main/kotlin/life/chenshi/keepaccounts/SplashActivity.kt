package life.chenshi.keepaccounts

import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import life.chenshi.keepaccounts.module.common.base.BaseActivity

class SplashActivity : BaseActivity() {

    override fun initView() {
        setContentView(R.layout.app_activity_splash)
        val job = flow<Int> {
            for (i in 10 downTo 0) {
                emit(i)
                delay(1000)
            }
        }.flowOn(Dispatchers.Default)
            .onEach {
                findViewById<TextView>(R.id.tv_countdown).text = "$it"
            }
            .launchIn(lifecycleScope)

        findViewById<TextView>(R.id.tv_countdown).setOnClickListener {
            job.cancel()
        }
    }

    override fun initListener() {
    }

    override fun initObserver() {
    }
}