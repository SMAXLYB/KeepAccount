package life.chenshi.keepaccounts.ui.setting.book

import life.chenshi.keepaccounts.R
import life.chenshi.keepaccounts.databinding.ActivityBookBinding
import life.chenshi.keepaccounts.module.common.base.BaseActivity
import life.chenshi.keepaccounts.module.common.utils.StatusBarUtil

class BookActivity : BaseActivity() {

    private val mBinding by bindingContentView<ActivityBookBinding>(R.layout.activity_book)

    override fun initView() {
        StatusBarUtil.init(this)
            .setColor(R.color.white)
            .setDarkMode(true)
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