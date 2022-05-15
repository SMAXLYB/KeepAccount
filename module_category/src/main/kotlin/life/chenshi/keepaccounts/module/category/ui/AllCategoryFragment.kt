package life.chenshi.keepaccounts.module.category.ui

import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.jeremyliao.liveeventbus.LiveEventBus
import dagger.hilt.android.AndroidEntryPoint
import life.chenshi.keepaccounts.module.category.R
import life.chenshi.keepaccounts.module.category.databinding.CategoryFragmentAllCategoryBinding
import life.chenshi.keepaccounts.module.category.vm.AllCategoryViewModel
import life.chenshi.keepaccounts.module.common.base.NavBindingFragment
import life.chenshi.keepaccounts.module.common.constant.BUSINESS
import life.chenshi.keepaccounts.module.common.constant.CATEGORY
import life.chenshi.keepaccounts.module.common.database.entity.AbstractCategory
import life.chenshi.keepaccounts.module.common.utils.ToastUtil
import life.chenshi.keepaccounts.module.common.utils.arguments
import life.chenshi.keepaccounts.module.common.utils.isNull
import life.chenshi.keepaccounts.module.common.utils.onBackPressed

@AndroidEntryPoint
class AllCategoryFragment : NavBindingFragment<CategoryFragmentAllCategoryBinding>() {

    override fun setLayoutId() = R.layout.category_fragment_all_category

    companion object {
        private const val TAG = "AllCategoryFragment"
    }

    private val mAllCategoryViewModel by viewModels<AllCategoryViewModel>()
    private val business by arguments<String>(BUSINESS)

    override fun initView() {
        // viewPager设置
        binding.pager.adapter = CategoryPagerAdapter(this)
        TabLayoutMediator(binding.tab, binding.pager) { tab, position ->
            tab.text = if (position == 0) {
                "支出"
            } else {
                "收入"
            }
        }.attach()

        mAllCategoryViewModel.business.value = business
    }

    override fun initListener() {
        binding.bar.apply {
            setLeftClickListener {
                onBackPressed()
            }

            setRightTitleClickListener {
                mAllCategoryViewModel.isDeleteMode.apply {
                    value = !value!!
                }
            }

            setRightIconClickListener {
                mAllCategoryViewModel.run {
                    // 主类必须不为空
                    if (currentMajorCategory.value.isNull()) {
                        ToastUtil.showShort("请至少选择一个类型")
                        return@setRightIconClickListener
                    }
                    LiveEventBus.get(CATEGORY, AbstractCategory::class.java)
                        .post(currentMinorCategory.value ?: currentMajorCategory.value)
                    onBackPressed()
                }
            }
        }
    }

    override fun initObserver() {
        mAllCategoryViewModel.apply {
            isDeleteMode.observe(this@AllCategoryFragment) {
                if (it) {
                    binding.bar.setRightTitle("完成")
                    business.value?.run {
                        binding.bar.hideRightIcon()
                    }
                } else {
                    binding.bar.setRightTitle("编辑")
                    business.value?.run {
                        binding.bar.showRightIcon()
                        binding.bar.setRightTitle("")
                    }
                }
            }

            business.observe(this@AllCategoryFragment) {
                it?.run {
                    // bar设置
                    binding.bar.setCenterTitle("选择类别")
                    binding.bar.hideCenterIcon()
                    binding.bar.setRightIcon(
                        AppCompatResources.getDrawable(requireContext(), R.drawable.common_action_bar_complete)!!
                    )
                }
            }
        }
    }

    class CategoryPagerAdapter(fragment: Fragment) :
        FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int {
            return 2
        }

        override fun createFragment(position: Int): Fragment {
            return CategoryPageFragment.newInstance(position)
        }
    }
}