package life.chenshi.keepaccounts.ui

import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import life.chenshi.keepaccounts.R
import life.chenshi.keepaccounts.common.utils.ToastUtil
import life.chenshi.keepaccounts.constant.SWITCHER_EXIT_APP
import life.chenshi.keepaccounts.module.common.base.BaseActivity
import life.chenshi.keepaccounts.module.common.utils.DataStoreUtil
import life.chenshi.keepaccounts.module.common.utils.StatusBarUtil

class MainActivity : BaseActivity() {
    private lateinit var mNavController: NavController
    private val EXIT_INTERVAL_TIME = 2000
    private var lastTime = 0L

    override fun initObserver() {
    }

    override fun initListener() {
    }

    override fun initView() {
        setContentView(R.layout.activity_main)

        StatusBarUtil.init(this)
            .setTransparent()
            .setDarkMode(true)

        // 初始化controller，绑定navMenu
        mNavController = findNavController(R.id.index_nav_host_fragment_container)
        findViewById<BottomNavigationView>(R.id.bottom_navigation_view).setupWithNavController(
            mNavController
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        mNavController.navigate(R.id.action_indexFragment_to_searchFragment)
        return true
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