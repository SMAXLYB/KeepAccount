package run_as_app

import android.os.Bundle
import life.chenshi.keepaccounts.module.common.base.BaseActivity
import life.chenshi.keepaccounts.module.search.R

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_activity_main)
    }

    override fun initView() {
    }

    override fun initListener() {
    }

    override fun initObserver() {
    }
}