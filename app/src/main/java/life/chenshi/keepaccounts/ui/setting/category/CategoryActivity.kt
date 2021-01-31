package life.chenshi.keepaccounts.ui.setting.category

import life.chenshi.keepaccounts.R
import life.chenshi.keepaccounts.base.BaseActivity
import life.chenshi.keepaccounts.databinding.LayoutCategoryBinding

class CategoryActivity : BaseActivity() {
    private val mBinding by bindingContentView<LayoutCategoryBinding>(R.layout.layout_category)

    override fun initView() {
        mBinding.barrier
    }

    override fun initListener() {
    }
    override fun initObserver() {
    }


}