package life.chenshi.keepaccounts.ui.setting.category

import androidx.activity.viewModels
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.jeremyliao.liveeventbus.LiveEventBus
import life.chenshi.keepaccounts.R
import life.chenshi.keepaccounts.database.entity.AbstractCategory
import life.chenshi.keepaccounts.databinding.LayoutCategoryBinding
import life.chenshi.keepaccounts.module.common.base.BaseActivity
import life.chenshi.keepaccounts.module.common.constant.BUSINESS
import life.chenshi.keepaccounts.module.common.utils.ToastUtil
import life.chenshi.keepaccounts.module.common.utils.getValueFromIntent
import life.chenshi.keepaccounts.module.common.utils.isNull

class CategoryActivity : BaseActivity() {
    companion object {
        private const val TAG = "CategoryActivity"
    }

    private val mCategoryViewModel by viewModels<CategoryViewModel>()
    private val mBinding by bindingContentView<LayoutCategoryBinding>(R.layout.layout_category)

    override fun initView() {
        // viewPager设置
        mBinding.pager.adapter = CategoryPagerAdapter(this)
        TabLayoutMediator(mBinding.tab, mBinding.pager) { tab, position ->
            tab.text = if (position == 0) {
                "支出"
            } else {
                "收入"
            }
        }.attach()

        mCategoryViewModel.business.value = getValueFromIntent<String>(BUSINESS)
    }

    override fun initListener() {
        mBinding.bar.apply {
            setLeftClickListener {
                onBackPressed()
            }

            setRightTitleClickListener {
                mCategoryViewModel.isDeleteMode.apply {
                    value = !value!!
                }
            }

            setRightIconClickListener {
                mCategoryViewModel.run {
                    // 主类必须不为空
                    if (currentMajorCategory.value.isNull()) {
                        ToastUtil.showShort("请至少选择一个类型")
                        return@setRightIconClickListener
                    }
                    LiveEventBus.get("category", AbstractCategory::class.java)
                        .post(currentMinorCategory.value ?: currentMajorCategory.value)
                    finish()
                }
            }
        }
    }

    override fun initObserver() {
        mCategoryViewModel.apply {
            isDeleteMode.observe(this@CategoryActivity) {
                if (it) {
                    mBinding.bar.setRightTitle("完成")
                    business.value?.run {
                        mBinding.bar.hideRightIcon()
                    }
                } else {
                    mBinding.bar.setRightTitle("编辑")
                    business.value?.run {
                        mBinding.bar.showRightIcon()
                        mBinding.bar.setRightTitle("")
                    }
                }
            }

            business.observe(this@CategoryActivity) {
                it?.run {
                    // bar设置
                    mBinding.bar.setCenterTitle("选择类别")
                    mBinding.bar.hideCenterIcon()
                    mBinding.bar.setRightIcon(AppCompatResources.getDrawable(this@CategoryActivity, R.drawable.action_bar_complete)!!)
                }
            }
        }
    }

    class CategoryPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
        override fun getItemCount(): Int {
            return 2
        }

        override fun createFragment(position: Int): Fragment {
            return CategoryFragment.newInstance(position)
        }
    }
}