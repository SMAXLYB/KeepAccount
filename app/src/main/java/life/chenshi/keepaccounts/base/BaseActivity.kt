package life.chenshi.keepaccounts.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import life.chenshi.keepaccounts.R

abstract class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_KeepAccounts)
        super.onCreate(savedInstanceState)
    }
}