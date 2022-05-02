package life.chenshi.keepaccounts.module.setting.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import life.chenshi.keepaccounts.module.common.base.NavHostActivity
import life.chenshi.keepaccounts.module.common.constant.PATH_SETTING_ALL_SETTING
import life.chenshi.keepaccounts.module.common.constant.PATH_SETTING_ASSETS
import life.chenshi.keepaccounts.module.common.constant.PATH_SETTING_THEME
import life.chenshi.keepaccounts.module.common.utils.startActivity
import life.chenshi.keepaccounts.module.common.view.CustomActionBar
import life.chenshi.keepaccounts.module.setting.R
import life.chenshi.keepaccounts.module.setting.vm.AllSettingViewModel

/**
 * Setting模块的宿主Activity
 */
@AndroidEntryPoint
class MainActivity : NavHostActivity() {

    companion object {
        @JvmStatic
        fun start(context: Context, data: Bundle) {
            context.startActivity<MainActivity> {
                putExtras(data)
            }
        }
    }

    private val mViewModel by viewModels<AllSettingViewModel>()
    private lateinit var mBar: CustomActionBar

    override fun initView() {
        mBar = findViewById(R.id.bar)
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

        mViewModel.rightIconAction.observe(this) { p ->
            p?.let {
                mBar.setRightIcon(it.first)
                mBar.setRightIconClickListener { _ ->
                    Log.d(TAG, "initObserver: ")
                    it.second.invoke()
                }
            } ?: kotlin.run {
                mBar.hideRightIcon()
            }
        }
    }

    override fun setHostFragmentId() = R.id.setting_nav_host_fragment_container
    override fun setNavGraphId() = R.navigation.setting_nav_main

    override fun getStartDestination(path: String): Int {
        return when (path) {
            PATH_SETTING_ALL_SETTING -> R.id.allSettingFragment
            PATH_SETTING_THEME -> R.id.themeSettingFragment
            PATH_SETTING_ASSETS -> R.id.assetsAccountFragment
            else -> 0
        }
    }

    override fun setContentView() {
        setContentView(R.layout.setting_activity_main)
    }
}