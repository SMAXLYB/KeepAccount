package life.chenshi.keepaccounts.ui

import android.view.Menu
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import life.chenshi.keepaccounts.R
import life.chenshi.keepaccounts.base.BaseActivity

class MainActivity : BaseActivity() {
    private lateinit var mNavController: NavController

    override fun initObserver() {
    }

    override fun initListener() {
    }

    override fun initView() {
        setContentView(R.layout.activity_main)

        // 初始化controller，绑定navMenu
        mNavController = findNavController(R.id.nav_host_fragment_container)
        findViewById<BottomNavigationView>(R.id.bottom_navigation_view).setupWithNavController(
            mNavController
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        mNavController.navigate(R.id.action_indexFragment_to_searchFragment)
        return true
    }
}