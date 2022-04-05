package life.chenshi.keepaccounts.module.record.ui

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navArgs
import life.chenshi.keepaccounts.module.common.base.BaseActivity
import life.chenshi.keepaccounts.module.common.constant.EDIT_RECORD
import life.chenshi.keepaccounts.module.common.utils.StatusBarUtil
import life.chenshi.keepaccounts.module.common.utils.nightMode
import life.chenshi.keepaccounts.module.record.R

/**
 * Record模块的宿主Activity
 */
class MainActivity : BaseActivity() {

    private val mainArgs by navArgs<MainActivityArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.record_activity_main)
        initStartDestination()
    }

    override fun configureDefaultStatusBar(): Boolean {
        StatusBarUtil.init(this)
            .setTransparent()
            .setDarkMode(nightMode())
        return false
    }

    override fun initView() {
    }

    override fun initListener() {
    }

    override fun initObserver() {
    }

    private fun initStartDestination() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.record_nav_host_fragment_container) as NavHostFragment
        val navController = navHostFragment.navController
        val navGraph = navController.navInflater.inflate(R.navigation.record_nav_main)
        mainArgs.startDestination?.let { path ->
            getStartDestination(path).takeUnless { it != 0 }?.let {
                navGraph.startDestination = it
            }
        }
        navController.setGraph(navGraph, intent.extras)
    }

    private fun getStartDestination(path: String): Int {
        return when (path) {
            EDIT_RECORD -> R.id.editRecordFragment
            else -> 0
        }
    }
}