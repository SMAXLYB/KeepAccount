package life.chenshi.keepaccounts

import android.annotation.SuppressLint
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import life.chenshi.keepaccounts.module.common.base.BaseActivity
import life.chenshi.keepaccounts.ui.AppTheme

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {

    private val mViewModel by viewModels<SplashViewModel>()

    override fun initView() {
        val job = flow {
            for (i in 5 downTo 0) {
                emit(i)
                delay(1000)
            }
        }
            .flowOn(Dispatchers.Default)
            .onEach {
                mViewModel.countTo(it)
            }
            .launchIn(lifecycleScope)

        setContent {
            AppTheme {
                SplashActivityScreen(mViewModel) { job.cancel() }
            }
        }
    }

    override fun initListener() {
    }

    override fun initObserver() {
    }
}

@Preview
@Composable
private fun SplashActivityScreen(
    viewModel: SplashViewModel = viewModel(),
    onClick: () -> Unit = {}
) {
    val counterState by viewModel.counter.collectAsState()
    Box(
        Modifier
            .background(color = colorResource(id = R.color.colorPrimary))
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Button(onClick = onClick) {
            Text(text = counterState.toString())
        }
        Image(
            painter = painterResource(id = R.drawable.common_ic_launcher_foreground),
            contentDescription = "Logo"
        )
        Text("测试")
    }
}