package life.chenshi.keepaccounts.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import life.chenshi.keepaccounts.R
import life.chenshi.keepaccounts.base.BaseActivity

class MainActivity : BaseActivity() {
    private lateinit var mNavController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        initView()
    }

    private fun initView() {
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
        Toast.makeText(this, "点击了搜索", Toast.LENGTH_SHORT).show()
        return super.onOptionsItemSelected(item)
    }
}