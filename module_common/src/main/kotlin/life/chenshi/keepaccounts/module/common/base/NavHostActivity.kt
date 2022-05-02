package life.chenshi.keepaccounts.module.common.base

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.annotation.NavigationRes
import androidx.navigation.fragment.NavHostFragment
import life.chenshi.keepaccounts.module.common.constant.START_DESTINATION

abstract class NavHostActivity : BaseActivity() {

    @IdRes
    protected abstract fun setHostFragmentId(): Int

    @NavigationRes
    protected abstract fun setNavGraphId(): Int

    protected abstract fun getStartDestination(path: String): Int

    protected abstract fun setContentView()

    protected abstract fun initView()

    @Deprecated("使用无参方法initView")
    override fun initView(savedInstanceState: Bundle?) {
        setContentView()
        initStartDestination()
        initView()
    }

    private fun initStartDestination() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(setHostFragmentId()) as NavHostFragment
        val navController = navHostFragment.navController
        navController.graph.setStartDestination(0)
        val navGraph = navController.navInflater.inflate(setNavGraphId())
        intent.extras?.getString(START_DESTINATION)?.let { path ->
            getStartDestination(path).takeIf { it != 0 }?.let {
                navGraph.setStartDestination(it)
            }
        }
        navController.setGraph(navGraph, intent.extras)
    }
}