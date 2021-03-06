package life.chenshi.keepaccounts.common.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import life.chenshi.keepaccounts.R

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_KeepAccounts)
        super.onCreate(savedInstanceState)
        initView()
        initListener()
        initObserver()
    }

    protected abstract fun initView()

    protected abstract fun initListener()

    protected abstract fun initObserver()

    protected inline fun <reified T : ViewDataBinding> bindingContentView(@LayoutRes resId: Int): Lazy<T> =
        lazy { DataBindingUtil.setContentView(this, resId) }
}