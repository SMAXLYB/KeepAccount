package life.chenshi.keepaccounts.module.setting.ui

import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navArgs
import life.chenshi.keepaccounts.module.common.base.BaseActivity
import life.chenshi.keepaccounts.module.common.constant.PATH_SETTING_ALL_SETTING
import life.chenshi.keepaccounts.module.common.constant.PATH_SETTING_THEME
import life.chenshi.keepaccounts.module.common.view.CustomActionBar
import life.chenshi.keepaccounts.module.setting.R
import life.chenshi.keepaccounts.module.setting.vm.AllSettingViewModel

/**
 * Setting模块的宿主Activity
 */
class MainActivity : BaseActivity() {

    private val mainArgs by navArgs<MainActivityArgs>()
    private val mViewModel by viewModels<AllSettingViewModel>()
    private lateinit var mBar: CustomActionBar
    private lateinit var mNavController: NavController

    override fun initView() {
        setContentView(R.layout.setting_activity_main)
        mBar = findViewById(R.id.bar)
        initStartDestination()
    }

    override fun initListener() {
        mBar.setLeftClickListener {
            onBackPressed()
        }
    }

    override fun initObserver() {
        mViewModel.title.observe(this) {
            mBar.setCenterTitle(it)
        }
    }

    private fun initStartDestination() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.setting_nav_host_fragment_container) as NavHostFragment
        mNavController = navHostFragment.navController
        val navGraph = mNavController.navInflater.inflate(R.navigation.setting_nav_main)
        mainArgs.startDestination?.let { path ->
            getStartDestination(path).takeIf { it != 0 }?.let {
                navGraph.setStartDestination(it)
            }
        }
        mNavController.setGraph(navGraph, intent.extras)
    }

    private fun getStartDestination(path: String): Int {
        return when (path) {
            PATH_SETTING_ALL_SETTING -> R.id.allSettingFragment
            PATH_SETTING_THEME -> R.id.themeSettingFragment
            else -> 0
        }
    }
}