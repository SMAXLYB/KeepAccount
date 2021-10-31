package life.chenshi.keepaccounts.module.common.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import life.chenshi.keepaccounts.module.common.R
import life.chenshi.keepaccounts.module.common.utils.StatusBarUtil

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.common_Theme_KeepAccounts)
        super.onCreate(savedInstanceState)
        if(configureDefaultStatusBar()){
            StatusBarUtil.init(this)
                .setColor(R.color.common_white)
                .setDarkMode(true)
        }
        initView()
        initListener()
        initObserver()
    }

    protected open fun configureDefaultStatusBar() = true

    protected abstract fun initView()

    protected abstract fun initListener()

    protected abstract fun initObserver()

    protected inline fun <reified T : ViewDataBinding> bindingContentView(@LayoutRes resId: Int): Lazy<T> =
        lazy { DataBindingUtil.setContentView(this, resId) }
}