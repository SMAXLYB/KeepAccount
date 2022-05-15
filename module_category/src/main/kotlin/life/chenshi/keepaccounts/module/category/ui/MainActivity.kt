package life.chenshi.keepaccounts.module.category.ui

import android.content.Context
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import dagger.hilt.android.AndroidEntryPoint
import life.chenshi.keepaccounts.module.category.R
import life.chenshi.keepaccounts.module.category.databinding.CategoryActivityMainBinding
import life.chenshi.keepaccounts.module.common.base.NavHostActivity
import life.chenshi.keepaccounts.module.common.utils.startActivity

@AndroidEntryPoint
class MainActivity : NavHostActivity() {

    private lateinit var mBinding: CategoryActivityMainBinding

    companion object {
        @JvmStatic
        fun start(context: Context, data: Bundle) {
            context.startActivity<MainActivity> {
                putExtras(data)
            }
        }
    }

    override fun setHostFragmentId() = R.id.category_nav_host_fragment_container

    override fun setNavGraphId() = R.navigation.category_nav_main

    override fun getStartDestination(path: String): Int {
        return when (path) {
            else -> 0
        }
    }

    override fun setContentView() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.category_activity_main)
    }

    override fun initView() {

    }

    override fun initListener() {
    }

    override fun initObserver() {
    }
}