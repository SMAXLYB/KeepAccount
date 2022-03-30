package life.chenshi.keepaccounts.ui

import android.content.res.Configuration
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import life.chenshi.keepaccounts.R
import life.chenshi.keepaccounts.module.common.base.BaseActivity
import life.chenshi.keepaccounts.module.common.constant.SWITCHER_EXIT_APP
import life.chenshi.keepaccounts.module.common.utils.*
import life.chenshi.keepaccounts.module.common.utils.storage.DataStoreUtil

class MainActivity : BaseActivity() {
    private lateinit var mNavController: NavController
    private lateinit var mBottomNavView: BottomNavigationView
    private var lastTime = 0L

    companion object {
        private const val EXIT_INTERVAL_TIME = 2000
    }

    override fun configureDefaultStatusBar(): Boolean {
        StatusBarUtil.init(this)
            .setTransparent()
            .setDarkMode(nightMode())
        return false
    }

    override fun initView() {
        setContentView(R.layout.app_activity_main)
        // 初始化controller，绑定navMenu
        mNavController =
            (supportFragmentManager.findFragmentById(R.id.index_nav_host_fragment_container) as NavHostFragment).navController
        mBottomNavView = findViewById<BottomNavigationView>(R.id.bottom_navigation_view).apply {
            setupWithNavController(mNavController)
        }

    }

    override fun initObserver() {
    }

    override fun initListener() {
        mNavController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.indexFragment, R.id.analysisFragment, R.id.settingFragment -> {
                    mBottomNavView.visible()
                }
                else -> {
                    mBottomNavView.gone()
                }
            }
        }
    }

    override fun onBackPressed() {
        lifecycleScope.launch {
            DataStoreUtil.readFromDataStore(SWITCHER_EXIT_APP, true)
                .take(1)
                .collect {
                    if (it) {
                        val currentTime = System.currentTimeMillis()
                        if (currentTime - lastTime > EXIT_INTERVAL_TIME) {
                            ToastUtil.showShort("再按一次退出应用")
                            lastTime = currentTime
                        } else {
                            super.onBackPressed()
                        }
                    } else {
                        super.onBackPressed()
                    }
                }
        }
    }
}