package life.chenshi.keepaccounts.module.record.ui

import androidx.databinding.DataBindingUtil
import androidx.navigation.navArgs
import dagger.hilt.android.AndroidEntryPoint
import life.chenshi.keepaccounts.module.common.base.NavHostActivity
import life.chenshi.keepaccounts.module.common.constant.RECORD_EDIT
import life.chenshi.keepaccounts.module.common.utils.StatusBarUtil
import life.chenshi.keepaccounts.module.common.utils.nightMode
import life.chenshi.keepaccounts.module.record.R
import life.chenshi.keepaccounts.module.record.databinding.RecordActivityMainBinding

/**
 * Record模块的宿主Activity
 */
@AndroidEntryPoint
class MainActivity : NavHostActivity() {

    private val mainArgs by navArgs<MainActivityArgs>()
    private lateinit var mBinding: RecordActivityMainBinding

    override fun setHostFragmentId() = R.id.record_nav_host_fragment_container

    override fun setNavGraphId() = R.navigation.record_nav_main

    override fun getStartDestination(path: String): Int {
        return when (path) {
            RECORD_EDIT -> R.id.editRecordFragment
            else -> 0
        }
    }

    override fun setContentView() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.record_activity_main)
    }

    override fun initView() {
    }

    override fun configureDefaultStatusBar(): Boolean {
        StatusBarUtil.init(this)
            .setTransparent()
            .setDarkMode(nightMode())
        return false
    }

    override fun initListener() {
    }

    override fun initObserver() {
    }
}