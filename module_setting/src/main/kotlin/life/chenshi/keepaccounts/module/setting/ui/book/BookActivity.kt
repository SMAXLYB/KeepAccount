package life.chenshi.keepaccounts.module.setting.ui.book

import life.chenshi.keepaccounts.module.common.base.BaseActivity
import life.chenshi.keepaccounts.module.setting.R
import life.chenshi.keepaccounts.module.setting.databinding.SettingActivityBookBinding

class BookActivity : BaseActivity() {

    private val mBinding by bindingContentView<SettingActivityBookBinding>(R.layout.setting_activity_book)

    override fun initView() {
        mBinding.bar.apply {
            setLeftClickListener {
                onBackPressed()
            }
        }
    }

    override fun initListener() {
    }

    override fun initObserver() {
    }
}