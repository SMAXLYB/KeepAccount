package life.chenshi.keepaccounts.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import life.chenshi.keepaccounts.R
import life.chenshi.keepaccounts.module.common.base.BaseActivity
import life.chenshi.keepaccounts.module.common.constant.SWITCHER_CLOSE_AD
import life.chenshi.keepaccounts.module.common.utils.StatusBarUtil
import life.chenshi.keepaccounts.module.common.utils.startActivity
import life.chenshi.keepaccounts.module.common.utils.storage.KVStoreHelper
import life.chenshi.keepaccounts.vm.SplashViewModel

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {

    private val mViewModel by viewModels<SplashViewModel>()

    override fun configureDefaultStatusBar(): Boolean {
        return false
    }

    override fun initView(savedInstanceState: Bundle?) {
        if (KVStoreHelper.read(SWITCHER_CLOSE_AD, false)) {
            navigationToHome()
            return
        }

        val job = flow {
            for (i in 3 downTo 0) {
                emit(i)
                delay(1000)
            }
        }
            .flowOn(Dispatchers.Default)
            .onEach {
                mViewModel.countTo(it)
                if (it == 1) {
                    navigationToHome()
                }
            }
            .launchIn(lifecycleScope)

        setContent {
            AppTheme {
                SplashActivityScreen() {
                    job.cancel()
                    navigationToHome()
                }
            }
        }

        StatusBarUtil.init(this).setFullScreen()
    }

    override fun initListener() {
    }

    override fun initObserver() {
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, 0);
    }

    private fun navigationToHome() {
        startActivity<MainActivity>()
        finish()
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
            .background(color = colorResource(id = R.color.common_blue_500))
            .fillMaxWidth()
            .fillMaxHeight(),
        Alignment.Center
    ) {
        Button(
            onClick = onClick,
            elevation = null,
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black.copy(alpha = 0.15f)),
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 36.dp)
                .align(Alignment.TopEnd)
        ) {
            Text(
                text = "跳过 $counterState",
                fontSize = 12.sp,
                style = TextStyle(color = Color.White, fontWeight = FontWeight.Medium),
            )
        }
        Image(
            painter = painterResource(id = R.drawable.common_ic_launcher_foreground),
            contentDescription = "Logo",
        )
    }
}