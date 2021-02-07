package life.chenshi.keepaccounts.ui.setting.category

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import life.chenshi.keepaccounts.R
import life.chenshi.keepaccounts.common.base.BaseActivity
import life.chenshi.keepaccounts.databinding.LayoutCategoryBinding

class CategoryActivity : BaseActivity() {
    private val mBinding by bindingContentView<LayoutCategoryBinding>(R.layout.layout_category)

    override fun initView() {
        val data = listOf("1111","2222","3333","4444","5555","66666","77777","8888","9999","11","22","33","44","55","66","77","88","99","1111","222","333","444","555","666","777","888","999","666","777","888","999")
        mBinding.rvCategory.layoutManager = LinearLayoutManager(this)
        mBinding.rvCategory.adapter = CategoryAdapter().apply {
            addNormals(data)
            addFooter()
        }

        mBinding.rvSubCategory.layoutManager = GridLayoutManager(this,4)
        mBinding.rvSubCategory.adapter = SubCategoryAdapter().apply {
            setHeaderOrFooterFullSpan(false)
            addNormals(data)
            addFooter()
        }
    }

    override fun initListener() {
    }

    override fun initObserver() {
    }
}