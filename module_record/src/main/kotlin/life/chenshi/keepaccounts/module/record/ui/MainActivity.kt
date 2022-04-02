package life.chenshi.keepaccounts.module.record.ui

import android.os.Bundle
import life.chenshi.keepaccounts.module.common.base.BaseActivity
import life.chenshi.keepaccounts.module.common.utils.StatusBarUtil
import life.chenshi.keepaccounts.module.common.utils.nightMode
import life.chenshi.keepaccounts.module.record.R

/**
 * Record模块的宿主Activity
 */
class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.record_activity_main)
    }

    override fun configureDefaultStatusBar(): Boolean {
        StatusBarUtil.init(this)
            .setTransparent()
            .setDarkMode(nightMode())
        return false
    }

    override fun initView() {
    }

    override fun initListener() {
    }

    override fun initObserver() {
    }
}