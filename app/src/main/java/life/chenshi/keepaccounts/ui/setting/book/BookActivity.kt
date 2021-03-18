package life.chenshi.keepaccounts.ui.setting.book

import life.chenshi.keepaccounts.R
import life.chenshi.keepaccounts.common.base.BaseActivity
import life.chenshi.keepaccounts.common.utils.StatusBarUtil
import life.chenshi.keepaccounts.databinding.ActivityBookBinding

class BookActivity : BaseActivity() {

    private val mBinding by bindingContentView<ActivityBookBinding>(R.layout.activity_book)

    override fun initView() {
        mBinding
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