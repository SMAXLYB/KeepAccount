package life.chenshi.keepaccounts.module.common.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import life.chenshi.keepaccounts.module.common.R
import life.chenshi.keepaccounts.module.common.constant.CURRENT_THEME
import life.chenshi.keepaccounts.module.common.constant.Theme
import life.chenshi.keepaccounts.module.common.utils.StatusBarUtil
import life.chenshi.keepaccounts.module.common.utils.getColorFromAttr
import life.chenshi.keepaccounts.module.common.utils.nightMode
import life.chenshi.keepaccounts.module.common.utils.storage.KVStoreHelper


abstract class BaseActivity : AppCompatActivity() {
    protected val TAG = this::class.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        val theme = Theme.getThemeFromName(KVStoreHelper.read(CURRENT_THEME, Theme.Default.name))
        setTheme(theme.styRes)
        super.onCreate(savedInstanceState)
        if (configureDefaultStatusBar()) {
            val backgroundColor = getColorFromAttr(R.attr.colorSurface)
            StatusBarUtil.init(this)
                .setColorByValue(backgroundColor)
                .setDarkMode(nightMode())
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