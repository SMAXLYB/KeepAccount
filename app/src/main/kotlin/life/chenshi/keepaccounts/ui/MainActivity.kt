package life.chenshi.keepaccounts.ui

import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import life.chenshi.keepaccounts.R
import life.chenshi.keepaccounts.databinding.AppActivityMainBinding
import life.chenshi.keepaccounts.module.common.base.BaseActivity
import life.chenshi.keepaccounts.module.common.constant.SWITCHER_EXIT_APP
import life.chenshi.keepaccounts.module.common.utils.*
import life.chenshi.keepaccounts.module.common.utils.storage.KVStoreHelper

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private lateinit var mNavController: NavController
    private lateinit var mBinding: AppActivityMainBinding
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
        mBinding = DataBindingUtil.setContentView(this, R.layout.app_activity_main)
        // 初始化controller，绑定navMenu
        mNavController =
            (supportFragmentManager.findFragmentById(R.id.index_nav_host_fragment_container) as NavHostFragment).navController
        mBinding.bottomNavigationView.setupWithNavController(mNavController)

    }

    override fun initObserver() {

    }

    override fun initListener() {
        mNavController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.indexFragment, R.id.analysisFragment, R.id.userProfileFragment -> {
                    mBinding.bottomNavigationView.visible()
                }
                else -> {
                    mBinding.bottomNavigationView.gone()
                }
            }
        }
    }

    override fun onBackPressed() {
        KVStoreHelper.read(SWITCHER_EXIT_APP, true).apply {
            if (this) {
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