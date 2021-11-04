package life.chenshi.keepaccounts

import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import life.chenshi.keepaccounts.module.common.base.BaseActivity
import life.chenshi.keepaccounts.ui.AppTheme

class SplashActivity : BaseActivity() {

    private val mViewModel by viewModels<SplashViewModel>()

    override fun initView() {
        // setContentView(R.layout.app_activity_splash)
        // val job = flow<Int> {
        //     for (i in 10 downTo 0) {
        //         emit(i)
        //         delay(1000)
        //     }
        // }.flowOn(Dispatchers.Default)
        //     .onEach {
        //         findViewById<TextView>(R.id.tv_countdown).text = "$it"
        //     }
        //     .launchIn(lifecycleScope)
        //
        // findViewById<TextView>(R.id.tv_countdown).setOnClickListener {
        //     job.cancel()
        // }

        setContent {
            AppTheme {
                // SplashActivityScreen(mViewModel)
            }
        }
    }

    override fun initListener() {
    }

    override fun initObserver() {
    }
}

// @Preview
// @Composable
// private fun SplashActivityScreen(viewModel: SplashViewModel = viewModel()) {
//     val counterState by viewModel.counter.collectAsState()
//     Box() {
//         Text("测试")
//     }
// }


@Preview(showBackground = true, backgroundColor = 0xffffff)
@Composable
private fun SplashActivityScreen() {
    Box() {
        Text("测试字体")
    }
}

@Preview
@Composable
private fun CircleButton(text: String = "6", onClick: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .background(Color.White, CircleShape)
            .defaultMinSize(40.dp, 40.dp)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text)
    }
}