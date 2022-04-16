package life.chenshi.keepaccounts.module.book.ui

import android.content.Context
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import life.chenshi.keepaccounts.module.book.R
import life.chenshi.keepaccounts.module.book.databinding.BookActivityMainBinding
import life.chenshi.keepaccounts.module.common.base.NavHostActivity
import life.chenshi.keepaccounts.module.common.utils.startActivity

class MainActivity : NavHostActivity() {

    private lateinit var mBinding: BookActivityMainBinding

    companion object {
        @JvmStatic
        fun start(context: Context, data: Bundle) {
            context.startActivity<MainActivity> {
                putExtras(data)
            }
        }
    }

    override fun setHostFragmentId() = R.id.book_nav_host_fragment_container

    override fun setNavGraphId() = R.navigation.book_nav_main

    override fun getStartDestination(path: String): Int {
        return when (path) {
            else -> 0
        }
    }

    override fun setContentView() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.book_activity_main)
    }

    override fun initListener() {
    }

    override fun initObserver() {
    }
}